package org.overture.codegen.vdm2rust.Transforms;

import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AVarDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.patterns.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class BorrowTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	int i = 0;
	
	public BorrowTrans(TransAssistantCG transAss) {
		transAssistant = transAss;
	}
	
	@Override
	public void outAApplyExpCG(AApplyExpCG node) throws AnalysisException {
		// TODO <exprX>(<expr with exprX>) -> <exprX> already borrowed as mutable.
		super.outAApplyExpCG(node);
	}
	
	@Override
	public void outACallObjectExpStmCG(ACallObjectExpStmCG node) throws AnalysisException {
		//problem: <exprX>.op(<expr with exprX>) -> <exprX> already borrowed as mutable.
		
		//simple solution: create temporaries always.
		
		//better solution: 
		//break objExp into expressions.
		//check args for occurences of expressions in each arg.
		//create temp vars for args, which contains expressions from the objExp.
		
		i = 0;
		List<AVarDeclCG> temps = node.getArgs().stream()
							 .map(arg -> argToVarDecl(arg))
							 .collect(Collectors.toList());		
		
		if(!temps.isEmpty()) {
			i = 0;
			List<SExpCG> newArgs = node.getArgs().stream() 
										 .map(arg -> argToVarExp(arg))
										 .collect(Collectors.toList());
			
			node.getArgs().clear();
			node.getArgs().addAll(newArgs);
			
			ABlockStmCG block = new ABlockStmCG();
			block.getLocalDefs().addAll(temps);
			block.setSourceNode(node.getSourceNode());
			block.getStatements().add(node.clone());
			
			transAssistant.replaceNodeWith(node, block);
			block.setScoped(StmAssistantCG.isScoped(block));			
		}
	}
	
	//TODO: tilføj trans for AApplyExpCG, hvis der er tale om en operation.
			
	AVarDeclCG argToVarDecl(SExpCG arg) {
		AIdentifierPatternCG idPattern = new AIdentifierPatternCG();
		idPattern.setName("arg" + i);
		i += 1;
		
		AVarDeclCG varDecl = new AVarDeclCG();
		varDecl.setFinal(true);
		varDecl.setType(arg.getType().clone());
		varDecl.setExp(arg.clone());
		varDecl.setPattern(idPattern);
		varDecl.setSourceNode(arg.getSourceNode());
		
		return varDecl;
	}
	
	SExpCG argToVarExp(SExpCG arg) {
		AIdentifierVarExpCG varExp = new AIdentifierVarExpCG();
		varExp.setIsLambda(false);
		varExp.setIsLocal(true);
		varExp.setType(arg.getType().clone());
		varExp.setName("arg" + i);
		i += 1;
		
		return varExp;
	}
}
