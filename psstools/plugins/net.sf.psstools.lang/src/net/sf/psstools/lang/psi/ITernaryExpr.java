package net.sf.psstools.lang.psi;

public interface ITernaryExpr extends IExpr {
	
	IExpr getCond();
	
	IExpr getTrue();
	
	IExpr getFalse();

}
