package org.psstools.psde.lang.ui;

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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
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
//		System.out.println("Version: " + version + " prefs_version: " + prefs_version);
		
		update_templates(
				(/*version.equals("1.2.3") || */!version.equals(prefs_version))
				);

		LangActivator.getInstance().getPreferenceStore().setValue(
				PREFS_VERSION, version);
	}
	
	private void update_templates(boolean update_value) {
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
		if (update_value) {
			LangActivator.getInstance().getPreferenceStore().setValue(
					PREFS_TEMPLATES, prefs.toString());
		}
	}

}
