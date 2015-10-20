package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;

public class InitialExpTrans extends DepthFirstAnalysisAdaptor {
	
	/*
	 * For instance vars: In the initial expressions other instance vars must be refered to as local, 
	 * since they are first assigned to the instance, when all instance vars have values. Example:
	 
		impl Default for A {
			fn default() -> A {
				let var1: T1 = ...;
				let var2: T1 = var1 + ...;
						
				A { var1: var1, var2: var2 }
			}
		} 
	 */
	
	@Override
	public void caseAIdentifierVarExpCG(AIdentifierVarExpCG node) throws AnalysisException {
		AFieldDeclCG fieldDecl = node.getAncestor(AFieldDeclCG.class);
		
		if(fieldDecl != null){
			boolean isInstanceVar = !fieldDecl.getStatic(); 	
			
			if(isInstanceVar){
				// this is in the initial expression for an instance variable.
				// force identifier to be local.
				node.setIsLocal(true);
			} 
		}
	}
}
