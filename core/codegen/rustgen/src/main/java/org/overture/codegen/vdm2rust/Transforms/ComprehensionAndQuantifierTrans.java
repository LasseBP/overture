package org.overture.codegen.vdm2rust.Transforms;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.PCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SMultipleBindCG;
import org.overture.codegen.cgast.SPatternCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AVarDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ABoolLiteralExpCG;
import org.overture.codegen.cgast.expressions.ACompMapExpCG;
import org.overture.codegen.cgast.expressions.ACompSeqExpCG;
import org.overture.codegen.cgast.expressions.ACompSetExpCG;
import org.overture.codegen.cgast.expressions.AExists1QuantifierExpCG;
import org.overture.codegen.cgast.expressions.AExistsQuantifierExpCG;
import org.overture.codegen.cgast.expressions.AExternalExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AForAllQuantifierExpCG;
import org.overture.codegen.cgast.expressions.ALambdaExpCG;
import org.overture.codegen.cgast.expressions.ALetBeStExpCG;
import org.overture.codegen.cgast.expressions.ALetDefExpCG;
import org.overture.codegen.cgast.expressions.AMapletExpCG;
import org.overture.codegen.cgast.expressions.ATupleExpCG;
import org.overture.codegen.cgast.expressions.SQuantifierExpBase;
import org.overture.codegen.cgast.patterns.ASetMultipleBindCG;
import org.overture.codegen.cgast.patterns.ATuplePatternCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.ALetBeStStmCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AMapMapTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ASetSetTypeCG;
import org.overture.codegen.cgast.types.ATupleTypeCG;
import org.overture.codegen.cgast.utils.AHeaderLetBeStCG;
import org.overture.codegen.logging.Logger;

public class ComprehensionAndQuantifierTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void outALetBeStExpCG(ALetBeStExpCG node) throws AnalysisException {
		AVarDeclCG bindingDecl = convLetBeStHeaderToVarDecl(node, node.getHeader());
		
