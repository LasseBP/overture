package org.overture.interpreter.tests;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.overture.interpreter.tests.framework.ClassRtTestCase;
import org.overturetool.test.framework.BaseTestSuite;
import org.overturetool.test.framework.Properties;
import org.overturetool.vdmj.lex.LexLocation;

public class ClassVdmRtInterpreter extends BaseTestSuite {
	
	public static Test suite() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException
	{
		LexLocation.absoluteToStringLocation = false;
		Properties.recordTestResults = true;
		String name = "Interpreter Class TestSuite";
		String root = "src\\test\\resources\\classesRT";
		//String root = "src\\test\\resources\\test";
		TestSuite test = createTestCompleteFile(name, root, ClassRtTestCase.class,"");
		return test;
	}
}
