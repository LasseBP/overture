package org.overture.codegen.vdm2rust.Transforms;

import org.apache.commons.lang.StringUtils;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFuncDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;

/**
 * Changes access modifiers from public/private to pub/""
 */
public class AccessModfierTrans extends DepthFirstAnalysisAdaptor {
	
	@Override
	public void inAClassDeclCG(AClassDeclCG node) throws AnalysisException {
		node.setAccess(getNewAccess(node.getAccess()));
	}
	
	@Override
	public void inAFieldDeclCG(AFieldDeclCG node) throws AnalysisException {
		node.setAccess(getNewAccess(node.getAccess()));
	}
	
	@Override
	public void inAFuncDeclCG(AFuncDeclCG node) throws AnalysisException {
		node.setAccess(getNewAccess(node.getAccess()));
	}
	
	@Override
	public void inAMethodDeclCG(AMethodDeclCG node) throws AnalysisException {
		node.setAccess(getNewAccess(node.getAccess()));
	}
	
	private static String getNewAccess(String access) throws AnalysisException
	{
		if(StringUtils.isEmpty(access))
			throw new AnalysisException("Invalid access modifier string.");
		else if (access.equals("private"))
			return "";
		else if (access.equals("public"))
			return "pub";
		else 
			throw new AnalysisException("Invalid access modifier string: " + access);
		
	}
}
