package org.psstools.psde.lang.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;
import org.psstools.psde.lang.pSS.action_declaration;
import org.psstools.psde.lang.pSS.component_declaration;

public class PSSResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	
	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		if (eObject instanceof component_declaration) {
//			System.out.println("Create for component");
			acceptor.accept(EObjectDescription.create(((component_declaration)eObject).getName(), eObject));
		} else if (eObject instanceof action_declaration) {
//			System.out.println("Create for action");
			acceptor.accept(EObjectDescription.create(((action_declaration)eObject).getName(), eObject));
		}
//		if (getQualifiedNameProvider() == null)
//			return false;
//		try {
//			QualifiedName qualifiedName = getQualifiedNameProvider().getFullyQualifiedName(eObject);
//			System.out.println("createEObjectDescriptions: " + eObject + " qualifiedName=" + qualifiedName);
//			if (qualifiedName != null) {
//				acceptor.accept(EObjectDescription.create(qualifiedName, eObject));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return true;		
	}

	@Override
	public boolean createReferenceDescriptions(EObject from, URI exportedContainerURI,
			IAcceptor<IReferenceDescription> acceptor) {
		
		// TODO Auto-generated method stub
		boolean ret = super.createReferenceDescriptions(from, exportedContainerURI, acceptor);
		
//		System.out.println("createReferenceDescriptions: " + from + " " + exportedContainerURI + " => " + ret);
		
		return ret;
	}
	
	

}
