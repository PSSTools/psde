package net.sf.psstools.lang.tests;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PSSLanguageTestsPlugin extends Plugin implements BundleActivator {
	
	private static PSSLanguageTestsPlugin			plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.stop(context);
	}

	public static PSSLanguageTestsPlugin getDefault() {
		return plugin;
	}

}
