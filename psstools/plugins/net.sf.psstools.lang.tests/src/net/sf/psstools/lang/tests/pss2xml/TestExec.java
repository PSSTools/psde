package net.sf.psstools.lang.tests.pss2xml;

import junit.framework.TestCase;

public class TestExec extends TestCase {

	public void testTargetTemplateExec_1() {
		String doc = 
				"component top_comp {\n" +
				"	action a {\n" +
				"		exec body C = \"\"\"\n" +
				"			a = b;\n" +
				"		\"\"\"\n" +
				"	}\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
}
