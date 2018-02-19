package org.psstools.psde.lang.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.osgi.framework.Version;
import org.psstools.psde.lang.ui.internal.LangActivator;

public class TemplatePrefsInitializer extends AbstractPreferenceInitializer {
	
	private static final String			PREFS_VERSION = 
			"org.psstools.psde.lang.PSS.prefs_version";
	private static final String			PREFS_TEMPLATES =
			"org.psstools.psde.lang.PSS.templates";

	@Override
	public void initializeDefaultPreferences() {
		// Get the plugin version
		String version = LangActivator.getInstance().getBundle().getVersion().toString();
		String prefs_version = LangActivator.getInstance().getPreferenceStore().getString(PREFS_VERSION);
		System.out.println("Version: " + version + " prefs_version: " + prefs_version);
		
		if (version.equals("1.2.3") || !version.equals(prefs_version)) {
			update_templates();
			
			LangActivator.getInstance().getPreferenceStore().setValue(
					PREFS_VERSION, version);
		}
	}
	
	private void update_templates() {
		// TODO: for now, we just force-update all templates. 
		// Not the best approach, but ok for now
		String default_templates = LangActivator.getInstance().getPreferenceStore().getDefaultString(
				PREFS_TEMPLATES);
		String current_templates = LangActivator.getInstance().getPreferenceStore().getString(
				PREFS_TEMPLATES);
		
		StringBuilder prefs = new StringBuilder();
		prefs.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		prefs.append("<templates>");
		Enumeration<URL> templates = LangActivator.getInstance().getBundle().findEntries(
				"/templates", "*.xml", true);
		
		while (templates.hasMoreElements()) { 
			URL u = templates.nextElement();
			InputStream in = null;
			try {
				in = u.openStream();
			} catch (IOException e) {
				System.out.println("Error: failed to open template \"" + u.getPath() + "\"");
			}
			
			if (in != null) {
				byte tmp[] = new byte[1024];
				int sz;
				boolean in_template = false;
				
				StringBuilder template = new StringBuilder();
			
				try {
					while ((sz = in.read(tmp, 0, tmp.length)) > 0) {
						for (int i=0; i<sz; i++) {
							if (!in_template) {
								if (tmp[i] == '>') {
									in_template = true;
								}
							} else {
								if (tmp[i] == '<') {
									in_template = false;
								}
							}
			
							if (tmp[i] != '\r') {
								if (in_template || tmp[i] != '\n') {
									template.append((char)tmp[i]);
								}
							}
						}
					}
					
					in.close();
				} catch (IOException e) { }
			
				int idx;
				if ((idx=template.indexOf(">")) != 0) {
					idx++;
					while (idx < template.length() && 
							Character.isWhitespace(template.charAt(idx))) {
						template.replace(idx, idx+1, "");
					}
				}
				
				if (idx > 0 && (idx=template.indexOf("<", idx)) != 0) {
					idx--;
					while (idx > 0 && 
							Character.isWhitespace(template.charAt(idx))) {
						template.replace(idx, idx+1, "");
						idx--;
					}
				}
				
				prefs.append(template);
			}
		}
	
		prefs.append("</templates>");
		
		LangActivator.getInstance().getPreferenceStore().setDefault(
				PREFS_TEMPLATES, prefs.toString());
		LangActivator.getInstance().getPreferenceStore().setValue(
				PREFS_TEMPLATES, prefs.toString());
	}

}
