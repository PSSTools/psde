package net.sf.psstools.lang.psi.impl;

import net.sf.psstools.lang.psi.IExpr;

public class ExprImpl implements IExpr {
	private Type			fType;
	
	protected ExprImpl(Type type) {
		fType = type;
	}

	@Override
	public Type getExprType() {
		return fType;
	}

}
