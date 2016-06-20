/****************************************************************************
 * Copyright 2015 Mentor Graphics Corporation
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
package net.sf.psstools.lang.pss2xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import net.sf.psstools.lang.PSSStandaloneSetup;
import net.sf.psstools.lang.pSS.Model;

public class PSS2XML {

	//
	// - root action
	// - packages to include
	public static void main(String args[]) {
		OutputStream out = null;
	
		PSSStandaloneSetup setup = new PSSStandaloneSetup();
		Injector injector = setup.createInjectorAndDoEMFRegistration();
//		Injector injector = setup.createInjector();
		XtextResourceSet resource_set = (XtextResourceSet)injector.getInstance(XtextResourceSet.class);
		resource_set.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
//		Resource resource = resource_set.createResource(URI.createURI("temp"));
//		Resource resource = resource_set.createResource(URI.createURI("http://www.sf.net/psstools/lang/PSS"));

		List<String> packages = new ArrayList<>();
		
		for (int i=0; i<args.length; i++) {
			String arg = args[i];
			
			if (arg.charAt(0) == '-') {
				if (arg.equals("-o")) {
					String path = args[++i];
					if (path.equals("-")) {
						out = System.out;
					} else {
						try {
							out = new FileOutputStream(path);
						} catch (IOException e) {
							System.out.println("Error: Failed to open file " + path);
							System.exit(1);
						}
					}
				} else {
					System.out.println("Error: Unknown option " + arg);
				}
			} else if (arg.endsWith(".pss")) {
				File f = new File(arg);
				String uri = "file://" + f.getAbsolutePath().replace('\\', '/');
				Resource r = resource_set.getResource(URI.createURI(uri), true);
			} else {
				System.out.println("Error: Unknown argument " + arg);
			}
			System.out.println("ARG[" + i + "] " + args[i]);
		}
		
		if (out == null) {
			System.out.println("Error: no output path specified");
			System.exit(1);
		}
		
		TreeIterator<Notifier> all_contents = resource_set.getAllContents();
		
		EcoreUtil.resolveAll(resource_set);
		
		Elaborator elab = new Elaborator(out);
		
		while (all_contents.hasNext()) {
			Notifier n = all_contents.next();
			if (n instanceof Model) {
				elab.elaborate((Model)n);
//				Model m = (Model)n;
//				for (portable_stimulus_description d : m.getRoot()) {
//					model.add(d);
//				}
			}
		}
		
		elab.close();
	}
	
	private void recurse(String ind, EObject obj) {
		System.out.println(ind + obj.toString());
		
//		if (obj instanceof graph_declaration) {
//			graph_declaration g = (graph_declaration)obj;
//			graph_or_struct_declaration gs = (graph_or_struct_declaration)g.getSuper();
//			System.out.println("Super: " + gs.eIsProxy());
//			System.out.println("  instanceof graph: " + (gs instanceof graph_declaration));
//			System.out.println("  instanceof struct: " + (gs instanceof graph_declaration));
//			recurse("      ", gs);
//			for (EObject eo : g.eCrossReferences()) {
//				System.out.println("cross: " + eo);
//			}
//		}
		for (EObject eo : obj.eContents()) {
			recurse(ind + "  ", eo);
		}
	}	

}
