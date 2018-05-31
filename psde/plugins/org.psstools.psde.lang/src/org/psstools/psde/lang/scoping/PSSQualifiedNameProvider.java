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
