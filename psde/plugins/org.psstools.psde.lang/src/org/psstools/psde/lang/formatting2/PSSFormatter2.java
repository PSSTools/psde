package org.psstools.psde.lang.formatting2;

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
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.psstools.psde.lang.pSS.action_declaration;
import org.psstools.psde.lang.pSS.impl.action_declarationImpl;

public class PSSFormatter2 extends AbstractFormatter2 {
	

	public void format(action_declaration action, IFormattableDocument document) {
		System.out.println("format: action_declaration");
	
//		textRegionExtensions.regionFor(action).keyword("{").
	}
	
	@Override
	public void format(Object obj, IFormattableDocument document) {
		System.out.println("format");
		if (obj instanceof action_declaration) {
			format((action_declaration)obj, document);
		}
		
		// TODO Auto-generated method stub

	}

}
