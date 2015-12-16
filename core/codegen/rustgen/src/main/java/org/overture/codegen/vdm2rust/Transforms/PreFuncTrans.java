package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;

import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.ADefaultClassDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AFuncDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.logging.Logger;

public class PreFuncTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void caseAMethodDeclCG(AMethodDeclCG node) throws AnalysisException {
		ADefaultClassDeclCG enclosingClass = node.getAncestor(ADefaultClassDeclCG.class);
		
		if(enclosingClass == null)
		{
			Logger.getLog().printErrorln("Could not find enclosing class for method: " + node);
			return;
		}
		
		SDeclCG preCond = node.getPreCond();
		if(preCond instanceof AFuncDeclCG)
		{
			//convert to method, to be able to access self through self keyword.			
			AMethodDeclCG preCondMethod = DeclAssistantCG.funcToMethod((AFuncDeclCG) preCond);
			preCondMethod.setStatic(node.getStatic());
			enclosingClass.getMethods().add(preCondMethod);

			if(!node.getStatic()) {
				//No need to pass self as the last argument
				LinkedList<STypeCG> paramTypes = preCondMethod.getMethodType().getParams();
				paramTypes.remove(paramTypes.size() - 1);
				
				LinkedList<AFormalParamLocalParamCG> formalParams = preCondMethod.getFormalParams();
				formalParams.remove(formalParams.size() - 1);
			}
		}
	}
	
	@Override
	public void caseAFuncDeclCG(AFuncDeclCG node) throws AnalysisException {
		ADefaultClassDeclCG enclosingClass = node.getAncestor(ADefaultClassDeclCG.class);
		
		if(enclosingClass == null)
		{
			Logger.getLog().printErrorln("Could not find enclosing class for method: " + node);
			return;
		}
		
		SDeclCG preCond = node.getPreCond();
		if(preCond instanceof AFuncDeclCG)
		{
			enclosingClass.getFunctions().add((AFuncDeclCG)preCond);
		}
	}

}
