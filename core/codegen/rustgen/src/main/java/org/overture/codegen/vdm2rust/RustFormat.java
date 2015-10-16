package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.overture.ast.util.ClonableString;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.expressions.AUndefinedExpCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.merging.MergeVisitor;
import org.overture.codegen.merging.TemplateCallable;
import org.overture.codegen.merging.TemplateManager;
import org.overture.codegen.merging.TemplateStructure;
import org.overture.codegen.utils.GeneralUtils;

public class RustFormat {
	
	private MergeVisitor mergeVisitor;
	private IRInfo info;
	private FormatUtils util;

	public RustFormat(IRInfo info)
	{
		util = new FormatUtils();
		TemplateManager templateManager = new TemplateManager(new TemplateStructure("RustTemplates"));
		TemplateCallable[] templateCallables = new TemplateCallable[]{new TemplateCallable("RustFormat", this), 
																	  new TemplateCallable("Util", util)};
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

	public String formatInitialExp(SExpCG exp) throws AnalysisException
	{
		// Examples:
		// private int a; (exp == null || exp instanceof AUndefinedExpCG)
		// private int a = 2; (otherwise)

		return exp == null || exp instanceof AUndefinedExpCG ? "Default::default()" : format(exp);
	}
	
	public String escapeChar(char c)
	{
		return GeneralUtils.isEscapeSequence(c) ? StringEscapeUtils.escapeJavaScript(c
				+ "")
				: c + "";
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