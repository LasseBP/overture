package org.overture.codegen.assistant;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.expressions.ARealLiteralExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.expressions.SBinaryExp;
import org.overture.ast.expressions.SUnaryExp;
import org.overture.ast.types.AIntNumericBasicType;
import org.overture.ast.types.ANatNumericBasicType;
import org.overture.ast.types.ANatOneNumericBasicType;
import org.overture.ast.types.PType;
import org.overture.codegen.cgast.expressions.AIsolationUnaryExpCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.expressions.SBinaryExpCG;
import org.overture.codegen.cgast.expressions.SUnaryExpCG;
import org.overture.codegen.cgast.types.PTypeCG;
import org.overture.codegen.visitor.OoAstInfo;

public class ExpAssistantCG
{
	public static PExpCG isolateExpression(PExpCG exp)
	{
		AIsolationUnaryExpCG isolationExp = new AIsolationUnaryExpCG();
		isolationExp.setExp(exp);
		isolationExp.setType(exp.getType());
		return isolationExp;
	}
	
	public PExpCG handleUnaryExp(SUnaryExp vdmExp, SUnaryExpCG codeGenExp, OoAstInfo question) throws AnalysisException
	{
		PExpCG expCg = vdmExp.getExp().apply(question.getExpVisitor(), question);
		PTypeCG typeCg = vdmExp.getType().apply(question.getTypeVisitor(), question);
		
		codeGenExp.setType(typeCg);
		codeGenExp.setExp(expCg);
		
		return codeGenExp;
	}
	
	public PExpCG handleBinaryExp(SBinaryExp vdmExp, SBinaryExpCG codeGenExp, OoAstInfo question) throws AnalysisException
	{	
		PTypeCG typeCg = vdmExp.getType().apply(question.getTypeVisitor(), question);
		codeGenExp.setType(typeCg);
		
		PExp vdmExpLeft = vdmExp.getLeft();
		PExp vdmExpRight = vdmExp.getRight();
		
		PExpCG leftExpCg = vdmExpLeft.apply(question.getExpVisitor(), question);
		PExpCG rightExpCg = vdmExpRight.apply(question.getExpVisitor(), question);
		
		codeGenExp.setLeft(leftExpCg);
		codeGenExp.setRight(rightExpCg);

		PTypeCG leftTypeCg = vdmExpLeft.getType().apply(question.getTypeVisitor(), question);
		codeGenExp.getLeft().setType(leftTypeCg);
		PTypeCG rightTypeCg = vdmExpRight.getType().apply(question.getTypeVisitor(), question);
		codeGenExp.getRight().setType(rightTypeCg);
		
		return codeGenExp;
	}
	
	public boolean isIntegerType(PExp exp)
	{	
		PType type = exp.getType();

		//Expressions like 1.0 are considered real literal expressions
		//of type NatOneNumericBasicType
		
		return (type instanceof ANatOneNumericBasicType 
				|| type instanceof ANatNumericBasicType
				|| type instanceof AIntNumericBasicType) 
				&& !(exp instanceof ARealLiteralExp);
	}
}