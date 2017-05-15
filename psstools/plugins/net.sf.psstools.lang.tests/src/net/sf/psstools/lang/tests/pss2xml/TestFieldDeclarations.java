package net.sf.psstools.lang.tests.pss2xml;

import junit.framework.TestCase;

public class TestFieldDeclarations extends TestCase {
	
	public void testArrayStructFieldDeclaration() {
		String doc =
			"struct my_struct {\n" +
			"	rand bit		my_arr[5];\n" +
			"}\n"
			;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(), 
				doc,
				xml);
	}

	public void testArrayActionFieldDeclaration() {
		String doc =
			"component my_component {\n" +
			"	action my_action {\n" +
			"		rand bit		my_arr[5];\n" +
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
