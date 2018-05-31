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
package org.psstools.psde.lang.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.eclipse.xtext.util.Modules2;
import org.psstools.psde.lang.PSSRuntimeModule;
import org.psstools.psde.lang.PSSStandaloneSetup;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class PSSIdeSetup extends PSSStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new PSSRuntimeModule(), new PSSIdeModule()));
	}
	
}
