package org.overture.ide.plugins.codegen.commands;

import java.io.File;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.codegen.utils.GeneratedModule;
import org.overture.codegen.vdm2java.JavaCodeGen;
import org.overture.ide.core.IVdmModel;
import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overture.ide.plugins.codegen.Activator;
import org.overture.ide.plugins.codegen.CodeGenConsole;
import org.overture.ide.plugins.codegen.util.PluginVdm2JavaUtil;
import org.overture.ide.ui.utility.VdmTypeCheckerUi;

public class Vdm2JavaCommand extends AbstractHandler
{

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IProject)
			{
				IProject project = ((IProject) firstElement);
				IVdmProject vdmProject = (IVdmProject) project.getAdapter(IVdmProject.class);

				if (vdmProject == null)
					return null;

				final IVdmModel model = vdmProject.getModel();

				if (model == null || !model.isParseCorrect())
					return null;

				if (!model.isTypeChecked())
					VdmTypeCheckerUi.typeCheck(HandlerUtil.getActiveShell(event), vdmProject);

				if (!model.isTypeCorrect()
						|| !PluginVdm2JavaUtil.isSupportedVdmDialect(vdmProject))
					return null;

				final JavaCodeGen vdm2java = new JavaCodeGen();

				try
				{
					List<IVdmSourceUnit> sources = model.getSourceUnits();
					List<SClassDefinition> mergedParseLists = PluginVdm2JavaUtil.mergeParseLists(sources);

					List<GeneratedModule> userspecifiedClasses = vdm2java.generateJavaFromVdm(mergedParseLists);
					File outputFolder = PluginVdm2JavaUtil.getOutputFolder(vdmProject);
					vdm2java.generateJavaSourceFiles(outputFolder, userspecifiedClasses);
					
					GeneratedModule quotes = vdm2java.generateJavaFromVdmQuotes();
					if(quotes != null)
					{
						File quotesFolder = PluginVdm2JavaUtil.getQuotesFolder(vdmProject);
						vdm2java.generateJavaSourceFile(quotesFolder, quotes);
					}
					
					List<GeneratedModule> utils = vdm2java.generateJavaCodeGenUtils();

					File utilsFolder = PluginVdm2JavaUtil.getUtilsFolder(vdmProject);
					vdm2java.generateJavaSourceFiles(utilsFolder, utils);
					
					project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

					for (GeneratedModule generatedModule : userspecifiedClasses)
					{
						CodeGenConsole.GetInstance().println("*************");
						CodeGenConsole.GetInstance().println(generatedModule.getContent());
					}

				} catch (AnalysisException ex)
				{
					Activator.log("Failed generating code", ex);
					return null;
				} catch (Exception ex)
				{
					Activator.log("Failed generating code", ex);
					return null;
				}
			}
		}
		return null;
	}
}