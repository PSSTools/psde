package org.psstools.psde.lang.apps;

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.xtext.Alternatives;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.XtextStandaloneSetup;
import org.eclipse.xtext.impl.AbstractElementImpl;
import org.eclipse.xtext.impl.AssignmentImpl;
import org.eclipse.xtext.impl.CrossReferenceImpl;
import org.eclipse.xtext.impl.GroupImpl;
import org.eclipse.xtext.impl.KeywordImpl;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.impl.TypeRefImpl;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class ExtractKeywordsApp implements IApplication {
	private Set<String>					fKeywords = new HashSet<String>();

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Injector injector = new XtextStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resource_set = (XtextResourceSet)injector.getInstance(XtextResourceSet.class);
		resource_set.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		String args[] = (String[])context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
	
		File grammar_file = null;
		for (int i=0; i<args.length; i++) {
			if (args[i].charAt(0) == '-') {
				if (args[i].equals("-grammar")) {
					i++;
					grammar_file = new File(args[i]);
				} 
			}
		}
		
		if (grammar_file == null) {
			System.out.println("Error: no -grammar specified");
			return new Integer(1);
		}
		
		String grammar_uri =
				"file://" + grammar_file.getAbsolutePath().replace('\\', '/');
		Resource rs = resource_set.getResource(URI.createURI(grammar_uri), true);
		
		process((XtextResource)rs);
		
		// TODO Auto-generated method stub
		return new Integer(0);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	
	public void process(XtextResource resource) {
		
		ICompositeNode node = NodeModelUtils.getNode(resource.getContents().get(0));
		fKeywords.clear();
		
		traverse("", resource.getContents().get(0));
		
		System.out.println("Keywords:");
		
		String kw_arr[] = fKeywords.toArray(new String[fKeywords.size()]);
		Arrays.sort(kw_arr);
		
		for (String kw : kw_arr) {
			if (kw.length() > 0 && Character.isJavaIdentifierStart(kw.charAt(0))) {
				System.out.println("  " + kw);
			}
		}
	}
	
	private void traverse(
			String 					ind, 
			EObject 				obj) {
		// Before we 
		if (obj instanceof ParserRule) {
			ParserRule ri = (ParserRule)obj;

			for (EObject eo : obj.eContents()) {
				production(ind + "    ", eo);
			}
		} else if (obj instanceof TerminalRule &&
				!((TerminalRule)obj).getName().equals("WS")) {
			
			TerminalRule t = (TerminalRule)obj;
			
			for (EObject eo : obj.eContents()) {
				terminal_production(ind + "    ", eo);
			}
			
		} else {
			for (EObject eo : obj.eContents()) {
				traverse(ind, eo);
			}
		}
	}
	
	private void production(String ind, EObject obj) {
		AbstractElementImpl ai = (obj instanceof AbstractElementImpl)?(AbstractElementImpl)obj:null;
		String closing = "";
		
		if (obj instanceof Alternatives) {
			boolean all_keywords = true;
			for (EObject eo : obj.eContents()) {
				if (!(eo instanceof KeywordImpl)) {
					all_keywords = false;
				}
			}
			
			if (all_keywords) {
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					
					production(ind, eo);
				}
			} else {
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					production(ind, eo);
				}
			}
		} else if (obj instanceof AssignmentImpl) {
			production(ind, obj.eContents().get(0));
		} else if (obj instanceof RuleCallImpl) {
			RuleCallImpl rc = (RuleCallImpl)obj;

		} else if (obj instanceof GroupImpl) {
			for (EObject eo : obj.eContents()) {
				production(ind, eo);
			}
		} else if (obj instanceof KeywordImpl) {
			KeywordImpl k = (KeywordImpl)obj;
			fKeywords.add(k.getValue());
		} else if (obj instanceof CrossReferenceImpl) {
			CrossReferenceImpl cr = (CrossReferenceImpl)obj;
			for (EObject eo : obj.eContents()) {
				production(ind, eo);
			}
		} else if (obj instanceof RuleCallImpl) {
			RuleCallImpl rc = (RuleCallImpl)obj;
		} else if (obj instanceof TypeRefImpl) {
		}
	}

	private void terminal_production(String ind, EObject obj) {
	}	

	private void debug(String msg) {
//		System.out.println(msg);
	}
	
	private void error(String msg) {
		System.out.println("Error: " + msg);
	}	

}
