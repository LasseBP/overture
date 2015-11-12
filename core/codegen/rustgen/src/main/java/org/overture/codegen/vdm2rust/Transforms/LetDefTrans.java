package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.expressions.ABlockExpCG;
import org.overture.codegen.cgast.expressions.ALetDefExpCG;
import org.overture.codegen.logging.Logger;

public class LetDefTrans extends DepthFirstAnalysisAdaptor {

	@Override
	public void outALetDefExpCG(ALetDefExpCG node) throws AnalysisException {
		ABlockExpCG blockExp = new ABlockExpCG();
		blockExp.setExp(node.getExp());
		blockExp.setLocalDefs(node.getLocalDefs());
		blockExp.setType(node.getType());
		blockExp.setSourceNode(node.getSourceNode());
		
		if(node.parent() != null) {
			node.parent().replaceChild(node, blockExp);
		}
		else {
			Logger.getLog().printErrorln("Could not find parent of " + node + " in " + "'" + this.getClass().getSimpleName() + "'" );
		}
		blockExp.setScoped(StmAssistantCG.isScoped(blockExp));
	}
}
