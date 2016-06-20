package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IBinaryExpr;
import net.sf.psstools.lang.psi.IExpr;

public class BinaryExprImpl extends ExprImpl implements IBinaryExpr {
	private IExpr			fLhs;
	private Op				fOp;
	private IExpr			fRhs;
	
	public BinaryExprImpl(IExpr lhs, Op op, IExpr rhs) {
		super(Type.BINARY_EXPR);
		fLhs = lhs;
		fOp = op;
		fRhs = rhs;
	}

	@Override
	public Op getOp() {
		return fOp;
	}

	@Override
	public IExpr getLhs() {
		return fLhs;
	}

	@Override
	public IExpr getRhs() {
		return fRhs;
	}

}
