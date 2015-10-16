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
import org.overture.codegen.vdm2rust.Transforms.FuncScopeAdderTrans;
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
		IRInfo irInfo = codeGen.getInfo();
		TransAssistantCG transAssistant = codeGen.getTransAssistant();
		
		// Construct the transformations
		CallObjStmTrans callObjTr = new CallObjStmTrans(irInfo);
		AccessModfierTrans accTrans = new AccessModfierTrans();
		ConstructorTrans constructorTrans = new ConstructorTrans(transAssistant);
		AssignStmTrans assignTr = new AssignStmTrans(transAssistant);
		FuncScopeAdderTrans selfAndScope = new FuncScopeAdderTrans(transAssistant);


		// Set up order of transformations
		transformations = new ArrayList<DepthFirstAnalysisAdaptor>();

		transformations.add(assignTr);
		transformations.add(callObjTr);
		transformations.add(accTrans);
		transformations.add(constructorTrans);
		transformations.add(selfAndScope);
	}

	public List<DepthFirstAnalysisAdaptor> getTransformations() {
		return transformations;
	}	
}