		ALetDefExpCG letDefExp = new ALetDefExpCG();
		letDefExp.getLocalDefs().add(bindingDecl);
		letDefExp.setExp(node.getValue());
		letDefExp.setSourceNode(node.getSourceNode());
		letDefExp.setType(node.getType());
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, letDefExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
	}
	
	@Override
	public void outALetBeStStmCG(ALetBeStStmCG node) throws AnalysisException {
		/* goal:
		 * VDM: let x,y in set {1,2,3} be st y > x in return mk_(x,y);
		 * Rust:
		 * {
		 * 	let (x,y): (u64, u64) = cartesian_set!(set!{1,2,3}, set!{1,2,3})
		 * 								.be_such_that(|(x,y): (u64, u64)| y > x);
		 * 	return (x,y);
		 * } 
		 * */
		
		AVarDeclCG bindingDecl = convLetBeStHeaderToVarDecl(node, node.getHeader());
		
		ABlockStmCG blockStm = new ABlockStmCG();
		blockStm.getLocalDefs().add(bindingDecl);
		blockStm.getStatements().add(node.getStatement());
		blockStm.setSourceNode(node.getSourceNode());
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, blockStm);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
		blockStm.setScoped(StmAssistantCG.isScoped(blockStm));
	}

	
	
	@Override
	public void outAExists1QuantifierExpCG(AExists1QuantifierExpCG node) throws AnalysisException {
		replaceQuantifier(node, "exists1");
	}
	
	@Override
	public void outAExistsQuantifierExpCG(AExistsQuantifierExpCG node) throws AnalysisException {
		replaceQuantifier(node, "exists");
	}
	
	@Override
	public void outAForAllQuantifierExpCG(AForAllQuantifierExpCG node) throws AnalysisException {
		replaceQuantifier(node, "forall");
	}
	
	@Override
	public void outACompMapExpCG(ACompMapExpCG node) throws AnalysisException {		
		replaceMBCompExp(node, "map_compr", convertMapletToTuple(node.getFirst()), node.getPredicate(), node.getBindings());
	}	

	@Override
	public void outACompSeqExpCG(ACompSeqExpCG node) throws AnalysisException {
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = node.getSetBind().getSet();		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		SPatternCG argPattern = node.getSetBind().getPattern();
		replaceCompExp(node, "seq_compr", node.getFirst(), node.getPredicate(), argPattern, cartSetExp, setExpT);
	}
	
	@Override
	public void outACompSetExpCG(ACompSetExpCG node) throws AnalysisException {
		// goal:
		// cartesian_set!(s1,s2,s3).set_compr(|(x,_y,_z)| x == 1, |(_x,y,z)| y*z);
		
		replaceMBCompExp(node, "set_compr", node.getFirst(), node.getPredicate(), node.getBindings());
	}
	
	protected AVarDeclCG convLetBeStHeaderToVarDecl(PCG node, AHeaderLetBeStCG header)
			throws AnalysisException {
		List<SMultipleBindCG> bindings = Arrays.asList(header.getBinding());
		List<ASetMultipleBindCG> setMbs = castToSetMBList(bindings);
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = getCartesianSetExp(setMbs);		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		SPatternCG argPattern = getArgPattern(setMbs);
		//create lambdas
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, header.getSuchThat());
		
		AApplyExpCG beStExp = createMethodAppExp(node, setExpT.getSetOf().clone(), cartSetExp, predLambda, 
				null, "be_such_that");
		
		AVarDeclCG bindingDecl = new AVarDeclCG(); 
		bindingDecl.setFinal(true);
		bindingDecl.setExp(beStExp);
		bindingDecl.setPattern(argPattern.clone());
		bindingDecl.setType(setExpT.getSetOf().clone());
		return bindingDecl;
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
	
	protected void replaceQuantifier(SQuantifierExpBase node, String methodName) throws AnalysisException {
		List<ASetMultipleBindCG> setMbs = castToSetMBList(node.getBindList());
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = getCartesianSetExp(setMbs);		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		//create lambda
		SPatternCG argPattern = getArgPattern(setMbs);
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, node.getPredicate());
		
		replaceWithMethodCallExp(node, cartSetExp, predLambda, methodName);
	}
	
	protected void replaceMBCompExp(SExpCG node, String methodName, SExpCG firstExp, SExpCG predExp, LinkedList<SMultipleBindCG> bindings)
			throws AnalysisException {
		List<ASetMultipleBindCG> setMbs = castToSetMBList(bindings);
		
		//setExp over which to perform the comprehension
		SExpCG cartSetExp = getCartesianSetExp(setMbs);		
		ASetSetTypeCG setExpT = (ASetSetTypeCG)cartSetExp.getType();
		
		SPatternCG argPattern = getArgPattern(setMbs);
		
		replaceCompExp(node, methodName, firstExp, predExp, argPattern, cartSetExp, setExpT);
	}

	protected void replaceCompExp(SExpCG node, String methodName, SExpCG firstExp, SExpCG predExp,
			SPatternCG argPattern, SExpCG cartSetExp, ASetSetTypeCG setExpT) {
		//create lambdas
		AFormalParamLocalParamCG param = createLambdaParam(argPattern, setExpT);			
		ALambdaExpCG predLambda = createPredicateLambdaExp(setExpT, param, predExp);		
		ALambdaExpCG firstLambda = createFirstLambdaExp(setExpT, param, firstExp);

		replaceWithMethodCallExp(node, cartSetExp, predLambda, firstLambda, methodName);
	}
	
	protected void replaceWithMethodCallExp(SExpCG node, SExpCG cartSetExp, ALambdaExpCG predLambda, 
			String method_name) {
		replaceWithMethodCallExp(node, cartSetExp, predLambda, null, method_name);
	}

	protected void replaceWithMethodCallExp(SExpCG node, SExpCG cartSetExp, ALambdaExpCG predLambda,
			ALambdaExpCG firstLambda, String method_name) {
		AApplyExpCG compExp = createMethodAppExp(node, node.getType(), cartSetExp, predLambda, firstLambda, method_name);
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, compExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
	}

	protected AApplyExpCG createMethodAppExp(PCG node, STypeCG type, SExpCG cartSetExp, ALambdaExpCG predLambda,
			ALambdaExpCG firstLambda, String method_name) {
		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(type.clone());
		methodType.getParams().add(predLambda.getType().clone());
		if(firstLambda != null) {
			methodType.getParams().add(firstLambda.getType().clone());
		}
		
		AFieldExpCG fieldExp = new AFieldExpCG();
		fieldExp.setMemberName(method_name);
		fieldExp.setObject(cartSetExp);
		fieldExp.setType(methodType);
		
		AApplyExpCG compExp = new AApplyExpCG();
		compExp.setRoot(fieldExp);
		compExp.setType(type);
		compExp.setSourceNode(node.getSourceNode());
		compExp.getArgs().add(predLambda);
		if(firstLambda != null) {
			compExp.getArgs().add(firstLambda);
		}
		return compExp;
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
			AExternalExpCG cartMacroExp = new AExternalExpCG();
			cartMacroExp.setTargetLangExp("cartesian_set!");
			
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
			cartMacroExp.setType(cartMacroType);
			
			cartesianExp.setType(cartSetType.clone());
			cartesianExp.setRoot(cartMacroExp);
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

	protected List<ASetMultipleBindCG> castToSetMBList(List<SMultipleBindCG> bindings) throws AnalysisException {
		
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
