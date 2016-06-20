package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IConstraintExprStmt;
import net.sf.psstools.lang.psi.IExpr;

public class ConstraintExprStmtImpl extends ConstraintStmtImpl implements IConstraintExprStmt {
	private IExpr			fExpr;
	
	public ConstraintExprStmtImpl(IExpr expr) {
		super(Type.CONSTRAINT_EXPR_STMT);
		fExpr = expr;
	}

	@Override
	public IExpr getExpr() {
		return fExpr;
	}

}
