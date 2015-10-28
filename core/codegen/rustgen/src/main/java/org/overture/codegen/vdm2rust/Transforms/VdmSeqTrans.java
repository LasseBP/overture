package org.overture.codegen.vdm2rust.Transforms;


import org.overture.codegen.cgast.SExpCG;
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
import org.overture.codegen.cgast.expressions.ATailUnaryExpCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.AMapSeqStateDesignatorCG;
import org.overture.codegen.cgast.statements.AMapSeqUpdateStmCG;
import org.overture.codegen.cgast.types.ASeqSeqTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.ConstructionUtils;

public class VdmSeqTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	
	public VdmSeqTrans(TransAssistantCG transAss) {
		transAssistant = transAss;
	}
	
	@Override
	public void inAEnumSeqExpCG(AEnumSeqExpCG node) throws AnalysisException {
		AApplyExpCG n = ConstructionUtils.createVariadicExternalExp(node, node.getMembers(), "seq!");		
		transAssistant.replaceNodeWith(node, n);
	}

	@Override
	public void inAHeadUnaryExpCG(AHeadUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "head");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inATailUnaryExpCG(ATailUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "head");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inALenUnaryExpCG(ALenUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "len");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAElemsUnaryExpCG(AElemsUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "elems");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAIndicesUnaryExpCG(AIndicesUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "inds");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAReverseUnaryExpCG(AReverseUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "reverse");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inASeqConcatBinaryExpCG(ASeqConcatBinaryExpCG node)
		throws AnalysisException {		
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "conc", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inADistConcatUnaryExpCG(ADistConcatUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "dconc");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inASeqModificationBinaryExpCG(ASeqModificationBinaryExpCG node)
		throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "modify", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAApplyExpCG(AApplyExpCG node) throws AnalysisException {
		if(node.getRoot().getType() instanceof ASeqSeqTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getRoot(), "get", node.getArgs());
			transAssistant.replaceNodeWith(node, n);
		}
	}
	
	@Override
	public void inAMapSeqGetExpCG(AMapSeqGetExpCG node)
			throws AnalysisException {
		if(node.getCol().getType() instanceof ASeqSeqTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getCol(), "get", node.getIndex());
			transAssistant.replaceNodeWith(node, n);
		}
	}

	@Override
	public void inAMapSeqStateDesignatorCG(AMapSeqStateDesignatorCG node)
			throws AnalysisException {
		throw new AnalysisException("MapSeqStateDesignator nodes should not exist in the IR at this stage.");
	}
	
	@Override
	public void inAMapSeqUpdateStmCG(AMapSeqUpdateStmCG node)
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
