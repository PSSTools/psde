package net.sf.psstools.lang.model_builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import com.ibm.icu.text.Normalizer.Mode;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.bins_declaration;
import net.sf.psstools.lang.pSS.component_body_item;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.component_field_declaration;
import net.sf.psstools.lang.pSS.coverspec_declaration;
import net.sf.psstools.lang.pSS.enum_declaration;
import net.sf.psstools.lang.pSS.export_action;
import net.sf.psstools.lang.pSS.extend_stmt;
import net.sf.psstools.lang.pSS.import_class_decl;
import net.sf.psstools.lang.pSS.import_method_decl;
import net.sf.psstools.lang.pSS.import_stmt;
import net.sf.psstools.lang.pSS.inline_type_object_declaration;
import net.sf.psstools.lang.pSS.object_bind_stmt;
import net.sf.psstools.lang.pSS.overrides_declaration;
import net.sf.psstools.lang.pSS.package_body_item;
import net.sf.psstools.lang.pSS.package_declaration;
import net.sf.psstools.lang.pSS.struct_declaration;
import net.sf.psstools.lang.pSS.typedef_declaration;
import net.sf.psstools.lang.psi.IAction;
import net.sf.psstools.lang.psi.IComponent;
import net.sf.psstools.lang.psi.IContext;
import net.sf.psstools.lang.psi.IPackage;

public class PSSModelBuilder {
	private List<Model>						fModels;
	private IContext						fContext;
	
	public PSSModelBuilder(TreeIterator<Notifier> contents) {
		fModels = new ArrayList<Model>();
		
		while (contents.hasNext()) {
			Notifier n = contents.next();
			
			if (n instanceof Model) {
				fModels.add((Model)n);
			}
		}
	}
	
	public void build(IContext context) {
		fContext = context;
		
		for (Model m : fModels) {
			for (EObject o : m.eContents()) {
				
				if (o instanceof component_declaration) {
					IComponent comp = component_decl((component_declaration)o);
					fContext.add_component(comp);
				} else if (o instanceof package_declaration) {
					IPackage pkg = package_decl((package_declaration)o);
					fContext.add_package(pkg);
				}
			}
		}
	}
	
	protected IComponent component_decl(component_declaration c) {
		IComponent super_c = null;
		
		if (c.getSuper() != null) {
			// TODO: find super
		}
	
		IComponent comp = fContext.mk_component(
				c.getName(), super_c);
		
		for (component_body_item bi : c.getBody()) {
			if (bi instanceof overrides_declaration) {
				error("unsupported component statement");
			} else if (bi instanceof component_field_declaration) {
			} else if (bi instanceof action_declaration) {
				IAction action = action_decl((action_declaration)bi);
//				fContext.add_ac);
			} else if (bi instanceof struct_declaration) {
			} else if (bi instanceof import_stmt) {
			} else if (bi instanceof object_bind_stmt) {
			} else if (bi instanceof inline_type_object_declaration) {
			} else {
				
			}
			
		}
		
		return comp;
	}
	
	protected IPackage package_decl(package_declaration p) {
		IPackage pkg = fContext.mk_package(p.getName().toString());
		
		for (package_body_item bi : p.getBody()) {
			if (bi instanceof action_declaration) {
				error("unsupported package statement");
//			} else if (bi instanceof component_declaration) {
//				error("unsupported package statement");
			} else if (bi instanceof struct_declaration) {
				error("unsupported package statement");
			} else if (bi instanceof enum_declaration) {
				error("unsupported package statement");
			} else if (bi instanceof coverspec_declaration) {
				error("unsupported package statement");
			} else if (bi instanceof import_method_decl) {
				error("unsupported package statement");
			} else if (bi instanceof import_class_decl) {
				error("unsupported package statement");
			} else if (bi instanceof export_action) {
				error("unsupported package statement");
			} else if (bi instanceof typedef_declaration) {
				error("unsupported package statement");
			} else if (bi instanceof bins_declaration) {
				error("unsupported package statement");
			} else if (bi instanceof import_stmt) {
				error("unsupported package statement");
			} else if (bi instanceof extend_stmt) {
				error("unsupported package statement");
			} else {
				error("unknown package statement");
			}
		}
	
		return pkg;
	}
	
	protected IAction action_decl(action_declaration action_decl) {
		IAction action = null; // fContext.mk_ac

		return action;
	}

	private void error(String msg) {
		System.out.println("Error: " + msg);
	}
}
