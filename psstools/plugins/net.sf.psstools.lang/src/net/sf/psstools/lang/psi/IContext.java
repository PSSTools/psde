package net.sf.psstools.lang.psi;

public interface IContext extends IExprFactory, IConstraintFactory {
	
	IPackage get_global_package();

	IPackage mk_package(String name);
	
	IComponent mk_component(String name, IComponent super_component);

	void add_component(IComponent comp);
	
	void add_package(IPackage pkg);

}
