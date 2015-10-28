package org.overture.codegen.vdm2rust.Transforms;

import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.AVarDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.patterns.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.APlainCallStmCG;
import org.overture.codegen.cgast.statements.AReturnStmCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class ConstructorTrans extends DepthFirstAnalysisAdaptor {
	TransAssistantCG transAssistant;
	
	public ConstructorTrans(TransAssistantCG transAssistant) {
		this.transAssistant = transAssistant;
	}
	
	@Override
	public void defaultInSClassDeclCG(SClassDeclCG node) throws AnalysisException {
		List<AMethodDeclCG> constructors = node.getMethods().stream()
												.filter(method -> method.getIsConstructor())
												.collect(Collectors.toList());
		
		AMethodDeclCG selectedConstructor = null;
		if(constructors.size() > 1) {
			selectedConstructor = constructors.stream()
									.filter(method -> method.getFormalParams().size() > 0)
									.findFirst()
									.get();			
			 
			// Rename here to make sure this constructor does not compare equal to other constructors (nodes .equals is based on string repr).
			// conventional name in Rust.
			selectedConstructor.setName("new");
		
			constructors.remove(selectedConstructor);
			// remove non-selected constructors.
			for(AMethodDeclCG condemned : constructors) {
				node.removeChild(condemned);
			}
		} else {
			selectedConstructor = constructors.get(0);
		}
		
		transformConstructor(selectedConstructor);
	}

	protected void transformConstructor(AMethodDeclCG node) {
		/*
		 * let mut instance: ClassT = ClassT::default();
		 * [instance.cg_init_x(<args>)]
		 * return instance  
		 */
		
		// the new function/constructor is an associated function.
		node.setStatic(true);
		
		SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
		SStmCG oldBody = node.getBody();
		boolean hasCustomConstructor = oldBody instanceof APlainCallStmCG;
		
		AClassTypeCG type = new AClassTypeCG();
		type.setName(enclosingClass.getName());
		type.setOptional(false);
		
		ABlockStmCG newBody = new ABlockStmCG();
		
		AExplicitVarExpCG defaultFunc = new AExplicitVarExpCG();
		defaultFunc.setName("default");
		defaultFunc.setType(type.clone());
		defaultFunc.setClassType(type.clone());			
		
		AApplyExpCG applyDefaultFunc = new AApplyExpCG();
		applyDefaultFunc.setType(type.clone());
		applyDefaultFunc.setRoot(defaultFunc);
		
		AIdentifierPatternCG objPattern = new AIdentifierPatternCG();
		objPattern.setName("instance");
		
		AVarDeclCG classVar = new AVarDeclCG();
		classVar.setFinal(!hasCustomConstructor);
		classVar.setExp(applyDefaultFunc);
		classVar.setPattern(objPattern);	
		classVar.setType(type.clone());
					
		newBody.getLocalDefs().add(classVar);
		
		AIdentifierVarExpCG instanceVarExp = new AIdentifierVarExpCG();
		instanceVarExp.setIsLocal(true);
		instanceVarExp.setName("instance");
		instanceVarExp.setType(type.clone());
		instanceVarExp.setIsLambda(false);
		
		if(hasCustomConstructor)
		{
			// we need to call the custom constructor in the new body too.
			APlainCallStmCG cg_init_call_old = (APlainCallStmCG)oldBody;
			
			ACallObjectExpStmCG cg_init_call = new ACallObjectExpStmCG();
			cg_init_call.setArgs(cg_init_call_old.getArgs());
			cg_init_call.setFieldName(cg_init_call_old.getName());
			cg_init_call.setType(new AVoidTypeCG());
			cg_init_call.setObj(instanceVarExp.clone());
			
			newBody.getStatements().add(cg_init_call);
		}
		
		AReturnStmCG returnStm = new AReturnStmCG();
		returnStm.setExp(instanceVarExp.clone());
		newBody.getStatements().add(returnStm);
		
		node.setBody(newBody);
	}
}
