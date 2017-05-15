package net.sf.psstools.lang.tests.pss2xml;

import junit.framework.TestCase;

public class TestUseExample extends TestCase {
	
	public void testDmaChainingExample() {
		PSS2XMLTests.runTestWithReference(
				"/data/pss2xml/use_example/dma_chaining_example2.pss",
				"");
	}
	
	public void testActionExec_1() {
		PSS2XMLTests.runTestWithReference(
				"/data/pss2xml/use_example/pss_action_exec_1.pss",
				"");
	}

}
