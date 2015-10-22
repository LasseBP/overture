package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;

import org.overture.codegen.cgast.PCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ASelfExpCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.APlainCallStmCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class FuncScopeAdderTrans extends DepthFirstAnalysisAdaptor {
	
	TransAssistantCG transAssistant;
	IRInfo irInfo;
	
	public FuncScopeAdderTrans(TransAssistantCG transAssistant) {
		this.transAssistant = transAssistant;
		irInfo = transAssistant.getInfo();
	}
	
	@Override
	public void caseACallObjectExpStmCG(ACallObjectExpStmCG node) throws AnalysisException {
		/*
		 * Given: let a: A = ... in
		 * transform a.func() to A`func() if func really is a function. 
		 */
		
		for(SExpCG arg : node.getArgs()) {
			arg.apply(this);
		}
		
		STypeCG objType = node.getObj().getType();
		if(objType instanceof AClassTypeCG)
		{
			AClassTypeCG objClass = (AClassTypeCG)objType;
			SClassDeclCG clazz = irInfo.getClass(objClass.getName());
			
			if(clazz != null)
			{
				replaceStmIfFunc(node, clazz, objClass, node.getFieldName(), node.getType(), node.getArgs());
			}
		}		
	}	
	
	@Override
	public void caseAApplyExpCG(AApplyExpCG node) throws AnalysisException {
		if(node.getRoot() instanceof AFieldExpCG)
		{
			// self.func() -> SelfT`func()
			AFieldExpCG fieldExp = (AFieldExpCG)node.getRoot();
			STypeCG objType = fieldExp.getObject().getType();
			if(objType instanceof AClassTypeCG)
			{
				AClassTypeCG objClass = (AClassTypeCG)objType;
				SClassDeclCG clazz = irInfo.getClass(objClass.getName());
				
				changeRootToExplicitVar(node, fieldExp, clazz, objClass, fieldExp.getMemberName());
			}			
		}		
		else if(node.getRoot() instanceof AIdentifierVarExpCG)
		{
			/*
			 * funcCall() -> ClazzT`funcCall().
			 */
			
			AIdentifierVarExpCG ident = (AIdentifierVarExpCG)node.getRoot();
			SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
			
			AClassTypeCG classT = new AClassTypeCG();
			classT.setName(enclosingClass.getName());
			
			String funcName = ident.getName();
			
			changeRootToExplicitVar(node, ident, enclosingClass, classT, funcName);			
		}
	}

	protected void changeRootToExplicitVar(AApplyExpCG node, SExpCG rootExp, SClassDeclCG definingClass,
			AClassTypeCG classT, String fieldName) {
		boolean isFunction = definingClass.getFunctions()
				.stream()
				.anyMatch(func -> func.getName().equals(fieldName));
		
		boolean isStaticOperation = definingClass.getMethods()
				.stream()
				.anyMatch(method -> method.getStatic() && method.getName().equals(fieldName));
		
		if(isFunction || isStaticOperation) {
			AExplicitVarExpCG explIdent = new AExplicitVarExpCG();
			explIdent.setClassType(classT);
			explIdent.setName(fieldName);
			explIdent.setIsLambda(false);
			explIdent.setIsLocal(false);
			explIdent.setSourceNode(rootExp.getSourceNode());
			explIdent.setType(rootExp.getType());
			
			node.replaceChild(rootExp, explIdent);				
		}
	}	
	
	@Override
	public void caseAPlainCallStmCG(APlainCallStmCG node) throws AnalysisException {
		
		/*
		 * opCall() -> self.opCall().
		 */
		
		for(SExpCG arg : node.getArgs()) {
			arg.apply(this);
		}
		
		SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
		
		/*
		 * Check enclosing class for operations of the same name.
		 * If this is intended to call a free-standing function of 
		 * the same name (added manually to the IR), then this will
		 * not give the intended result.
		 */
		boolean isOperation = enclosingClass.getMethods()
				.stream()
				.anyMatch(method -> method.getName().equals(node.getName()));
		
		if(isOperation) {
			SStmCG call;
			if(node.getIsStatic()) {
				if(node.getClassType() == null) {
					//classtype is missing - add it.
					AClassTypeCG classT = new AClassTypeCG();
					classT.setName(enclosingClass.getName());
					node.setClassType(classT);
				}
			} else {
				ACallObjectExpStmCG selfCall = new ACallObjectExpStmCG();
				selfCall.setObj(new ASelfExpCG());
				selfCall.setFieldName(node.getName());
				selfCall.setType(node.getType().clone());			
				selfCall.setSourceNode(node.getSourceNode());
				selfCall.getArgs().addAll(node.getArgs());
				
				call = selfCall;
				if(node.parent() != null) {
					node.parent().replaceChild(node, call);
				}
				else {
					Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
				}
			}
			
			
		}
		// else: this is a free-standing function added by some IR transformation.
	}
	
	protected void replaceStmIfFunc(PCG node, SClassDeclCG enclosingClass, AClassTypeCG classT, String funcName,
			STypeCG funcReturnType, LinkedList<SExpCG> args) {
		/*
		 * Check enclosing class for functions of the same name.
		 * If this is intended to call a free-standing function of 
		 * the same name (added manually to the IR), then this will
		 * not give the intended result.
		 */
		
		boolean isFunction = enclosingClass.getFunctions()
				.stream()
				.anyMatch(func -> func.getName().equals(funcName));
		
		boolean isStaticOperation = enclosingClass.getMethods()
				.stream()
				.anyMatch(method -> method.getStatic() && method.getName().equals(funcName));
		
		if(isFunction || isStaticOperation) {
			
			SStmCG plainCall = transAssistant.consStaticCall(classT, 
															funcName, 
															funcReturnType, 
															args);
			plainCall.setSourceNode(node.getSourceNode());
			
			if(node.parent() != null) {
				node.parent().replaceChild(node, plainCall);
			}
			else {
				Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
			}	
		}
	}
}
