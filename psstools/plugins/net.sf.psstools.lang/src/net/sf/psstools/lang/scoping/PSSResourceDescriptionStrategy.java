package net.sf.psstools.lang.scoping;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.package_declaration;

public class PSSResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	public PSSResourceDescriptionStrategy() {
		System.out.println("PSSResourceDescriptionStrategy");
	}
	
	@Override
	protected IReferenceDescription createReferenceDescription(EObject owner, URI exportedContainerURI,
			EReference eReference, int indexInList, EObject target) {
//		System.out.println("createReferenceDescription");
		// TODO Auto-generated method stub
		return super.createReferenceDescription(owner, exportedContainerURI, eReference, indexInList, target);
	}

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		QualifiedName qualifiedName = getQualifiedNameProvider().getFullyQualifiedName(eObject);
		
		System.out.println("createEObjectDescription: " + eObject + " " +
				getQualifiedNameProvider());
		if (eObject instanceof component_declaration) {
			System.out.println("Create for component");
			acceptor.accept(EObjectDescription.create(
					((component_declaration)eObject).getName(), eObject));
		} else if (eObject instanceof action_declaration) {
			System.out.println("Create for action");
			if (qualifiedName != null) {
				System.out.println("  use qualified name: " + qualifiedName);
				acceptor.accept(EObjectDescription.create(qualifiedName, eObject));
			} else {
				System.out.println("  use leaf name: " + ((action_declaration)eObject).getName());
				acceptor.accept(EObjectDescription.create(
					((action_declaration)eObject).getName(), eObject));
			}
//		} else if (eObject instanceof package_declaration) {
//			acceptor.accept(EObjectDescription.create(((package_declaration)eObject).getName(), eObject));
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
