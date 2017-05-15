package net.sf.psstools.lang.tests.pss2xml;

import junit.framework.TestCase;

public class TestDataTypes extends TestCase {
	
	public void testBoolType() {
		String doc = 
				"struct my_struct {" +
				"	bool		field1;\n" +
				"	rand bool	field2;\n" +
				"}\n"
				;
		String xml =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<pss:model\n" +
				" xmlns:pss=\"http://accellera.org/PSS\"\n" +
				" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				" xsi:schemaLocation=\"http://accellera.org/PSS PSSModel.xsd\">\n" +
				"  <pss:struct name=\"my_struct\">\n" +
				"    <pss:field name=\"field1\">\n" +
				"      <pss:type>\n" +
                "        <pss:bool/>\n" +
                "      </pss:type>\n" +
                "    </pss:field>\n" +
                "    <pss:field name=\"field2\" qualifier=\"rand\">\n" +
                "      <pss:type>\n" +
                "        <pss:bool/>\n" +
                "          </pss:type>\n" +
                "      </pss:field>\n" +
                "  </pss:struct>\n" +
                "</pss:model>\n"
                ;
	
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
	public void testStringType() {
		String doc = 
				"struct my_struct {" +
				"	rand string	field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testBitType() {
		String doc = 
				"struct my_struct {" +
				"	rand bit[3:0]	field1;\n" +
				"	bit				field2;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testIntType() {
		String doc = 
				"struct my_struct {" +
				"	rand int[3:0]	field1;\n" +
				"	int				field2;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
	public void testChandleType() {
		String doc = 
				"struct my_struct {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}	
	
	public void testStructExtension() {
		String doc = 
				"struct base_s {\n" +
				"}\n" +
				"\n" +
				"struct my_struct : base_s {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testMemoryStruct() {
		String doc = 
				"memory struct my_struct {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}

	public void testStreamStruct() {
		String doc = 
				"stream struct my_struct {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
	public void testStateStruct() {
		String doc = 
				"state struct my_struct {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}	
	
	public void testResourceStruct() {
		String doc = 
				"resource struct my_struct {" +
				"	chandle			field1;\n" +
				"}\n"
				;
		String xml = null;
		
		PSS2XMLTests.runTestWithRefString(
				getName(),
				doc,
				xml);
	}
	
}
