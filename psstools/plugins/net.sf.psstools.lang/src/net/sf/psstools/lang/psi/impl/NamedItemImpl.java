package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.INamedItem;

public class NamedItemImpl implements INamedItem {
	private String				fName;
	
	protected NamedItemImpl(String name) {
		fName = name;
	}

	@Override
	public String getName() {
		return fName;
	}

}
