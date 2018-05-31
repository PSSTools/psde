package org.psstools.psde.lang.formatter;

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
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.parsetree.reconstr.ITokenStream;

public class PSSFormatter extends AbstractDeclarativeFormatter {
	
	public PSSFormatter() {
	}

	@Override
	protected void configureFormatting(FormattingConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITokenStream createFormatterStream(String indent, ITokenStream out, boolean preserveWhitespaces) {
		// TODO Auto-generated method stub
		System.out.println("createFormatterStream");
		return super.createFormatterStream(indent, out, preserveWhitespaces);
	}

	@Override
	public ITokenStream createFormatterStream(EObject context, String indent, ITokenStream out,
			boolean preserveWhitespaces) {
		System.out.println("createFormatterStream(2)");
		// TODO Auto-generated method stub
		return super.createFormatterStream(context, indent, out, preserveWhitespaces);
	}
	
	

}
