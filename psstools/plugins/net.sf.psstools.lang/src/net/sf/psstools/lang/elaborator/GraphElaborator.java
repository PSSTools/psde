package net.sf.psstools.lang.elaborator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import net.sf.psstools.lang.elaborator.processor.GraphProcDirective;
import net.sf.psstools.lang.elaborator.processor.GraphProcDirectiveDumper;
import net.sf.psstools.lang.elaborator.processor.GraphProcDirectiveFactory;
import net.sf.psstools.lang.elaborator.processor.GraphProcessingStrategy;
import net.sf.psstools.lang.elaborator.rules.RuleBlockProduction;
import net.sf.psstools.lang.elaborator.rules.RuleProduction;
import net.sf.psstools.lang.elaborator.rules.RuleRepeatProduction;
import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.data_declaration;
import net.sf.psstools.lang.pSS.data_instantiation;
import net.sf.psstools.lang.pSS.data_type;
import net.sf.psstools.lang.pSS.graph_block_stmt;
import net.sf.psstools.lang.pSS.graph_production;
import net.sf.psstools.lang.pSS.graph_repeat_stmt;
import net.sf.psstools.lang.pSS.integer_type;
import net.sf.psstools.lang.pSS.portable_stimulus_description;
import net.sf.psstools.lang.pSS.struct_body_item;
import net.sf.psstools.lang.pSS.struct_declaration;
import net.sf.psstools.lang.pSS.struct_field_declaration;
import net.sf.psstools.lang.pSS.type_identifier;
import net.sf.psstools.lang.pSS.user_defined_type;

public class GraphElaborator {
	
	enum ElabOptions {
		BUILD_PROCESSING_STRATEGY
	}

	private List<Model>							fModelList;
	private GraphElabResult						fElabResult;
	private Stack<IDeclScope>					fScopeStack;
	private Map<ElabOptions, Object>			fElabOptions;
	private GraphExpressionElaborator			fExprElaborator;
	private DeclScope							fGlobalScope;
	private Stack<GraphElaboratorDeclContext>	fContext;
	
	public GraphElaborator() {
		fGlobalScope = new DeclScope();
		fModelList = new ArrayList<Model>();
		fScopeStack = new Stack<IDeclScope>();
		fElabOptions = new HashMap<GraphElaborator.ElabOptions, Object>();
		
		// Set default options
		setElabOption(ElabOptions.BUILD_PROCESSING_STRATEGY, true);
		fExprElaborator = new GraphExpressionElaborator();
		
	}
	
	public GraphElaborator(TreeIterator<Notifier> contents) {
		this();
	
		while (contents.hasNext()) {
			Notifier n = contents.next();
			if (n instanceof Model) {
				addModel((Model)n);
			}
		}
	}
	
	public void setElabOption(ElabOptions option, boolean value) {
		fElabOptions.remove(option);
		fElabOptions.put(option, Boolean.valueOf(value));
	}
	
	private boolean getBoolElabOption(ElabOptions option) {
		if (!fElabOptions.containsKey(option)) {
			return false;
		} else {
			Object obj = fElabOptions.get(option);
			if (obj instanceof Boolean) {
				return ((Boolean)obj).booleanValue();
			} else {
				return false;
			}
		}
	}
	
	public void addModel(Model m) {
		fModelList.add(m);
	}
	
