package org.overture.codegen.vdm2rust;

import java.util.ArrayList;
import java.util.List;

import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.traces.TracesTrans;
import org.overture.codegen.trans.AssignStmTrans;
import org.overture.codegen.trans.CallObjStmTrans;
import org.overture.codegen.trans.DivideTrans;
import org.overture.codegen.trans.Exp2StmTrans;
import org.overture.codegen.trans.IsExpTrans;
import org.overture.codegen.trans.LetBeStTrans;
import org.overture.codegen.trans.PostCheckTrans;
import org.overture.codegen.trans.PreCheckTrans;
import org.overture.codegen.trans.PrePostTrans;
import org.overture.codegen.trans.SeqConvTrans;
import org.overture.codegen.trans.WhileStmTrans;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.trans.funcvalues.FuncValTrans;
import org.overture.codegen.trans.iterator.ILanguageIterator;
import org.overture.codegen.trans.iterator.JavaLanguageIterator;
import org.overture.codegen.trans.letexps.FuncTrans;
import org.overture.codegen.trans.letexps.IfExpTrans;
import org.overture.codegen.trans.patterns.PatternTrans;
import org.overture.codegen.trans.uniontypes.UnionTypeTrans;
import org.overture.codegen.vdm2rust.Transforms.AccessModfierTrans;
import org.overture.codegen.vdm2rust.Transforms.ConstructorTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmSetTrans;

public class RustTransSeries {

	private RustCodeGen codeGen;
	private List<DepthFirstAnalysisAdaptor> transformations;

	public RustTransSeries(RustCodeGen codeGen)
	{
		this.codeGen = codeGen;
		init();
	}
	
	public void init()
	{
		// Data and functionality to support the transformations
		//IRInfo irInfo = codeGen.getInfo();
		TransAssistantCG transAssistant = codeGen.getTransAssistant();
		
		//VdmSetTrans setTrans = new VdmSetTrans(transAssistant); 
		
		// Construct the transformations
		FuncTrans funcTr = new FuncTrans(transAssistant);
//		DivideTrans divideTr = new DivideTrans(info);
//		CallObjStmTrans callObjTr = new CallObjStmTrans(info);
//		AssignStmTrans assignTr = new AssignStmTrans(transAssist);
//		PrePostTrans prePostTr = new PrePostTrans(info);
//		IfExpTrans ifExpTr = new IfExpTrans(transAssist);
//		FuncValTrans funcValTr = new FuncValTrans(transAssist, funcValAssist, funcValPrefixes);
//		ILanguageIterator langIte = new JavaLanguageIterator(transAssist, iteVarPrefixes);
//		LetBeStTrans letBeStTr = new LetBeStTrans(transAssist, langIte, iteVarPrefixes);
//		WhileStmTrans whileTr = new WhileStmTrans(transAssist, varMan.whileCond());
//		Exp2StmTrans exp2stmTr = new Exp2StmTrans(iteVarPrefixes, transAssist, consExists1CounterData(), langIte, exp2stmPrefixes);
//		PatternTrans patternTr = new PatternTrans(iteVarPrefixes, transAssist, patternPrefixes, varMan.casesExp());
//		PreCheckTrans preCheckTr = new PreCheckTrans(transAssist, new JavaValueSemanticsTag(false));
//		PostCheckTrans postCheckTr = new PostCheckTrans(postCheckCreator, transAssist, varMan.funcRes(), new JavaValueSemanticsTag(false));
//		IsExpTrans isExpTr = new IsExpTrans(transAssist, varMan.isExpSubject());
//		SeqConvTrans seqConvTr = new SeqConvTrans(transAssist);
//		TracesTrans tracesTr = new TracesTrans(transAssist, iteVarPrefixes, tracePrefixes, langIte, new JavaCallStmToStringBuilder());
//		UnionTypeTrans unionTypeTr = new UnionTypeTrans(transAssist, unionTypePrefixes, codeGen.getJavaFormat().getValueSemantics().getCloneFreeNodes());
//		JavaToStringTrans javaToStringTr = new JavaToStringTrans(info);
//		RecMethodsTrans recTr = new RecMethodsTrans(codeGen.getJavaFormat().getRecCreator());
		AccessModfierTrans accTrans = new AccessModfierTrans();
		ConstructorTrans constructorTrans = new ConstructorTrans();

		// Start concurrency transformations
//		SentinelTransformation concurrencytransform = new SentinelTransformation(irInfo, classes);
//		MainClassConcTransformation mainclassTransform = new MainClassConcTransformation(irInfo, classes);
//		MutexDeclTransformation mutexTransform = new MutexDeclTransformation(irInfo, classes);
//		InstanceVarPPEvalTransformation instanceVarPPEval = new InstanceVarPPEvalTransformation(irInfo, transAssistant, classes);
		// End concurrency transformations

		// Set up order of transformations
		transformations = new ArrayList<DepthFirstAnalysisAdaptor>();

//		transformations.add(setTrans);
//		series.add(atomicTr);
//		series.add(divideTr);
//		series.add(assignTr);
//		series.add(callObjTr);
		transformations.add(funcTr);
//		series.add(prePostTr);
//		series.add(ifExpTr);
//		series.add(funcValTr);
//		series.add(letBeStTr);
//		series.add(whileTr);
//		series.add(exp2stmTr);
//		series.add(tracesTr);
//		series.add(patternTr);
//		series.add(preCheckTr);
//		series.add(postCheckTr);
//		series.add(isExpTr);
//		series.add(unionTypeTr);
//		series.add(javaToStringTr);
//		series.add(sentinelTr);
//		series.add(mutexTr);
//		series.add(mainClassTr);
//		series.add(seqConvTr);
//		series.add(evalPermPredTr);
//		series.add(recTr);
		transformations.add(accTrans);
		transformations.add(constructorTrans);
	}

	public List<DepthFirstAnalysisAdaptor> getTransformations() {
		return transformations;
	}	
}
