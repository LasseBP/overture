package org.overture.codegen.vdm2rust;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.SPatternCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.declarations.AUnionEnumDeclCG;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.ALetDefExpCG;
import org.overture.codegen.cgast.name.ATypeNameCG;
import org.overture.codegen.cgast.patterns.ARecordPatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.types.ARecordTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.ir.IRInfo;

public class FormatUtils {
	
	IRInfo info;
	
	public FormatUtils(IRInfo info) {
		this.info = info;
	}
	
	public static boolean isScoped(ABlockStmCG block)
	{
		return block != null && block.getScoped() != null && block.getScoped();
	}
	
	public static boolean isScoped(ALetDefExpCG block)
	{
		return StmAssistantCG.isScoped(block);
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
	
	public LinkedHashMap<String, SPatternCG> getNamePatternPairs(ARecordPatternCG pattern) {
		ARecordTypeCG recordType = (ARecordTypeCG)pattern.getType();
		ATypeNameCG typeName = recordType.getName();
		SClassDeclCG definingClass = info.getClass(typeName.getDefiningClass());
		ARecordDeclCG recordTypeDecl = definingClass.getTypeDecls()
													.stream()
													.filter(decl -> decl.getDecl() instanceof ARecordDeclCG)
													.map(decl -> (ARecordDeclCG)decl.getDecl())
													.filter(recDecl -> recDecl.getName().equals(typeName.getName()))
													.findFirst()
													.get();
		
		//linked hashmaps are iterated in insertion order
		LinkedHashMap<String, SPatternCG> namePatternMap = new LinkedHashMap<>();
		
		Iterator<AFieldDeclCG> fieldIter = recordTypeDecl.getFields().iterator();
		Iterator<SPatternCG> patternIter = pattern.getPatterns().iterator();
		while(fieldIter.hasNext() && patternIter.hasNext()) {
			namePatternMap.put(fieldIter.next().getName(), patternIter.next());
		}		
		
		return namePatternMap;
	}
}
