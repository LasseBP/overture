package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AFuncDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ASelfExpCG;
import org.overture.codegen.cgast.patterns.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.APlainCallStmCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;

public class PreCheckTrans extends DepthFirstAnalysisAdaptor {
	@Override
	public void caseAFuncDeclCG(AFuncDeclCG node) throws AnalysisException {
		// insert: assert!(ClazzT::pre_...(<args>))
		
		if(node.getPreCond() instanceof AFuncDeclCG) {
			AFuncDeclCG precondFunc = (AFuncDeclCG)node.getPreCond();
			
			AMethodDeclCG method = DeclAssistantCG.funcToMethod(node);
			
			SClassDeclCG enclosingClass = node.getAncestor(SClassDeclCG.class);
			enclosingClass.getFunctions().remove(node);
			enclosingClass.getMethods().add(method);
			
			AClassTypeCG classT = new AClassTypeCG();
			classT.setName(enclosingClass.getName());			
			
			AExplicitVarExpCG preCondIdent = new AExplicitVarExpCG();
			preCondIdent.setClassType(classT);
			preCondIdent.setName(precondFunc.getName());
			preCondIdent.setIsLambda(false);
			preCondIdent.setIsLocal(false);
			preCondIdent.setType(precondFunc.getMethodType().clone());			
			
			insertPreCondCheck(method, preCondIdent);
		}		
	}
	
	@Override
	public void caseAMethodDeclCG(AMethodDeclCG method) throws AnalysisException {
		// insert: assert!(self.pre_...(<args>))
		
		if(method.getPreCond() instanceof AFuncDeclCG) {
			AFuncDeclCG precondFunc = (AFuncDeclCG)method.getPreCond();			
			
			AFieldExpCG fieldExpr = new AFieldExpCG();
			fieldExpr.setMemberName(precondFunc.getName());
			fieldExpr.setObject(new ASelfExpCG());
			fieldExpr.setType(precondFunc.getMethodType().clone());
			
			insertPreCondCheck(method, fieldExpr);
		}
	}

	protected void insertPreCondCheck(AMethodDeclCG method, SExpCG preCondCallRoot) throws AnalysisException {
		AApplyExpCG preCondCall = new AApplyExpCG();
		preCondCall.setRoot(preCondCallRoot);
		preCondCall.setType(new ABoolBasicTypeCG());
		
		for(AFormalParamLocalParamCG param : method.getFormalParams()) {
			if(param.getPattern() instanceof AIdentifierPatternCG) {
				AIdentifierPatternCG identifier = (AIdentifierPatternCG)param.getPattern();
				AIdentifierVarExpCG identExpr = new AIdentifierVarExpCG();
				identExpr.setIsLambda(false);
				identExpr.setIsLocal(true);
				identExpr.setName(identifier.getName());
				identExpr.setType(param.getType().clone());
				
				preCondCall.getArgs().add(identExpr);
			} else {
				//TODO: handle non-identifier patterns for preconds. Work around: wrap in func
				throw new AnalysisException("Only Identifier patterns are supported for methods with preconditions.");
			}
		}
		
		APlainCallStmCG assertCall = new APlainCallStmCG();
		assertCall.setType(new AVoidTypeCG());
		assertCall.setName("assert!");
		assertCall.setIsStatic(true);
		assertCall.getArgs().add(preCondCall);
		
		SStmCG body = method.getBody();
		ABlockStmCG block = new ABlockStmCG();
		block.getStatements().addFirst(assertCall);
		block.getStatements().addLast(body.clone());
		method.replaceChild(body, block);
		block.setScoped(StmAssistantCG.isScoped(block));
	}
}
