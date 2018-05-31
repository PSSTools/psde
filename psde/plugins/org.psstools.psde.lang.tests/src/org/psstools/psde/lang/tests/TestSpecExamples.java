package org.psstools.psde.lang.tests;

/****************************************************************************
 * Copyright 2015-2018 Mentor Graphics Corporation
 * All Rights Reserved Worldwide
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See
 * the License for the specific language governing
 * permissions and limitations under the License.
 ****************************************************************************/
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
import org.psstools.psde.lang.PSSStandaloneSetup;

import com.google.inject.Injector;

import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class TestSpecExamples extends TestCase {
	private File					fFile;
	
	public TestSpecExamples(File file, String name) {
		fFile = file;
	}

	@Test
	public void testParse() throws IOException {
		Injector injector = new PSSStandaloneSetup().createInjectorAndDoEMFRegistration();
//		PSSLanguageTestUtils test_utils = 
//				(PSSLanguageTestUtils)injector.getInstance(PSSLanguageTestUtils.class);
//		
//		InputStream in = new FileInputStream(fFile);
//		StringBuilder sb = new StringBuilder();
//		byte data[] = new byte[4096];
//		int sz;
//		
//		while ((sz=in.read(data, 0, data.length)) > 0) {
//			for (int i=0; i<sz; i++) {
//				sb.append((char)data[i]);
//			}
//		}
//		in.close();
//	
//		String text = sb.toString();
//		test_utils.parseTest(text);
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
