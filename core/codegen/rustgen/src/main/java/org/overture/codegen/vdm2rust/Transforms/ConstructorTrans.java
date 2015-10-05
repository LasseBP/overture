package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;

public class ConstructorTrans extends DepthFirstAnalysisAdaptor {
	@Override
	public void caseAMethodDeclCG(AMethodDeclCG node) throws AnalysisException {
		
		if(node.getIsConstructor())
		{
			node.setName("new");
			//TODO: Transform body to return new, might need new IR.			
		}
	}
}
