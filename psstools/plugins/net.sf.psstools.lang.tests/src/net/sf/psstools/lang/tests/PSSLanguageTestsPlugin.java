package net.sf.psstools.lang.tests;

import java.io.File;

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
	
	public File getSchemaPath() {
		String bundle_path = getBundle().getLocation();
		
		if (bundle_path.startsWith("reference:")) {
			bundle_path = bundle_path.substring("reference:".length());
		}
		
		if (bundle_path.startsWith("file:")) {
			bundle_path = bundle_path.substring("file:".length());
		}
		
		if (bundle_path.startsWith("/") && !bundle_path.startsWith("//")) {
			bundle_path = bundle_path.substring(1);
		}
		
		return new File(bundle_path, "../../../psi/schema/PSSModel.xsd");
	}

}
