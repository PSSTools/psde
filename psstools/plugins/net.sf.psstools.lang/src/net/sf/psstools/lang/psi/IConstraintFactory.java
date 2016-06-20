package net.sf.psstools.lang.psi;

import java.util.List;

public interface IConstraintFactory {
	
	IConstraintSet mk_constraint_set(
			List<IConstraintStmt> constraints);
	
	IConstraintSet mk_constraint_set(IConstraintStmt ... constraints); 

	IConstraintBlock mk_constraint_block(
			String				name,
			IConstraintSet		constraints,
			boolean				is_anonymous,
			boolean				is_dynamic);
	
	IConstraintBlock mk_constraint_block(
			String				name,
			IConstraintSet		constraints);
	
	IConstraintExprStmt mk_expr_constraint(IExpr expr);
	
	IConstraintForeachStmt mk_foreach_constraint(
			IExpr				expr,
			IConstraintSet		constraints);
	
	IConstraintForeachStmt mk_foreach_constraint(
			IExpr						expr,
			List<IConstraintStmt>		constraints);
	
	IConstraintForeachStmt mk_foreach_constraint(
			IExpr						expr,
			IConstraintStmt ... 		constraints);
	
}
