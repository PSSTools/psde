package net.sf.psstools.lang.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import net.sf.psstools.lang.pSS.component_declaration;

public class PSSQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {
	
	public PSSQualifiedNameProvider() {
		System.out.println("PSSQualifiedNameProvider");
	}

	public QualifiedName getFullyQualifiedNamed(EObject obj) {
		QualifiedName ret = super.getFullyQualifiedName(obj);
		System.out.println("getFullyQualifiedName: " + obj + " " + ret);
		return ret;
	}

	public QualifiedName qualifiedName(component_declaration d) {
		System.out.println("QualifiedName: component_declaration");
		return null;
	}
	
//	public QualifiedName qualifiedName(type_identifier d) {
//		System.out.println("QualifiedName: type_identifier");
//		return null;
//	}
}
