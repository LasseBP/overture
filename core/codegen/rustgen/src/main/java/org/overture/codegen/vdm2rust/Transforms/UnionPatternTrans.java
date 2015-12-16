package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.ACaseAltExpExpCG;
import org.overture.codegen.cgast.expressions.ACasesExpCG;
import org.overture.codegen.cgast.patterns.AVariantPatternCG;
import org.overture.codegen.cgast.types.AUnionTypeCG;

public class UnionPatternTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void outACasesExpCG(ACasesExpCG node) throws AnalysisException {
		if(node.getExp().getType() instanceof AUnionTypeCG) {
			AUnionTypeCG unionType = (AUnionTypeCG)node.getExp().getType();
			
			for(ACaseAltExpExpCG caseAlt : node.getCases()) {
				int typeIndex = unionType.getTypes().indexOf(caseAlt.getPatternType());
				
				if(typeIndex == -1) {
					throw new AnalysisException("Type was not found in union type for expression of match-expression.");
				}
				
				AVariantPatternCG variantPattern = new AVariantPatternCG();
				variantPattern.setEnumType(unionType.clone());
				variantPattern.setInner(caseAlt.getPattern().clone());
				variantPattern.setName(UnionDeclTrans.CHOICEPREFIX + Integer.toString(typeIndex));
				variantPattern.setSourceNode(caseAlt.getPattern().getSourceNode());
				caseAlt.setPattern(variantPattern);
			}
		}
	}
}
