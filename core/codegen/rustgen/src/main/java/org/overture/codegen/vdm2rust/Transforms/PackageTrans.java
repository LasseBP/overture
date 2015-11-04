package org.overture.codegen.vdm2rust.Transforms;

import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.SClassDeclCG;

public class PackageTrans extends DepthFirstAnalysisAdaptor {
	@Override
	public void defaultInSClassDeclCG(SClassDeclCG node) throws AnalysisException {
		node.setPackage(node.getName() + "_mod");
	}
}
