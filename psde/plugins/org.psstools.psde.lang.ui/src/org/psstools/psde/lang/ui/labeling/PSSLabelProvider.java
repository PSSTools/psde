/****************************************************************************
 * Copyright 2015-2018 Mentor Graphics Corporation
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
package org.psstools.psde.lang.ui.labeling;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import org.psstools.psde.lang.pSS.Model;
import org.psstools.psde.lang.pSS.action_declaration;
import org.psstools.psde.lang.pSS.activity_declaration;
import org.psstools.psde.lang.pSS.bool_type;
import org.psstools.psde.lang.pSS.component_declaration;
import org.psstools.psde.lang.pSS.component_field_declaration;
import org.psstools.psde.lang.pSS.data_declaration;
import org.psstools.psde.lang.pSS.data_instantiation;
import org.psstools.psde.lang.pSS.enum_declaration;
import org.psstools.psde.lang.pSS.extend_stmt;
import org.psstools.psde.lang.pSS.hierarchical_id;
import org.psstools.psde.lang.pSS.integer_type;
import org.psstools.psde.lang.pSS.object_bind_stmt;
import org.psstools.psde.lang.pSS.overrides_declaration;
import org.psstools.psde.lang.pSS.package_declaration;
import org.psstools.psde.lang.pSS.string_type;
import org.psstools.psde.lang.pSS.struct_declaration;
import org.psstools.psde.lang.pSS.type_identifier;
import org.psstools.psde.lang.pSS.user_defined_datatype;

import com.google.inject.Inject;

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
public class PSSLabelProvider extends DefaultEObjectLabelProvider {

	@Inject
	public PSSLabelProvider(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	
	// Labels and icons can be computed like this:
	
	public String text(overrides_declaration e) { return "override"; }
	
	public String text(activity_declaration g) { return "activity"; }
	
	public String text(data_instantiation o) {
		data_declaration dd = (data_declaration)o.eContainer();
		return o.getName() + " : " + typename(dd);
	}
	
	public String text(object_bind_stmt o) {
		String ret = "bind " + hid2string(o.getLhs());
		
		if (o.getRhs().getItems().size() > 1) {
			ret += '{';
		}
		
//		for (var i=0; i<o.rhs.items.size; i++) {
//			ret += o.rhs.items.size
//		}
		
		if (o.getRhs().getItems().size() > 1) {
			ret += '}';
		}
		return ret;
	}
	
	public String text(extend_stmt e) {
		String ret = tid2string(e.getName());
		if (e.isAction()) ret += " : action";
		if (e.isStruct()) ret += " : struct";
		if (e.isEnum_e()) ret += " : enum";
		if (e.isComponent()) ret += " : component";
		return ret;
	}
	
	public String text(package_declaration p) {
		return hid2string((hierarchical_id)p.getName()) + " : package";
	}
	
	
	private String typename(data_declaration dd) {
		if (dd.getType() instanceof integer_type) {
			integer_type i_t = (integer_type)dd.getType();
			return i_t.getTypename();
		} else if (dd.getType() instanceof string_type) {
			return "string";
		} else if (dd.getType() instanceof bool_type) {
			return "bool";
		} else if (dd.getType() instanceof user_defined_datatype) {
			user_defined_datatype ud_t = (user_defined_datatype)dd.getType();
			return tid2string(ud_t.getTypename());
		} else {
			return "unknown";
		}
	}
	
	private String tid2string(type_identifier ti) {
		String ret = "";
		for (int i=0; i<ti.getElems().size(); i++) {
			String id = ti.getElems().get(i);
			ret += id;
			if (i+1 < ti.getElems().size()) {
				ret += "::";
			}
		}
		return ret;
	}
	
	private String hid2string(hierarchical_id hi) {
		String ret = "";
		for (int i=0; i<hi.getPath().size(); i++) {
			ret += hi.getPath().get(i);
			if (i+1<hi.getPath().size()) {
				ret += ".";
			}
		}
		return ret;
	}
	
	// Labels and icons can be computed like this:
	
	

	public Object image(action_declaration a) { return "action_obj.gif"; }


	public Object image(component_declaration c) { return "class_obj.gif"; }
	public Object image(activity_declaration g) { return "activity_obj.gif"; }
	public Object image(extend_stmt s) { 
		if (s.isComponent()) {
			return "extends_component.gif";
		} else if (s.isAction()) {
			return "extends_action.gif";
		} else if (s.isStruct()) {
			return "extends_struct.gif";
		}
	
		return "extends.gif"; 
	}
	public Object image(component_field_declaration f) { 
		return "field_public_obj.gif"; 
	}
	public Object image(data_instantiation f) { 
		return "field_public_obj.gif"; 
	}
	public Object image(enum_declaration e) { 
		return "enum_obj.gif"; 
	}
	public Object image(Model m) { 
		return "module_obj.gif"; 
	}
	public Object image(struct_declaration s) { 
		return "struct_obj.gif"; 
	}
	public Object image(package_declaration p) { 
		return "package.gif"; 
	}
//	public Object image(struct_field_declaration f) { 
//		return "field_public_obj.gif"; 
//	}

}
