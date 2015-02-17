/*
* generated by Xtext
*/
package net.sf.psstools.lang.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import net.sf.psstools.lang.services.PSSGrammarAccess;

public class PSSParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private PSSGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected net.sf.psstools.lang.parser.antlr.internal.InternalPSSParser createParser(XtextTokenStream stream) {
		return new net.sf.psstools.lang.parser.antlr.internal.InternalPSSParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}
	
	public PSSGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(PSSGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}