package net.sf.psstools.lang.psi;

import java.util.List;

public interface IConstraintUniqueStmt extends IConstraintStmt {
	
	List<IExpr> getItems();

}
