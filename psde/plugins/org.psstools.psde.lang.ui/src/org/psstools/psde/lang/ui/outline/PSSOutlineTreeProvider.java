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
package org.psstools.psde.lang.ui.outline;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;
import org.psstools.psde.lang.pSS.action_declaration;
import org.psstools.psde.lang.pSS.activity_action_traversal_stmt;
import org.psstools.psde.lang.pSS.activity_declaration;
import org.psstools.psde.lang.pSS.attr_field;
import org.psstools.psde.lang.pSS.component_declaration;
import org.psstools.psde.lang.pSS.constraint_declaration;
import org.psstools.psde.lang.pSS.covergroup_declaration;
import org.psstools.psde.lang.pSS.data_declaration;
import org.psstools.psde.lang.pSS.data_instantiation;
import org.psstools.psde.lang.pSS.enum_declaration;
import org.psstools.psde.lang.pSS.exec_block_stmt;
import org.psstools.psde.lang.pSS.function_decl;
import org.psstools.psde.lang.pSS.inline_covergroup;
import org.psstools.psde.lang.pSS.object_bind_stmt;
import org.psstools.psde.lang.pSS.overrides_declaration;
import org.psstools.psde.lang.pSS.struct_declaration;
import org.psstools.psde.lang.pSS.typedef_declaration;

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#outline
 */
public class PSSOutlineTreeProvider extends DefaultOutlineTreeProvider {
	

	@Override
	protected boolean _isLeaf(EObject modelElement) {
		return super._isLeaf(modelElement);
	}
	
	@Override
	public IOutlineNode createRoot(IXtextDocument document) {
		// TODO Auto-generated method stub
		return super.createRoot(document);
	}

	public boolean _isLeaf(constraint_declaration e) { return true; }
	public boolean _isLeaf(typedef_declaration e) { return true; }
	public boolean _isLeaf(data_instantiation e) { return true; }
	public boolean _isLeaf(overrides_declaration e) { return true; }
	public boolean _isLeaf(enum_declaration e) { return true; }
	public boolean _isLeaf(object_bind_stmt e) { return true; }
	public boolean _isLeaf(exec_block_stmt e) { return true; }
	public boolean _isLeaf(function_decl e) { return true; }
	
	public void _createChildren(IOutlineNode parentNode, activity_declaration activity) {
		for (EObject c : activity.getBody()) {
			if (c instanceof activity_action_traversal_stmt) {
				activity_action_traversal_stmt t = (activity_action_traversal_stmt)c;
				if (t.getItem() != null) {
					createNode(parentNode, t.getItem());
				}
			}
		}
	}

        public void _createChildren(IOutlineNode parentNode, action_declaration struct) {
                for (EObject child : struct.getBody()) {
                        if (child instanceof attr_field) {
                                attr_field field = (attr_field)child;
                                for (EObject value : field.getDeclaration().getInstances()) {
                                        createNode(parentNode, value);
                                }
                        } else {
                                createNode(parentNode, child);
                        }
                }
        }

        public void _createChildren(IOutlineNode parentNode, covergroup_declaration cs) {
                for (EObject child : cs.getBody_items()) {
                        createNode(parentNode, child);
                }
        }

        public void _createChildren(IOutlineNode parentNode, component_declaration component) {
        	for (EObject child : component.getBody()) {
        		if (child instanceof attr_field) {
        			_createChildren(parentNode, ((attr_field)child).getDeclaration());
        		} else {
        			if (!(child instanceof object_bind_stmt)) {
        				createNode(parentNode, child);
        			}
        		}
        	}
        }
        
        public void _createChildren(IOutlineNode parent, struct_declaration struct) {
        	for (EObject child : struct.getBody()) {
        		if (child instanceof attr_field) {
        			_createChildren(parent, ((attr_field)child).getDeclaration());
        		} else {
        			if (false 
        					|| (child instanceof constraint_declaration)
        					|| (child instanceof covergroup_declaration)
        					|| (child instanceof inline_covergroup)
        					|| (child instanceof exec_block_stmt)) {
        				createNode(parent, child);
        			}
        		}
        	}
        }

        public void _createChildren(IOutlineNode parentNode, data_declaration dd) {
                for (data_instantiation child : dd.getInstances()) {
                        createNode(parentNode, child);
                }
        }
        
        public void _createChildren(IOutlineNode parent, attr_field f) {
        	_createChildren(parent, f.getDeclaration());
        }

}
