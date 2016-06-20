package net.sf.psstools.lang.psi.impl;

import java.util.List;

import net.sf.psstools.lang.psi.IConstraintForeachStmt;
import net.sf.psstools.lang.psi.IConstraintSet;
import net.sf.psstools.lang.psi.IConstraintStmt;
import net.sf.psstools.lang.psi.IExpr;

public class ConstraintForeachStmtImpl extends ConstraintStmtImpl implements IConstraintForeachStmt {
	
	private IExpr				fExpr;
	private IConstraintSet		fBody;
	
	public ConstraintForeachStmtImpl(
			IExpr				expr,
			IConstraintSet		body) {
		super(Type.CONSTRAINT_FOREACH_STMT);
		
		fExpr = expr;
		fBody = body;
	}
	
	public ConstraintForeachStmtImpl(
			IExpr					expr,
			List<IConstraintStmt>	body) {
		super(Type.CONSTRAINT_FOREACH_STMT);
		
		fExpr = expr;
		fBody = new ConstraintSetImpl(body);
	}

	public ConstraintForeachStmtImpl(
			IExpr					expr,
			IConstraintStmt...		body) {
		super(Type.CONSTRAINT_FOREACH_STMT);
		
		fExpr = expr;
		fBody = new ConstraintSetImpl(body);
	}
	
	@Override
	public IExpr getExpr() {
		return fExpr;
	}

	@Override
	public IConstraintSet getConstraints() {
		return fBody;
	}
	

}
