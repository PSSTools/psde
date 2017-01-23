package net.sf.psstools.lang.docbuilder;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HTMLDocRenderer implements IDocRenderer {
	private PrintStream				fPS;
	private boolean					fWSInserted;
	private int						fLen;
	private int						fHeadBase = 1;
	private List<String>			fKeywords;
	
	public HTMLDocRenderer(OutputStream os) {
		fPS = new PrintStream(os);
	
		fKeywords = new ArrayList<String>();
		
		fPS.println("<head>");
		fPS.println("<style>");
		fPS.println("  .kw {");
		fPS.println("    color:#FF0000;");
		fPS.println("    font-weight: bold;");
		fPS.println("  }");
		fPS.println("</style>");
		fPS.println("</head>");
		
		fPS.println();
		fPS.println("<body>");
		fPS.println("<pre>");
	}
	
	public void close() {
		fPS.println("</pre>");
		
		Collections.sort(fKeywords);
	
		fPS.println("<h" + (fHeadBase+1) + ">Keywords</h" + (fHeadBase+1) + ">");
		fPS.println("PSS reserves the keywords listed in the following table");
		fPS.println("<p/>");
		fPS.println("<p/>");
		// Add in keywords table
		int per_column = 15;
		int col_per_page = 4;
		int per_page = per_column * col_per_page;
		int num_pages = ((fKeywords.size()-1)/per_page)+1;
		for (int page=0; page<num_pages; page++) {
			fPS.println("<table cellpadding=\"5\" cellspacing=\"5\" frame=\"box\">");
			for (int row=0; row<per_column; row++) {
				fPS.println("<tr>");
				for (int col=0; col<col_per_page; col++) {
					int idx = (page*per_page) + (col*per_column) + row;
					if (idx < fKeywords.size()) {
						fPS.println("<td><pre>" + fKeywords.get(idx) + "</pre></td>");
					} else {
						fPS.println("<td/>");
					}
				}
				fPS.println("</tr>");
			}
			fPS.println("</table>");
		}
		
		fPS.println("</body>");
		fPS.flush();
	}

	@Override
	public void insertWS() {
		if (!fWSInserted) {
			fWSInserted = true;
			fPS.print(' ');
			fLen++;
		}
	}

	@Override
	public void keyword(String kw) {
		if (kw.length() > 1 && Character.isJavaIdentifierStart(kw.charAt(0))) {
			if (!fKeywords.contains(kw)) {
				fKeywords.add(kw);
			}
		}
		fLen += kw.length();
		fWSInserted = false;
		fPS.print("<span class=\"kw\">" + kw + "</span>");
	}

	@Override
	public void append(String text) {
		fLen += text.length();
		fPS.print(text);
		fWSInserted = (text.length() > 0 &&
				Character.isWhitespace(text.charAt(text.length()-1)));
	}

	@Override
	public void header(int level, String text) {
		fWSInserted = false;
		fLen += text.length();
		level += fHeadBase;
	
		fPS.println("</pre>");
		fPS.println("<h" + level + ">" + text + "</h" + level + ">");
		fPS.println("<pre>");
	}

	@Override
	public int length() {
		return fLen;
	}

}
