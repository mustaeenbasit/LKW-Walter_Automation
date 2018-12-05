package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27905 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that contact_id isn't available in Meetings sub panel
	 * @throws Exception
	 */
	@Test
	public void Meetings_27905_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-938
		VoodooControl accountsModuleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl opportunitiesModuleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		VoodooControl subPanelBtnCtrl = new VoodooControl("td", "id", "subpanelsBtn");		
		VoodooControl subPanelMeetingsCtrl = new VoodooControl("a", "css", "#Buttons tr:nth-child(1) td:nth-child(2) tr:nth-child(2) a.studiolink");
		VoodooControl contactIdCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='contact_id']");
		
		// Verify that contact_id is not in Studio > Accounts/Opportunities > SubPanel > Meetings
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
			
			// Click on Studio > Accounts > SubPanel > Meetings		
			subPanelMeetingsCtrl.click();
	
			// Verify that contact_id is not exist. 
			contactIdCtrl.assertExists(false);
			sugar().admin.studio.clickStudio();
		}
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}