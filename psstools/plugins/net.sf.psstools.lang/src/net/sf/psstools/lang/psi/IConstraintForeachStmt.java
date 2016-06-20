package net.sf.psstools.lang.psi;

public interface IConstraintForeachStmt extends IConstraintStmt {
	
	IExpr getExpr();
	
	IConstraintSet getConstraints();

}
