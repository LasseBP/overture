package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AExplicitVarExpCG;
import org.overture.codegen.cgast.expressions.ANewExpCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ARecordTypeCG;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class NewExpTrans extends DepthFirstAnalysisAdaptor {
	
private TransAssistantCG transAssistant;
	
	public NewExpTrans(TransAssistantCG transAssistant)
	{
		this.transAssistant = transAssistant;
	}
	
	@Override
	public void outANewExpCG(ANewExpCG node) throws AnalysisException {
		
		AMethodTypeCG constrType = null;
		
		if (node.getType() instanceof AClassTypeCG)
		{
			SClassDeclCG classDecl = transAssistant.getInfo().getAssistantManager().getDeclAssistant().findClass(transAssistant.getInfo().getClasses(), node.getName().getName());
			constrType = classDecl.getMethods().stream().filter(method -> method.getIsConstructor()).findFirst().get().getMethodType();
		} else if (node.getType() instanceof ARecordTypeCG)
		{
			SClassDeclCG classDecl = transAssistant.getInfo().getAssistantManager().getDeclAssistant().findClass(transAssistant.getInfo().getClasses(), node.getName().getDefiningClass());
			ARecordDeclCG record = transAssistant.getInfo().getAssistantManager().getDeclAssistant().findRecord(classDecl, node.getName().getName());
			constrType = record.getMethods().get(0).getMethodType();
		}
		
		AExplicitVarExpCG explIdent = new AExplicitVarExpCG();
		explIdent.setClassType(node.getType().clone());
		explIdent.setName("new");
		explIdent.setIsLambda(false);
		explIdent.setIsLocal(false);
		explIdent.setType(constrType.clone());
		
		AApplyExpCG applyExp = new AApplyExpCG();
		applyExp.setRoot(explIdent);
		applyExp.setArgs(node.getArgs());
		applyExp.setType(node.getType().clone());
		applyExp.setSourceNode(node.getSourceNode());
		
		transAssistant.replaceNodeWith(node, applyExp);
	}	
}
