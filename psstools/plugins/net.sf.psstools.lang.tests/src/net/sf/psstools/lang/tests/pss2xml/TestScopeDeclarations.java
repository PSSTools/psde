package net.sf.psstools.lang.tests.pss2xml;

import junit.framework.TestCase;

public class TestScopeDeclarations extends TestCase {
	
	public void testActionDeclaration() {
		String doc =
				"component foo {\n" +
				"	action A {\n" +
				"	}\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
	public void testActionExtendsDeclaration() {
		String doc =
				"component foo {\n" +
				"	action A {\n" +
				"	}\n" +
				"\n" +
				"	action B : A {\n" +
				"	}\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testComponentDeclaration() {
		String doc =
				"component C1 {\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testComponentExtendsDeclaration() {
		String doc =
				"component C1 {\n" +
				"}\n" +
				"\n" +
				"component C2 : C1 {\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
	public void testPackageDeclaration() {
		String doc =
				"package P1 {\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}	
}
