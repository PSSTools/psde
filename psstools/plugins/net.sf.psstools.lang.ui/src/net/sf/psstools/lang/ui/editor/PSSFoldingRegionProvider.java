package net.sf.psstools.lang.ui.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionProvider;

import net.sf.psstools.lang.pSS.action_declaration;
import net.sf.psstools.lang.pSS.activity_constraint_stmt;
import net.sf.psstools.lang.pSS.activity_declaration;
import net.sf.psstools.lang.pSS.activity_select_stmt;
import net.sf.psstools.lang.pSS.activity_parallel_stmt;
import net.sf.psstools.lang.pSS.activity_schedule_stmt;
import net.sf.psstools.lang.pSS.component_declaration;
import net.sf.psstools.lang.pSS.constraint_declaration;
import net.sf.psstools.lang.pSS.coverspec_declaration;
import net.sf.psstools.lang.pSS.enum_declaration;
import net.sf.psstools.lang.pSS.extend_stmt;
import net.sf.psstools.lang.pSS.package_declaration;
import net.sf.psstools.lang.pSS.struct_declaration;

public class PSSFoldingRegionProvider extends DefaultFoldingRegionProvider {
	
	private static final Set<Class<?>>		fFoldingRegions;
	
	static {
		fFoldingRegions = new HashSet<Class<?>>();
	
		fFoldingRegions.add(action_declaration.class);
		
		fFoldingRegions.add(component_declaration.class);
		fFoldingRegions.add(coverspec_declaration.class);
		
		fFoldingRegions.add(activity_declaration.class);
		fFoldingRegions.add(activity_select_stmt.class);
		fFoldingRegions.add(activity_parallel_stmt.class);
		fFoldingRegions.add(activity_schedule_stmt.class);
		
		fFoldingRegions.add(enum_declaration.class);
		fFoldingRegions.add(extend_stmt.class);
		
		fFoldingRegions.add(package_declaration.class);
		
		fFoldingRegions.add(struct_declaration.class);
	}
	
	protected boolean isHandled(action_declaration e) {
		System.out.println("isHandled: " + e);
		return true;
	}

	@Override
	protected boolean isHandled(EObject e) {
		for (Class<?> c : fFoldingRegions) {
			if (c.isAssignableFrom(e.getClass())) {
				return true;
			}
		}
		
		// More-specific checks
		if (e instanceof constraint_declaration) {
			constraint_declaration c = (constraint_declaration)e;
			if (c.isBlock()) {
				return true;
			}
		}
		
		if (e instanceof activity_constraint_stmt) {
			if (((activity_constraint_stmt)e).isBlock()) {
				return true;
			}
		}

		return false;
	}
	

}
