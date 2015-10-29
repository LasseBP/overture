package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AFieldExpCG;
import org.overture.codegen.cgast.expressions.AFieldNumberExpCG;
import org.overture.codegen.cgast.expressions.AMapSeqGetExpCG;
import org.overture.codegen.cgast.expressions.AStaticVarExpCG;
import org.overture.codegen.cgast.expressions.SVarExpBase;
import org.overture.codegen.cgast.statements.AAssignToExpStmCG;
import org.overture.codegen.cgast.statements.ACallObjectExpStmCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ATokenBasicTypeCG;
import org.overture.codegen.cgast.types.SBasicTypeCG;
import org.overture.codegen.logging.Logger;

public class ValueSemanticsTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void defaultInSExpCG(SExpCG node) throws AnalysisException {
		INode parent = node.parent();
		STypeCG type = node.getType();
		boolean isStatic = node instanceof AStaticVarExpCG;
		
		// any other expression *produces* a value. These *refer* to values.
		if(!(node instanceof SVarExpBase ||
			 node instanceof AFieldExpCG ||
			 node instanceof AFieldNumberExpCG ||
			 node instanceof AMapSeqGetExpCG)) {
			return;
		}
		
		if (parent instanceof AFieldExpCG ||
			parent instanceof ACallObjectExpStmCG) {
			return;
		}
		
		if (parent instanceof AAssignToExpStmCG)
		{
			AAssignToExpStmCG assignment = (AAssignToExpStmCG) parent;
			if (assignment.getTarget() == node)
			{
				return;
			}
		}
		
		if(type instanceof AMethodTypeCG ||
		   type instanceof AClassTypeCG ||
		   (!isStatic && type instanceof SBasicTypeCG && !(type instanceof ATokenBasicTypeCG))) {		
			return;
		}
		
		addCloneCall(node);
	}
	
	protected static void addCloneCall(SExpCG node) {
		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(node.getType().clone());
		
		AFieldExpCG rootExp = new AFieldExpCG();
		
		AApplyExpCG applyExp = new AApplyExpCG();
		applyExp.setType(node.getType().clone());
		applyExp.setRoot(rootExp);
		applyExp.setSourceNode(node.getSourceNode());
		
		rootExp.setMemberName("clone");
		rootExp.setType(methodType);
		rootExp.setObject(node.clone());
		rootExp.setSourceNode(node.getSourceNode());
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, applyExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + ValueSemanticsTrans.class.getSimpleName() + "'" );
		}
	}
	
}
