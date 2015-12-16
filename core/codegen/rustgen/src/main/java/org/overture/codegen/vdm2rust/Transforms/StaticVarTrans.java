package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.AStaticVarExpCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;

public class StaticVarTrans extends DepthFirstAnalysisAdaptor {
	
	IRInfo irInfo;
	
	public StaticVarTrans(IRInfo irInfo) {
		this.irInfo = irInfo;
	}
	
	/*
	 * For static vars: ??
	 * For values: must be refered to as explicit var exp, with a module name. 
	 */
	
	@Override
	public void caseAIdentifierVarExpCG(AIdentifierVarExpCG node) throws AnalysisException {
		if(node.getIsLocal()) {
			return;
		}
		
		SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
				
		replaceIfStaticVarExp(node, node.getName(), enclosingClass);
	}	
	
	@Override
	public void caseAExplicitVarExpCG(AExplicitVarExpCG node) throws AnalysisException {
		SClassDeclCG declaringClass = irInfo.getClass(((AClassTypeCG)node.getClassType()).getName());
		
		if(declaringClass != null) {
			replaceIfStaticVarExp(node, node.getName(), declaringClass);
		}
	}
	
	@Override
	public void caseAFieldExpCG(AFieldExpCG node) throws AnalysisException {
		STypeCG rootType = node.getObject().getType();
		if(rootType instanceof AClassTypeCG) {
			AClassTypeCG classType = (AClassTypeCG)rootType;
			
			SClassDeclCG declaringClass = irInfo.getClass(classType.getName());
			if(declaringClass != null) {
				replaceIfStaticVarExp(node, node.getMemberName(), declaringClass);
			}
		}
	}
	
	protected void replaceIfStaticVarExp(SExpCG node, String memberName, SClassDeclCG declaringClass) {
		AFieldDeclCG fieldDecl = declaringClass.getFields()
												.stream()
												.filter(field -> field.getName().equals(memberName))
												.findFirst()
												.orElse(null);
		
		if(DeclAssistantCG.isLibraryName(declaringClass.getName())) {
			return;
		}
		
//		 AFuncDeclCG funcDecl = declaringClass.getFunctions()
//				.stream()
//				.filter(func -> func.getName().equals(memberName))
//				.findFirst()
//				.orElse(null);
//		 
//		 // could be static operation
//		 AMethodDeclCG methDecl = declaringClass.getMethods()
//					.stream()
//					.filter(meth -> meth.getName().equals(memberName))
//					.findFirst()
//					.orElse(null);
		  
		boolean replaceNode = false;
		boolean isValue = true;
		
		if(fieldDecl != null) {
			isValue = fieldDecl.getStatic() && fieldDecl.getFinal();
			replaceNode = isValue || fieldDecl.getStatic();			
		}
		
//		if(funcDecl != null) {
//			replaceNode = true;
//			isValue = true;
//		}
//		
//		if(methDecl != null) {
//			replaceNode = methDecl.getStatic();
//			isValue = true;
//		}
			
		if(replaceNode) {
			AStaticVarExpCG staticVarExp = new AStaticVarExpCG();
			staticVarExp.setIsFinal(isValue);
			staticVarExp.setIsLocal(false);
			staticVarExp.setIsLambda(false);
			staticVarExp.setName(memberName);
			
			staticVarExp.setPackage(declaringClass.getPackage());
			staticVarExp.setIsRootPackage(true);
			
			staticVarExp.setType(node.getType());
			staticVarExp.setSourceNode(node.getSourceNode());
			
			if(node.parent() != null) {
				node.parent().replaceChild(node, staticVarExp);
			}
			else {
				Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
			}				
		}			
	}
}
