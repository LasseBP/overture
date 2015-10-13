package org.overture.codegen.vdm2rust.Transforms;

import java.util.LinkedList;

import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.ARecordDeclCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ATernaryIfExpCG;
import org.overture.codegen.cgast.name.ATypeNameCG;
import org.overture.codegen.cgast.patterns.AIdentifierPatternCG;
import org.overture.codegen.cgast.statements.AAssignToExpStmCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.types.ABoolBasicTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.ARecordTypeCG;

public class RecordTrans extends DepthFirstAnalysisAdaptor {
	@Override
	public void caseARecordDeclCG(ARecordDeclCG record) throws AnalysisException {
		AMethodDeclCG constructor = consDefaultCtorSignature(record.getName());
		
		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(consRecordType(record));

		ABlockStmCG body = new ABlockStmCG();
		constructor.setBody(body);

		LinkedList<AFormalParamLocalParamCG> formalParams = constructor.getFormalParams();
		LinkedList<SStmCG> bodyStms = body.getStatements();
		LinkedList<AFieldDeclCG> fields = record.getFields();
		
		for (AFieldDeclCG field : fields)
		{
			String name = field.getName();
			STypeCG type = field.getType();

			String paramName = "_" + name;

			AIdentifierPatternCG idPattern = new AIdentifierPatternCG();
			idPattern.setName(paramName);

			methodType.getParams().add(type.clone());
			
			// Construct formal parameter of the constructor
			AFormalParamLocalParamCG formalParam = new AFormalParamLocalParamCG();
			formalParam.setPattern(idPattern);
			formalParam.setType(type.clone());
			formalParams.add(formalParam);

			// Construct the initialization of the record field using the
			// corresponding formal parameter.
			AAssignToExpStmCG assignment = new AAssignToExpStmCG();
			AIdentifierVarExpCG id = new AIdentifierVarExpCG();
			id.setName(name);
			id.setType(type.clone());
			id.setIsLambda(false);
			id.setIsLocal(true);

			AIdentifierVarExpCG varExp = new AIdentifierVarExpCG();
			varExp.setType(type.clone());
			varExp.setName(paramName);
			varExp.setIsLocal(true);

			assignment.setTarget(id);
			
			assignment.setExp(varExp);

			bodyStms.add(assignment);
		}

		constructor.setMethodType(methodType);
		
		
	}
	
	public AMethodDeclCG consDefaultCtorSignature(String recordName)
	{
		AMethodDeclCG constructor = new AMethodDeclCG();
		constructor.setImplicit(false);
		constructor.setAccess("pub");
		constructor.setIsConstructor(true);
		constructor.setName(recordName);
		
		return constructor;
	}
	
	public ARecordTypeCG consRecordType(ARecordDeclCG record)
	{
		AClassDeclCG defClass = record.getAncestor(AClassDeclCG.class);
		ATypeNameCG typeName = new ATypeNameCG();
		typeName.setDefiningClass(defClass.getName());
		typeName.setName(record.getName());

		ARecordTypeCG returnType = new ARecordTypeCG();

		returnType.setName(typeName);
		return returnType;
	}
}
