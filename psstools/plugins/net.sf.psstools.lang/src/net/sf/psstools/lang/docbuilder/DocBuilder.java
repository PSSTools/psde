/****************************************************************************
 * Copyright 2015 Mentor Graphics Corporation
 * All Rights Reserved Worldwide
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See
 * the License for the specific language governing
 * permissions and limitations under the License.
 ****************************************************************************/
package net.sf.psstools.lang.docbuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.Alternatives;
import org.eclipse.xtext.CharacterRange;
import org.eclipse.xtext.Group;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.NegatedToken;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.impl.AbstractElementImpl;
import org.eclipse.xtext.impl.AssignmentImpl;
import org.eclipse.xtext.impl.CrossReferenceImpl;
import org.eclipse.xtext.impl.GroupImpl;
import org.eclipse.xtext.impl.KeywordImpl;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.impl.TypeRefImpl;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;

public class DocBuilder {
	private IDocRenderer				fRenderer;
	private List<ILeafNode>				fLeafNodes = new ArrayList<ILeafNode>();
	private int							fLeafNodeIdx;
	private Set<String>					fKeywords = new HashSet<String>();
	
	public DocBuilder(IDocRenderer renderer) {
		fRenderer = renderer;
	}
	
	public void process(XtextResource resource) {
		
		ICompositeNode node = NodeModelUtils.getNode(resource.getContents().get(0));
		fKeywords.clear();
		
		fLeafNodes.clear();
		for (ILeafNode ln : node.getLeafNodes()) {
			fLeafNodes.add(ln);
		}
		fLeafNodeIdx = 0;
		
		traverse("", resource.getContents().get(0));
	}
	
	private void traverse(
			String 					ind, 
			EObject 				obj) {
		// Before we 
		if (obj instanceof ParserRule) {
			ParserRule ri = (ParserRule)obj;

			if (processHeaderComment(obj)) {
				// The header provides a replacement for the element
				return;
			}
			
			fRenderer.append(ind);
			fRenderer.append(ri.getName() + " ::= ");
			for (EObject eo : obj.eContents()) {
				production(ind + "    ", eo);
			}
			fRenderer.append("\n\n");
		} else if (obj instanceof TerminalRule &&
				!((TerminalRule)obj).getName().equals("WS")) {
			if (processHeaderComment(obj)) {
				return;
			}
			
			TerminalRule t = (TerminalRule)obj;
			
			fRenderer.append(ind);
			fRenderer.append(t.getName() + " ::= ");
			
			for (EObject eo : obj.eContents()) {
				terminal_production(ind + "    ", eo);
			}
			
			fRenderer.append("\n\n");
		} else {
			for (EObject eo : obj.eContents()) {
				traverse(ind, eo);
			}
		}
	}
	
