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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.folding.DefaultFoldingRegionProvider;
import org.psstools.psde.lang.pSS.action_declaration;
import org.psstools.psde.lang.pSS.activity_declaration;
import org.psstools.psde.lang.pSS.activity_match_stmt;
import org.psstools.psde.lang.pSS.activity_parallel_stmt;
import org.psstools.psde.lang.pSS.activity_schedule_stmt;
import org.psstools.psde.lang.pSS.activity_select_stmt;
import org.psstools.psde.lang.pSS.activity_sequence_block_stmt;
import org.psstools.psde.lang.pSS.component_declaration;
import org.psstools.psde.lang.pSS.constraint_block;
import org.psstools.psde.lang.pSS.constraint_declaration;
import org.psstools.psde.lang.pSS.covergroup_declaration;
import org.psstools.psde.lang.pSS.enum_declaration;
import org.psstools.psde.lang.pSS.exec_block;
import org.psstools.psde.lang.pSS.exec_body_stmt;
import org.psstools.psde.lang.pSS.extend_stmt;
import org.psstools.psde.lang.pSS.inline_covergroup;
import org.psstools.psde.lang.pSS.overrides_declaration;
import org.psstools.psde.lang.pSS.package_declaration;
import org.psstools.psde.lang.pSS.struct_declaration;

public class PSSFoldingRegionProvider extends DefaultFoldingRegionProvider {
	
	@Override
	protected boolean isHandled(EObject e) {
		return (
				false
				|| (e instanceof package_declaration)
				|| (e instanceof extend_stmt)
				|| (e instanceof action_declaration)
				|| (e instanceof activity_declaration)
				|| (e instanceof exec_block)
				|| (e instanceof struct_declaration)
				|| (e instanceof component_declaration)
				|| (e instanceof activity_declaration)
				|| (e instanceof activity_sequence_block_stmt)
				|| (e instanceof activity_select_stmt)
				|| (e instanceof activity_match_stmt)
				|| (e instanceof activity_parallel_stmt)
				|| (e instanceof activity_schedule_stmt)
				|| (e instanceof overrides_declaration)
				|| (e instanceof enum_declaration)
				|| (e instanceof constraint_declaration)
				|| (e instanceof constraint_block)
				|| (e instanceof covergroup_declaration)
				|| (e instanceof inline_covergroup)
				);
	}

}
