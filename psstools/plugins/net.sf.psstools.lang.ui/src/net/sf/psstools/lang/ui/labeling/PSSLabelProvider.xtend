/*
* generated by Xtext
*/
package net.sf.psstools.lang.ui.labeling

import com.google.inject.Inject
import net.sf.psstools.lang.pSS.Model
import net.sf.psstools.lang.pSS.action_declaration
import net.sf.psstools.lang.pSS.bool_type
import net.sf.psstools.lang.pSS.component_declaration
import net.sf.psstools.lang.pSS.component_field_declaration
import net.sf.psstools.lang.pSS.data_declaration
import net.sf.psstools.lang.pSS.data_instantiation
import net.sf.psstools.lang.pSS.enum_declaration
import net.sf.psstools.lang.pSS.extend_stmt
import net.sf.psstools.lang.pSS.activity_declaration
import net.sf.psstools.lang.pSS.hierarchical_id
import net.sf.psstools.lang.pSS.integer_type
import net.sf.psstools.lang.pSS.object_bind_stmt
import net.sf.psstools.lang.pSS.overrides_declaration
import net.sf.psstools.lang.pSS.package_declaration
import net.sf.psstools.lang.pSS.string_type
import net.sf.psstools.lang.pSS.struct_declaration
import net.sf.psstools.lang.pSS.struct_field_declaration
import net.sf.psstools.lang.pSS.type_identifier
import net.sf.psstools.lang.pSS.user_defined_datatype
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#labelProvider
 */
class PSSLabelProvider extends DefaultEObjectLabelProvider {
	
	@Inject
	new(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}
	
	def text(overrides_declaration e) { "override" }
	
	def text(activity_declaration g) { "graph" }
	
	def text(data_instantiation o) {
		var dd = o.eContainer as data_declaration;
		return o.name + " : " + typename(dd);
	}
	
	def text(object_bind_stmt o) {
		var ret = "bind " + hid2string(o.lhs);
		
		if (o.rhs.items.size > 1) {
			ret += '{';
		}
		
//		for (var i=0; i<o.rhs.items.size; i++) {
//			ret += o.rhs.items.size
//		}
		
		if (o.rhs.items.size > 1) {
			ret += '}';
		}
		return ret;
	}
	
	def text(extend_stmt e) {
		var ret = tid2string(e.name);
		if (e.isAction) ret += " : action";
		if (e.isStruct) ret += " : struct";
		if (e.isEnum_e) ret += " : enum";
		if (e.isComponent) ret += " : component";
		return ret;
	}
	
	def text(package_declaration p) {
		return hid2string(p.name as hierarchical_id) + " : package";
	}
	
	
	def typename(data_declaration dd) {
		if (dd.type instanceof integer_type) {
			var i_t = dd.type as integer_type;
			return i_t.typename;
		} else if (dd.type instanceof string_type) {
			return "string";
		} else if (dd.type instanceof bool_type) {
			return "bool";
		} else if (dd.type instanceof user_defined_datatype) {
			var ud_t = dd.type as user_defined_datatype;
			return tid2string(ud_t.typename);
		} else {
			return "unknown";
		}
	}
	
	def tid2string(type_identifier ti) {
		var ret = "";
		for (var i=0; i<ti.elems.size; i++) {
			var id = ti.elems.get(i);
			ret += id;
			if (i+1 < ti.elems.size) {
				ret += "::";
			}
		}
		return ret;
	}
	
	def hid2string(hierarchical_id hi) {
		var ret = "";
		for (var i=0; i<hi.path.size; i++) {
			ret += hi.path.get(i);
			if (i+1<hi.path.size) {
				ret += ".";
			}
		}
		return ret;
	}
	
	// Labels and icons can be computed like this:

	def image(action_declaration a) { 'action_obj.gif'; }
	def image(component_declaration c) { 'class_obj.gif'; }
	def image(activity_declaration g) { 'activity_obj.gif'; }
	def image(extend_stmt s) { 
		if (s.isComponent()) {
			return "extends_component.gif";
		} else if (s.isAction()) {
			return "extends_action.gif";
		}
	
		return 'extends.gif'; 
	}
	def image(component_field_declaration f) { 
//		System.out.println("data_declaration: " + f.eContainer);
		'field_public_obj.gif'; 
	}
	def image(data_instantiation f) { 
//		System.out.println("data_instantiation: " + f.eContainer);
		return 'field_public_obj.gif'; 
	}
	def image(enum_declaration e) { 'enum_obj.gif'; }
	def image(Model m) { 'module_obj.gif'; }
	def image(struct_declaration s) { 'struct_obj.gif'; }
	def image(package_declaration p) { 'package.gif'; }
	def image(struct_field_declaration f) { 'field_public_obj.gif'; }
	
//	def text(Greeting ele) {
//		'A greeting to ' + ele.name
//	}
//
//	def image(Greeting ele) {
//		'Greeting.gif'
//	}
}
