package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_18740 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Web logic hooks should appear in the administration screen 
	 * @throws Exception
	 */
	@Test
	public void Admin_18740_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify link 'Web Logic Hooks' in Administration screen
		sugar().admin.adminTools.getControl("webLogicHook").assertEquals(customData.get("link"), true);
		
		// Verify label- 'Manage Sugar Web Logic Hooks' of link 'Web Logic Hooks'
		// Used Xpath to locate label so that if location changes of WebLogic Hooks still it finds it.
		new VoodooControl("td", "xpath", "//*[@id='contentTable']/tbody/tr/td/div[2]/div/div/table[6]/tbody/tr[contains(.,'"+customData.get("label")+"')]/td[4]").assertEquals(customData.get("label"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
