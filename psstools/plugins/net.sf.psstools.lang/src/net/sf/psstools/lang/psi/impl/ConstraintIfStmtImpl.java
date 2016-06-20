package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IConstraintIfStmt;
import net.sf.psstools.lang.psi.IConstraintStmt;
import net.sf.psstools.lang.psi.IExpr;

public class ConstraintIfStmtImpl extends ConstraintStmtImpl implements IConstraintIfStmt {
	private IExpr				fCond;
	private IConstraintStmt		fTrue;
	private IConstraintStmt		fFalse;
	
	public ConstraintIfStmtImpl(
			IExpr				cond,
			IConstraintStmt		true_s,
			IConstraintStmt		false_s) {
		super(Type.CONSTRAINT_IF_STMT);
		fCond = cond;
		fTrue = true_s;
		fFalse = false_s;
	}

	@Override
	public IExpr get_cond_expr() {
		return fCond;
	}

	@Override
	public IConstraintStmt get_true() {
		return fTrue;
	}

	@Override
	public IConstraintStmt get_false() {
		return fFalse;
	}

}
