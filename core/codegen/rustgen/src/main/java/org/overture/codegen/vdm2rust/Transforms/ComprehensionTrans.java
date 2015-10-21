package org.overture.codegen.vdm2rust.Transforms;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SPatternCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ABoolLiteralExpCG;
import org.overture.codegen.cgast.expressions.ACompMapExpCG;
import org.overture.codegen.cgast.expressions.ACompSeqExpCG;
import org.overture.codegen.cgast.expressions.ACompSetExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ALambdaExpCG;
import org.overture.codegen.cgast.patterns.ASetMultipleBindCG;
import org.overture.codegen.cgast.patterns.ATuplePatternCG;
import org.overture.codegen.cgast.patterns.ATypeMultipleBindCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ASetSetTypeCG;
import org.overture.codegen.cgast.types.ATupleTypeCG;
import org.overture.codegen.logging.Logger;

public class ComprehensionTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void caseACompMapExpCG(ACompMapExpCG node) throws AnalysisException {
		// TODO Auto-generated method stub
		super.caseACompMapExpCG(node);
	}
	
	@Override
	public void caseACompSeqExpCG(ACompSeqExpCG node) throws AnalysisException {
		// TODO Auto-generated method stub
		super.caseACompSeqExpCG(node);
	}
	
	@Override
	public void caseACompSetExpCG(ACompSetExpCG node) throws AnalysisException {
		
		// let result = cart.set_compr(|(x,_y,_z)| x == 1, |(_x,y,z)| y*z);
		
		boolean containsTypeBinds = node.getBindings().stream()
										.anyMatch(mb -> mb instanceof ATypeMultipleBindCG);
		if(containsTypeBinds) {
			throw new AnalysisException("Type binds are not allowed.");
		}
		
		List<ASetMultipleBindCG> setMbs = node.getBindings().stream()
												.map(ASetMultipleBindCG.class::cast)
												.collect(Collectors.toList());
		
		List<SPatternCG> patterns = setMbs.stream()
											.flatMap(mb -> mb.getPatterns().stream())
											.collect(Collectors.toList());
		
		Map<SPatternCG, SExpCG> patternSets = patterns.stream()
											   .collect(Collectors.toMap(Function.identity(), 
															  		pt -> setMbs.stream()
														  					.filter(mb -> mb.getPatterns().contains(pt))
															  				.findFirst().orElse(null).getSet()));
		
		SExpCG setExp = null;
		SPatternCG pattern = null;
		
		if(patterns.size() > 1) {
			//tuple pattern for the Exp and Pred lambdas.
			ATuplePatternCG tupleArgPattern = new ATuplePatternCG();
			tupleArgPattern.getPatterns().addAll(patterns);
			pattern = tupleArgPattern;
				
			//cartesian product of the sets for each pattern			
			AIdentifierVarExpCG cartMacroIdent = new AIdentifierVarExpCG();
			cartMacroIdent.setName("cartesian_set!");
			cartMacroIdent.setIsLambda(false);
			cartMacroIdent.setIsLocal(true); //hack: macros can be called as if local
			
			AMethodTypeCG cartMacroType = new AMethodTypeCG();
			ATupleTypeCG cartSetElemT = new ATupleTypeCG();
			AApplyExpCG cartesianExp = new AApplyExpCG();
			
			for(Map.Entry<SPatternCG, SExpCG> psKvp: patternSets.entrySet()) {
				ASetSetTypeCG setType = (ASetSetTypeCG)psKvp.getValue().getType();
				
				cartesianExp.getArgs().add(psKvp.getValue().clone());
				cartMacroType.getParams().add(setType.clone());
				cartSetElemT.getTypes().add(setType.getSetOf().clone());
			}
			
			ASetSetTypeCG cartSetType = new ASetSetTypeCG();
			cartSetType.setEmpty(false);
			cartSetType.setSetOf(cartSetElemT);
			
			cartMacroType.setResult(cartSetType.clone());
			cartMacroIdent.setType(cartMacroType);
			
			cartesianExp.setType(cartSetType.clone());
			cartesianExp.setRoot(cartMacroIdent);
			setExp = cartesianExp;		
			
		} else {
			setExp = setMbs.get(0).getSet();
			pattern = setMbs.get(0).getPatterns().getFirst();
		}
		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)setExp.getType();
		
		AFormalParamLocalParamCG param = new AFormalParamLocalParamCG();
		param.setPattern(pattern.clone());
		param.setType(setExpT.getSetOf().clone());		
		
		ALambdaExpCG predLambda = new ALambdaExpCG();
		AMethodTypeCG predType = new AMethodTypeCG();
		predType.setResult(new ABoolBasicTypeCG());
		predType.getParams().add(setExpT.getSetOf().clone());
		predLambda.setType(predType.clone());
		predLambda.getParams().add(param.clone());
		if(node.getPredicate() == null) {
			ABoolLiteralExpCG trueExp = new ABoolLiteralExpCG();
			trueExp.setValue(true);
			trueExp.setType(new ABoolBasicTypeCG());
			predLambda.setExp(trueExp);
		} else {
			predLambda.setExp(node.getPredicate().clone());
		}
		
		ALambdaExpCG expLambda = new ALambdaExpCG();
		AMethodTypeCG expType = new AMethodTypeCG();
		expType.setResult(node.getFirst().getType().clone());
		expType.getParams().add(setExpT.getSetOf().clone());
		expLambda.setType(expType.clone());
		expLambda.getParams().add(param.clone());
		expLambda.setExp(node.getFirst().clone());
		
		AMethodTypeCG setCompMType = new AMethodTypeCG();
		expType.setResult(node.getType().clone());
		expType.getParams().add(predType.clone());
		expType.getParams().add(expType.clone());
		
		AFieldExpCG fieldExp = new AFieldExpCG();
		fieldExp.setMemberName("set_compr");
		fieldExp.setObject(setExp);
		fieldExp.setType(setCompMType);
		
		AApplyExpCG compExp = new AApplyExpCG();
		compExp.setRoot(fieldExp);
		compExp.setType(node.getType());
		compExp.setSourceNode(node.getSourceNode());
		compExp.getArgs().add(predLambda);
		compExp.getArgs().add(expLambda);
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, compExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
	}

}
