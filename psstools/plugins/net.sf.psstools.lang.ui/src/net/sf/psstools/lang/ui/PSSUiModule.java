/****************************************************************************
 * Copyright 2015 Mentor Graphics Corporation
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
package net.sf.psstools.lang.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.folding.IFoldingRegionProvider;
import org.eclipse.xtext.ui.editor.folding.IFoldingStructureProvider;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.outline.impl.OutlinePage;

import net.sf.psstools.lang.ui.editor.PSSFoldingRegionProvider;
import net.sf.psstools.lang.ui.editor.PSSFoldingStructureProvider;
import net.sf.psstools.lang.ui.editor.PSSOutlinePage;
import net.sf.psstools.lang.ui.editor.hover.PSSHoverProvider;
import net.sf.psstools.lang.ui.internal.PSSActivator;

/**
 * Use this class to register components to be used within the IDE.
 */
public class PSSUiModule extends net.sf.psstools.lang.ui.AbstractPSSUiModule {
	private static AbstractUIPlugin		fPlugin;
	
	public PSSUiModule(AbstractUIPlugin plugin) {
		super(plugin);
		fPlugin = plugin;
	}
	
	public static <T> T getInstance(Class<T> type) {
		return PSSActivator.getInstance().getInjector(
				PSSActivator.NET_SF_PSSTOOLS_LANG_PSS).getInstance(type);
	}
	
	public static AbstractUIPlugin getPlugin() {
		return fPlugin;
	}
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				"net.sf.psstools.lang.ui", key);
	}
	
	public Class<? extends IFoldingStructureProvider> bindIFoldingStructureProvider() {
		return PSSFoldingStructureProvider.class;
	}
	
	public Class<? extends OutlinePage> bindOutlinePage() {
		return PSSOutlinePage.class;
	}

	public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
		return net.sf.psstools.lang.ui.contentassist.BasePSSProposalProvider.class;
	}
	
	public Class<? extends IFoldingRegionProvider> bindIFoldingRegionProvider() {
		return PSSFoldingRegionProvider.class;
	}

	// contributed by org.eclipse.xtext.ui.generator.labeling.LabelProviderFragment
	public Class<? extends org.eclipse.jface.viewers.ILabelProvider> bindILabelProvider() {
		return net.sf.psstools.lang.ui.labeling.PSSDecoratingLabelProvider.class;
	}
	
	public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
		return PSSHoverProvider.class;
	}
}
