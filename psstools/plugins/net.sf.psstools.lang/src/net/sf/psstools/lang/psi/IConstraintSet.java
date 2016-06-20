package net.sf.psstools.lang.psi;

import java.util.List;

public interface IConstraintSet extends IConstraintStmt {
	
	List<IConstraintStmt> get_constraint_stmts();

}
