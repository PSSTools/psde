package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IExpr;
import net.sf.psstools.lang.psi.IUnaryExpr;

public class UnaryExprImpl extends ExprImpl implements IUnaryExpr {
	private Op				fOp;
	private IExpr			fExpr;
	
	public UnaryExprImpl(Op op, IExpr expr) {
		super(Type.UNARY_EXPR);
		fOp = op;
		fExpr = expr;
	}

	@Override
	public Op getOp() {
		return fOp;
	}

	@Override
	public IExpr getExpr() {
		return fExpr;
	}

}
