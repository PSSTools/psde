package net.sf.psstools.lang.pss2xml;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.action_body_item;
import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.action_field_declaration;
import net.sf.psstools.lang.pSS.array_dim;
import net.sf.psstools.lang.pSS.component_body_item;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.component_field_declaration;
import net.sf.psstools.lang.pSS.component_path;
import net.sf.psstools.lang.pSS.constraint_declaration;
import net.sf.psstools.lang.pSS.data_declaration;
import net.sf.psstools.lang.pSS.data_instantiation;
import net.sf.psstools.lang.pSS.data_type;
import net.sf.psstools.lang.pSS.dec_number;
import net.sf.psstools.lang.pSS.enum_declaration;
import net.sf.psstools.lang.pSS.enum_item;
import net.sf.psstools.lang.pSS.exec_block;
import net.sf.psstools.lang.pSS.exec_block_stmt;
import net.sf.psstools.lang.pSS.exec_body_stmt;
import net.sf.psstools.lang.pSS.expression;
import net.sf.psstools.lang.pSS.graph_declaration;
import net.sf.psstools.lang.pSS.graph_labeled_production;
import net.sf.psstools.lang.pSS.graph_parallel_stmt;
import net.sf.psstools.lang.pSS.graph_production;
import net.sf.psstools.lang.pSS.graph_repeat_stmt;
import net.sf.psstools.lang.pSS.graph_sequence_stmt;
import net.sf.psstools.lang.pSS.hex_number;
import net.sf.psstools.lang.pSS.hierarchical_id;
import net.sf.psstools.lang.pSS.import_method_decl;
import net.sf.psstools.lang.pSS.import_stmt;
import net.sf.psstools.lang.pSS.integer_type;
import net.sf.psstools.lang.pSS.logical_equality_expr;
import net.sf.psstools.lang.pSS.logical_inequality_expr;
import net.sf.psstools.lang.pSS.method_parameter;
import net.sf.psstools.lang.pSS.method_prototype;
import net.sf.psstools.lang.pSS.number;
import net.sf.psstools.lang.pSS.object_bind_stmt;
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
import net.sf.psstools.lang.pSS.target_code_exec_block;
import net.sf.psstools.lang.pSS.target_file_exec_block;
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
	
		println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		println("<pss:model \n" +
				"  xmlns:pss=\"http://accellera.org/PSS\"\n" +
				"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"  xsi:schemaLocation=\"http://accellera.org/PSS PSSModel.xsd\">");
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
		println("</pss:model>");
		fPS.flush();
	}
	
	private void elaborate_action(action_declaration a) {
		String tag = "<pss:action name=\"" + a.getName() + "\"";
		
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
			} else if (it instanceof exec_block_stmt) {
				elaborate_exec_block_stmt((exec_block_stmt)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
		
		dec_indent();
		println("</pss:action>");
	}
	
	private void elaborate_action_field(action_field_declaration f) {
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<pss:field name=\"" + inst.getName() + "\"";
		
			if (f instanceof struct_field_modifier) {
				struct_field_modifier m = (struct_field_modifier)f;
				if (m.isRand()) {
					tag += " type=\"rand\"";
				}
			}
			
			tag += ">";
			println(tag);
			
			inc_indent();
			elaborate_field_type(inst.getDim(), decl.getType());
			dec_indent();
			
			println("</pss:field>");
		}		
	}
	
	private void elaborate_field_type(array_dim dim, EObject type) {
		elaborate_type(type);
		
		if (dim != null) {
			enter("dim");
			if (dim.getLhs() != null) {
				elaborate_expr(dim.getLhs(), "pss:lhs");
				
				if (dim.getRhs() != null) {
					elaborate_expr(dim.getRhs(), "pss:rhs");
				}
			}
			
			exit("dim");
		}		
	}
	
	
	private void elaborate_component(component_declaration c) {
		println("<pss:component name=\"" + c.getName() + "\">");
	
		// TODO: handle super
		if (c.getSuper() != null) {
//			tag += " super=\"" + c.getSuper().getName() + "\"";
		}
		
		inc_indent();
		
		for (component_body_item it : c.getBody()) {
			if (it instanceof action_declaration) {
				elaborate_action((action_declaration)it);
			} else if (it instanceof struct_declaration) {
				elaborate_struct((struct_declaration)it);
			} else if (it instanceof import_stmt) {
				elaborate_import((import_stmt)it);
			} else if (it instanceof component_field_declaration) {
				elaborate_component_field((component_field_declaration)it);
			} else if (it instanceof object_bind_stmt) {
				elaborate_bind_stmt((object_bind_stmt)it);
			} else {
				println("<unknown_component_item class=\"" + it.getClass() + "\"/>");
			}
		}
		
		dec_indent();
		println("</pss:component>");
	}

	private void elaborate_component_field(component_field_declaration f) {
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<pss:field name=\"" + inst.getName() + "\"";

			if (f.isPool()) {
				tag += " qualifier=\"pool\"";
			} else if (f instanceof struct_field_modifier) {
				struct_field_modifier m = (struct_field_modifier)f;
				if (m.isRand()) {
					tag += " qualifier=\"rand\"";
				}
			}
			
			tag += ">";
			println(tag);
		
			inc_indent();
			elaborate_field_type(inst.getDim(), decl.getType());
			dec_indent();
			
			println("</pss:field>");
		}		
	}
	
	private void elaborate_bind_stmt(object_bind_stmt bind) {
		enter("<pss:bind>");
		
		to_hierarchical_id(bind.getLhs(), "pss:lhs");
		
		enter("<pss:rhs>");
		for (component_path p : bind.getRhs().getItems()) {
			to_hierarchical_id(p.getPath(), "pss:item");
		}
		enter("</pss:rhs>");
		
		exit("</pss:bind>");
	}
	
	private void elaborate_constraint(constraint_declaration c) {
		String tag = "<pss:constraint";
		
		if (c.getName() != null && !c.getName().equals("")) {
			tag += " name=\"" + c.getName() + "\"";
		}
		tag += ">";
		println(tag);
		inc_indent();
		elaborate_constraint_list(c.getBody(), null);

		
		dec_indent();
		println("</pss:constraint>");
	}
	
	private void elaborate_constraint_list(EList<EObject> constraints, String tag) {
		if (tag != null) {
			enter(tag);
		}
		
		for (EObject it : constraints) {
			if (it instanceof expression) {
				elaborate_expr((expression)it, "pss:stmt");
			} else {
				println("<unknown_constraint_item class=\"" + it.getClass() + "\"/>");
			}
		}		
		
		if (tag != null) {
			exit(tag);
		}
	}
	
	private void elaborate_expr(EObject obj) {
		elaborate_expr(obj, null);
	}
	
	private void elaborate_expr(EObject obj, String tag) {
		if (tag != null) {
			enter("<" + tag + ">");
		}
		
		if (obj instanceof expression) {
			elaborate_expr((expression)obj);
		} else if (obj instanceof open_range_list) {
			open_range_list r = (open_range_list)obj;
			
			for (open_range_value v : r.getRanges()) {
				if (v.getLhs() != null && v.getRhs() != null) {
					enter("<pss:range>");
					elaborate_expr(v.getLhs(), "pss:lhs");
					elaborate_expr(v.getRhs(), "pss:rhs");
					exit("</pss:range>");
				} else {
					elaborate_expr(v.getLhs(), "pss:value");
				}
			}
		}
		
		if (tag != null) {
			exit("</" + tag + ">");
		}
	}
	
	private void elaborate_expr(expression expr) {
		elaborate_expr(expr, null);
	}
	
	private void elaborate_expr(expression expr, String t) {
		if (t != null) {
			enter("<" + t + ">");
		}
		if (expr instanceof logical_inequality_expr) {
			logical_inequality_expr li = (logical_inequality_expr)expr;
			
			if (li.getOp().equals("inside")) {
				enter("<pss:inside>");
				elaborate_expr(li.getLeft(), "pss:lhs");
				elaborate_expr(li.getRight(), "pss:rhs");
				exit("</pss:inside>");
			} else {
				String tag = "<pss:binexp op=\"";

				if (li.getOp().equals("=")) {
					tag += "Eq";
				} else if (li.getOp().equals("==")) {
					tag += "EqEq";
				} else if (li.getOp().equals("!=")) {
					tag += "NotEq";
				} else if (li.getOp().equals(">=")) {
					tag += "GE";
				} else if (li.getOp().equals(">")) {
					tag += "GT";
				} else if (li.getOp().equals("<=")) {
					tag += "LE";
				} else if (li.getOp().equals("<")) {
					tag += "LT";
				} else if (li.getOp().equals("&")) {
					tag += "And";
				} else if (li.getOp().equals("&&")) {
					tag += "AndAnd";
				} else if (li.getOp().equals("|")) {
					tag += "Or";
				} else if (li.getOp().equals("||")) {
					tag += "OrOr";
				} else if (li.getOp().equals("-")) {
					tag += "Minus";
				} else if (li.getOp().equals("+")) {
					tag += "Plus";
				} else if (li.getOp().equals("*")) {
					tag += "Mul";
				} else if (li.getOp().equals("/")) {
					tag += "Div";
				} else if (li.getOp().equals("%")) {
					tag += "Mod";
				} else if (li.getOp().equals("[")) {
					tag += "ArrRef";
				} else {
					tag += "unknown-op: " + li.getOp();
				}
				tag += "\"/>";

				println(tag);
				inc_indent();
				
				println("<pss:lhs>");
				inc_indent();
				elaborate_expr(li.getLeft());
				dec_indent();
				println("</pss:lhs>");
				println("<pss:rhs>");
				inc_indent();
				elaborate_expr(li.getRight());
				
				dec_indent();
				println("</pss:rhs>");
				
				dec_indent();
				println("</pss:binexpr>");
			}
		} else if (expr instanceof logical_equality_expr) {
			logical_equality_expr le = (logical_equality_expr)expr;
			String tag = "<pss:binexp op=\"";

			if (le.getOp().equals("==")) {
				tag += "EqEq";
			} else if (le.getOp().equals("!=")) {
				tag += "NotEq";
			} else {
				tag += "Unknown:" + le.getOp();
			}
			tag += "\">";

			println(tag);
			inc_indent();
			
			println("<pss:lhs>");
			inc_indent();
			elaborate_expr(le.getLeft());
			dec_indent();
			println("</pss:lhs>");
			
			println("<pss:rhs>");
			inc_indent();
			elaborate_expr(le.getRight());
			dec_indent();
			println("</pss:rhs>");
			
			dec_indent();
			println("</pss:binexp>");			
		} else if (expr instanceof number) {
			number n = (number)expr;
			EObject v = n.getValue();
			
			if (v instanceof dec_number) {
				println("<pss:number>" + ((dec_number)v).getValue() + "</pss:number>");
			} else if (v instanceof oct_number) {
				println("<pss:number>" + ((oct_number)v).getValue() + "</pss:number>");
			} else if (v instanceof hex_number) {
				println("<pss:number>" + ((hex_number)v).getValue() + "</pss:number>");
			} else {
				println("<unknown_literal class=\"" + v.getClass() + "\"/>");
			}
		} else if (expr instanceof variable_ref) {
			variable_ref v = (variable_ref)expr;
			
			println("<pss:ref>");
			inc_indent();

			to_hierarchical_id(v.getExpr().getPath());
			
			if (v.getLhs() != null) {
				elaborate_expr(v.getLhs(), "pss:lhs");
			}
			if (v.getRhs() != null) {
				elaborate_expr(v.getRhs(), "pss:rhs");
			}
			
			dec_indent();
			println("</pss:ref>");
		} else if (expr instanceof hierarchical_id) {
			to_hierarchical_id((hierarchical_id)expr, "pss:ref");
		} else {
			println("<unknown_expr class=\"" + expr.getClass() + "\"/>");
		}
		
		if (t != null) {
			exit("</" + t + ">");
		}
	}
	
	private void elaborate_enum(enum_declaration enum_t) {
		println("<pss:enum name=\"" + enum_t.getName() + "\">");
		inc_indent();
		
		for (enum_item it : enum_t.getItems()) {
			// TODO specific values
			println("<pss:enumerator name=\"" + it.getName() + "\"/>");
		}
		
		dec_indent();
		println("</pss:enum>");
	}
	
	private void elaborate_graph(graph_declaration graph) {
		enter("pss:graph");
	
		elaborate_graph_production_list(graph.getBody());
		
		exit("pss:graph");
	}
	
	private void elaborate_graph_production_list(EList<graph_production> productions) {
		elaborate_graph_production_list(productions, null);
	}
	
	private void elaborate_graph_production_list(EList<graph_production> productions, String tag) {
		if (tag != null) {
			enter(tag);
		}
		
		for (graph_production p : productions) {
			elaborate_graph_production(p);
		}		
		
		if (tag != null) {
			exit(tag);
		}
	}
	
	private void elaborate_exec_block_stmt(exec_block_stmt exec) {
		enter("<pss:exec>");
		if (exec instanceof exec_block) {
			exec_block b = (exec_block)exec;
			String t = "<pss:block";
			
			t += " kind=\"" + b.getKind() + "\"";
			t += ">";
			enter(t);
			
			
			for (exec_body_stmt s : b.getBody_stmts()) {
				t = "<pss:stmt";
				
				if (s.getOp() != null) {
					t += " op=\"" + to_op(s.getOp()) + "\"";
				}
				
				t += ">";
				enter(t);
				
				elaborate_expr(s.getLhs(), "pss:lhs");
				
				if (s.getRhs() != null) {
					elaborate_expr(s.getRhs(), "pss:rhs");
				}
				exit("</pss:stmt>");
			}
			
			exit("</pss:block>");
		} else if (exec instanceof target_code_exec_block) { 
		} else if (exec instanceof target_file_exec_block) {
		}
		exit("</pss:exec>");
	}
	
	private void elaborate_graph_production(graph_production p) {
		elaborate_graph_production(p, null);
	}
	
	private void elaborate_graph_production(graph_production p, String tag) {
		if (tag != null) {
			enter(tag);
		}
		
		if (p instanceof graph_repeat_stmt) {
			elaborate_graph_repeat((graph_repeat_stmt)p);
		} else if (p instanceof graph_parallel_stmt) {
			elaborate_graph_parallel((graph_parallel_stmt)p);
		} else if (p instanceof graph_sequence_stmt) {
			elaborate_graph_sequence((graph_sequence_stmt)p);
		} else {
			println("<unknown_item class=\"" + p.getClass() + "\"/>");
		}		
		
		if (tag != null) {
			exit(tag);
		}
	}
	
	private void elaborate_graph_parallel(graph_parallel_stmt p) {
		enter("pss:parallel");

		for (graph_labeled_production lp : p.getProductions()) {
			elaborate_graph_production(lp.getProduction(), "pss:production");
		}

		exit("pss:parallel");
	}
	
	private void elaborate_graph_repeat(graph_repeat_stmt r) {
		String tag = "pss:repeat type=\"";
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
		
		enter(tag);
		
		if (expr != null) {
			elaborate_expr(expr, "pss:expr");
		}

		elaborate_graph_production_list(r.getBody().getStmt_list(), "pss:body");
		
		exit("pss:repeat");
	}
	
	private void elaborate_graph_sequence(graph_sequence_stmt s) {
		if (s.getItems().size() > 0) {
			enter("pss:sequential");
			for (String it : s.getItems()) {
				println("<pss:traverse name=\"" + it + "\"/>");
			}
			exit("pss:sequential");
		} else {
			if (s.getType() != null) {
				println("<unknown_item type=\"inline action declaration\"/>");
			} else {
				String tag = "<pss:traverse name=\"" + s.getItem() + "\"";
				
				if (s.getInline_with() == null) {
					tag += "/>";
					println(tag);
				} else {
					tag += ">";
					println(tag);
					inc_indent();
					elaborate_constraint_list(s.getInline_with().getConstraints(), "pss:with");
					dec_indent();
					println("</traverse>");
				}
			}
		}
	}
	
	private void elaborate_import(import_stmt imp) {
		String tag = "pss:import_stmt";
		package_import_pattern p = imp.getPkg();
		
		if (p.isWildcard()) {
			tag += " wildcard=\"true\"";
		}
		
		println("<" + tag + ">");
		inc_indent();
		
		tid2hierarchical_id(p.getPackage());
		
		dec_indent();
		println("</pss:import_stmt>");
	}
	
	private void elaborate_import_method(import_method_decl imp) {
		elaborate_method_prototype(imp.getPrototype(), "pss:import_method");
	}
	
	private void elaborate_method_prototype(method_prototype p, String tag) {
		enter("<" + tag + " name=\"" + p.getName() + "\">");
		
		if (p.getRet_type().getType() != null) {
			elaborate_type(p.getRet_type().getType(), "pss:return");
		}
	
		enter("<pss:parameters>");
		for (method_parameter param : p.getParameter_list().getParameters()) {
			elaborate_method_parameter(param, "pss:parameter");
		}
		exit("</pss:parameters>");
		
		exit("</" + tag + ">");
	}
	
	private void elaborate_method_parameter(method_parameter p, String tag) {
		enter("<" + tag + " name=\"" + p.getName() + "\">");
		elaborate_type(p.getType(), null);
		exit("</" + tag + ">");
	}
	
	private void elaborate_package(package_declaration p) {
		String tag = "<pss:package name=\"" + hid2string(p.getName()) + "\">";
		println(tag);
		inc_indent();
		
		for (package_body_item it : p.getBody()) {
			if (it instanceof struct_declaration) {
				elaborate_struct((struct_declaration)it);
			} else if (it instanceof enum_declaration) {
				elaborate_enum((enum_declaration)it);
			} else if (it instanceof import_stmt) {
				elaborate_import((import_stmt)it);
			} else if (it instanceof import_method_decl) {
				elaborate_import_method((import_method_decl)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
	
		dec_indent();
		println("</pss:package>");
	}
	
	private void elaborate_struct(struct_declaration s) {
		String tag = "<pss:struct name=\"" + s.getName() + "\"";
		
		if (s.getSuper() != null) {
			tag += "super=\"" + s.getSuper().getName() + "\"";
		}
		
		if (s.getQualifier() != null) {
			tag += " qualifier=\"" + s.getQualifier().getType() + "\"";
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
		
		println("</pss:struct>");
	}
	
	private void elaborate_struct_field(struct_field_declaration f) {
		
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<pss:field name=\"" + inst.getName() + "\"";
		
			if (f instanceof struct_field_modifier) {
				struct_field_modifier m = (struct_field_modifier)f;
				if (m.isRand()) {
					tag += " qualifier=\"rand\"";
				}
			}
			
			tag += ">";
			println(tag);
		
			inc_indent();
			elaborate_field_type(inst.getDim(), decl.getType());
			dec_indent();
			
			println("</pss:field>");
		}
	}

	/**
	 * Elaborate a data type
	 * @param type
	 */
	private void elaborate_type(EObject type) {
		elaborate_type(type, "pss:type");
		
	}
	private void elaborate_type(EObject type, String t) {
		if (t != null) {
			enter("<" + t + ">");
		}
		
		if (type instanceof integer_type) {
			integer_type it = (integer_type)type;
			String tag;
			if (it.getTypename().equals("int")) {
				tag = "pss:int";
			} else {
				tag = "pss:bit";
			}
			
			println("<" + tag + ">");
			inc_indent();
			
			println("<pss:msb>");
			inc_indent();
			elaborate_expr(it.getLhs());
			dec_indent();
			println("</pss:msb>");
			
			println("<pss:lsb>");
			inc_indent();
			elaborate_expr(it.getRhs());
			dec_indent();
			println("</pss:lsb>");
			
			dec_indent();
			println("</" + tag + ">");
		} else if (type instanceof user_defined_type) {
			user_defined_type udt = (user_defined_type)type;
			tid2hierarchical_id(udt.getTypename(), "pss:user");
		} else {
			println("<unknown_item class=\"" + type.getClass()+ "\"/>");
		}

		if (t != null) {
			exit("</" + t + ">");
		}
	}
	
	private void tid2hierarchical_id(type_identifier tid, String tag) {
		to_hierarchical_id(tid.getElems(), tag);
	}
	
	private void tid2hierarchical_id(type_identifier id) {
		tid2hierarchical_id(id, null);
	}
	
	private void to_hierarchical_id(hierarchical_id id) {
		to_hierarchical_id(id.getPath());
	}
	
	private void to_hierarchical_id(hierarchical_id id, String tag) {
		to_hierarchical_id(id.getPath(), tag);
	}
	
	private void to_hierarchical_id(EList<String> path) {
		to_hierarchical_id(path, null);
	}
	
	private void to_hierarchical_id(EList<String> path, String tag) {
		if (tag != null) {
			println("<" + tag + ">");
			inc_indent();
		}
		
		for (String p : path) {
			println("<pss:path>" + p + "</pss:path>");
		}
	
		if (tag != null) {
			dec_indent();
			println("</" + tag + ">");
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

	private static String to_op(String op) {
		if (op.equals("=")) { return "Eq";
		} else if (op.equals("+=")) { return "PlusEq"; 
		} else if (op.equals("-=")) { return "MinusEq"; 
		} else if (op.equals("<<=")) { return "LShiftEq"; 
		} else if (op.equals(">>=")) { return "RShiftEq"; 
		} else if (op.equals("|="))  { return "OrEq"; 
		} else if (op.equals("&="))  { return "OrEq"; 
		} else {
			return "UnknownOp: " + op;
		}
	}
	
	private void println(String m) {
		fPS.println(fInd + m);
	}
	
	private void enter(String t) {
		if (t.charAt(0) == '<') {
			println(t);
		} else {
			if (!t.startsWith("pss:")) {
				t = "pss:" + t;
			}
			println("<" + t + ">");
		}
		inc_indent();
	}
	
	private void exit(String t) {
		dec_indent();
		
		if (t.charAt(0) == '<') {
			println(t);
		} else {
			if (!t.startsWith("pss:")) {
				t = "pss:" + t;
			}
			println("</" + t + ">");
		}
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