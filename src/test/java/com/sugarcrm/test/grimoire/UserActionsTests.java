package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class UserActionsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1053
		new VoodooControl("div", "id", "Users_detailview_tabs").assertVisible(true);  
		VoodooUtils.focusDefault();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").assertVisible(true); 
		VoodooUtils.focusDefault();
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// TODO: VOOD-1887
		new VoodooControl("div", "css", ".layout_Accounts").assertVisible(true); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}