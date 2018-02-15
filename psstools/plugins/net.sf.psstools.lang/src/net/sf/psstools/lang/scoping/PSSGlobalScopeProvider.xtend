package net.sf.psstools.lang.scoping

import com.google.common.base.Predicate
import com.google.common.collect.Lists
import java.util.Collections
import java.util.Iterator
import java.util.List
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider
import org.eclipse.xtext.scoping.impl.ResourceSetGlobalScopeProvider

class PSSGlobalScopeProvider extends ResourceSetGlobalScopeProvider {

//	override List<IContainer> getVisibleContainers(Resource resource) {
//		val c = super.getVisibleContainers(resource);
//		for (IContainer ci : c) {
//			System.out.println("Container: " + c);
//		}
//		return c;
//	}	
//	
//	override IScope getScope(IScope parent, Resource context, boolean ignoreCase, EClass type, Predicate<IEObjectDescription> filter) {
//		System.out.println("--> Global.getScope: parent=" + parent);
//		var IScope result = parent;
//		if (context === null || context.getResourceSet() === null) {
//			System.out.println("  return parent, since context=" + context);
//			return result;
//		}
//		val List<IContainer> containers = Lists.newArrayList(getVisibleContainers(context));
//		Collections.reverse(containers);
//		val Iterator<IContainer> iter = containers.iterator();
//		while (iter.hasNext()) {
//			val IContainer container = iter.next();
//			System.out.println("  process container " + container);
//			result = createContainerScopeWithContext(context, result, container, filter, type, ignoreCase);
//		}
//		return result;		
//	}
}