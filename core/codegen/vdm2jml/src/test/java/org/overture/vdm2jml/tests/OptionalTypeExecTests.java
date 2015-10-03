package org.overture.vdm2jml.tests;

import java.io.File;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.codegen.utils.GeneralUtils;

@RunWith(Parameterized.class)
public class OptionalTypeExecTests extends JmlExecTestBase
{
	public static final String TEST_DIR = JmlExecTestBase.TEST_RES_DYNAMIC_ANALYSIS_ROOT + "optionaltype";

	public static final String PROPERTY_ID = "optionaltype";
	
	public OptionalTypeExecTests(File inputFile)
	{
		super(inputFile);
	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data()
	{
		return TestUtil.collectVdmslFiles(GeneralUtils.getFilesRecursively(new File(TEST_DIR)));
	}
	
	protected String getPropertyId()
	{
		return PROPERTY_ID;
	}
}
