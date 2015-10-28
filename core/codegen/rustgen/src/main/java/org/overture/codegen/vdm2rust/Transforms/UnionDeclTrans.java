package org.overture.codegen.vdm2rust.Transforms;

import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.ANamedTypeDeclCG;
import org.overture.codegen.cgast.declarations.ATypeDeclCG;
import org.overture.codegen.cgast.declarations.AUnionEnumDeclCG;
import org.overture.codegen.cgast.name.ATypeNameCG;
import org.overture.codegen.cgast.types.AUnionTypeCG;

public class UnionDeclTrans extends DepthFirstAnalysisAdaptor {
	@Override
	public void caseANamedTypeDeclCG(ANamedTypeDeclCG node) throws AnalysisException {
		if(node.getType() instanceof AUnionTypeCG && 
		   node.parent() != null && node.parent() instanceof ATypeDeclCG){
			AUnionTypeCG unionType = (AUnionTypeCG)node.getType();
			
			AUnionEnumDeclCG unionEnumDecl = new AUnionEnumDeclCG();			
			unionEnumDecl.setName(node.getName().getName());
			unionEnumDecl.setSourceNode(node.getSourceNode());
			
			i = 0;
			List<ANamedTypeDeclCG> variants = unionType.getTypes()
												.stream()
												.map(UnionDeclTrans::toNamedType)
												.collect(Collectors.toList());
			
			variants.stream().forEach(variant -> variant.getName().setDefiningClass(unionEnumDecl.getName()));
			unionEnumDecl.getVariants().addAll(variants);
			
			node.parent().replaceChild(node, unionEnumDecl);
		}
	}
	
	private static int i = 0;
	private static ANamedTypeDeclCG toNamedType(STypeCG type) {
		ANamedTypeDeclCG namedType = new ANamedTypeDeclCG();
		namedType.setType(type.clone());
		
		ATypeNameCG typeName = new ATypeNameCG();
		typeName.setName("Ch" + Integer.toString(i++));

		namedType.setName(typeName);
		return namedType;
	}
}
