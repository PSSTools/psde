package net.sf.psstools.lang.pssc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import net.sf.psstools.lang.PSSStandaloneSetup;

public class PSSCApp implements IApplication {
	
	private static void print_help() {
		System.out.println("pssc [options] <files>");
		System.out.println("  Options:");
		System.out.println("    -h|-help|--help - Displays this message");
		System.out.println("  <files> - PSS files to parse (.pss extension)");
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Injector injector = new PSSStandaloneSetup().createInjectorAndDoEMFRegistration();

		String args[] = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		
		XtextResourceSet resource_set = (XtextResourceSet)injector.getInstance(
				XtextResourceSet.class);
		resource_set.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		List<Resource> resources = new ArrayList<Resource>();

		System.out.println("pssc - Portable Stimulus Specification Checker");
		System.out.println();
		System.out.println("Licensed under the Apache License, Version 2.0 (the");
		System.out.println("\"License\"); you may not use this file except in");
		System.out.println("compliance with the License.  You may obtain a copy of");
		System.out.println("the License at");
		System.out.println();
		System.out.println("http://www.apache.org/licenses/LICENSE-2.0");
		System.out.println();
		System.out.println("Unless required by applicable law or agreed to in");
		System.out.println("writing, software distributed under the License is");
		System.out.println("distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR");
		System.out.println("CONDITIONS OF ANY KIND, either express or implied.  See");
		System.out.println("the License for the specific language governing");
		System.out.println("permissions and limitations under the License.");
		System.out.println();
		System.out.println("Copyright 2016-2017 Mentor Graphics Corporation");
		System.out.println();
		System.out.println();
		
		int errors=0;
		for (int i=0; i<args.length && errors == 0; i++) {
			if (args[i].charAt(0) == '-') {
				if (args[i].equals("-h") || args[i].equals("-help") ||
						args[i].equals("--help")) {
					print_help();
					System.exit(0);
				} else {
					System.out.println("Error: unknown option " + args[i]);
					print_help();
					errors++;
				}
			} else if (args[i].endsWith(".pss")) {
				File f = new File(args[i]);
				
				if (!f.isFile()) {
					System.out.println("Error: file " + args[i] + " does not exist");
					errors++;
				} else {
					String uri = "file://" + f.getAbsolutePath().replace('\\', '/');
					Resource r = resource_set.getResource(URI.createURI(uri), true);
					resources.add(r);
				}
			} else {
				System.out.println("Error: unknown argument " + args[i]);
				print_help();
				errors++;
			}
		}
		
		if (resource_set.getResources().size() == 0) {
			System.out.println("Error: no files specified");
			errors++;
		}
		
		if (errors == 0) {
			for (Resource r : resource_set.getResources()) {
				String path = r.getURI().toFileString();
				if (path.startsWith("//")) {
					path = path.substring(2);
				}
				System.out.println("Note: checking " + path);
				for (Resource.Diagnostic d : r.getErrors()) {
					System.out.println("Error: " + path + ":" + d.getLine() + " " + 
							d.getMessage());
					errors++;

					if (errors > 10) {
						break;
					}
				}
			}
		}

		System.out.println("Errors: " + errors);
	
		if (errors > 0) {
			System.exit(1);
		}
		
		return Status.OK_STATUS;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
