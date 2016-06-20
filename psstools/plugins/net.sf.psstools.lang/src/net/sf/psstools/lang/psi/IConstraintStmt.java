package net.sf.psstools.lang.psi;

public interface IConstraintStmt {
	
	enum Type {
		CONSTRAINT_SET,
		CONSTRAINT_EXPR_STMT,
		CONSTRAINT_FOREACH_STMT,
		CONSTRAINT_IF_STMT,
		CONSTRAINT_IMPLICATION_STMT,
		CONSTRAINT_UNIQUE_STMT
	}

	IConstraintStmt.Type getType();
}
