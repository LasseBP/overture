package org.overture.codegen.tests.exec;

import java.io.File;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.ast.lex.Dialect;
import org.overture.codegen.execution.tests.CommonJavaGenCheckerTest;
import org.overture.codegen.ir.IRSettings;
import org.overture.codegen.tests.output.TracesExpansionOutputTest;
import org.overture.config.Release;

@RunWith(value = Parameterized.class)
public class TracesExpansionJavaGenTest extends CommonJavaGenCheckerTest
{
	public TracesExpansionJavaGenTest(String name, File vdmSpec,
			TestHandler testHandler)
	{
		super(vdmSpec, testHandler);
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> getData()
	{
		return collectTests(new File(TracesExpansionOutputTest.ROOT),new TraceHandler(Release.VDM_10, Dialect.VDM_PP));
	}
	
	@Override
	public IRSettings getIrSettings()
	{
		IRSettings irSettings = super.getIrSettings();
		irSettings.setGenerateTraces(true);
		
		return irSettings;
	}

	@Override
	protected String getPropertyId()
	{
		return "tracesexpansion";
	}
}
