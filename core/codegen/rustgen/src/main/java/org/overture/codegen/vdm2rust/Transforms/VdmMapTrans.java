package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ADistMergeUnaryExpCG;
import org.overture.codegen.cgast.expressions.ADomainResByBinaryExpCG;
import org.overture.codegen.cgast.expressions.ADomainResToBinaryExpCG;
import org.overture.codegen.cgast.expressions.AEnumMapExpCG;
import org.overture.codegen.cgast.expressions.AMapDomainUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMapInverseUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMapOverrideBinaryExpCG;
import org.overture.codegen.cgast.expressions.AMapRangeUnaryExpCG;
import org.overture.codegen.cgast.expressions.AMapSeqGetExpCG;
import org.overture.codegen.cgast.expressions.AMapUnionBinaryExpCG;
import org.overture.codegen.cgast.expressions.APowerNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.ARangeResByBinaryExpCG;
import org.overture.codegen.cgast.expressions.ARangeResToBinaryExpCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.statements.AMapSeqStateDesignatorCG;
import org.overture.codegen.cgast.statements.AMapSeqUpdateStmCG;
import org.overture.codegen.cgast.types.AMapMapTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.ConstructionUtils;

public class VdmMapTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	
	public VdmMapTrans(TransAssistantCG transAssistant) {
		this.transAssistant = transAssistant;
	}
	
	@Override
	public void inAEnumMapExpCG(AEnumMapExpCG node) throws AnalysisException {
		AApplyExpCG n = ConstructionUtils.createVariadicExternalExp(node, node.getMembers(), "map!");		
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAMapDomainUnaryExpCG(AMapDomainUnaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "domain");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAMapRangeUnaryExpCG(AMapRangeUnaryExpCG node) throws AnalysisException
	{
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "range");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAMapInverseUnaryExpCG(AMapInverseUnaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "inverse");
		transAssistant.replaceNodeWith(node, n);
	}
	
	
	@Override
	public void inAApplyExpCG(AApplyExpCG node) throws AnalysisException {
		if(node.getRoot().getType() instanceof AMapMapTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getRoot(), "get", node.getArgs());
			transAssistant.replaceNodeWith(node, n);
		}
	}

	
	@Override
	public void inAMapSeqGetExpCG(AMapSeqGetExpCG node)
			throws AnalysisException {
		if(node.getCol().getType() instanceof AMapMapTypeCG) {
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
		if(node.getCol().getType() instanceof AMapMapTypeCG) {
			ACallObjectExpStmCG insertCall = new ACallObjectExpStmCG();
			insertCall.setType(new AVoidTypeCG()); //actually returns Option<V> with old value, but we don't care
			insertCall.setObj(node.getCol());
			insertCall.setFieldName("insert");
			insertCall.setSourceNode(node.getSourceNode());
			insertCall.getArgs().add(node.getIndex());
			insertCall.getArgs().add(node.getValue());
			
			transAssistant.replaceNodeWith(node, insertCall);
		}
	}	
	
	@Override
	public void inAMapOverrideBinaryExpCG(AMapOverrideBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "ovrride", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAMapUnionBinaryExpCG(AMapUnionBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "merge", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inADistMergeUnaryExpCG(ADistMergeUnaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "merge");
		transAssistant.replaceNodeWith(node, n);
	}

	@Override
	public void inADomainResByBinaryExpCG(ADomainResByBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "dom_restrict_by", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inADomainResToBinaryExpCG(ADomainResToBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "dom_restrict_to", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inARangeResByBinaryExpCG(ARangeResByBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "rng_restrict_by", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inARangeResToBinaryExpCG(ARangeResToBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "rng_restrict_to", node.getRight());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAPowerNumericBinaryExpCG(APowerNumericBinaryExpCG node) throws AnalysisException {
		if(node.getLeft().getType() instanceof AMapMapTypeCG) {
			SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "iterate", node.getRight());
			transAssistant.replaceNodeWith(node, n);
		}
	}
}