	public GraphElabResult elaborate(String name) throws ElabException {
		GraphElabResult result = new GraphElabResult();
//		graph_or_struct_declaration gs = find_graph_or_struct(null /*name*/);
		
		// Push global scope
		fScopeStack.clear();
		fScopeStack.push(fGlobalScope);
		
//		if (!(gs instanceof graph_declaration)) {
//			error("Not instanceof graph");
//		}
//	
//		graph_declaration g = (graph_declaration)gs;
//		DataTypeGraph graph = elaborate_graph(g);
//		result.setGraph(graph);
		
//		if (getBoolElabOption(ElabOptions.BUILD_PROCESSING_STRATEGY)) {
//			GraphProcessingStrategy strategy = computeGraphProcessingStrategy(graph);
//			result.setProcessingStrategy(strategy);
//		}
		
		return result;
	}
	
//	private DataTypeGraph elaborate_graph(graph_declaration graph) throws ElabException {
//		int push_num=0;
////		GraphInstance inst = new GraphInstance(graph.getName());
//		DataTypeGraph ret = new DataTypeGraph(graph.getName());
//		symbol_definition root_symbol = null;
//		
//		// First build out the super structure
//		if (graph.getSuper() != null) {
//			
//		}
//	
//		DataType s = ret.getSuper();
//		while (s != null && s instanceof IDeclScope) {
//			push_num++;
//			fScopeStack.push((IDeclScope)s);
//			s = ret.getSuper();
//		}
//		
//		fScopeStack.push(ret);
//		push_num++;
//		
//		// Build the ports and identify interface types that will need to be built
////		for (port_declaration p : graph.getPorts()) {
////			InterfaceDeclaration ifc_decl;
////			if ((ifc_decl = ret.findInterfaceDecl(p.getIfc_type().getName())) == null) {
////				ifc_decl = elaborate_interface_decl(p.getIfc_type());
////				inst.addInterfaceDecl(ifc_decl);
////			}
////			
////			GraphInterface ifc = new GraphInterface(
////					p.getName(),
////					ifc_decl,
////					p.isExport());
////			inst.addInterface(ifc);
////		}
//		
//		// TODO: Find all structs and ensure they're registered with the ElabResult
//		find_struct_types(ret);
//	
//		// Process the body items
//		for (graph_body_item it : graph.getBody()) {
//			if (it instanceof struct_field_declaration) {
//				elaborate_field_declaration((struct_field_declaration)it);
//			} else if (it instanceof symbol_definition) {
//				symbol_definition def = (symbol_definition)it;
//				System.out.println("Symbol-def: field=" + def.getSymbol_name());
//				if (def.getSymbol_name().equals(graph.getName())) {
//					// Root symbol
//					root_symbol = def;
//				} else {
//					// Build out with resolution 
//					RuleProduction rule = buildRuleProduction(def, true);
//					DataField field = findVariable(def.getSymbol_name());
//					((DataTypeSymbol)field.getType()).setProduction(rule);
//					System.out.println("Symbol-def: field=" + field);
//				}
//			} else if (it instanceof symbol_declaration) {
//				symbol_declaration decl = (symbol_declaration)it;
//
//				
//				for (int i=0; i<decl.getDecl_list().size(); i++) {
//					graph_production def = (i==0)?decl.getInline_rule():null;
//					symbol_decl_item s_it = decl.getDecl_list().get(i);
//					
//					DataTypeSymbol dt = new DataTypeSymbol();
//					
//					if (def != null) {
//						dt.setProduction(buildRuleProduction(def, true));
//					}
//				
//					DataField field = new DataField(dt, s_it.getName());
//					ret.addVariable(field);
//				}
//			}
//		}
//		
//		if (root_symbol != null) {
//			debug("Elaborating root symbol");
//			RuleProduction root = buildRuleProduction(root_symbol.getProduction(), true);
//			ret.setRoot(root);
//		} else {
//			error("Root symbol not specified for graph " + graph.getName());
//		}
//
//		// Pop what we pushed
//		for (int i=0; i<push_num; i++) {
//			fScopeStack.pop();
//		}
//		
//		return ret;
//	}
	
	private void elaborate_field_declaration(struct_field_declaration fd) throws ElabException {
		data_declaration d = fd.getDeclaration();

		DataType dt = elaborate_data_type(d.getType());

		for (data_instantiation di : d.getInstances()) {
			System.out.println("addField: " + di.getName());
			DataField field = new DataField(dt, di.getName());
//			field.setIsRand(fd.isRand());
			addVariable(field);
		}

	}
	
	private void find_struct_types(DataTypeGraph inst) throws ElabException {
		for (Model m : fModelList) {
			for (portable_stimulus_description d : m.getRoot()) {
				if (d instanceof struct_declaration) {
					struct_declaration s = (struct_declaration)d;
//					if (fGlobalScope.findType(s.getName()) != null) {
//						continue;
//					}
					
					DataTypeStruct dt = new DataTypeStruct(s.getName());
					fScopeStack.push(dt);
					
					for (struct_body_item it : s.getBody()) {
						if (it instanceof struct_field_declaration) {
							elaborate_field_declaration((struct_field_declaration)it);
						}
					}
					
					fScopeStack.pop();
				
					fGlobalScope.addType(dt);
				}
			}
		}
	}
	
	private DataTypeStruct elaborate_struct_type(struct_declaration decl) {
		DataTypeStruct dts = new DataTypeStruct(decl.getName());
		
		for (struct_body_item it : decl.getBody()) {
			if (it instanceof data_declaration) {
				
			}
			
		}
		
		return dts;
	}
	
	private DataType elaborate_data_type(EObject dt) throws ElabException {
		DataType type = null;
		
		if (dt instanceof user_defined_type) {
			user_defined_type udt = (user_defined_type)dt;
			debug("user-defined type: " + udt.getTypename());
			
			if ((type = findType(udt.getTypename())) == null) {
				// Go searching
				Object decl = null; // graph_or_struct_declaration decl = find_graph_or_struct(udt.getTypename());
				
				if (decl == null) {
					error("Struct or graph \"" + udt.getTypename() + "\" undeclared");
				}
				
//				if (decl instanceof graph_declaration) {
//					// TODO: elab
//				} else if (decl instanceof struct_declaration) {
//					DataTypeStruct dts = elaborate_struct_type((struct_declaration)decl);
//				}
				
			}
			
		} else if (dt instanceof integer_type) {
			integer_type int_type = (integer_type)dt;
			
			DataTypeScalar stype = new DataTypeScalar();

			// TODO: need to establish bit-width
//			if (int_type.getAtom_type().equals("bit")) {
//				stype.setScalarType(DataTypeScalar.ScalarType.Bit);
//				stype.setIsSigned(!int_type.isSigned());
//			} else if (int_type.getAtom_type().equals("int")) {
//				stype.setScalarType(DataTypeScalar.ScalarType.Int);
//				stype.setIsSigned(!int_type.isUnsigned());
//			} else {
//				error("unknown integer atom type: " + int_type.getAtom_type());
//			}
		
			type = stype;
		}
		
		return type;
	}
	
//	private GraphConstraint buildConstraint()
	
