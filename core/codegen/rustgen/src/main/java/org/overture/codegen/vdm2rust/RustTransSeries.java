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
import org.overture.codegen.vdm2rust.Transforms.FuncScopeAdderTrans;
import org.overture.codegen.vdm2rust.Transforms.InitialExpTrans;
import org.overture.codegen.vdm2rust.Transforms.NewExpTrans;
import org.overture.codegen.vdm2rust.Transforms.PackageTrans;
import org.overture.codegen.vdm2rust.Transforms.StaticVarTrans;
import org.overture.codegen.vdm2rust.Transforms.TypeConverterTrans;
import org.overture.codegen.vdm2rust.Transforms.UnionDeclTrans;
import org.overture.codegen.vdm2rust.Transforms.UseStmTrans;
import org.overture.codegen.vdm2rust.Transforms.ValueSemanticsTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmMapTrans;
import org.overture.codegen.vdm2rust.Transforms.VdmSeqTrans;
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
		ValueSemanticsTrans valueTrans = new ValueSemanticsTrans();
		InitialExpTrans initExp = new InitialExpTrans();
		StaticVarTrans staticVar = new StaticVarTrans(irInfo);
		PackageTrans packageTrans = new PackageTrans();
		ComprehensionAndQuantifierTrans compTrans = new ComprehensionAndQuantifierTrans();
		UnionDeclTrans unionTrans = new UnionDeclTrans();
		TypeConverterTrans typeTrans = new TypeConverterTrans(transAssistant);
		VdmSetTrans setTrans = new VdmSetTrans(transAssistant);
		VdmMapTrans mapTrans = new VdmMapTrans(transAssistant);
		VdmSeqTrans seqTrans = new VdmSeqTrans(transAssistant);
		UseStmTrans useTrans = new UseStmTrans(irInfo);
		BorrowTrans borrowTrans = new BorrowTrans(transAssistant);
		NewExpTrans newTrans = new NewExpTrans(transAssistant);

		// Set up order of transformations
		transformations = new ArrayList<DepthFirstAnalysisAdaptor>();

		transformations.add(packageTrans);
		transformations.add(unionTrans);
		transformations.add(accTrans);		
		transformations.add(assignTr);
		transformations.add(callObjTr);
		transformations.add(staticVar);
		transformations.add(compTrans);				
		transformations.add(constructorTrans);
		transformations.add(newTrans);
		transformations.add(selfAndScope);
		transformations.add(initExp);
		transformations.add(setTrans);	
		transformations.add(mapTrans);
		transformations.add(seqTrans);
		transformations.add(typeTrans);	
		transformations.add(borrowTrans);
		transformations.add(valueTrans);
		transformations.add(useTrans);			
	}

	public List<DepthFirstAnalysisAdaptor> getTransformations() {
		return transformations;
	}	
}
