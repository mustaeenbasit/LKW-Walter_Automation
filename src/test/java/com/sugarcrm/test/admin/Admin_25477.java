package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_25477 extends SugarTest {
	VoodooControl oppCancelBtn, taskCancelBtn;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/** Verify no error to Cancel quick create UI.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_25477_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click on quick create to select sidecar module, Accounts.
		sugar().navbar.quickCreateAction(sugar().accounts.moduleNamePlural);
			
		// Verify, the Cancel button appears and functional
		sugar().accounts.createDrawer.cancel();
		
		// Verify, the quick create UI disappears without error.
		sugar().home.dashboard.assertContains("My Dashboard", true);
		
		// Click on quick create to select sidecar module, Opportunity.
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		
		// Verify, the Cancel button appears and functional
		sugar().opportunities.createDrawer.cancel();
		
		// Verify, the quick create UI disappears without error.
		sugar().home.dashboard.assertContains("My Dashboard", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
