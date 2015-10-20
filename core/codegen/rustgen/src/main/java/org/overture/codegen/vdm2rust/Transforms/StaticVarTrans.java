package org.overture.codegen.vdm2rust.Transforms;

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
		
		if(fieldDecl != null){
			boolean isInstanceVar = !fieldDecl.getStatic(); 
			boolean isStaticVar = !isInstanceVar && !fieldDecl.getFinal();
			boolean isValue = !isInstanceVar && fieldDecl.getFinal();			
			
			if(isValue || isStaticVar) {
				AStaticVarExpCG staticVarExp = new AStaticVarExpCG();
				staticVarExp.setIsFinal(isValue);
				staticVarExp.setIsLocal(false);
				staticVarExp.setIsLambda(false);
				staticVarExp.setName(memberName);
				staticVarExp.setPackage(declaringClass.getPackage());
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
}
