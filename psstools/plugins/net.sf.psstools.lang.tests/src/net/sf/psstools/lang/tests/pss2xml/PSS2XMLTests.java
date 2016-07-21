package net.sf.psstools.lang.tests.pss2xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType.Internal.ConversionDelegate.Factory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
		
		File schema_f = PSSLanguageTestsPlugin.getDefault().getSchemaPath();
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source src = new StreamSource(schema_f);
		Schema schema = null;
		
		try {
			schema = factory.newSchema(src);
		} catch (SAXException e) {
			TestCase.fail("Failed to read schema: " + e);
		}		
		
		// Validate the output against the schema
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder parser = null;
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);
			docBuilderFactory.setSchema(schema);
			parser = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			TestCase.fail("Failed to setup XML document: " + e);
		}
		
		final List<SAXParseException> errors = new ArrayList<SAXParseException>();
		parser.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				errors.add(exception);
			}
			
			@Override
			public void error(SAXParseException exception) throws SAXException {
				errors.add(exception);
			}
		});
		
		Document doc = null;
		try {
			doc = parser.parse(new ByteArrayInputStream(pss_out.toByteArray()));
		} catch (Exception e) {
			TestCase.fail("Failed to parser XML document: " + e);
		}
		
		for (SAXParseException e : errors) {
			System.out.println("Error: " + e);
		}
		
		TestCase.assertEquals(0, errors.size());
		

	
//		Validator validator = schema.newValidator();
//		
//		try {
//			validator.validate(new DOMSource(doc));
//		} catch (SAXException e) {
//			TestCase.fail("Failed to validate XML output: " + e);
//		} catch (IOException e) {
//			TestCase.fail("IOException during validation: " + e);
//		}
		
	}

}
