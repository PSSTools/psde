package net.sf.psstools.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.mwe2.language.mwe2.impl.ComponentImplCustom;

import net.sf.psstools.lang.psi.IBinaryExpr;
import net.sf.psstools.lang.psi.IComponent;
import net.sf.psstools.lang.psi.IConstraintBlock;
import net.sf.psstools.lang.psi.IConstraintExprStmt;
import net.sf.psstools.lang.psi.IConstraintForeachStmt;
import net.sf.psstools.lang.psi.IConstraintSet;
import net.sf.psstools.lang.psi.IConstraintStmt;
import net.sf.psstools.lang.psi.IContext;
import net.sf.psstools.lang.psi.IExpr;
import net.sf.psstools.lang.psi.IPackage;
import net.sf.psstools.lang.psi.ITernaryExpr;
import net.sf.psstools.lang.psi.IUnaryExpr;

public class ContextImpl implements IContext {
	private IPackage			fGlobalPackage;
	private List<IComponent>	fComponentList;
	private List<IPackage>		fPackageList;
	
	public ContextImpl() {
		fGlobalPackage = new PackageImpl(null);
		fComponentList = new ArrayList<IComponent>();
		fPackageList = new ArrayList<IPackage>();
	}

	@Override
	public IPackage get_global_package() {
		return fGlobalPackage;
	}

	@Override
	public IPackage mk_package(String name) {
		return new PackageImpl(name);
	}
	
	public IComponent mk_component(String name, IComponent super_comp) {
		return null;
	}
	
	
	@Override
	public void add_component(IComponent comp) {
		fComponentList.add(comp);
	}

	@Override
	public void add_package(IPackage pkg) {
		fPackageList.add(pkg);
	}

	@Override
	public ITernaryExpr mk_ternary_expr(IExpr cond, IExpr true_e, IExpr false_e) {
		return new TernaryExprImpl(cond, true_e, false_e);
	}

	@Override
	public IBinaryExpr mk_binary_expr(IExpr lhs, IBinaryExpr.Op op, IExpr rhs) {
		return new BinaryExprImpl(lhs, op, rhs);
	}

	@Override
	public IUnaryExpr mk_unary_expr(net.sf.psstools.lang.psi.IUnaryExpr.Op op, IExpr expr) {
		return new UnaryExprImpl(op, expr);
	}

	@Override
	public IConstraintSet mk_constraint_set(List<IConstraintStmt> constraints) {
		return new ConstraintSetImpl(constraints);
	}

	@Override
	public IConstraintSet mk_constraint_set(IConstraintStmt... constraints) {
		return new ConstraintSetImpl(constraints);
	}

	@Override
	public IConstraintBlock mk_constraint_block(
			String 			name, 
			IConstraintSet 	constraints, 
			boolean 		is_anonymous,
			boolean 		is_dynamic) {
		return new ConstraintBlockImpl(name, is_anonymous, is_dynamic, constraints);
	}

	@Override
	public IConstraintBlock mk_constraint_block(String name, IConstraintSet constraints) {
		return mk_constraint_block(name, constraints, false, false);
	}

	@Override
	public IConstraintExprStmt mk_expr_constraint(IExpr expr) {
		return new ConstraintExprStmtImpl(expr);
	}

	@Override
	public IConstraintForeachStmt mk_foreach_constraint(IExpr expr, IConstraintSet constraints) {
		return new ConstraintForeachStmtImpl(expr, constraints);
	}

	@Override
	public IConstraintForeachStmt mk_foreach_constraint(IExpr expr, List<IConstraintStmt> constraints) {
		return new ConstraintForeachStmtImpl(expr, constraints);
	}

	@Override
	public IConstraintForeachStmt mk_foreach_constraint(IExpr expr, IConstraintStmt... constraints) {
		return new ConstraintForeachStmtImpl(expr, constraints);
	}
	
}
