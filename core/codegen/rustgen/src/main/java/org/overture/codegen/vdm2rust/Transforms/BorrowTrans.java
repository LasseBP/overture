package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.ast.expressions.ADomainResByBinaryExp;
import org.overture.ast.expressions.ADomainResToBinaryExp;
import org.overture.ast.expressions.AInSetBinaryExp;
import org.overture.ast.expressions.APlusPlusBinaryExp;
import org.overture.ast.expressions.ARangeResByBinaryExp;
import org.overture.ast.expressions.ARangeResToBinaryExp;
import org.overture.ast.expressions.ASeqConcatBinaryExp;
import org.overture.ast.expressions.ASetDifferenceBinaryExp;
import org.overture.ast.expressions.ASetUnionBinaryExp;
import org.overture.codegen.assistant.AssistantBase;
import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AVarDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AExternalExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ALambdaExpCG;
import org.overture.codegen.cgast.expressions.ALetDefExpCG;
import org.overture.codegen.cgast.expressions.ASelfExpCG;
import org.overture.codegen.cgast.expressions.AStaticVarExpCG;
import org.overture.codegen.cgast.expressions.SLiteralExpCG;
import org.overture.codegen.cgast.patterns.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.ir.SourceNode;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class BorrowTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	int i = 0;
	
	public BorrowTrans(TransAssistantCG transAss) {
		transAssistant = transAss;
	}
	
	@Override
	public void outAApplyExpCG(AApplyExpCG node) throws AnalysisException {
						
		if(isBorrowSafe(node.getRoot(), node.getArgs(), node.getSourceNode())) {
			return;
		}
		
		i = 0;
		List<AVarDeclCG> temps = node.getArgs().stream()
							 .filter(arg -> !(arg instanceof ALambdaExpCG))
							 .map(arg -> argToVarDecl(arg))
							 .collect(Collectors.toList());		
		
		if(!temps.isEmpty()) {
			i = 0;
			List<SExpCG> newArgs = node.getArgs().stream() 
										 .map(arg -> argToVarExp(arg))
										 .collect(Collectors.toList());
			
			node.getArgs().clear();
			node.getArgs().addAll(newArgs);
			
			ALetDefExpCG block = new ALetDefExpCG();
			block.getLocalDefs().addAll(temps);
			block.setSourceNode(node.getSourceNode());
			block.setExp(node.clone());
			block.setType(node.getType().clone());
			
			transAssistant.replaceNodeWith(node, block);		
		}
	}
	
	

	@Override
	public void outACallObjectExpStmCG(ACallObjectExpStmCG node) throws AnalysisException {
		//problem: <exprX>.op(<expr with exprX>) -> <exprX> already borrowed as mutable.
		
		//simple solution: create temporaries always.
		
		//better solution: 
		//break objExp into expressions.
		//check args for occurences of expressions in each arg.
		//create temp vars for args, which contains expressions from the objExp.
		
		if(isBorrowSafe(node.getObj(), node.getArgs(), node.getSourceNode())) {
			return;
		}
				
		i = 0;
		List<AVarDeclCG> temps = node.getArgs().stream()
							 .filter(arg -> !(arg instanceof ALambdaExpCG))
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
	
	private static boolean isBorrowSafe(SExpCG root, LinkedList<SExpCG> args, SourceNode source) {
		//TODO: this is a mess.
		return root instanceof AExternalExpCG ||
			   root instanceof AExplicitVarExpCG ||
			   root instanceof AStaticVarExpCG ||
			   borrowSafeRootSource(source) ||
			   args.stream().allMatch(BorrowTrans::argIsBorrowSafe) ||
			   (root instanceof ASelfExpCG && args.stream().noneMatch(arg -> hasDescendantOfType(arg, ASelfExpCG.class)));
	}
	
	private static boolean argIsBorrowSafe(SExpCG arg) {
		return arg instanceof SLiteralExpCG ||
			   arg instanceof AExplicitVarExpCG ||
			   arg instanceof AStaticVarExpCG ||
			   (arg instanceof AIdentifierVarExpCG && ((AIdentifierVarExpCG)arg).getIsLocal() && !(arg.getType() instanceof AClassTypeCG));
	}
	
	private static boolean borrowSafeRootSource(SourceNode source) {
		if(source != null && source.getVdmNode() != null) {
			org.overture.ast.node.INode sourceNode = source.getVdmNode();
			return 	sourceNode instanceof ASeqConcatBinaryExp ||
					sourceNode instanceof ASetDifferenceBinaryExp ||
					sourceNode instanceof ASetUnionBinaryExp || 
					sourceNode instanceof AInSetBinaryExp ||
					sourceNode instanceof APlusPlusBinaryExp || //map override
					sourceNode instanceof ADomainResByBinaryExp ||
					sourceNode instanceof ADomainResToBinaryExp ||
					sourceNode instanceof ARangeResByBinaryExp ||
					sourceNode instanceof ARangeResToBinaryExp;
		}		
		return false;
	}
	
	public static boolean hasDescendantOfType(INode ancestor, Class<? extends INode> type) {
		try {
			return AssistantBase.hasDescendantOfType(ancestor, type);
		} catch (AnalysisException e) {
			e.printStackTrace();
			return false;
		}
	}
			
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
		if(arg instanceof ALambdaExpCG) {
			return arg;
		}
		
		AIdentifierVarExpCG varExp = new AIdentifierVarExpCG();
		varExp.setIsLambda(false);
		varExp.setIsLocal(true);
		varExp.setType(arg.getType().clone());
		varExp.setName("arg" + i);
		i += 1;
		
		return varExp;
	}
}
