package org.overture.codegen.tests.output;

import java.io.File;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.codegen.ir.IRSettings;
import org.overture.codegen.tests.output.util.OutputTestUtil;
import org.overture.codegen.tests.output.util.PpSpecificationTest;
import org.overture.core.tests.PathsProvider;

@RunWith(Parameterized.class)
public class TracesVerdictOutputTest extends PpSpecificationTest
{
	public static final String ROOT = "src" + File.separatorChar + "test"
			+ File.separatorChar + "resources" + File.separatorChar
			+ "traces_verdict_specs";

	public TracesVerdictOutputTest(String nameParameter, String inputParameter,
			String resultParameter)
	{
		super(nameParameter, inputParameter, resultParameter);
	}

	@Override
	public IRSettings getIrSettings()
	{
		IRSettings irSettings = super.getIrSettings();
		irSettings.setGenerateTraces(true);
		irSettings.setGeneratePreCondChecks(true);
		irSettings.setGeneratePreConds(true);

		return irSettings;
	}

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> testData()
	{
		return PathsProvider.computePaths(ROOT);
	}

	@Override
	protected String getUpdatePropertyString()
	{
		return OutputTestUtil.UPDATE_PROPERTY_PREFIX + "tracesverdict";
	}
}
