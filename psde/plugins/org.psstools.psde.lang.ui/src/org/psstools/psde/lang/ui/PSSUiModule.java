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
package org.psstools.psde.lang.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.outline.impl.OutlineFilterAndSorter.IComparator;
import org.psstools.psde.lang.ui.labeling.PSSLabelProvider;

import com.google.inject.Binder;

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
public class PSSUiModule extends AbstractPSSUiModule {

	public PSSUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		
		binder.bind(IFoldingRegionProvider.class).to(
				PSSFoldingRegionProvider.class);
	}
	
	@Override
	public Class<? extends ILabelProvider> bindILabelProvider() {
		return PSSLabelProvider.class;
	}
	
	
}
