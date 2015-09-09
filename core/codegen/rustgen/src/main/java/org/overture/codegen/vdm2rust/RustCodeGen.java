package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AModuleDeclCG;
import org.overture.codegen.ir.CodeGenBase;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.ir.IRStatus;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.utils.GeneratedData;
import org.overture.codegen.utils.GeneratedModule;


public class RustCodeGen extends CodeGenBase {
	
	public GeneratedData generateRustFromVdm(List<SClassDefinition> ast)
			throws AnalysisException
	{
		List<IRStatus<org.overture.codegen.cgast.INode>> statuses = new LinkedList<>();
		
		// This is run pr. class
		for(SClassDefinition node : ast)
		{	
			// Try to produce the IR
			IRStatus<org.overture.codegen.cgast.INode> status = generator.generateFrom(node);
			System.out.println("Running..");
			
			// If it was successful, then status is different from null
			if(status != null)
			{
				statuses.add(status);
			}
			
		}
		
		// By now we have the IR in 'statuses'
		
		//List<GeneratedModule> generated = new LinkedList<GeneratedModule>();
		
		// Event notification
		//statuses = initialIrEvent(statuses);
		//statuses = filter(statuses, generated);
		
		this.transAssistant = new TransAssistantCG(generator.getIRInfo());
		
		List<IRStatus<AModuleDeclCG>> moduleStatuses = IRStatus.extract(statuses, AModuleDeclCG.class);
		List<IRStatus<org.overture.codegen.cgast.INode>> modulesAsNodes = IRStatus.extract(moduleStatuses);
		
		List<IRStatus<AClassDeclCG>> classStatuses = IRStatus.extract(modulesAsNodes, AClassDeclCG.class);
		classStatuses.addAll(IRStatus.extract(statuses, AClassDeclCG.class));
		List<AClassDeclCG> classes = getClassDecls(classStatuses);
		//FunctionValueAssistant functionValueAssistant = new FunctionValueAssistant();
		
		// Transform IR	
		RustTransSeries xTransSeries = new RustTransSeries(this);
		List<DepthFirstAnalysisAdaptor> transformations = xTransSeries.consAnalyses(classes);
		
		// Generate IR to syntax (generate code)
		
		GeneratedData data = new GeneratedData();
		
		// Add generated code to 'data'
		//templateManager = new TemplateManager(new TemplateStructure("MyTemplates"));
		//MergeVisitor mergeVisitor = new MergeVisitor(templateManager, new TemplateCallable[]{new TemplateCallable("CGh", new CGHelper())});
		
		RustFormat my_formatter = new RustFormat();
		
		List<GeneratedModule> generated = new LinkedList<GeneratedModule>();
		
		for (DepthFirstAnalysisAdaptor trans : transformations)
		{
			for (IRStatus<AClassDeclCG> status : IRStatus.extract(statuses,AClassDeclCG.class))
			{
				try
				{
					if (!getInfo().getDeclAssistant().isLibraryName(status.getIrNodeName()))
					{
						generator.applyPartialTransformation(status, trans);
					}

				} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
				{
					Logger.getLog().printErrorln("Error when generating code for class "
							+ status.getIrNodeName() + ": " + e.getMessage());
					Logger.getLog().printErrorln("Skipping class..");
					e.printStackTrace();
				}
			}
		}
		
		for(IRStatus<AClassDeclCG> status : IRStatus.extract(statuses,AClassDeclCG.class))
		{
			StringWriter writer = new StringWriter();
			AClassDeclCG classCg = status.getIrNode();

			try {
				classCg.apply(my_formatter.GetMergeVisitor(), writer);
				
				GeneratedModule generatedModule = new GeneratedModule(status.getIrNodeName(), classCg, writer.toString());
				generatedModule.setTransformationWarnings(status.getTransformationWarnings());
				generated.add(generatedModule);
				
			} catch (org.overture.codegen.cgast.analysis.AnalysisException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		data.setClasses(generated);
		
		return data;
	}
	
	private List<AClassDeclCG> getClassDecls(
			List<IRStatus<AClassDeclCG>> statuses)
	{
		List<AClassDeclCG> classDecls = new LinkedList<AClassDeclCG>();

		for (IRStatus<AClassDeclCG> status : statuses)
		{
			classDecls.add(status.getIrNode());
		}

		return classDecls;
	}
}