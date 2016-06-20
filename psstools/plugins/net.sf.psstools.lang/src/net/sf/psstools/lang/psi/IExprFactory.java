package net.sf.psstools.lang.psi;

public interface IExprFactory {
	
	ITernaryExpr mk_ternary_expr(
			IExpr			cond,
			IExpr			true_e,
			IExpr			false_e);
	
	IBinaryExpr mk_binary_expr(
			IExpr			lhs,
			IBinaryExpr.Op	op,
			IExpr			rhs);
	
	IUnaryExpr mk_unary_expr(
			IUnaryExpr.Op	op,
			IExpr			expr);

}
