package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IExpr;
import net.sf.psstools.lang.psi.ITernaryExpr;

public class TernaryExprImpl extends ExprImpl implements ITernaryExpr {
	private IExpr			fCond;
	private IExpr			fTrue;
	private IExpr			fFalse;
	
	public TernaryExprImpl(IExpr cond, IExpr true_e, IExpr false_e) {
		super(Type.TERNATRY_EXPR);
		fCond = cond;
		fTrue = true_e;
		fFalse = false_e;
	}

	@Override
	public IExpr getCond() {
		return fCond;
	}

	@Override
	public IExpr getTrue() {
		return fTrue;
	}

	@Override
	public IExpr getFalse() {
		return fFalse;
	}

}
