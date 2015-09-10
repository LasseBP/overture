package org.overture.codegen.vdm2rust;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.log.NullLogChute;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.node.INode;
import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AModuleDeclCG;
import org.overture.codegen.ir.CodeGenBase;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.ir.IRStatus;
import org.overture.codegen.ir.IrNodeInfo;
import org.overture.codegen.ir.VdmNodeInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.merging.MergeVisitor;
import org.overture.codegen.merging.VelocityLogger;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.utils.GeneratedData;
import org.overture.codegen.utils.GeneratedModule;

public class RustCodeGen extends CodeGenBase {
	
	private RustFormat rustFormatter;
	private RustTransSeries rustTransSeries;
	private VelocityLogger velocityLog;

	public RustCodeGen(){
		initVelocity();

		this.transAssistant = new TransAssistantCG(generator.getIRInfo());
		this.rustFormatter = new RustFormat();
		this.rustTransSeries = new RustTransSeries(this);
	}
	
	private void initVelocity() {
		this.velocityLog = new VelocityLogger();
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM, velocityLog);                        
		Velocity.init();
	}

	public VelocityLogger getVelocityLog() {
		return velocityLog;
	}

	public GeneratedData generateRustFromVdm(List<SClassDefinition> ast)
			throws AnalysisException
	{
		List<IRStatus<org.overture.codegen.cgast.INode>> iRstatuses = genIrStatus(ast);		
		List<GeneratedModule> generated = new LinkedList<GeneratedModule>();
		
		//filter based on unsupported nodes
		iRstatuses = filter(iRstatuses, generated);
		
		List<IRStatus<AClassDeclCG>> classStatuses = IRStatus.extract(iRstatuses, AClassDeclCG.class);
		
		//apply transformations
		for (DepthFirstAnalysisAdaptor trans : rustTransSeries.getTransformations())
		{
			for (IRStatus<AClassDeclCG> status : classStatuses)
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
		
		classStatuses = filter(classStatuses, generated);
		
		List<String> skipping = new ArrayList<>();
		
		MergeVisitor mergeVisitor = rustFormatter.GetMergeVisitor();
		
		for(IRStatus<AClassDeclCG> status : classStatuses)
		{
			StringWriter writer = new StringWriter();
			AClassDeclCG classCg = status.getIrNode();
			String className = status.getIrNodeName();
			INode vdmClass = status.getIrNode().getSourceNode().getVdmNode();
			
			rustFormatter.init();
			
			try
			{
				if (shouldBeGenerated(vdmClass, generator.getIRInfo().getAssistantManager().getDeclAssistant()))
				{
					classCg.apply(mergeVisitor, writer);

					if (mergeVisitor.hasMergeErrors())
					{
						generated.add(new GeneratedModule(className, classCg, mergeVisitor.getMergeErrors()));
					} else if (mergeVisitor.hasUnsupportedTargLangNodes())
					{
						generated.add(new GeneratedModule(className, new HashSet<VdmNodeInfo>(), mergeVisitor.getUnsupportedInTargLang()));
					} else
					{
						String code = writer.toString();						
						
						GeneratedModule generatedModule = new GeneratedModule(className, classCg, code);
						generatedModule.setTransformationWarnings(status.getTransformationWarnings());
						generated.add(generatedModule);
					}
				} else
				{
					if (!skipping.contains(className))
					{
						skipping.add(className);
					}
				}

			} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
			{
				Logger.getLog().printErrorln("Error generating code for class "
						+ status.getIrNodeName() + ": " + e.getMessage());
				Logger.getLog().printErrorln("Skipping class..");
				e.printStackTrace();
			}
		}
		
		GeneratedData data = new GeneratedData();
		data.setClasses(generated);
		data.setSkippedClasses(skipping);
		return data;
	}
	
	private List<IRStatus<org.overture.codegen.cgast.INode>> genIrStatus(
			Iterable<SClassDefinition> ast) throws AnalysisException
	{
		List<IRStatus<org.overture.codegen.cgast.INode>> statuses = new ArrayList<>();
		
		for(INode astNode : ast)
		{
//			VdmAstJavaValidator v = validateVdmNode(node);
//			
//			if(v.hasUnsupportedNodes())
//			{
//				// We can tell by analysing the VDM AST that the IR generator will produce an
//				// IR tree that the Java backend cannot code generate
//				String nodeName = getInfo().getDeclAssistant().getNodeName(node);
//				HashSet<VdmNodeInfo> nodesCopy = new HashSet<VdmNodeInfo>(v.getUnsupportedNodes());
//				statuses.add(new IRStatus<org.overture.codegen.cgast.INode>(nodeName, /* no IR node */null, nodesCopy));
//			}
//			else
//			{
				// Try to produce the IR
				IRStatus<org.overture.codegen.cgast.INode> status = generator.generateFrom(astNode);
				
				if(status != null)
				{
					statuses.add(status);
				}
//			}
		}	
		return statuses;
	}
	
	private <T extends org.overture.codegen.cgast.INode> List<IRStatus<T>> filter(
			List<IRStatus<T>> statuses, List<GeneratedModule> generated)
	{
		List<IRStatus<T>> filtered = new LinkedList<IRStatus<T>>();
		
		for(IRStatus<T> status : statuses)
		{
			if(status.canBeGenerated())
			{
				filtered.add(status);
			}
			else
			{
				generated.add(new GeneratedModule(status.getIrNodeName(), status.getUnsupportedInIr(), new HashSet<IrNodeInfo>()));
			}
		}
		
		return filtered;
	}
	
	private boolean shouldBeGenerated(INode node,
			DeclAssistantCG declAssistant)
	{
		if (declAssistant.isLibrary(node))
		{
			return false;
		}

		String name = null;
		
		if(node instanceof SClassDefinition)
		{
			name = ((SClassDefinition) node).getName().getName();
		}
		else if(node instanceof AModuleModules)
		{
			name = ((AModuleModules) node).getName().getName();
		}
		else
		{
			return true;
		}

		// for (SClassDefinition superDef : classDef.getSuperDefs())
		// {
		// if (declAssistant.classIsLibrary(superDef))
		// {
		// return false;
		// }
		// }

		return true;
	}
}