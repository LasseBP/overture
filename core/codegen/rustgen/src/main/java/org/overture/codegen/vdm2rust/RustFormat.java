package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.overture.ast.util.ClonableString;
import org.overture.codegen.assistant.TypeAssistantCG;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AUndefinedExpCG;
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
	
	public static String getQuoteModuleName()
	{
		return "quotes";
	}

	public MergeVisitor GetMergeVisitor()
	{
		return mergeVisitor;
	}
	
	public String format(INode node) throws AnalysisException
	{	
		StringWriter writer = new StringWriter();
		
		if(node instanceof STypeCG) {
			//special case
			formatType((STypeCG)node, writer);
		} else {
			node.apply(mergeVisitor, writer);
		}
		
		return writer.toString();
	}

	protected void formatType(STypeCG node, StringWriter writer) throws AnalysisException {
		boolean isOptional = node.getOptional() != null ? node.getOptional() : false;
		
		if(isOptional) {
			writer.write("Option<");
		}
		
		String definingClassName = TypeAssistantCG.getDefiningClass(node);
		if(definingClassName != null && !definingClassName.isEmpty()) {
			SClassDeclCG definingClass = info.getClass(definingClassName);
			SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
			
			//specify which module type was defined in, if it is not the enclosing module.
			if(definingClass != null && enclosingClass != null && definingClass != enclosingClass) {
				writer.write("::" + definingClass.getPackage() + "::");
			}
		}
		
		if(node.getNamedInvType() != null) {
			//if the type has an alias, print that instead.
			writer.write(node.getNamedInvType().getName().getName());
		} else {
			node.apply(mergeVisitor, writer);
		}
		
		if(isOptional) {
			writer.write(">");
		}
	}
	
	public String formatList(List<? extends INode> nodes)
			throws AnalysisException
	{
		if (nodes.isEmpty())
		{
			return "";
		}
		
		INode firstnodes = nodes.get(0);

		StringWriter writer = new StringWriter();
		writer.append(format(firstnodes));

		for (int i = 1; i < nodes.size(); i++)
		{
			INode currentnodes = nodes.get(i);

			writer.append(", " + format(currentnodes));
		}

		String result = writer.toString();
		
		return result;
	}
	
	public String formatGenericTypes(List<STypeCG> types)
			throws AnalysisException
	{
		if (types.isEmpty())
		{
			return "";
		}

		return "<" + formatList(types) + ">";
	}

	public String formatInitialExp(SExpCG exp) throws AnalysisException
	{
		// Examples:
		// private int a; (exp == null || exp instanceof AUndefinedExpCG)
		// private int a = 2; (otherwise)

		return exp == null || exp instanceof AUndefinedExpCG ? "Default::default()" : format(exp);
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
	
	public String escapeChar(char c)
	{
		return GeneralUtils.isEscapeSequence(c) ? StringEscapeUtils.escapeJavaScript(c
				+ "")
				: c + "";
	}
}