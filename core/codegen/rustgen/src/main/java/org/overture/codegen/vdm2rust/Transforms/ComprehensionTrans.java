package org.overture.codegen.vdm2rust.Transforms;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SMultipleBindCG;
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
import org.overture.codegen.cgast.expressions.AMapletExpCG;
import org.overture.codegen.cgast.expressions.ATupleExpCG;
import org.overture.codegen.cgast.patterns.ASetMultipleBindCG;
import org.overture.codegen.cgast.patterns.ATuplePatternCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AMapMapTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ASetSetTypeCG;
import org.overture.codegen.cgast.types.ATupleTypeCG;
import org.overture.codegen.logging.Logger;

public class ComprehensionTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void caseACompMapExpCG(ACompMapExpCG node) throws AnalysisException {
		List<ASetMultipleBindCG> setMbs = castToSetMBList(node.getBindings());
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = getCartesianSetExp(setMbs);		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		//create lambdas
		SPatternCG argPattern = getArgPattern(setMbs);
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, node.getPredicate());		
		ALambdaExpCG firstLambda = createFirstLambdaExp(setExpT, param, convertMapletToTuple(node.getFirst()));

		replaceCompWithMethodCallExp(node, cartSetExp, predLambda, firstLambda, "map_compr");
	}

	@Override
	public void caseACompSeqExpCG(ACompSeqExpCG node) throws AnalysisException {
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = node.getSetBind().getSet();		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		//create lambdas
		SPatternCG argPattern = node.getSetBind().getPattern();
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, node.getPredicate());		
		ALambdaExpCG firstLambda = createFirstLambdaExp(setExpT, param, node.getFirst());

		replaceCompWithMethodCallExp(node, cartSetExp, predLambda, firstLambda, "seq_compr");
	}
	
	@Override
	public void caseACompSetExpCG(ACompSetExpCG node) throws AnalysisException {
		// goal:
		// cartesian_set!(s1,s2,s3).set_compr(|(x,_y,_z)| x == 1, |(_x,y,z)| y*z);
		
		List<ASetMultipleBindCG> setMbs = castToSetMBList(node.getBindings());
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = getCartesianSetExp(setMbs);		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		//create lambdas
		SPatternCG argPattern = getArgPattern(setMbs);
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, node.getPredicate());		
		ALambdaExpCG firstLambda = createFirstLambdaExp(setExpT, param, node.getFirst());

		replaceCompWithMethodCallExp(node, cartSetExp, predLambda, firstLambda, "set_compr");
	}
	
	protected SExpCG convertMapletToTuple(AMapletExpCG first) {
		ATupleExpCG tupleExp = new ATupleExpCG();
		tupleExp.getArgs().add(first.getLeft());
		tupleExp.getArgs().add(first.getRight());
		
		AMapMapTypeCG mapType = (AMapMapTypeCG)first.getType();
		ATupleTypeCG tupleType = new ATupleTypeCG();
		tupleType.getTypes().add(mapType.getFrom());
		tupleType.getTypes().add(mapType.getTo());
		
		tupleExp.setTupleType(tupleType.clone());
		tupleExp.setType(tupleType);
		
		return tupleExp;
	}

	protected void replaceCompWithMethodCallExp(SExpCG node, SExpCG cartSetExp, ALambdaExpCG predLambda,
			ALambdaExpCG firstLambda, String comp_method_name) {
		AMethodTypeCG setCompMType = new AMethodTypeCG();
		setCompMType.setResult(node.getType().clone());
		setCompMType.getParams().add(predLambda.getType().clone());
		setCompMType.getParams().add(firstLambda.getType().clone());
		
		AFieldExpCG fieldExp = new AFieldExpCG();
		fieldExp.setMemberName(comp_method_name);
		fieldExp.setObject(cartSetExp);
		fieldExp.setType(setCompMType);
		
		AApplyExpCG compExp = new AApplyExpCG();
		compExp.setRoot(fieldExp);
		compExp.setType(node.getType());
		compExp.setSourceNode(node.getSourceNode());
		compExp.getArgs().add(predLambda);
		compExp.getArgs().add(firstLambda);
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, compExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
	}

	protected ALambdaExpCG createFirstLambdaExp(ASetSetTypeCG setExpT, AFormalParamLocalParamCG param, SExpCG firstExp) {
		ALambdaExpCG expLambda = new ALambdaExpCG();
		AMethodTypeCG expType = new AMethodTypeCG();
		expType.setResult(firstExp.getType().clone());
		expType.getParams().add(setExpT.getSetOf().clone());
		expLambda.setType(expType.clone());
		expLambda.getParams().add(param.clone());
		expLambda.setExp(firstExp);
		return expLambda;
	}

	protected ALambdaExpCG createPredicateLambdaExp(ASetSetTypeCG setExpT, AFormalParamLocalParamCG param,
			SExpCG predicate) {
		
		ALambdaExpCG predLambda = new ALambdaExpCG();
		AMethodTypeCG predType = new AMethodTypeCG();
		predType.setResult(new ABoolBasicTypeCG());
		predType.getParams().add(setExpT.getSetOf().clone());
		predLambda.setType(predType);
		predLambda.getParams().add(param.clone());
		
		if(predicate == null) {
			ABoolLiteralExpCG trueExp = new ABoolLiteralExpCG();
			trueExp.setValue(true);
			trueExp.setType(new ABoolBasicTypeCG());
			predLambda.setExp(trueExp);
		} else {
			predLambda.setExp(predicate);
		}
		
		return predLambda;
	}

	protected AFormalParamLocalParamCG createLambdaParam(SPatternCG pattern, ASetSetTypeCG setExpT) {
		AFormalParamLocalParamCG param = new AFormalParamLocalParamCG();
		param.setPattern(pattern.clone());
		param.setType(setExpT.getSetOf().clone());
		return param;
	}

	protected SExpCG getCartesianSetExp(List<ASetMultipleBindCG> setMbs) {
		SExpCG cartSetExp;
		List<SExpCG> setsExps = setMbs.stream()
				.flatMap(mb -> Collections.nCopies(mb.getPatterns().size(), mb.getSet())
										  .stream())
				.collect(Collectors.toList());		
		
		if(setsExps.size() > 1) {
			//cartesian product of the sets for each pattern			
			AIdentifierVarExpCG cartMacroIdent = new AIdentifierVarExpCG();
			cartMacroIdent.setName("cartesian_set!");
			cartMacroIdent.setIsLambda(false);
			cartMacroIdent.setIsLocal(true); //hack: macros must be called as if local
			
			AMethodTypeCG cartMacroType = new AMethodTypeCG();
			ATupleTypeCG cartSetElemT = new ATupleTypeCG();
			AApplyExpCG cartesianExp = new AApplyExpCG();
			
			for(SExpCG setExp: setsExps) {
				ASetSetTypeCG setType = (ASetSetTypeCG)setExp.getType();
				
				cartesianExp.getArgs().add(setExp.clone());
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
			cartSetExp = cartesianExp;		
			
		} else {
			cartSetExp = setsExps.get(0);
			
		}
		return cartSetExp;
	}

	protected SPatternCG getArgPattern(List<ASetMultipleBindCG> setMbs) {
		SPatternCG pattern;
		
		List<SPatternCG> patterns = setMbs.stream()
				.flatMap(mb -> mb.getPatterns().stream())
				.collect(Collectors.toList());
		
		if(patterns.size() > 1) {
			//tuple pattern for the Exp and Pred lambdas.
			ATuplePatternCG tupleArgPattern = new ATuplePatternCG();
			tupleArgPattern.getPatterns().addAll(patterns);
			pattern = tupleArgPattern;
		} else {
			pattern = patterns.get(0);
		}
		
		return pattern;
	}

	protected List<ASetMultipleBindCG> castToSetMBList(LinkedList<SMultipleBindCG> bindings) throws AnalysisException {
		
		try {
			return bindings.stream()
							.map(ASetMultipleBindCG.class::cast)
							.collect(Collectors.toList());
		}
		catch(ClassCastException e){
			throw new AnalysisException("Type binds are not allowed.");
		}
	}
}
