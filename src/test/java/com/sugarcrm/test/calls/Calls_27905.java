package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27905 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that contact_id isn't available in Calls sub panel
	 * @throws Exception
	 */
	@Test
	public void Calls_27905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		VoodooControl accountsModuleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl opportunitiesModuleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		VoodooControl subPanelBtnCtrl = new VoodooControl("td", "id", "subpanelsBtn");		
		VoodooControl subPanelCallsCtrl = new VoodooControl("a", "css", "#Buttons tr:nth-child(1) td:nth-child(1) tr:nth-child(2) a.studiolink");
		VoodooControl contactIdDefaultCtrl = new VoodooControl("li", "css", "#Default .draggable[data-name='contact_id']");
		VoodooControl contactIdHiddenCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='contact_id']");

		// Verify that contact_id is not in Studio > Accounts/Opportunities > SubPanel > Calls
		for(int i = 0; i < 2; i++) {
			// Click on Studio > Accounts 
			if(i == 0)
				accountsModuleCtrl.click();
			else
				opportunitiesModuleCtrl.click();

			// Click on Studio > Accounts > SubPanel
			VoodooUtils.waitForReady();
			subPanelBtnCtrl.click();
			VoodooUtils.waitForReady();

			// Click on Calls SubPanel		
			subPanelCallsCtrl.click();
			VoodooUtils.waitForReady();

			// Verify that the contact_id is not exist in Default & Hidden section
			contactIdDefaultCtrl.assertExists(false);
			contactIdHiddenCtrl.assertExists(false);
			sugar().admin.studio.clickStudio();
			VoodooUtils.waitForReady();
		}

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}