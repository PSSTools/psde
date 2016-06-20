package net.sf.psstools.lang.psi.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.psstools.lang.psi.IPackageImportStmt;

public class PackageImportStmtImpl implements IPackageImportStmt {
	private List<String>			fPath;
	private boolean					fIsWildcard;
	
	public PackageImportStmtImpl(List<String> path, boolean is_wildcard) {
		fPath = new ArrayList<String>(path);
		fIsWildcard = is_wildcard;
	}

	@Override
	public List<String> getPath() {
		return fPath;
	}

	@Override
	public boolean isWildcard() {
		return fIsWildcard;
	}

}
