package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.assistant.TypeAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.AAbsUnaryExpCG;
import org.overture.codegen.cgast.expressions.AFloorUnaryExpCG;
import org.overture.codegen.cgast.expressions.APowerNumericBinaryExpCG;
import org.overture.codegen.cgast.expressions.ARealLiteralExpCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ARealNumericBasicTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.ConstructionUtils;

public class NumericTrans extends DepthFirstAnalysisAdaptor {
	TransAssistantCG transAssist;
	
	public NumericTrans(TransAssistantCG transAssist) {
		this.transAssist = transAssist;
	}
	
	@Override
	public void caseARealLiteralExpCG(ARealLiteralExpCG node) throws AnalysisException {
		//literal real expressions may be typed as the narrowest type possible, Nat even, by the type checker.
		node.setType(TypeAssistantCG.getLiteralType(node));
	}
	
	@Override
	public void caseAPowerNumericBinaryExpCG(APowerNumericBinaryExpCG node) throws AnalysisException {
		node.getLeft().apply(this);
		node.getRight().apply(this);
		
		STypeCG leftType = node.getLeft().getType();
		if(TypeAssistantCG.isNumericType(leftType)) {
			AMethodTypeCG methodType = new AMethodTypeCG();
			methodType.setResult(new ARealNumericBasicTypeCG());
			methodType.getParams().add(new ARealNumericBasicTypeCG());
			
			if(!TypeAssistantCG.isRealOrRat(leftType)) {
				SExpCG realExp = TypeConverterTrans.createFromExp(node.getLeft(), new ARealNumericBasicTypeCG(), leftType);
				transAssist.replaceNodeWith(node.getLeft(), realExp);
			}	
			
			SExpCG n = ConstructionUtils.consExpCall(node, node.getLeft(), "pow", methodType, node.getRight());
			transAssist.replaceNodeWith(node, n);
		}
	}
	
	@Override
	public void caseAAbsUnaryExpCG(AAbsUnaryExpCG node) throws AnalysisException {
		node.getExp().apply(this);
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "abs");
		transAssist.replaceNodeWith(node, n);
	}
	
	@Override
	public void caseAFloorUnaryExpCG(AFloorUnaryExpCG node) throws AnalysisException {
		node.getExp().apply(this);
		SExpCG n = ConstructionUtils.consExpCall(node, node.getExp(), "floor");
		transAssist.replaceNodeWith(node, n);
	}
}
