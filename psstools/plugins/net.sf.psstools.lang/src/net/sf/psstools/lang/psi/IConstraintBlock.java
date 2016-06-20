package net.sf.psstools.lang.psi;

public interface IConstraintBlock extends INamedItem {
	
	boolean is_anonymous();
	
	boolean is_dynamic();

	IConstraintSet get_constraint_set();

}
