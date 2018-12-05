package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_18637 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify display module setting 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_18637_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().admin.disableModuleDisplayViaJs(sugar().accounts);
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().bugs.navToListView();
		sugar().navbar.showAllModules();
		
		// TODO VOOD-784
		new VoodooControl("li", "css", "div.module-list li[data-module='"+sugar().accounts.moduleNamePlural+"']").assertExists(false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
