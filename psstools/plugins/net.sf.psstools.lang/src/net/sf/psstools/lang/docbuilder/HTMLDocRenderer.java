package net.sf.psstools.lang.docbuilder;

import java.io.OutputStream;
import java.io.PrintStream;

public class HTMLDocRenderer implements IDocRenderer {
	private PrintStream				fPS;
	private boolean					fWSInserted;
	private int						fLen;
	private int						fHeadBase = 1;
	
	public HTMLDocRenderer(OutputStream os) {
		fPS = new PrintStream(os);
		
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
