package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.overture.ast.expressions.ABooleanConstExp;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SPatternCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.ABoolIsExpCG;
import org.overture.codegen.cgast.expressions.ABoolLiteralExpCG;
import org.overture.codegen.cgast.expressions.ACompMapExpCG;
import org.overture.codegen.cgast.expressions.ACompSeqExpCG;
import org.overture.codegen.cgast.expressions.ACompSetExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.ALambdaExpCG;
import org.overture.codegen.cgast.patterns.ASetMultipleBindCG;
import org.overture.codegen.cgast.patterns.ATypeMultipleBindCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ASetSetTypeCG;
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
			//TODO: comprehensions with multiple patterns.
			//tuple pattern
			//cartesian
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
