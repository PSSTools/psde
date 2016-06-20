package net.sf.psstools.lang.ui.editor.hover;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

public class PSSHoverProvider extends DefaultEObjectHoverProvider {
	
	public PSSHoverProvider() {
		super();
	}

	@Override
	protected String getDocumentation(EObject o) {
		System.out.println("getDocumentation: " + o);
		// TODO Auto-generated method stub
		return super.getDocumentation(o);
	}

	@Override
	protected boolean hasHover(EObject o) {
		System.out.println("hasHover: " + o);
		// TODO Auto-generated method stub
		return super.hasHover(o);
	}
	
	
}
