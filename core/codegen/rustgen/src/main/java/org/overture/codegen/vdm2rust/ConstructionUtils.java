package org.overture.codegen.vdm2rust;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.AExternalExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.types.AExternalTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;



public class ConstructionUtils {
	
	public static AApplyExpCG consUtilCall(String utils_name,String memberName, STypeCG returnType )
	{
		AExplicitVarExpCG member = new AExplicitVarExpCG();

		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(returnType.clone());
		member.setType(methodType);
		member.setIsLambda(false);
		member.setIsLocal(false);
		//member.setIsStatic(true);
		
		AExternalTypeCG classType = new AExternalTypeCG();
		classType.setName(utils_name);
		member.setClassType(classType);
		member.setName(memberName);
		AApplyExpCG call = new AApplyExpCG();
		
		call.setType(returnType.clone());
		call.setRoot(member);
		
		return call;
	}
	
	public static List<STypeCG> getExpressionTypes(List<? extends SExpCG> expressions) {
		return expressions
				.stream()
				.map(member -> member.getType().clone())
				.collect(Collectors.toList());
	}
	
	public static SExpCG consExpCall(SExpCG oldExp, SExpCG root, String memberName, SExpCG... args) {		
		return consExpCall(oldExp, root, memberName, null, Arrays.asList(args));
	}
	
	public static SExpCG consExpCall(SExpCG oldExp, SExpCG root, String memberName, AMethodTypeCG mType, SExpCG... args) {		
		return consExpCall(oldExp, root, memberName, mType, Arrays.asList(args));
	}
	
	public static SExpCG consExpCall(SExpCG oldExp, SExpCG root, String memberName, List<SExpCG> args) {
		return consExpCall(oldExp, root, memberName, null, args);
	}
	
	public static SExpCG consExpCall(SExpCG oldExp, SExpCG root, String memberName, AMethodTypeCG mType, List<SExpCG> args) {
		AApplyExpCG instanceCall = new AApplyExpCG();
		
		AMethodTypeCG methodType = null;
		if(mType != null) {
			methodType = mType;
		} else {
			methodType = new AMethodTypeCG();
			methodType.setOptional(false);
			methodType.setResult(oldExp.getType().clone());
			methodType.getParams().addAll(getExpressionTypes(args));
		}
		
		AFieldExpCG fieldExp = new AFieldExpCG();
		fieldExp.setMemberName(memberName);
		fieldExp.setObject(root);
		fieldExp.setType(methodType);
		
		instanceCall.setRoot(fieldExp);
		instanceCall.setType(oldExp.getType().clone());
		instanceCall.getArgs().addAll(args);
		instanceCall.setSourceNode(oldExp.getSourceNode());
		
		return instanceCall;
	}
	
	public static AApplyExpCG createVariadicExternalExp(SExpCG oldNode, List<? extends SExpCG> args, String expression) {
		return createVariadicExternalExp(oldNode, args, expression, null);
	}
	
	public static AApplyExpCG createVariadicExternalExp(SExpCG oldNode, List<? extends SExpCG> args, String expression, STypeCG argType) {
		AMethodTypeCG macroType = new AMethodTypeCG();
		macroType.setResult(oldNode.getType().clone());
		if(argType == null) {
			macroType.getParams().addAll(getExpressionTypes(args));
		} else {
			List<STypeCG> argTypes = Collections.nCopies(args.size(), argType)
												.stream()
												.map(argT -> argT.clone())
												.collect(Collectors.toList());
			macroType.getParams().addAll(argTypes);
		}
		macroType.setOptional(false);
		
		AExternalExpCG setMacroExp = new AExternalExpCG();
		setMacroExp.setTargetLangExp(expression);
		setMacroExp.setType(macroType);
		
		AApplyExpCG n = new AApplyExpCG();	
		n.setRoot(setMacroExp);
		n.getArgs().addAll(args);
		n.setType(oldNode.getType().clone());
		n.setSourceNode(oldNode.getSourceNode());
		return n;
	}
	
	

}
