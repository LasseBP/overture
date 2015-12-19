package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ASelfExpCG;
import org.overture.codegen.cgast.expressions.AStaticVarExpCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class StaticVarTrans extends DepthFirstAnalysisAdaptor {
	
	IRInfo irInfo;
	TransAssistantCG transAssist;
	
	public StaticVarTrans(TransAssistantCG transAssist) {
		this.transAssist = transAssist;
		this.irInfo = transAssist.getInfo();
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
				
		replaceVarExp(node, node.getName(), enclosingClass);
	}	
	
	@Override
	public void caseAExplicitVarExpCG(AExplicitVarExpCG node) throws AnalysisException {
		SClassDeclCG declaringClass = irInfo.getClass(((AClassTypeCG)node.getClassType()).getName());
		
		if(declaringClass != null) {
			replaceVarExp(node, node.getName(), declaringClass);
		}
	}
	
	@Override
	public void caseAFieldExpCG(AFieldExpCG node) throws AnalysisException {
		STypeCG rootType = node.getObject().getType();
		if(rootType instanceof AClassTypeCG) {
			AClassTypeCG classType = (AClassTypeCG)rootType;
			
			SClassDeclCG declaringClass = irInfo.getClass(classType.getName());
			if(declaringClass != null) {
				replaceVarExp(node, node.getMemberName(), declaringClass);
			}
		}
	}
	
	protected void replaceVarExp(SExpCG node, String memberName, SClassDeclCG declaringClass) {
		if(DeclAssistantCG.isLibraryName(declaringClass.getName())) {
			return;
		}
		
		AFieldDeclCG fieldDecl = declaringClass.getFields()
												.stream()
												.filter(field -> field.getName().equals(memberName))
												.findFirst()
												.orElse(null);
		
		AMethodDeclCG methodDecl = declaringClass.getMethods()
				.stream()
				.filter(field -> field.getName().equals(memberName))
				.findFirst()
				.orElse(null);
		
		
		
		SExpCG newNode = null;
		
		if(node instanceof AIdentifierVarExpCG && methodDecl != null && !methodDecl.getStatic()) {
			newNode = createSelfFieldExp(node, memberName, declaringClass);
		}
		
		if(fieldDecl != null) {
			boolean isValue = fieldDecl.getStatic() && fieldDecl.getFinal();
			
			if(fieldDecl.getStatic()) {
				AStaticVarExpCG staticVarExp = new AStaticVarExpCG();
				staticVarExp.setIsFinal(isValue);
				staticVarExp.setIsLocal(false);
				staticVarExp.setIsLambda(false);
				staticVarExp.setName(memberName);
				
				staticVarExp.setPackage(declaringClass.getPackage());
				staticVarExp.setIsRootPackage(true);
				
				staticVarExp.setType(node.getType());
				staticVarExp.setSourceNode(node.getSourceNode());
				
				newNode = staticVarExp;
			} else {				
				newNode = createSelfFieldExp(node, memberName, declaringClass);
			}
		}	
		
		if(newNode != null) {
			transAssist.replaceNodeWith(node, newNode);
		}
			
					
	}

	protected AFieldExpCG createSelfFieldExp(SExpCG node, String memberName, SClassDeclCG declaringClass) {
		AClassTypeCG classType = new AClassTypeCG();
		classType.setName(declaringClass.getName());
		
		ASelfExpCG selfExp = new ASelfExpCG();
		selfExp.setType(classType);
		
		AFieldExpCG fieldExp = new AFieldExpCG();
		fieldExp.setMemberName(memberName);
		fieldExp.setObject(selfExp);
		fieldExp.setSourceNode(node.getSourceNode());
		fieldExp.setType(node.getType().clone());
		return fieldExp;
	}
}
