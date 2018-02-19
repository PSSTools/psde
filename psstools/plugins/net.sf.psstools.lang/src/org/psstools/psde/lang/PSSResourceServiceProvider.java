package org.psstools.psde.lang;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.resource.impl.DefaultResourceServiceProvider;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.validation.IResourceValidator;

public class PSSResourceServiceProvider extends DefaultResourceServiceProvider {
	
	public PSSResourceServiceProvider() {
		System.out.println("PSSResourceServiceProvider");
	}

	@Override
	public Manager getResourceDescriptionManager() {
		// TODO Auto-generated method stub
		return super.getResourceDescriptionManager();
	}

	
}
