package org.overture.codegen.vdm2rust;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.lex.Dialect;
import org.overture.codegen.cgast.declarations.SClassDeclCG;
import org.overture.codegen.ir.IrNodeInfo;
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

		List<File> files = Arrays.stream(args)
								.map(arg -> new File(arg))
								.collect(Collectors.toList());
		
		Settings.dialect = Dialect.VDM_PP;
		RustCodeGen rustGen = new RustCodeGen();
		
		rustGen.getSettings().setGeneratePreConds(true);
		rustGen.getSettings().setGeneratePreCondChecks(true);
		rustGen.getSettings().setGenerateInvariants(true); 

		try {
			TypeCheckResult<List<SClassDefinition>> tcResult = TypeCheckerUtil.typeCheckPp(files);
			
			if(GeneralCodeGenUtils.hasErrors(tcResult))
			{
				Logger.getLog().printError("Found errors in VDM model:");
				Logger.getLog().printErrorln(GeneralCodeGenUtils.errorStr(tcResult));
				return;
			}
			
			GeneratedData data = rustGen.generateRustFromVdm(tcResult.result);
			String outputPath = files.get(0).getParent() + File.separator + "generated" + File.separator + "rust";
			processData(false, outputPath, rustGen, data);
			
		} catch (AnalysisException e) {
			Logger.getLog().println("Could not code generate model: "
					+ e.getMessage());
		}

	}

	public static void processData(boolean printCode,
			final String outputPath, RustCodeGen vdmCodGen, GeneratedData data) {
		List<GeneratedModule> generatedClasses = data.getClasses();
		List<String> generatedModules = new ArrayList<>();
		
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
					if (outputPath != null)
					{						
						File sourceDir = new File(outputPath + File.separator + "src");
						sourceDir.mkdirs();
						String moduleName = ((SClassDeclCG)generatedClass.getIrNode()).getPackage();
						try (PrintWriter outFile = new PrintWriter(sourceDir.toString() + File.separator + moduleName + ".rs")) {
							outFile.println(generatedClass.getContent());
							generatedModules.add(moduleName);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
										
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
		
		List<GeneratedModule> quotes = data.getQuoteValues();

		Logger.getLog().println("\nGenerated following quote modules:");

		if (quotes != null && !quotes.isEmpty())
		{
			if(outputPath != null)
			{
				for (GeneratedModule q : quotes)
				{
					File sourceDir = new File(outputPath + File.separator + "src");
					sourceDir.mkdirs();
					try (PrintWriter outFile = new PrintWriter(sourceDir.toString() + File.separator + q.getName() + ".rs")) {
						outFile.println(q.getContent());
						generatedModules.add(q.getName());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			
			for (GeneratedModule q : quotes)
			{
				Logger.getLog().print(q.getName() + " ");
			}

			Logger.getLog().println("");
		}
		
		if(outputPath != null) {
			generateMain(generatedModules, outputPath);
			generateToml(outputPath);
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

	private static void generateToml(String outputPath) {
		try (PrintWriter outFile = new PrintWriter(outputPath + File.separator + "Cargo.toml")) {
			
			outFile.write("[package]\n");
			outFile.write("name = \"project_name\"\n");
			outFile.write("version = \"0.1.0\"\n");
			outFile.write("authors = [\"Overture User <info@overturetool.org>\"]");
			
			outFile.write("\n\n[dependencies]\n");
			outFile.write("codegen_runtime = { git = \"https://github.com/LasseBP/codegen_runtime.git\" }\n");
			outFile.write("lazy_static = \"0.1.*\"");		
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		
	}

	private static void generateMain(List<String> generatedModules, String outputPath) {
		try (PrintWriter outFile = new PrintWriter(outputPath.toString() + File.separator + "src" + File.separator + "main.rs")) {
			
			outFile.write("#![allow(non_snake_case, non_upper_case_globals,\n" +
						  " unused_variables, dead_code, unused_mut,unused_parens,\n" +
						  "non_camel_case_types,unused_imports)] \n\n");
			
			outFile.write("#[macro_use]\n" +
					  	  "extern crate lazy_static;\n\n");
			
			outFile.write("#[macro_use]\n" +
				  	  	  "extern crate codegen_runtime;\n\n");
			
			for(String moduleName : generatedModules) {
				outFile.write("mod " + moduleName + ";\n");
			}
			
			outFile.write("\nuse codegen_runtime::*;\n\n");
			
			//TODO: entry point goes here.
			outFile.write("fn main() { println!(\"Hello Rusty VDM!\"); }");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	
}
