package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ACardUnaryExpCG;
import org.overture.codegen.cgast.expressions.ADistIntersectUnaryExpCG;
import org.overture.codegen.cgast.expressions.ADistUnionUnaryExpCG;
import org.overture.codegen.cgast.expressions.AEnumSetExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AInSetBinaryExpCG;
import org.overture.codegen.cgast.expressions.APowerSetUnaryExpCG;
import org.overture.codegen.cgast.expressions.ARangeSetExpCG;
import org.overture.codegen.cgast.expressions.ASetDifferenceBinaryExpCG;
import org.overture.codegen.cgast.expressions.ASetIntersectBinaryExpCG;
import org.overture.codegen.cgast.expressions.ASetProperSubsetBinaryExpCG;
import org.overture.codegen.cgast.expressions.ASetSubsetBinaryExpCG;
import org.overture.codegen.cgast.expressions.ASetUnionBinaryExpCG;
import org.overture.codegen.cgast.types.AExternalTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ARealNumericBasicTypeCG;
import org.overture.codegen.cgast.utils.AInfoExternalType;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.ConstructionUtils;

public class VdmSetTrans extends DepthFirstAnalysisAdaptor {
	
	private TransAssistantCG transAssistant;
	
	public VdmSetTrans(TransAssistantCG transAss) {
		transAssistant = transAss;
	}
	
	@Override
	public void inAEnumSetExpCG(AEnumSetExpCG node) throws AnalysisException {		
		AApplyExpCG n = ConstructionUtils.createVariadicExternalExp(node, node.getMembers(), "set!");		
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAInSetBinaryExpCG(AInSetBinaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "in_set", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}	

	@Override
	public void inASetDifferenceBinaryExpCG(ASetDifferenceBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "difference", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}

	@Override
	public void inASetUnionBinaryExpCG(ASetUnionBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "union", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inASetIntersectBinaryExpCG(ASetIntersectBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "inter", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inASetSubsetBinaryExpCG(ASetSubsetBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "is_subset", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	@Override
	public void inASetProperSubsetBinaryExpCG(ASetProperSubsetBinaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getRight(), "is_psubset", node.getLeft());
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inACardUnaryExpCG(ACardUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "len");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inADistIntersectUnaryExpCG(ADistIntersectUnaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "dinter");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inADistUnionUnaryExpCG(ADistUnionUnaryExpCG node)
			throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "dunion");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inAPowerSetUnaryExpCG(APowerSetUnaryExpCG node) throws AnalysisException {
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "powersets");
		transAssistant.replaceNodeWith(node, n);
	}
	
	@Override
	public void inARangeSetExpCG(ARangeSetExpCG node) throws AnalysisException {
		AInfoExternalType externalTypeInfo = new AInfoExternalType();
		externalTypeInfo.setNamespace("codegen_runtime");
		AExternalTypeCG cgRtSetType = new AExternalTypeCG();
		cgRtSetType.setOptional(false);
		cgRtSetType.setInfo(externalTypeInfo);
		cgRtSetType.setName("Set");
		
		AExplicitVarExpCG rangeFunc = new AExplicitVarExpCG();
		rangeFunc.setClassType(cgRtSetType);
		rangeFunc.setIsLambda(false);
		rangeFunc.setIsLocal(false);
		rangeFunc.setName("range");
		
		AMethodTypeCG funcType = new AMethodTypeCG();
		funcType.setOptional(false);
		funcType.setResult(node.getType().clone());
		funcType.getParams().add(new ARealNumericBasicTypeCG());
		funcType.getParams().add(new ARealNumericBasicTypeCG());
		
		rangeFunc.setType(funcType);
		AApplyExpCG rangeExp = new AApplyExpCG();		
		rangeExp.setRoot(rangeFunc);
		rangeExp.setType(node.getType().clone());
		rangeExp.getArgs().add(node.getFirst().clone());
		rangeExp.getArgs().add(node.getLast().clone());
		rangeExp.setSourceNode(node.getSourceNode());
		
		transAssistant.replaceNodeWith(node, rangeExp);
	}	
}
