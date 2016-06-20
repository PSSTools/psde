package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IConstraintStmt;

public class ConstraintStmtImpl implements IConstraintStmt {
	private Type				fType;
	
	public ConstraintStmtImpl(IConstraintStmt.Type type) {
		fType = type;
	}

	@Override
	public Type getType() {
		return fType;
	}

}
