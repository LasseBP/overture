package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.List;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.merging.MergeVisitor;
import org.overture.codegen.merging.TemplateCallable;
import org.overture.codegen.merging.TemplateManager;
import org.overture.codegen.merging.TemplateStructure;

public class RustFormat {
	
	private MergeVisitor mergeVisitor;

	public RustFormat()
	{
		TemplateManager templateManager = new TemplateManager(new TemplateStructure("RustTemplates"));
		TemplateCallable[] templateCallables = new TemplateCallable[]{new TemplateCallable("RustFormat", this)};
		this.mergeVisitor = new MergeVisitor(templateManager, templateCallables);
	}
	
	public String format(INode node) throws AnalysisException
	{
		StringWriter writer = new StringWriter();
		node.apply(mergeVisitor, writer);

		return writer.toString();
	}

	public MergeVisitor GetMergeVisitor()
	{
		return mergeVisitor;
	}
	
	public String formatOperationBody(SStmCG body) throws AnalysisException
	{
		String NEWLINE = "\n";
		if (body == null)
		{
			return ";";
		}

		StringWriter generatedBody = new StringWriter();

		generatedBody.append("{" + NEWLINE + NEWLINE);
		generatedBody.append(handleOpBody(body));
		generatedBody.append(NEWLINE + "}");

		return generatedBody.toString();
	}

	private String handleOpBody(SStmCG body) throws AnalysisException
	{
		AMethodDeclCG method = body.getAncestor(AMethodDeclCG.class);
		
		if(method == null)
		{
			Logger.getLog().printErrorln("Could not find enclosing method when formatting operation body. Got: " + body);
		}
		else if(method.getAsync() != null && method.getAsync())
		{
			return "new VDMThread(){ "
			+ "\tpublic void run() {"
			+ "\t " + format(body)
			+ "\t} "
			+ "}.start();";
		}
		
		return format(body);
	}
	
	public String formatArgs(List<? extends SExpCG> exps)
			throws AnalysisException
	{
		StringWriter writer = new StringWriter();

		if (exps.size() <= 0)
		{
			return "";
		}

		SExpCG firstExp = exps.get(0);
		writer.append(format(firstExp));

		for (int i = 1; i < exps.size(); i++)
		{
			SExpCG exp = exps.get(i);
			writer.append(", " + format(exp));
		}

		return writer.toString();
	}
}