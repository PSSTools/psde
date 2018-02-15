package net.sf.psstools.lang.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;
import org.eclipse.xtext.resource.impl.DefaultResourceDescription;

public class PSSQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {
	
	public PSSQualifiedNameProvider() {
		System.out.println("PSSQualifiedNameProvider");
	}

	public QualifiedName getFullyQualifiedNamed(EObject obj) {
		QualifiedName ret = super.getFullyQualifiedName(obj);
		System.out.println("getFullyQualifiedName: " + obj + " " + ret);
		return ret;
	}
	
}
