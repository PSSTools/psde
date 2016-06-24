package net.sf.psstools.lang.pss2xml;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.action_body_item;
import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.action_field_declaration;
import net.sf.psstools.lang.pSS.component_body_item;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.constraint_declaration;
import net.sf.psstools.lang.pSS.data_declaration;
import net.sf.psstools.lang.pSS.data_instantiation;
import net.sf.psstools.lang.pSS.dec_number;
import net.sf.psstools.lang.pSS.enum_declaration;
import net.sf.psstools.lang.pSS.enum_item;
import net.sf.psstools.lang.pSS.expression;
import net.sf.psstools.lang.pSS.graph_declaration;
import net.sf.psstools.lang.pSS.graph_labeled_production;
import net.sf.psstools.lang.pSS.graph_parallel_stmt;
import net.sf.psstools.lang.pSS.graph_production;
import net.sf.psstools.lang.pSS.graph_repeat_stmt;
import net.sf.psstools.lang.pSS.graph_sequence_stmt;
import net.sf.psstools.lang.pSS.hex_number;
import net.sf.psstools.lang.pSS.hierarchical_id;
import net.sf.psstools.lang.pSS.import_stmt;
import net.sf.psstools.lang.pSS.integer_type;
import net.sf.psstools.lang.pSS.logical_equality_expr;
import net.sf.psstools.lang.pSS.logical_inequality_expr;
import net.sf.psstools.lang.pSS.number;
import net.sf.psstools.lang.pSS.oct_number;
import net.sf.psstools.lang.pSS.open_range_list;
import net.sf.psstools.lang.pSS.open_range_value;
import net.sf.psstools.lang.pSS.package_body_item;
import net.sf.psstools.lang.pSS.package_declaration;
import net.sf.psstools.lang.pSS.package_identifier;
import net.sf.psstools.lang.pSS.package_import_pattern;
import net.sf.psstools.lang.pSS.struct_body_item;
import net.sf.psstools.lang.pSS.struct_declaration;
import net.sf.psstools.lang.pSS.struct_field_declaration;
import net.sf.psstools.lang.pSS.struct_field_modifier;
import net.sf.psstools.lang.pSS.type_identifier;
import net.sf.psstools.lang.pSS.user_defined_type;
import net.sf.psstools.lang.pSS.variable_ref;

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
			tag += " super=\"" + a.getSuper_spec().getSuper().getName() + "\"";
		}
		tag += ">";
		println(tag);
		inc_indent();
		
		for (action_body_item it : a.getBody()) {
			if (it instanceof constraint_declaration) {
				elaborate_constraint((constraint_declaration)it);
			} else if (it instanceof action_field_declaration) {
				elaborate_action_field((action_field_declaration)it);
			} else if (it instanceof graph_declaration) {
				elaborate_graph((graph_declaration)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
		
		dec_indent();
		println("</action>");
	}
	
	private void elaborate_action_field(action_field_declaration f) {
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<field name=\"" + inst.getName() + "\"";
		
			if (f instanceof struct_field_modifier) {
				struct_field_modifier m = (struct_field_modifier)f;
				if (m.isRand()) {
					tag += " type=\"rand\"";
				}
			}
			
			tag += ">";
			println(tag);
			
			inc_indent();
			elaborate_type(decl.getType());
			dec_indent();
			
			println("</field>");
		}		
	}
	
	private void elaborate_component(component_declaration c) {
		String tag = "<component name=\"" + c.getName() + "\"";
		
		if (c.getSuper() != null) {
			tag += " super=\"" + c.getSuper().getName() + "\"";
		}
		
		tag += ">";
		println(tag);
		inc_indent();
		
		for (component_body_item it : c.getBody()) {
			if (it instanceof action_declaration) {
				elaborate_action((action_declaration)it);
			} else if (it instanceof struct_declaration) {
				elaborate_struct((struct_declaration)it);
			} else if (it instanceof import_stmt) {
				elaborate_import((import_stmt)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
		
		dec_indent();
		println("</component>");
	}
	
	private void elaborate_constraint(constraint_declaration c) {
		String tag = "<constraint";
		
		if (c.getName() != null && !c.getName().equals("")) {
			tag += " name=\"" + c.getName() + "\"";
		}
		tag += ">";
		println(tag);
		inc_indent();
		for (EObject it : c.getBody()) {
			if (it instanceof expression) {
				elaborate_expr((expression)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
		
		dec_indent();
		println("</constraint>");
	}
	
	private void elaborate_expr(EObject obj) {
		if (obj instanceof expression) {
			elaborate_expr((expression)obj);
		} else if (obj instanceof open_range_list) {
			open_range_list r = (open_range_list)obj;
			
			println("<rangelist>");
			inc_indent();
			
			for (open_range_value v : r.getRanges()) {
				println("<rangelist-item>");
				inc_indent();
				elaborate_expr(v.getLhs());
				if (v.getRhs() != null) {
					elaborate_expr(v.getRhs());
				}
				dec_indent();
				println("</rangelist-item>");
			}
			
			dec_indent();
			println("</rangelist>");
		}
	}
	
	private void elaborate_expr(expression expr) {
		if (expr instanceof logical_inequality_expr) {
			logical_inequality_expr li = (logical_inequality_expr)expr;
			
			if (li.getOp().equals("inside")) {
				println("<inside>");
				inc_indent();
				elaborate_expr(li.getLeft());
				elaborate_expr(li.getRight());
				dec_indent();
				println("</inside>");
			} else {
				String tag = "<binexpr op=\"";

				if (li.getOp().equals("<")) {
					tag += "LT";
				} else if (li.getOp().equals(">")) {
					tag += "GT";
				} else if (li.getOp().equals(">=")) {
					tag += "GE";
				} else if (li.getOp().equals("<=")) {
					tag += "LE";
				} else {
					tag += "Unknown:" + li.getOp();
				}
				tag += "/>";

				println(tag);

				inc_indent();
				elaborate_expr(li.getLeft());
				elaborate_expr(li.getRight());
				dec_indent();
				println("</binexpr>");
			}
		} else if (expr instanceof logical_equality_expr) {
			logical_equality_expr le = (logical_equality_expr)expr;
			String tag = "<binexpr op=\"";

			if (le.getOp().equals("==")) {
				tag += "EqEq";
			} else if (le.getOp().equals("!=")) {
				tag += "NotEq";
			} else {
				tag += "Unknown:" + le.getOp();
			}
			tag += "/>";

			println(tag);

			inc_indent();
			elaborate_expr(le.getLeft());
			elaborate_expr(le.getRight());
			dec_indent();
			println("</binexpr>");			
		} else if (expr instanceof number) {
			number n = (number)expr;
			EObject v = n.getValue();
			
			if (v instanceof dec_number) {
				println("<literal type=\"int\" value=\"" + 
						((dec_number)v).getValue() + "\"/>");
			} else if (v instanceof oct_number) {
				println("<literal type=\"int\" value=\"" + 
						((oct_number)v).getValue() + "\"/>");
			} else if (v instanceof hex_number) {
				println("<literal type=\"int\" value=\"" + 
						((hex_number)v).getValue() + "\"/>");
			} else {
				println("<unknown_literal class=\"" + v.getClass() + "\"/>");
			}
		} else if (expr instanceof variable_ref) {
			variable_ref v = (variable_ref)expr;
			EList<String> path = v.getExpr().getPath();
			
			for (int i=0; i<path.size(); i++) {
				if (i+1 < path.size()) {
					println("<fieldref name=\"" + path.get(i) + "\">");
					inc_indent();
				} else {
					println("<fieldref name=\"" + path.get(i) + "\"/>");
				}
			}
			
			for (int i=0; path.size()>0 && i<path.size()-1; i++) {
				dec_indent();
				println("</fieldref>");
			}
		} else {
			println("<unknown_expr class=\"" + expr.getClass() + "\"/>");
		}
	}
	
	private void elaborate_enum(enum_declaration enum_t) {
		println("<enum name=\"" + enum_t.getName() + "\">");
		inc_indent();
		
		for (enum_item it : enum_t.getItems()) {
			// TODO specific values
			println("<enumerator name=\"" + it.getName() + "\"/>");
		}
		
		dec_indent();
		println("</enum>");
	}
	
	private void elaborate_graph(graph_declaration graph) {
		println("<graph>");
		inc_indent();
	
		elaborate_graph_production_list(graph.getBody());
		
		dec_indent();
		println("</graph>");
	}
	
	private void elaborate_graph_production_list(EList<graph_production> productions) {
		for (graph_production p : productions) {
			elaborate_graph_production(p);
		}		
	}
	
	private void elaborate_graph_production(graph_production p) {
		if (p instanceof graph_repeat_stmt) {
			elaborate_graph_repeat((graph_repeat_stmt)p);
		} else if (p instanceof graph_parallel_stmt) {
			elaborate_graph_parallel((graph_parallel_stmt)p);
		} else if (p instanceof graph_sequence_stmt) {
			elaborate_graph_sequence((graph_sequence_stmt)p);
		} else {
			println("<unknown_item class=\"" + p.getClass() + "\"/>");
		}		
	}
	
	private void elaborate_graph_parallel(graph_parallel_stmt p) {
		println("<parallel>");
		inc_indent();

		for (graph_labeled_production lp : p.getProductions()) {
			elaborate_graph_production(lp.getProduction());
		}
		
		dec_indent();
		println("</parallel>");
	}
	
	private void elaborate_graph_repeat(graph_repeat_stmt r) {
		String tag = "<repeat type=\"";
		expression expr = r.getExpr();
	
		if (r.isCount()) {
			String ivar = r.getIvar();
			tag += "count\"";
			
			if (ivar != null) {
				tag += " itvar=\"" + ivar + "\"";
			}
		} else if (r.isPre_cond()) {
			tag += "while\"";
		} else if (r.isPost_cond()) {
			tag += "until\"";
		} else {
			tag += "<UNKNOWN>\"";
		}
		
		tag += "/>";
		println(tag);
		inc_indent();
		
		if (expr != null) {
			elaborate_expr(expr);
		}
		
		elaborate_graph_production_list(r.getBody().getStmt_list());
		
		dec_indent();
		println("</repeat>");
	}
	
	private void elaborate_graph_sequence(graph_sequence_stmt s) {
		if (s.getItems().size() > 0) {
			println("<sequential>");
			inc_indent();
			for (String it : s.getItems()) {
				println("<traverse action=\"" + it + "\"/>");
			}
			dec_indent();
			println("</sequential>");
		} else {
			if (s.getType() != null) {
				println("<unknown_item type=\"inline action declaration\"/>");
			} else {
				String tag = "<travserse action=\"" + s.getItem() + "\"";
				
				if (s.getInline_with() == null) {
					tag += "/>";
					println(tag);
				} else {
					tag += ">";
					println(tag);
					inc_indent();
					println("<unknown_item type=\"inline with constraint\"/>");
					dec_indent();
					println("</traverse>");
				}
			}
		}
	}
	
	private void elaborate_import(import_stmt imp) {
		package_import_pattern p = imp.getPkg();
		String imp_s = tid2string(p.getPackage());
		
		if (p.isWildcard()) {
			imp_s += "::*";
		}
		
		println("<import package=\"" + imp_s + "\"/>");
	}
	
	private void elaborate_package(package_declaration p) {
		String tag = "<package name=\"" + hid2string(p.getName()) + "\">";
		println(tag);
		inc_indent();
		
		for (package_body_item it : p.getBody()) {
			if (it instanceof struct_declaration) {
				elaborate_struct((struct_declaration)it);
			} else if (it instanceof enum_declaration) {
				elaborate_enum((enum_declaration)it);
			} else if (it instanceof import_stmt) {
				elaborate_import((import_stmt)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
	
		dec_indent();
		println("</package>");
	}
	
	private void elaborate_struct(struct_declaration s) {
		String tag = "<struct name=\"" + s.getName() + "\"";
		
		if (s.getSuper() != null) {
			tag += "super=\"" + s.getSuper().getName() + "\"";
		}
		
		if (s.getQualifier() != null) {
			tag += " type=\"" + s.getQualifier().getType() + "\"";
		}
		
		tag += ">";
		println(tag);
	
		inc_indent();
		
		for (struct_body_item it : s.getBody()) {
			if (it instanceof struct_field_declaration) {
				elaborate_struct_field((struct_field_declaration)it);
			} else if (it instanceof constraint_declaration) {
				elaborate_constraint((constraint_declaration)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
			
		}
		
		dec_indent();
		
		println("</struct>");
	}
	
	private void elaborate_struct_field(struct_field_declaration f) {
		
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<field name=\"" + inst.getName() + "\"";
		
			if (f instanceof struct_field_modifier) {
				struct_field_modifier m = (struct_field_modifier)f;
				if (m.isRand()) {
					tag += " type=\"rand\"";
				}
			}
			
			tag += ">";
			println(tag);
			
			inc_indent();
			elaborate_type(decl.getType());
			dec_indent();
			
			println("</field>");
		}
	}

	/**
	 * Elaborate a data type
	 * @param type
	 */
	private void elaborate_type(EObject type) {
		
		if (type instanceof integer_type) {
			integer_type t = (integer_type)type;
			String tag;
			if (t.getTypename().equals("int")) {
				tag = "<int ";
			} else {
				tag = "<bit ";
			}
			
			tag += "msb=\"" + expr2string(t.getLhs()) + "\" ";
			tag += "lsb=\"" + expr2string(t.getRhs()) + "\"";
			
			tag += "/>";
			println(tag);
		} else if (type instanceof user_defined_type) {
			user_defined_type udt = (user_defined_type)type;
			println("<user-defined name=\"" + tid2string(udt.getTypename()) + "\"/>");
		} else {
			println("<unknown_item class=\"" + type.getClass()+ "\"/>");
		}
	}
	
	private String expr2string(expression expr) {
		if (expr instanceof number) {
			number n = (number)expr;
			if (n.getValue() instanceof oct_number) {
				return ((oct_number)n.getValue()).getValue();
			} else if (n.getValue() instanceof dec_number) {
				return ((dec_number)n.getValue()).getValue();
			} else if (n.getValue() instanceof hex_number) {
				return ((hex_number)n.getValue()).getValue();
			} else {
				return "unknown_number: " + n.getValue();
			}
		} else {
			return "unknown_expr: " + expr.getClass();
		}
	}
	
	private static String hid2string(package_identifier id) {
		if (id instanceof hierarchical_id) {
			return hid2string((hierarchical_id)id);
		} else {
			return "UNKNOWN: " + id.getClass();
		}
	}
	
	private static String hid2string(hierarchical_id id) {
		String ret = id.getPath().get(0);
		
		return ret;
	}
	
	private static String tid2string(type_identifier id) {
		String ret = "";
		
		for (int i=0; i<id.getElems().size(); i++) {
			if (ret.length() > 0) {
				ret += "::";
			}
			ret += id.getElems().get(i);
		}

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
