package org.overture.codegen.vdm2rust;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.lex.Dialect;
import org.overture.codegen.analysis.vdm.Renaming;
import org.overture.codegen.analysis.violations.InvalidNamesResult;
import org.overture.codegen.ir.IrNodeInfo;
import org.overture.codegen.logging.ILogger;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.utils.GeneralCodeGenUtils;
import org.overture.codegen.utils.GeneratedData;
import org.overture.codegen.utils.GeneratedModule;
import org.overture.config.Settings;
import org.overture.typechecker.util.TypeCheckerUtil;
import org.overture.typechecker.util.TypeCheckerUtil.TypeCheckResult;

public class RustGenMain {

	public static void main(String[] args) {

		// Se i JavaCodeGenMain

		File file = new File(args[0]);

		List<File> files = new ArrayList<>();
		files.add(file);
		
		Settings.dialect = Dialect.VDM_PP;
		RustCodeGen rustGen = new RustCodeGen();

		try {
			TypeCheckResult<List<SClassDefinition>> tcResult = TypeCheckerUtil.typeCheckPp(files);
			
			if(GeneralCodeGenUtils.hasErrors(tcResult))
			{
				Logger.getLog().printError("Found errors in VDM model:");
				Logger.getLog().printErrorln(GeneralCodeGenUtils.errorStr(tcResult));
				return;
			}
			
			
			GeneratedData data = rustGen.generateRustFromVdm(tcResult.result);
			
			logVelocityMsgs(rustGen.getVelocityLog());
			
			processData(true, null, rustGen, data);
			
		} catch (AnalysisException e) {
			Logger.getLog().println("Could not code generate model: "
					+ e.getMessage());
		}

	}

	public static void processData(boolean printCode,
			final File outputDir, RustCodeGen vdmCodGen, GeneratedData data) {
		List<GeneratedModule> generatedClasses = data.getClasses();

		Logger.getLog().println("");
		
		if(!generatedClasses.isEmpty())
		{
			for (GeneratedModule generatedClass : generatedClasses)
			{
				if (generatedClass.hasMergeErrors())
				{
					Logger.getLog().println(String.format("Class %s could not be merged. Following merge errors were found:", generatedClass.getName()));
	
					GeneralCodeGenUtils.printMergeErrors(generatedClass.getMergeErrors());
				} else if (!generatedClass.canBeGenerated())
				{
					Logger.getLog().println("Could not generate class: "
							+ generatedClass.getName() + "\n");
	
					if (generatedClass.hasUnsupportedIrNodes())
					{
						Logger.getLog().println("Following VDM constructs are not supported by the code generator:");
						GeneralCodeGenUtils.printUnsupportedIrNodes(generatedClass.getUnsupportedInIr());
					}
	
					if (generatedClass.hasUnsupportedTargLangNodes())
					{
						Logger.getLog().println("Following constructs are not supported by the code generator:");
						GeneralCodeGenUtils.printUnsupportedNodes(generatedClass.getUnsupportedInTargLang());
					}
	
				} else
				{
					
					if (printCode)
					{
						Logger.getLog().println("**********");
						Logger.getLog().println(generatedClass.getContent());
						Logger.getLog().println("\n");
					} else
					{
						Logger.getLog().println("Generated class : "
								+ generatedClass.getName());
					}
	
					Set<IrNodeInfo> warnings = generatedClass.getTransformationWarnings();
	
					if (!warnings.isEmpty())
					{
						Logger.getLog().println("Following transformation warnings were found:");
						GeneralCodeGenUtils.printUnsupportedNodes(generatedClass.getTransformationWarnings());
					}
				}
			}
		}
		else
		{
			Logger.getLog().println("No classes were generated!");
		}

		
		if(data.getWarnings() != null && !data.getWarnings().isEmpty())
		{
			Logger.getLog().println("");
			for(String w : data.getWarnings())
			{
				Logger.getLog().println("[WARNING] " + w);
			}
		}
	}
	
	private static void logVelocityMsgs(VelocityLogger velocityLog) {
		ILogger log = Logger.getLog();
		
		log.println("*** Velocity log Starts ***");
		for(String msg : velocityLog.getMessages())
		{
			log.println(msg);
		}
		log.println("*** Velocity log Ends ***");
	}
}
