package net.sf.psstools.lang.pss2xml;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.emf.ecore.EObject;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.hierarchical_id;
import net.sf.psstools.lang.pSS.package_declaration;
import net.sf.psstools.lang.pSS.struct_declaration;

public class Elaborator {
	private PrintStream			fPS;
	private String				fInd;
	private int					fIndInc = 4;
	
	public Elaborator(OutputStream	out) {
		fPS = new PrintStream(out);
		fInd = "";
		
		println("<model>");
		inc_indent();
	}
	
	public void elaborate(Model m) {
		for (EObject o : m.eContents()) {
			if (o instanceof action_declaration) {
				elaborate_action((action_declaration)o);
			} else if (o instanceof component_declaration) {
				elaborate_component((component_declaration)o);
			} else if (o instanceof package_declaration) {
				elaborate_package((package_declaration)o);
			} else if (o instanceof struct_declaration) {
				elaborate_struct((struct_declaration)o);
			} else {
				System.out.println("Object: " + o.getClass());
			}
		}
		
	}
	
	public void close() {
		dec_indent();
		println("</model>");
		fPS.flush();
	}
	
	private void elaborate_action(action_declaration a) {
		String tag = "<action name=\"" + a.getName() + "\"";
		
		if (a.getSuper_spec() != null) {
			// TODO:
		}
		tag += ">";
		println(tag);
		inc_indent();
		
		dec_indent();
		println("</action>");
	}
	
	private void elaborate_component(component_declaration c) {
		String tag = "<component name=\"" + c.getName() + "\"";
		
		if (c.getSuper() != null) {
			// TODO:
		}
		
		tag += ">";
		println(tag);
		inc_indent();
		
		dec_indent();
		println("</component>");
	}
	
	private void elaborate_package(package_declaration p) {
		String tag = "<package name=\"" + p.getName() + "\">";
		println(tag);
		inc_indent();
	
		dec_indent();
		println("</package>");
	}
	
	private void elaborate_struct(struct_declaration s) {
		String tag = "<struct name=\"" + s.getName() + "\"";
		
		if (s.getSuper() != null) {
			// TODO:
		}
		
		tag += ">";
		println(tag);
		
		println("</struct>");
	}
	
	private String hid2string(hierarchical_id id) {
		String ret = id.getPath().get(0);
		
		return ret;
	}
	
	private void println(String m) {
		fPS.println(fInd + m);
	}

	private void inc_indent() {
		for (int i=0; i<fIndInc; i++) {
			fInd += ' ';
		}
	}
	
	private void dec_indent() {
		if (fInd.length() > fIndInc) {
			fInd = fInd.substring(fIndInc);
		} else {
			fInd = "";
		}
	}
	

}
