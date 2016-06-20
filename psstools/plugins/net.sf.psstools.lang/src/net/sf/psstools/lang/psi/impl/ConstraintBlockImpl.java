package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IConstraintBlock;
import net.sf.psstools.lang.psi.IConstraintSet;

public class ConstraintBlockImpl extends NamedItemImpl implements IConstraintBlock {
	private boolean			fIsAnonymous;
	private boolean			fIsDynamic;
	private IConstraintSet	fConstraintSet;
	
	public ConstraintBlockImpl(
			String 				name, 
			boolean				is_anonymous,
			boolean				is_dynamic,
			IConstraintSet		constraint_set) {
		super(name);
		fIsAnonymous = is_anonymous;
		fIsDynamic = is_dynamic;
		fConstraintSet = constraint_set;
	}

	@Override
	public boolean is_anonymous() {
		return fIsAnonymous;
	}

	@Override
	public boolean is_dynamic() {
		return fIsDynamic;
	}

	@Override
	public IConstraintSet get_constraint_set() {
		return fConstraintSet;
	}

}
