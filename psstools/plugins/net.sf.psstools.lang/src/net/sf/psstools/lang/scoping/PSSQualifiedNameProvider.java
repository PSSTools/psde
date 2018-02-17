package net.sf.psstools.lang.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.component_declaration;

public class PSSQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {
	
	public PSSQualifiedNameProvider() {
//		System.out.println("PSSQualifiedNameProvider");
	}
	
	@Override
	public QualifiedName getFullyQualifiedName(EObject obj) {
		System.out.println("getFullyQualifiedName: " + obj);
		QualifiedName ret = null;
		if (obj instanceof action_declaration) {
			action_declaration a = (action_declaration)obj;
			if (a.eContainer() instanceof component_declaration) {
				ret = QualifiedName.create(
						((component_declaration)a.eContainer()).getName(),
						a.getName());
			}
		} else {
			ret = super.getFullyQualifiedName(obj);
		}
	
		System.out.println("  QN: " + ret);
		return ret;
	}



	@Override
	protected QualifiedName qualifiedName(Object ele) {
		QualifiedName ret = null;
		System.out.println("qualifiedName: " + ele);
		
		if (ele instanceof action_declaration &&
				((action_declaration)ele).eContainer() instanceof component_declaration) {
			ret = QualifiedName.create(
					((component_declaration)((action_declaration)ele).eContainer()).getName(),
					((action_declaration)ele).getName());
		} else {
			ret = super.qualifiedName(ele);
		}
		
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
