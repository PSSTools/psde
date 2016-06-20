package net.sf.psstools.lang.psi;

public interface IUnaryExpr extends IExpr {
	
	enum Op {
		UNOP_PLUS,
		UNOP_MINUS,
		UNOP_NOT,
		UNOP_NEG,
		UNOP_AND,
		UNOP_OR,
		UNOP_XOR		
	}
	

	IUnaryExpr.Op getOp();
	
	IExpr getExpr();

}
