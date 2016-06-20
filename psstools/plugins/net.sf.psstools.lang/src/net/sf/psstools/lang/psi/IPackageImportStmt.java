package net.sf.psstools.lang.psi;

import java.util.List;

public interface IPackageImportStmt {
	
	List<String> getPath();
	
	boolean isWildcard();

}
