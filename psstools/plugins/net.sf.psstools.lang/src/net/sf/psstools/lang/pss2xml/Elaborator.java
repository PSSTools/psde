package net.sf.psstools.lang.pss2xml;

import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.action_body_item;
import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.action_field_declaration;
import net.sf.psstools.lang.pSS.action_super_spec;
import net.sf.psstools.lang.pSS.activity_action_traversal_stmt;
import net.sf.psstools.lang.pSS.activity_declaration;
import net.sf.psstools.lang.pSS.activity_labeled_stmt;
import net.sf.psstools.lang.pSS.activity_parallel_stmt;
import net.sf.psstools.lang.pSS.activity_repeat_stmt;
import net.sf.psstools.lang.pSS.activity_stmt;
import net.sf.psstools.lang.pSS.array_dim;
import net.sf.psstools.lang.pSS.bool_type;
import net.sf.psstools.lang.pSS.chandle_type;
import net.sf.psstools.lang.pSS.component_body_item;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.component_field_declaration;
import net.sf.psstools.lang.pSS.component_path;
import net.sf.psstools.lang.pSS.component_pool_declaration;
import net.sf.psstools.lang.pSS.constraint_declaration;
import net.sf.psstools.lang.pSS.data_declaration;
import net.sf.psstools.lang.pSS.data_instantiation;
import net.sf.psstools.lang.pSS.dec_number;
import net.sf.psstools.lang.pSS.enum_declaration;
import net.sf.psstools.lang.pSS.enum_item;
import net.sf.psstools.lang.pSS.exec_block;
import net.sf.psstools.lang.pSS.exec_block_stmt;
import net.sf.psstools.lang.pSS.exec_body_stmt;
import net.sf.psstools.lang.pSS.expression;
import net.sf.psstools.lang.pSS.expression_constraint_item;
import net.sf.psstools.lang.pSS.function_call;
import net.sf.psstools.lang.pSS.function_decl;
import net.sf.psstools.lang.pSS.function_id;
import net.sf.psstools.lang.pSS.hex_number;
import net.sf.psstools.lang.pSS.hierarchical_id;
import net.sf.psstools.lang.pSS.import_stmt;
import net.sf.psstools.lang.pSS.integer_type;
import net.sf.psstools.lang.pSS.logical_and_expr;
import net.sf.psstools.lang.pSS.logical_equality_expr;
import net.sf.psstools.lang.pSS.logical_inequality_expr;
import net.sf.psstools.lang.pSS.method_call;
import net.sf.psstools.lang.pSS.method_function_call;
import net.sf.psstools.lang.pSS.method_hierarchical_id;
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
import net.sf.psstools.lang.pSS.string_type;
import net.sf.psstools.lang.pSS.struct_body_item;
import net.sf.psstools.lang.pSS.struct_field_declaration;
import net.sf.psstools.lang.pSS.struct_field_modifier;
import net.sf.psstools.lang.pSS.struct_type;
import net.sf.psstools.lang.pSS.target_code_exec_block;
import net.sf.psstools.lang.pSS.target_file_exec_block;
import net.sf.psstools.lang.pSS.type_identifier;
import net.sf.psstools.lang.pSS.user_defined_datatype;
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
			} else if (o instanceof struct_type) {
				elaborate_struct((struct_type)o);
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
		enter("action name=\"" + a.getName() + "\"");
		
		if (a.getSuper_spec() != null) {
			action_super_spec super_s = a.getSuper_spec();
			enter("super");
			tid2path(super_s.getSuper());
			exit("super");
		}
		
		for (action_body_item it : a.getBody()) {
			if (it instanceof constraint_declaration) {
				elaborate_constraint((constraint_declaration)it);
			} else if (it instanceof action_field_declaration) {
				elaborate_action_field((action_field_declaration)it);
			} else if (it instanceof activity_declaration) {
				elaborate_activity((activity_declaration)it);
			} else if (it instanceof exec_block_stmt) {
				elaborate_exec_block_stmt((exec_block_stmt)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
	
		exit("action");
	}
	
	private void elaborate_action_field(action_field_declaration f) {
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<pss:field name=\"" + inst.getName() + "\"";
	
			if (f.getModifiers() != null) {
				if (f.getModifiers().isRand()) {
					tag += " qualifier=\"rand\"";
				} else if (f.getModifiers().isAction()) {
					tag += " qualifier=\"action\"";
				} else if (f.getModifiers().isLock()) {
					tag += " qualifier=\"lock\"";
				} else if (f.getModifiers().isShared()) {
					tag += " qualifier=\"share\"";
				} else {
					System.out.println("Error: unknown modifier");
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
			elaborate_expr(dim.getDim(), "dim");
		}		
	}
	
	
	private void elaborate_component(component_declaration c) {
		enter("component name=\"" + c.getName() + "\"");

		// TODO: need to check what happens when the 'super' doesn't exist
		if (c.getSuper() != null) {
			enter("super");
			tid2path(c.getSuper().getSuper());
			exit("super");
		}
		
		for (component_body_item it : c.getBody()) {
			if (it instanceof action_declaration) {
				elaborate_action((action_declaration)it);
			} else if (it instanceof struct_type) {
				elaborate_struct((struct_type)it);
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
		
		exit("component");
	}

	private void elaborate_component_field(component_field_declaration f) {
		data_declaration decl = f.getDeclaration();
		
		for (data_instantiation inst : decl.getInstances()) {
			String tag = "<pss:field name=\"" + inst.getName() + "\"";
		
			if (f instanceof component_pool_declaration) {
				// TODO: pool size
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
			} else if (it instanceof expression_constraint_item) {
				expression_constraint_item e = (expression_constraint_item)it;
				if (e.getImpl_constraint() != null) {
					System.out.println("Error: handle implication constraint");
				} else {
					elaborate_expr(e.getExpr(), "pss:stmt");
				}
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
			enter(tag);
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
			exit(tag);
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
				String tag = "binexp op=\"";

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
				tag += "\"";

				enter(tag);
				
				elaborate_expr(li.getLeft(), "pss:lhs");
				elaborate_expr(li.getRight(), "pss:rhs");
				
				exit("binexp");
			}
		} else if (expr instanceof logical_and_expr) {
			logical_and_expr e = (logical_and_expr)expr;
			
			enter("binexp op=\"AndAnd\"");
			elaborate_expr(e.getLeft(), "pss:lhs");
			elaborate_expr(e.getRight(), "pss:rhs");
			exit("binexp");
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

//TODO:			to_hierarchical_id(v.getExpr().getPath());
			
//			if (v.getLhs() != null) {
//				elaborate_expr(v.getLhs(), "pss:lhs");
//			}
//			if (v.getRhs() != null) {
//				elaborate_expr(v.getRhs(), "pss:rhs");
//			}
			
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
	
	private void elaborate_activity(activity_declaration a) {
		enter("pss:activity");
	
		elaborate_activity_stmt_list(a.getBody());
		
		exit("pss:activity");
	}
	
	private void elaborate_activity_stmt_list(EList<activity_stmt> productions) {
		elaborate_activity_stmt_list(productions, null);
	}
	
	private void elaborate_activity_stmt_list(EList<activity_stmt> productions, String tag) {
		if (tag != null) {
			enter(tag);
		}
		
		for (activity_stmt p : productions) {
			elaborate_activity_stmt(p);
		}		
		
		if (tag != null) {
			exit(tag);
		}
	}
	
	private void elaborate_exec_block_stmt(exec_block_stmt exec) {
		enter("exec");
		if (exec instanceof exec_block) {
			exec_block b = (exec_block)exec;
			enter("block kind=\"" + b.getKind() + "\"");
			
			for (exec_body_stmt s : b.getBody_stmts()) {
				
				if (s.getOp() == null) {
					// Method call
					elaborate_method_function_call((method_function_call)s.getLhs(), "pss:call");
				} else {
					// Assignment
					String t = "assign op=\"" + to_op(s.getOp()) + "\"";
					enter(t);
					elaborate_expr(s.getLhs(), "pss:lhs");
					elaborate_expr(s.getRhs(), "pss:rhs");
					exit(t);
				}
			}
			
			exit("block");
		} else if (exec instanceof target_code_exec_block) {
			target_code_exec_block b = (target_code_exec_block)exec;
			String kind_s = b.getKind();
			String lang_s = b.getLanguage();
			String body_s = b.getBody();
			
			if (body_s.startsWith("\"\"\"")) {
				body_s = body_s.substring(3, body_s.length()-3);
			} else if (body_s.startsWith("\"")) {
				body_s = body_s.substring(1, body_s.length()-1);
			}
			
			String tag = "code_block kind=\"" + kind_s + "\" language=\"" + lang_s + "\"";
			enter(tag);
			println(body_s);
			exit("code_block");
		} else if (exec instanceof target_file_exec_block) {
		}
		exit("</pss:exec>");
	}
	
	private void elaborate_activity_stmt(activity_stmt p) {
		elaborate_activity_stmt(p, null);
	}
	
	private void elaborate_activity_stmt(activity_stmt p, String tag) {
		if (tag != null) {
			enter(tag);
		}
		
		if (p instanceof activity_repeat_stmt) {
			elaborate_activity_repeat((activity_repeat_stmt)p);
		} else if (p instanceof activity_parallel_stmt) {
			elaborate_activity_parallel((activity_parallel_stmt)p);
		} else if (p instanceof activity_action_traversal_stmt) {
			elaborate_activity_action_traversal_stmt((activity_action_traversal_stmt)p);
		} else {
			println("<unknown_item class=\"" + p.getClass() + "\"/>");
		}		
		
		if (tag != null) {
			exit(tag);
		}
	}
	
	private void elaborate_activity_parallel(activity_parallel_stmt p) {
		enter("pss:parallel");

		for (activity_labeled_stmt lp : p.getProductions()) {
			elaborate_activity_stmt(lp.getProduction(), "pss:production");
		}

		exit("pss:parallel");
	}
	
	private void elaborate_activity_repeat(activity_repeat_stmt r) {
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

		elaborate_activity_stmt_list(r.getBody().getStmt_list(), "pss:body");
		
		exit("pss:repeat");
	}
	
	private void elaborate_activity_action_traversal_stmt(activity_action_traversal_stmt s) {
//			enter("pss:sequential");
//			for (String it : s.getItems()) {
//				println("<pss:traverse name=\"" + it + "\"/>");
//			}
//			exit("pss:sequential");
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
	
	private void elaborate_import_method(function_decl imp) {
		elaborate_method_prototype(imp.getPrototype(), "pss:import_function");
	}
	
	private void elaborate_method_function_call(method_function_call c, String tag) {
		enter(tag);
		if (c instanceof function_call) {
			function_call fc = (function_call)c;
			to_hierarchical_id(fc.getFunction(), "pss:function");
			if (fc.getParameters() != null) {
				enter("parameters");
				for (expression p : fc.getParameters().getParameters()) {
					elaborate_expr(p, "pss:parameter");
				}
				exit("parameters");
			}
		} else if (c instanceof method_call) {
			method_call mc = (method_call)c;
			to_hierarchical_id(mc.getMethod(), "pss:function");
			if (mc.getParameters() != null) {
				enter("parameters");
				for (expression p : mc.getParameters().getParameters()) {
					elaborate_expr(p, "pss:parameter");
				}
				exit("parameters");
			}
		} else {
			println("<unknown-element class=\"" + c.getClass()+ "\"/>");
		}
		
		exit(tag);
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
		elaborate_type(p.getType(), "pss:type");
		exit("</" + tag + ">");
	}
	
	private void elaborate_package(package_declaration p) {
		String tag = "<pss:package name=\"" + hid2string(p.getName()) + "\">";
		println(tag);
		inc_indent();
		
		for (package_body_item it : p.getBody()) {
			if (it instanceof struct_type) {
				elaborate_struct((struct_type)it);
			} else if (it instanceof enum_declaration) {
				elaborate_enum((enum_declaration)it);
			} else if (it instanceof import_stmt) {
				elaborate_import((import_stmt)it);
			} else if (it instanceof function_decl) {
				elaborate_import_method((function_decl)it);
			} else {
				println("<unknown_item class=\"" + it.getClass() + "\"/>");
			}
		}
	
		dec_indent();
		println("</pss:package>");
	}
	
	private void elaborate_struct(struct_type s) {
		String tag = "struct name=\"" + s.getName() + "\"";
		
		if (s.getType() != null && !s.getType().equals("struct")) {
			tag += " qualifier=\"" + s.getType() + "\"";
		}
		
		enter(tag);
		
		if (s.getSuper() != null) {
			enter("super");
			tid2path(s.getSuper().getSuper());
			exit("super");
		}
		
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
			
			enter(tag);
		
			if (it.getLhs() != null) {
				enter("msb");
				elaborate_expr(it.getLhs());
				exit("msb");
				
				if (it.getRhs() != null) {
					enter("lsb");
					elaborate_expr(it.getRhs());
					exit("lsb");
				}
			}
		
			exit(tag);
		} else if (type instanceof bool_type) {
			println("<pss:bool/>");
		} else if (type instanceof string_type) {
			println("<pss:string/>");
		} else if (type instanceof chandle_type) {
			println("<pss:chandle/>");
		} else if (type instanceof user_defined_datatype) {
			user_defined_datatype udt = (user_defined_datatype)type;
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
	
	private void to_hierarchical_id(function_id id, String tag) {
		to_hierarchical_id((hierarchical_id)id, tag);
	}
	
	private void to_hierarchical_id(String id, String tag) {
		if (tag != null) {
			println("<" + tag + ">");
			inc_indent();
		}
		
		println("<pss:path>" + id + "</pss:path>");
	
		if (tag != null) {
			dec_indent();
			println("</" + tag + ">");
		}		
	}	
	
//	private void to_hierarchical_id(hierarchical_type_identifier id, String tag) {
//		type_identifier ti_id = (type_identifier)id;
//		to_hierarchical_id(ti_id.getElems(), tag);
//	}

	private void to_hierarchical_id(method_hierarchical_id id, String tag) {
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
	
	private void tid2path(type_identifier id) {
		for (String elem : id.getElems()) {
			println("<pss:path>" + elem + "</pss:path>");
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
