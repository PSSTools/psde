package net.sf.psstools.lang.tests.pss2xml;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.psstools.lang.PSSStandaloneSetup;
import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pss2xml.Elaborator;
import net.sf.psstools.lang.tests.PSSLanguageTestsPlugin;

public class PSS2XMLTests extends TestSuite {
	
	public static void runTestWithReference(
			String				pss_file,
			String				xml_file) {
		Bundle b = PSSLanguageTestsPlugin.getDefault().getBundle();
		URL pss_file_u = b.getEntry(pss_file);
		URL xml_file_u = b.getEntry(xml_file);
		
		TestCase.assertNotNull(pss_file_u);

		PSSStandaloneSetup setup = new PSSStandaloneSetup();
		Injector injector = setup.createInjectorAndDoEMFRegistration();
		XtextResourceSet resource_set = 
				(XtextResourceSet)injector.getInstance(XtextResourceSet.class);
		
		resource_set.addLoadOption(
				XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		System.out.println("URI: " + pss_file_u);
		resource_set.getResource(URI.createURI(pss_file_u.toString()), true);
		
		ByteArrayOutputStream pss_out = new ByteArrayOutputStream();
		
		TreeIterator<Notifier> all_contents = resource_set.getAllContents();
		EcoreUtil.resolveAll(resource_set);

		Elaborator elab = new Elaborator(pss_out);
		
		while (all_contents.hasNext()) {
			Notifier n = all_contents.next();
			
			if (n instanceof Model) {
				elab.elaborate((Model)n);
			}
		}
		
		elab.close();
		
		System.out.println("Result: " + pss_out.toString());
	}

}
