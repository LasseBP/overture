package org.overture.codegen.vdm2rust;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.lex.Dialect;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.utils.GeneralCodeGenUtils;
import org.overture.codegen.utils.GeneratedData;
import org.overture.codegen.utils.GeneratedModule;
import org.overture.typechecker.util.TypeCheckerUtil;
import org.overture.typechecker.util.TypeCheckerUtil.TypeCheckResult;

public class RustGenMain {

	public static void main(String[] args) {

		// Se i JavaCodeGenMain

		File file = new File(args[0]);

		List<File> files = new ArrayList<>();
		files.add(file);

		try {
			TypeCheckResult<List<SClassDefinition>> tcResult = TypeCheckerUtil.typeCheckPp(files);
			
			if(GeneralCodeGenUtils.hasErrors(tcResult))
			{
				Logger.getLog().printError("Found errors in VDM model:");
				Logger.getLog().printErrorln(GeneralCodeGenUtils.errorStr(tcResult));
				return;
			}
			
			RustCodeGen xGen = new RustCodeGen();
			GeneratedData data = xGen.generateRustFromVdm(tcResult.result);
		
			for (GeneratedModule module : data.getClasses()) {
				
				if (module.canBeGenerated()) {
					System.out.println(module.getContent());
				}
			}

		} catch (AnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