	private RuleProduction buildRuleProduction(Object production, boolean resolve) throws ElabException {
		RuleProduction ret = null;
//		debug("graph_production: " + production);
		if (production instanceof graph_repeat_stmt) {
			graph_repeat_stmt rstmt = (graph_repeat_stmt)production;
			
			RuleRepeatProduction repeat = new RuleRepeatProduction(
					buildRuleProduction(rstmt.getBody(), resolve));
			ret = repeat;
		} else if (production instanceof graph_block_stmt) {
			graph_block_stmt block_stmt = (graph_block_stmt)production;
			RuleBlockProduction block = new RuleBlockProduction();
			
			for (graph_production rp : block_stmt.getStmt_list()) {
				block.addChild(buildRuleProduction(rp, resolve));
			}
			ret = block;
//		} else if (production instanceof graph_select_parallel_block) {
//			graph_select_parallel_block block_stmt = (graph_select_parallel_block)production;
//			RuleBlockProduction block = new RuleBlockProduction();
//		
//			for (graph_select_parallel_schedule_production p : block_stmt.getProductions()) {
//				block.addChild(buildRuleProduction(p, resolve));
//			}
//			ret = block;
//		} else if (production instanceof graph_select_stmt) {
//			graph_select_stmt select = (graph_select_stmt)production;
//			RuleStmtProduction stmt_ret = new RuleStmtProduction(RuleStmtType.Alternative);
//			for (graph_select_production p : select.getProductions()) {
//				stmt_ret.addChild(buildRuleProduction(p.getProduction(), resolve));
//			}
//			ret = stmt_ret;
//		} else if (production instanceof graph_parallel_production) {
//			graph_parallel_stmt fork = (graph_parallel_stmt)production;
//			RuleStmtProduction stmt_ret = new RuleStmtProduction(RuleStmtType.Parallel);
//			for (graph_parallel_production p : fork.getProductions()) {
//				stmt_ret.addChild(buildRuleProduction(p.getProduction(), resolve));
//			}
//			ret = stmt_ret;
		} else {
			debug("unknown graph_production: " + production);
		}
		
		if (ret == null) {
			error("null return for " + production);
		}
		return ret;
	}
	
	private DataField findVariable(String name) throws ElabException {
		// Implied context
		DataField f = null;
		for (int i=fScopeStack.size()-1; i>=0; i--) {
			IDeclScope scope = fScopeStack.get(i);
		
			if ((f = scope.findVariable(name)) != null) {
				break;
			}
		}
		
		return f;
	}
	
	private DataType findType(type_identifier name) throws ElabException {
		DataType t = null;
		
		for (int i=fScopeStack.size()-1; i>=0; i--) {
			IDeclScope scope = fScopeStack.get(i);
			
			if ((t = scope.findType(name)) != null) {
				break;
			}
		}
		
		return t;
	}
	
	private DataType findType(String name, DataType.Type type) throws ElabException {
		DataType t = null;
		
		if (t == null) {
			
		}
		
		return t;
	}
	
	private void addType(DataType type) {
		fScopeStack.peek().addType(type);
	}
	
	private void addVariable(DataField field) {
		fScopeStack.peek().addVariable(field);
	}

	private GraphProcessingStrategy computeGraphProcessingStrategy(DataTypeGraph instance) throws ElabException {
		GraphProcessingStrategy strategy = new GraphProcessingStrategy();
		
		RuleProduction production = instance.getRoot();

		GraphProcDirectiveFactory factory = new GraphProcDirectiveFactory();
		GraphProcDirective root = factory.build(production);
		
		strategy.setProcessingDirectives(root);
		
		GraphProcDirectiveDumper.dump(System.out, root);
	
		return strategy;
	}
	
//	private void addIntegerFields()
	
	private data_type resolve_user_defined_type(user_defined_type type) {
		return null;
	}
	
//	private graph_or_struct_declaration find_graph_or_struct(type_identifier name) {
//		for (Model m : fModelList) {
//			for (portable_stimulus_description d : m.getRoot()) {
//				if (d instanceof graph_declaration) {
//					if (((graph_declaration)d).getName().equals(name)) {
//						return (graph_declaration)d;
//					}
//				} else if (d instanceof struct_declaration) {
//					if (((struct_declaration)d).getName().equals(name)) {
//						return (struct_declaration)d;
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	private void error(String msg) throws ElabException {
		throw new ElabException(msg);
	}
	
	private void debug(String msg) {
		System.out.println("[GraphElaborator] " + msg);
	}

}
