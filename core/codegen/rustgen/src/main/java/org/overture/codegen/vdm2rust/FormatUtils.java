package org.overture.codegen.vdm2rust;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;

public class FormatUtils {
	public static boolean isScoped(ABlockStmCG block)
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
		return declaration instanceof ARecordDeclCG;
	}
}
