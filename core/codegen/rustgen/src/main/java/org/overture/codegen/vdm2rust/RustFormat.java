package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.overture.ast.util.ClonableString;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.types.AObjectTypeCG;
import org.overture.codegen.cgast.types.AUnionTypeCG;
import org.overture.codegen.cgast.types.AUnknownTypeCG;
import org.overture.codegen.cgast.types.SBasicTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.merging.MergeVisitor;
import org.overture.codegen.merging.TemplateCallable;
import org.overture.codegen.merging.TemplateManager;
import org.overture.codegen.merging.TemplateStructure;
import org.overture.codegen.utils.GeneralUtils;

public class RustFormat {
	
	private MergeVisitor mergeVisitor;
	private IRInfo info;

	public RustFormat(IRInfo info)
	{
		TemplateManager templateManager = new TemplateManager(new TemplateStructure("RustTemplates"));
		TemplateCallable[] templateCallables = new TemplateCallable[]{new TemplateCallable("RustFormat", this)};
		this.mergeVisitor = new MergeVisitor(templateManager, templateCallables);
		this.info = info;
	}
	
	public void init()
	{
		mergeVisitor.init();
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
	
	public boolean isNull(INode node)
	{
		return node == null;
	}
	
	public static String formatMetaData(List<ClonableString> metaData)
	{
		if(metaData == null || metaData.isEmpty())
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(ClonableString str : metaData)
		{
			sb.append(str.value).append('\n');
		}
		
		return sb.append('\n').toString();
	}
	
	public String formatGenericTypes(List<STypeCG> types)
			throws AnalysisException
	{
		if (types.isEmpty())
		{
			return "";
		}

		return "<" + formattedTypes(types, "") + ">";
	}

	public String formattedTypes(List<STypeCG> types, String typePostFix)
			throws AnalysisException
	{
		STypeCG firstType = types.get(0);

		StringWriter writer = new StringWriter();
		writer.append(format(firstType) + typePostFix);

		for (int i = 1; i < types.size(); i++)
		{
			STypeCG currentType = types.get(i);

			writer.append(", " + format(currentType) + typePostFix);
		}

		String result = writer.toString();
		
		return result;
	}
	
	public String escapeStr(String str)
	{
		String escaped = "";
		for (int i = 0; i < str.length(); i++)
		{
			char currentChar = str.charAt(i);
			escaped += GeneralUtils.isEscapeSequence(currentChar) ? StringEscapeUtils.escapeJava(currentChar
					+ "")
					: currentChar + "";
		}

		return escaped;
	}

	public static boolean castNotNeeded(STypeCG type)
	{
		return type instanceof AObjectTypeCG || type instanceof AUnknownTypeCG || type instanceof AUnionTypeCG;
	}
	
	public String escapeChar(char c)
	{
		return GeneralUtils.isEscapeSequence(c) ? StringEscapeUtils.escapeJavaScript(c
				+ "")
				: c + "";
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
	
	public String format(List<AFormalParamLocalParamCG> params)
			throws AnalysisException
	{
		StringWriter writer = new StringWriter();

		if (params.size() <= 0)
		{
			return "";
		}

		AFormalParamLocalParamCG firstParam = params.get(0);
		writer.append(format(firstParam));

		for (int i = 1; i < params.size(); i++)
		{
			AFormalParamLocalParamCG param = params.get(i);
			writer.append(", ");
			writer.append(format(param));
		}
		return writer.toString();
	}
}