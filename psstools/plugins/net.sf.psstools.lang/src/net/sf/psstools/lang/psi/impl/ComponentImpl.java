package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IComponent;

public class ComponentImpl extends NamedItemImpl implements IComponent {
	private IComponent			fSuperComp;
	
	public ComponentImpl(String name, IComponent super_comp) {
		super(name);
		fSuperComp = super_comp;
	}

}
