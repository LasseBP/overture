package org.overture.codegen.vdm2rust.Transforms;

import java.util.Arrays;

import org.overture.ast.expressions.AStringLiteralExp;
import org.overture.codegen.assistant.ExpAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ADistConcatUnaryExpCG;
import org.overture.codegen.cgast.expressions.AElemsUnaryExpCG;
import org.overture.codegen.cgast.expressions.AEnumSeqExpCG;
import org.overture.codegen.cgast.expressions.AHeadUnaryExpCG;
import org.overture.codegen.cgast.expressions.AIndicesUnaryExpCG;
import org.overture.codegen.cgast.expressions.ALenUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMapSeqGetExpCG;
import org.overture.codegen.cgast.expressions.AReverseUnaryExpCG;
import org.overture.codegen.cgast.expressions.ASeqConcatBinaryExpCG;
import org.overture.codegen.cgast.expressions.ASeqModificationBinaryExpCG;
import org.overture.codegen.cgast.expressions.AStringLiteralExpCG;
import org.overture.codegen.cgast.expressions.ASubSeqExpCG;
import org.overture.codegen.cgast.expressions.ATailUnaryExpCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.AMapSeqStateDesignatorCG;
import org.overture.codegen.cgast.statements.AMapSeqUpdateStmCG;
import org.overture.codegen.cgast.types.ACharBasicTypeCG;
import org.overture.codegen.cgast.types.ASeqSeqTypeCG;
import org.overture.codegen.cgast.types.AStringTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.ConstructionUtils;

public class VdmSeqTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	
	public VdmSeqTrans(TransAssistantCG transAss) {
		transAssistant = transAss;
	}
	
	@Override
	public void outAEnumSeqExpCG(AEnumSeqExpCG node) throws AnalysisException {
		ASeqSeqTypeCG declaredType = (ASeqSeqTypeCG)ExpAssistantCG.getDeclaredType(node, transAssistant.getInfo());
		STypeCG seqOf = declaredType != null ? declaredType.getSeqOf() : ((ASeqSeqTypeCG)node.getType()).getSeqOf();
		
		AApplyExpCG n = null;
		
		if(node.getSourceNode() != null && node.getSourceNode().getVdmNode() instanceof AStringLiteralExp && seqOf instanceof ACharBasicTypeCG) {
			AStringLiteralExp stringNode = (AStringLiteralExp)node.getSourceNode().getVdmNode();
			AStringLiteralExpCG cgStringNode = new AStringLiteralExpCG();
			cgStringNode.setIsNull(false);
			cgStringNode.setType(new AStringTypeCG());
			cgStringNode.setSourceNode(node.getSourceNode());
			cgStringNode.setValue(stringNode.getValue().getValue());
			
			n = ConstructionUtils.createVariadicExternalExp(node, Arrays.asList(cgStringNode), "strseq!");			
		}else {
			n = ConstructionUtils.createVariadicExternalExp(node, node.getMembers(), "seq!", seqOf.clone());
		}		
				
		transAssistant.replaceNodeWith(node, n);
	}

	@Override
	public void outAHeadUnaryExpCG(AHeadUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "head");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outATailUnaryExpCG(ATailUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "head");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outASubSeqExpCG(ASubSeqExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getSeq(), "sub_seq", node.getFrom(), node.getTo());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outALenUnaryExpCG(ALenUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "len");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outAElemsUnaryExpCG(AElemsUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "elems");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outAIndicesUnaryExpCG(AIndicesUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "inds");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outAReverseUnaryExpCG(AReverseUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "reverse");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outASeqConcatBinaryExpCG(ASeqConcatBinaryExpCG node)
		throws AnalysisException {		
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "conc", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outADistConcatUnaryExpCG(ADistConcatUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "dconc");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outASeqModificationBinaryExpCG(ASeqModificationBinaryExpCG node)
		throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "modify", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void outAApplyExpCG(AApplyExpCG node) throws AnalysisException {
		if(node.getRoot().getType() instanceof ASeqSeqTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getRoot(), "get", node.getArgs());
			transAssistant.replaceNodeWith(node, n);
		}
	}
	
	@Override
	public void outAMapSeqGetExpCG(AMapSeqGetExpCG node)
			throws AnalysisException {
		if(node.getCol().getType() instanceof ASeqSeqTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getCol(), "get", node.getIndex());
			transAssistant.replaceNodeWith(node, n);
		}
	}

	@Override
	public void outAMapSeqStateDesignatorCG(AMapSeqStateDesignatorCG node)
			throws AnalysisException {
		throw new AnalysisException("MapSeqStateDesignator nodes should not exist in the IR at this stage.");
	}
	
	@Override
	public void outAMapSeqUpdateStmCG(AMapSeqUpdateStmCG node)
			throws AnalysisException {
		if(node.getCol().getType() instanceof ASeqSeqTypeCG) {
			ACallObjectExpStmCG putCall = new ACallObjectExpStmCG();
			putCall.setType(new AVoidTypeCG());
			putCall.setObj(node.getCol());
			putCall.setFieldName("put");
			putCall.setSourceNode(node.getSourceNode());
			putCall.getArgs().add(node.getIndex());
			putCall.getArgs().add(node.getValue());
			
			transAssistant.replaceNodeWith(node, putCall);
		}
	}
}
