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
package org.psstools.psde.lang.scoping;


/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class PSSScopeProvider extends AbstractPSSScopeProvider {
//	override getScope(EObject context, EReference reference) {
//		System.out.println("getScope: " + context + " " + reference);
//		val rootE = EcoreUtil2.getRootContainer(context);
//		val rootR = rootE.eResource.contents
//		
//		if (context instanceof action_super_spec) {
//			val action_super_spec ti = context as action_super_spec;
//	
//			val rootElement = EcoreUtil2.getRootContainer(context);
//			System.out.println("rootElement=" + rootElement);
//			val candidates = EcoreUtil2.getAllContentsOfType(rootElement, action_declaration);
//			System.out.println("candidates=" + candidates);
////			return Scopes.sc
//		} else if (context instanceof component_super_spec) {
//			val rootElement = EcoreUtil2.getRootContainer(context);
//			System.out.println("rootElement=" + rootElement.eContainer + " " + rootElement.eResource);
//			val candidates = EcoreUtil2.getAllContentsOfType(rootElement, component_declaration);
//			System.out.println("candidates=" + candidates);
//			
//			val candidate_t = new ArrayList<type_identifier>()
//			for (c : candidates) {
//				val ti = PSSPackage.eINSTANCE.EFactoryInstance.create(PSSPackage.eINSTANCE.gettype_identifier()) as type_identifier;
//				ti.elems.add(c.name);
//				candidate_t.add(ti)
//			}
//			
//		
//			val existingScope = Scopes.scopeFor(candidate_t);
//			
//			//return new FilteringScope(existingScope, [getEObjectOrProxy != context]);	
//			return existingScope
//		} else {
//			
//		}
//		return super.getScope(context, reference);
//	}
}
