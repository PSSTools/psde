package net.sf.psstools.lang.ui.labeling;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.ui.PlatformUI;

import com.google.inject.Inject;

public class PSSDecoratingLabelProviderSav extends DecoratingLabelProvider {

	@Inject
	public PSSDecoratingLabelProviderSav(AdapterFactoryLabelProvider delegate) {
		super(new PSSLabelProvider(delegate),
				PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());
	}

}
