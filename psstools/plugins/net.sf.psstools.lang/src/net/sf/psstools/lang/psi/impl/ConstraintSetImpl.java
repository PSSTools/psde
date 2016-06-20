package net.sf.psstools.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.psstools.lang.psi.IConstraintSet;
import net.sf.psstools.lang.psi.IConstraintStmt;

public class ConstraintSetImpl extends ConstraintStmtImpl implements IConstraintSet {
	private List<IConstraintStmt>		fConstraints;
	
	public ConstraintSetImpl(List<IConstraintStmt> constraints) {
		super(IConstraintStmt.Type.CONSTRAINT_SET);
		fConstraints = new ArrayList<IConstraintStmt>(constraints);
	}
	
	public ConstraintSetImpl(IConstraintStmt... constraints) {
		super(IConstraintStmt.Type.CONSTRAINT_SET);
		fConstraints = new ArrayList<IConstraintStmt>();
		for (IConstraintStmt s : constraints) {
			fConstraints.add(s);
		}
	}

	@Override
	public List<IConstraintStmt> get_constraint_stmts() {
		return fConstraints;
	}

}
