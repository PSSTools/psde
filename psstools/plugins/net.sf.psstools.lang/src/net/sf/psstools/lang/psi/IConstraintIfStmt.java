package net.sf.psstools.lang.psi;

public interface IConstraintIfStmt extends IConstraintStmt {
	
	IExpr get_cond_expr();
	
	IConstraintStmt get_true();
	
	IConstraintStmt get_false();

}
