package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;


public class KnowledgeBase_30655 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Language column doesn't have sorting ability in KB listview
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30655_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB module
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// TODO: VOOD-1768 ListView header element defs don't work for unsortable headers.
		// Verify that Language column doesn't have sorting ability
		new VoodooControl("th","css","[data-fieldname=language]").assertAttribute("class", "sorting", false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}