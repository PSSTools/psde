package net.sf.psstools.lang.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.inject.Injector;

import junit.framework.TestCase;
import net.sf.psstools.lang.PSSStandaloneSetup;

@RunWith(Parameterized.class)
public class TestSpecExamples extends TestCase {
	private File					fFile;
	
	public TestSpecExamples(File file, String name) {
		fFile = file;
	}

	@Test
	public void testParse() throws IOException {
		Injector injector = new PSSStandaloneSetup().createInjectorAndDoEMFRegistration();
		PSSLanguageTestUtils test_utils = 
				(PSSLanguageTestUtils)injector.getInstance(PSSLanguageTestUtils.class);
		
		InputStream in = new FileInputStream(fFile);
		StringBuilder sb = new StringBuilder();
		byte data[] = new byte[4096];
		int sz;
		
		while ((sz=in.read(data, 0, data.length)) > 0) {
			for (int i=0; i<sz; i++) {
				sb.append((char)data[i]);
			}
		}
		in.close();
	
		String text = sb.toString();
		test_utils.parseTest(text);
	}
	
	@Parameters(name="{1}")
	public static Collection<Object[]> buildTestSet() {
		File spec_examples = new File(System.getProperty("PSSTOOLS"), 
				"examples/spec_examples");
		System.out.println("spec_examples=" + spec_examples.getAbsolutePath());
		Collection<Object[]> data = new ArrayList<Object[]>();
	
		for (File f : spec_examples.listFiles()) {
			if (f.getName().endsWith(".pss")) {
				String name = f.getName();
				if (name.lastIndexOf('.') != -1) {
					name = name.substring(0, name.lastIndexOf('.'));
				}
				Object[] test_data = new Object[] { f, name };
				data.add(test_data);
			}
		}
	
		return data;
	}

}
