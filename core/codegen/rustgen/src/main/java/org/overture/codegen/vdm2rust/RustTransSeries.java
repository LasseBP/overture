package org.overture.codegen.vdm2rust;

import java.util.ArrayList;
import java.util.List;

import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.trans.AssignStmTrans;
import org.overture.codegen.trans.CallObjStmTrans;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.vdm2rust.Transforms.AccessModfierTrans;
import org.overture.codegen.vdm2rust.Transforms.BorrowTrans;
import org.overture.codegen.vdm2rust.Transforms.ComprehensionAndQuantifierTrans;
import org.overture.codegen.vdm2rust.Transforms.ConstructorTrans;
import org.overture.codegen.vdm2rust.Transforms.NumericTrans;
import org.overture.codegen.vdm2rust.Transforms.FuncScopeAdderTrans;
import org.overture.codegen.vdm2rust.Transforms.InitialExpTrans;
import org.overture.codegen.vdm2rust.Transforms.NewExpTrans;
import org.overture.codegen.vdm2rust.Transforms.PackageTrans;
import org.overture.codegen.vdm2rust.Transforms.PreCheckTrans;
import org.overture.codegen.vdm2rust.Transforms.PreFuncTrans;
import org.overture.codegen.vdm2rust.Transforms.StaticVarTrans;
import org.overture.codegen.vdm2rust.Transforms.TypeConverterTrans;
import org.overture.codegen.vdm2rust.Transforms.UnionDeclTrans;
import org.overture.codegen.vdm2rust.Transforms.UnionPatternTrans;
import org.overture.codegen.vdm2rust.Transforms.UseStmTrans;
import org.overture.codegen.vdm2rust.Transforms.ValueSemanticsTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmMapTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmSeqTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmSetTrans;

public class RustTransSeries {

	private RustCodeGen codeGen;

	private CallObjStmTrans callObjTr;
	private AccessModfierTrans accTrans;
	private ConstructorTrans constructorTrans;
	private AssignStmTrans assignTr;
	private FuncScopeAdderTrans selfAndScope;
	private ValueSemanticsTrans valueTrans;
	private InitialExpTrans initExp;
	private StaticVarTrans staticVar;
	private PackageTrans packageTrans;
	private ComprehensionAndQuantifierTrans compTrans;
	private UnionDeclTrans unionTrans;
	private TypeConverterTrans typeTrans;
	private VdmSetTrans setTrans;
	private VdmMapTrans mapTrans;
	private VdmSeqTrans seqTrans;
	private UseStmTrans useTrans;
	private BorrowTrans borrowTrans;
	private NewExpTrans newTrans;
	private PreFuncTrans preFuncTrans;

	private PreCheckTrans preCheckTrans;

	private org.overture.codegen.trans.ConstructorTrans constructorInitTrans;

	private UnionPatternTrans unionPatternTrans;

	private NumericTrans floatTrans;

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
		callObjTr = new CallObjStmTrans(irInfo);
		accTrans = new AccessModfierTrans();
		constructorTrans = new ConstructorTrans(transAssistant);
		constructorInitTrans = new org.overture.codegen.trans.ConstructorTrans(transAssistant, "cg_init");
		assignTr = new AssignStmTrans(transAssistant);
		selfAndScope = new FuncScopeAdderTrans(transAssistant);
		valueTrans = new ValueSemanticsTrans();
		initExp = new InitialExpTrans();
		staticVar = new StaticVarTrans(irInfo);
		packageTrans = new PackageTrans();
		compTrans = new ComprehensionAndQuantifierTrans();
		unionTrans = new UnionDeclTrans();
		unionPatternTrans = new UnionPatternTrans();
		typeTrans = new TypeConverterTrans(transAssistant);
		setTrans = new VdmSetTrans(transAssistant);
		mapTrans = new VdmMapTrans(transAssistant);
		seqTrans = new VdmSeqTrans(transAssistant);
		useTrans = new UseStmTrans(irInfo);
		borrowTrans = new BorrowTrans(transAssistant);
		newTrans = new NewExpTrans(transAssistant);
		preFuncTrans = new PreFuncTrans();
		preCheckTrans = new PreCheckTrans();
		floatTrans = new NumericTrans(transAssistant);
	}

	public List<DepthFirstAnalysisAdaptor> getTransformations() {
		IRInfo irInfo = codeGen.getInfo();
		
		// Set up order of transformations
		List<DepthFirstAnalysisAdaptor> transformations = new ArrayList<DepthFirstAnalysisAdaptor>();

		transformations.add(floatTrans);
		transformations.add(accTrans);
		transformations.add(packageTrans);
		transformations.add(constructorInitTrans);
		transformations.add(constructorTrans);
		
		if(irInfo.getSettings().generatePreConds()){
			transformations.add(preFuncTrans);
		}
		
		if(irInfo.getSettings().generatePreCondChecks()){
			transformations.add(preCheckTrans);
		}
		
		transformations.add(unionTrans);
		transformations.add(unionPatternTrans);
		transformations.add(assignTr);
		transformations.add(callObjTr);
		transformations.add(staticVar);
		transformations.add(compTrans);				
		transformations.add(newTrans);
		transformations.add(selfAndScope);
		transformations.add(initExp);
		transformations.add(setTrans);	
		transformations.add(mapTrans);
		transformations.add(seqTrans);
		//transformations.add(typeTrans);		
		transformations.add(valueTrans);
		transformations.add(borrowTrans);
		transformations.add(typeTrans);
		transformations.add(useTrans);
		
		return transformations;
	}	
}
