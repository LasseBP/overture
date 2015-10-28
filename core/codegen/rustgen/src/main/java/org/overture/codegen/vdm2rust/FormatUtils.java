package org.overture.codegen.vdm2rust;

import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.declarations.AUnionEnumDeclCG;
import org.overture.codegen.cgast.expressions.ABlockExpCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;

public class FormatUtils {
	public static boolean isScoped(ABlockStmCG block)
	{
		return block != null && block.getScoped() != null && block.getScoped();
	}
	
	public static boolean isScoped(ABlockExpCG block)
	{
		return block != null && block.getScoped() != null && block.getScoped();
	}
	
	public static boolean isNull(INode node)
	{
		return node == null;
	}
	
	public static boolean isVoidType(STypeCG node)
	{
		return node instanceof AVoidTypeCG;
	}
	
	public static boolean hasAccessInTemplate(SDeclCG declaration)
	{
		return declaration instanceof ARecordDeclCG ||
			   declaration instanceof AUnionEnumDeclCG;
	}
	
	public static List<AFieldDeclCG> getInstanceVarFields(List<AFieldDeclCG> fields)
	{
		return fields.stream()
				.filter(field -> !field.getStatic())
				.collect(Collectors.toList());
	}
	
	public static List<AFieldDeclCG> getStaticVarFields(List<AFieldDeclCG> fields)
	{
		return fields.stream()
				.filter(field -> field.getStatic() && !field.getFinal())
				.collect(Collectors.toList());
	}
	
	public static List<AFieldDeclCG> getValueFields(List<AFieldDeclCG> fields)
	{
		return fields.stream()
				.filter(field -> field.getFinal())
				.collect(Collectors.toList());
	}
}
