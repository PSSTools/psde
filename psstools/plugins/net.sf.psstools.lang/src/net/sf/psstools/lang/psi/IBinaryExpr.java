package net.sf.psstools.lang.psi;

public interface IBinaryExpr extends IExpr {

	enum Op {
		BINOP_LOGICAL_OR,		// ||
		BINOP_LOGICAL_AND,		// &&
		BINOP_BINARY_OR,		// |
		BINOP_BINARY_XOR,		// ^
		BINOP_BINARY_AND,		// &
		BINOP_EQ,				// ==
		BINOP_NEQ,				// !=
		BINOP_LT,				// <
		BINOP_LE,				// <=
		BINOP_GT,				// >
		BINOP_GE,				// >=
		BINOP_LSHIFT,			// <<
		BINOP_RSHIFT,			// >>
		BINOP_ADD,				// +
		BINOP_SUB,				// -
		BINOP_MUL,				// *
		BINOP_DIV,				// /
		BINOP_MOD,				// %
		BINOP_EXP				// **		
	}
	
	IBinaryExpr.Op getOp();
	
	IExpr getLhs();
	
	IExpr getRhs();

}
