package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.overture.ast.util.ClonableString;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.ADefaultClassDeclCG;
import org.overture.codegen.ir.IRInfo;

public class UseStmTrans extends DepthFirstAnalysisAdaptor {
	private IRInfo info;

	public UseStmTrans(IRInfo info)
	{
		this.info = info;
	}

	@Override
	public void caseADefaultClassDeclCG(ADefaultClassDeclCG node) throws AnalysisException
	{
		List<ClonableString> dep = new LinkedList<>();
		
		dep.add(new ClonableString("codegen_runtime::*"));
		
		if(!info.getQuoteValues().isEmpty()) {
			dep.add(new ClonableString("quotes"));
		}
		
		List<ClonableString> classUseDecls = info.getClasses()
			.stream()
			.filter(clazz -> clazz != node)
			.map(clazz -> new ClonableString(clazz.getPackage() + "::" + clazz.getName()))
			.collect(Collectors.toList());
		
		dep.addAll(classUseDecls);
		
		node.setDependencies(dep);
	}
}