	private boolean processHeaderComment(EObject eobj) {
		boolean skip_item = false;
		ICompositeNode ri_n = NodeModelUtils.getNode(eobj);
		int lineno = ri_n.getStartLine();
		
		// Spin forward
		while (fLeafNodes != null && fLeafNodeIdx < fLeafNodes.size() &&
				fLeafNodes.get(fLeafNodeIdx).getStartLine() < lineno) {
			ILeafNode ln = fLeafNodes.get(fLeafNodeIdx);
			
			if (ln.getText() != null && ln.getText().trim().startsWith("/*")) {
				List<String> lines = processComment(ln.getText());
				String str;
				
				if (lines.size() > 0 && 
						((str = lines.get(0).trim()) != null) &&
						(str.startsWith("H1:") ||
						 str.startsWith("H2:") ||
						 str.startsWith("H3:"))) {
					int heading_idx = 0; // str.indexOf("H1:");
					int heading_level = 1;
					try {
						heading_level = Integer.parseInt(str.substring(1,2));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					
					String text = str.substring("H1:".length()).trim();
//					fRenderer.setFont(fHeaderFont);
//					fRenderer.append("\n");
//					fRenderer.append(text);
//					fRenderer.append("\n");
//					fRenderer.clrFont();
//					fRenderer.append("\n");
					fRenderer.header(heading_level, text);
				} else if (lines.size() > 0 && lines.get(0).trim().startsWith("BNF:")) {
					int heading_idx = lines.get(0).indexOf("BNF:");
					String text = lines.get(0).substring(heading_idx+"BNF:".length()).trim();
					process(text);
					fRenderer.append("\n\n");
					skip_item = true;
				}
			}
			fLeafNodeIdx++;
		}		
		
		return skip_item;
	}
	
	private List<String> processComment(String comment) {
		List<String> ret = new ArrayList<String>();
		StringBuilder line = new StringBuilder();
		int i=0;
		int ch;
		
		while (i<comment.length()) {
			line.setLength(0);
	
			// Trim leading whitespace
			while (i < comment.length() && Character.isWhitespace(comment.charAt(i))) {
				i++;
			}
			
			ch = (i<comment.length())?comment.charAt(i):-1;
			
			if (ch == '/') {
				ch = (i+1<comment.length())?comment.charAt(i+1):-1;
				if (ch == '*') {
					// skip this line
					while (i < comment.length() && comment.charAt(i) != '\n') {
						i++;
					}
					i++;
				} else if (ch == '/') {
					// Read this line
					while (i < comment.length() && comment.charAt(i) != '\n') {
						if (comment.charAt(i) != '\r') {
							line.append(comment.charAt(i));
						}
						i++;
					}
					i++;
				}
			} else {
				if (ch == '*') {
					i++;
				}
				
				while (i < comment.length() && comment.charAt(i) != '\n') {
					if (comment.charAt(i) != '\r') {
						line.append(comment.charAt(i));
					}
					i++;
				}
				i++;
			}
			
			if (line.length() > 0) {
				ret.add(line.toString());
			}
		}
		
		return ret;
	}
	
	private void production(String ind, EObject obj) {
		AbstractElementImpl ai = (obj instanceof AbstractElementImpl)?(AbstractElementImpl)obj:null;
		String closing = "";
		
		if (ai != null && ai.getCardinality() != null) {
			fRenderer.insertWS();
			if (ai.getCardinality().equals("*")) {
				fRenderer.append("{");
				closing = "}";
			} else if (ai.getCardinality().equals("?")) {
				fRenderer.append("[");
				closing = "]";
			}
		}
		
		if (obj instanceof Alternatives) {
			boolean all_keywords = true;
			for (EObject eo : obj.eContents()) {
				if (!(eo instanceof KeywordImpl)) {
					all_keywords = false;
				}
			}
			
			if (all_keywords) {
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					
					fRenderer.insertWS();
					int old_len = fRenderer.length();
					production(ind, eo);
					if (old_len != fRenderer.length() && i+1 < obj.eContents().size()) {
						fRenderer.append(" | ");
					}
				}
			} else {
				fRenderer.append("\n" + ind + "  ");
				for (int i=0; i<obj.eContents().size(); i++) {
					EObject eo = obj.eContents().get(i);
					int old_len = fRenderer.length();
					production(ind, eo);
					if (old_len != fRenderer.length() && i+1 < obj.eContents().size()) {
						fRenderer.append("\n" + ind + "| ");
					}
				}
			}
		} else if (obj instanceof AssignmentImpl) {
			production(ind, obj.eContents().get(0));
		} else if (obj instanceof RuleCallImpl) {
			RuleCallImpl rc = (RuleCallImpl)obj;

			fRenderer.insertWS();
			if (rc.basicGetRule() != null) {
				fRenderer.append(rc.basicGetRule().getName());
			} else {
//				System.out.println("ruleCall: " + rc + " " + rc.eGet(XtextPackage.RULE_CALL, false, false));
			}
		} else if (obj instanceof GroupImpl) {
			for (EObject eo : obj.eContents()) {
				production(ind, eo);
			}
		} else if (obj instanceof KeywordImpl) {
			KeywordImpl k = (KeywordImpl)obj;
			fRenderer.insertWS();
			fRenderer.keyword(k.getValue());
			fKeywords.add(k.getValue());
		} else if (obj instanceof CrossReferenceImpl) {
			CrossReferenceImpl cr = (CrossReferenceImpl)obj;
			for (EObject eo : obj.eContents()) {
				production(ind, eo);
			}
		} else if (obj instanceof RuleCallImpl) {
			RuleCallImpl rc = (RuleCallImpl)obj;
			fRenderer.insertWS();
//			sb.append(rc.getRule().getName());
			fRenderer.append(rc.basicGetRule().getName());
		} else if (obj instanceof TypeRefImpl) {
		}
	
		if (!closing.equals("")) {
			fRenderer.insertWS();
			fRenderer.append(closing);
		}
	}

	private void terminal_production(String ind, EObject obj) {
		debug("terminal_production: " + obj);
		
		if (obj instanceof Group) {
			Group g = (Group)obj;
			debug("Group: " + g.getCardinality());
			for (int i=0; i<g.getElements().size(); i++) {
				AbstractElement e = g.getElements().get(i);
				terminal_production(ind, e);
				if (i+1 < g.getElements().size()) {
					fRenderer.append(" ");
				}
			}
		} else if (obj instanceof CharacterRange) {
			CharacterRange r = (CharacterRange)obj;
			debug("CharacterRange: " + r.getCardinality());
			fRenderer.append("[");
			fRenderer.keyword(r.getLeft().getValue());
			fRenderer.append("-");
			fRenderer.keyword(r.getRight().getValue());
			fRenderer.append("]");
		} else if (obj instanceof Alternatives) {
			Alternatives alt = (Alternatives)obj;
			String end = null;
			if (alt.getCardinality() != null) {
				if (alt.getCardinality().equals("?")) {
					fRenderer.append("[");
					end = "]";
				} else if (alt.getCardinality().equals("*")) {
					fRenderer.append("{");
					end = "}";
				}
			}
			for (int i=0; i<alt.getElements().size(); i++) {
				terminal_production(ind, alt.getElements().get(i));
				if (i+1 < alt.getElements().size()) {
					fRenderer.append("|");
				}
			}
			
			if (end != null) {
				fRenderer.append(end);
			}			
		} else if (obj instanceof Keyword) {
			fRenderer.keyword(((Keyword)obj).getValue());
		} else if (obj instanceof NegatedToken) {
			NegatedToken n = (NegatedToken)obj;
			fRenderer.append("!");
			terminal_production(ind, n.getTerminal());
		} else {
			debug("Unknown obj: " + obj);
		}
	}	

	private void process(String markup) {
		int last_idx=0, idx=0;
		
		while ((idx=markup.indexOf("<kw>", last_idx)) != -1) {
			if (idx > last_idx) {
				fRenderer.append(markup.substring(last_idx, idx));
			}
			int end=markup.indexOf("</kw>", idx);
			
			if (end < 0) {
				error("Markup \"" + markup + "\" has unterminated <kw> tag");
				break;
			}
			fRenderer.keyword(markup.substring(idx+"<kw>".length(), end));
			
			last_idx=end+"</kw>".length();
		}
		
		if (last_idx < markup.length()) {
			fRenderer.append(markup.substring(last_idx, markup.length()));
		}
	}
	
	private void debug(String msg) {
//		System.out.println(msg);
	}
	
	private void error(String msg) {
		System.out.println("Error: " + msg);
	}
}
