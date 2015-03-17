/*
* generated by Xtext
*/
package net.sf.psstools.lang.ui.outline

import net.sf.psstools.lang.pSS.action_declaration
import net.sf.psstools.lang.pSS.constraint_declaration
import net.sf.psstools.lang.pSS.data_instantiation
import net.sf.psstools.lang.pSS.field_declaration
import net.sf.psstools.lang.pSS.graph_declaration
import net.sf.psstools.lang.pSS.struct_declaration
import net.sf.psstools.lang.pSS.symbol_definition
import net.sf.psstools.lang.pSS.typedef_declaration
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider

/**
 * Customization of the default outline structure.
 *
 * see http://www.eclipse.org/Xtext/documentation.html#outline
 */
class PSSOutlineTreeProvider extends DefaultOutlineTreeProvider {
	
	def _isLeaf(constraint_declaration e) { true }
	def _isLeaf(symbol_definition e) { true }
	def _isLeaf(typedef_declaration e) { true }
	def _isLeaf(data_instantiation e) { true }
	def _isLeaf(action_declaration e) { true }
	
	def _createChildren(IOutlineNode parentNode, struct_declaration struct) {
		for (EObject child : struct.body) {
			if (child instanceof field_declaration) {
				var field = child as field_declaration;
				for (EObject value : field.declaration.instances) {
					createNode(parentNode, value);
				}
			} else {
				createNode(parentNode, child);
			}
		}
	}

	def _createChildren(IOutlineNode parentNode, graph_declaration struct) {
		for (EObject child : struct.body) {
			if (child instanceof field_declaration) {
				var field = child as field_declaration;
				for (EObject value : field.declaration.instances) {
					createNode(parentNode, value);
				}
			} else {
				createNode(parentNode, child);
			}
		}
	}	
}
