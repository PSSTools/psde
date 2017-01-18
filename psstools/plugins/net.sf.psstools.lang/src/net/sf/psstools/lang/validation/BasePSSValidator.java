package net.sf.psstools.lang.validation;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.validation.Check;

import net.sf.psstools.lang.pSS.Model;
import net.sf.psstools.lang.pSS.PSSPackage;
import net.sf.psstools.lang.pSS.action_field_declaration;
import net.sf.psstools.lang.pSS.exec_block_stmt;
import net.sf.psstools.lang.pSS.user_defined_datatype;

public class BasePSSValidator extends PSSValidator {
	

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
//		System.out.println("--> validate");
		boolean ret = super.validate(eDataType, value, diagnostics, context);
//		System.out.println("<-- validate");
		
		return ret;
	}
	
	@Check
	public void checkActionExists(action_field_declaration decl) {
		if (decl.getDeclaration().getType() instanceof user_defined_datatype) {
			user_defined_datatype udt = (user_defined_datatype)decl.getDeclaration().getType();
//			System.out.println("TYPE: " + udt.getTypename());
//			error("Undefined type", decl,
//					PSSPackage.eINSTANCE.getaction_field_declaration_Declaration());
		}
		
	}
	
	@Check
	public void checkVariableRefs(exec_block_stmt exec) {
//		System.out.println("check exec: " + exec.getBody());
//		try {
//			throw new Exception();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
