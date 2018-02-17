package net.sf.psstools.lang.scoping;

import org.eclipse.xtext.naming.IQualifiedNameConverter;

public class PSSQualifiedNameConverter extends IQualifiedNameConverter.DefaultImpl {

	@Override
	public String getDelimiter() {
		return "::";
	}

}
