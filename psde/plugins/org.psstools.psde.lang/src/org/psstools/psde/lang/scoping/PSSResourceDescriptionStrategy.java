package org.psstools.psde.lang.scoping;

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
