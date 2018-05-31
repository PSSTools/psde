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
package org.psstools.psde.lang;

import org.eclipse.xtext.formatting.IFormatter;
import org.eclipse.xtext.formatting.impl.BaseFormatter;
import org.eclipse.xtext.formatting2.IFormatter2;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.psstools.psde.lang.formatter.PSSFormatter;
import org.psstools.psde.lang.formatting2.PSSFormatter2;
import org.psstools.psde.lang.scoping.PSSResourceDescriptionStrategy;

import com.google.inject.Binder;
import com.google.inject.Provider;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class PSSRuntimeModule extends AbstractPSSRuntimeModule {

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(IDefaultResourceDescriptionStrategy.class)
			.to(PSSResourceDescriptionStrategy.class);
	}

	BaseFormatter f;
	
	@Override
	public Class<? extends IFormatter> bindIFormatter() {
		return PSSFormatter.class;
	}
	
//	@Override
//	public Class<? extends IFormatter2> bindIFormatter2() {
//		return PSSFormatter2.class;
//	}

//	@Override
//	public void configureFormatterPreferences(Binder binder) {
//		// TODO Auto-generated method stub
//		super.configureFormatterPreferences(binder);
//	}
	
	
	
	

//	def bindIResourceDescriptionStrategy() {
//		System.out.println("bindIResourceDescriptionStrategy");
//		return PSSResourceDescriptionStrategy;
//	}
//	
//	def bindDefaultResourceDescriptionStrategy() {
//		System.out.println("bindDefaultResourceDescriptionStrategy");
//		return PSSResourceDescriptionStrategy;
//	}
//	
//	def bindIResourceServiceProvider() {
//		return PSSResourceServiceProvider;
//	}
	
}
