package net.sf.psstools.lang.psi;

public interface IExpr {
	enum Type {
		BINARY_EXPR,
		UNARY_EXPR,
		TERNATRY_EXPR,
		INSIDE_EXPR
	}
	
	IExpr.Type getExprType();

}
