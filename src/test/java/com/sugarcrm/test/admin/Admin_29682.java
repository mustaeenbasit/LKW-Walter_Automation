package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29682 extends SugarTest {
	VoodooControl languageCtrl, disableDivCtrl;
	FieldSet fs = new FieldSet();

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that Language is not showing at login page after disable from Admin->Languages
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29682_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin->Languages
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("language").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1932, TR-11191
		// Used xpath because no better testability hook available for enable disable the languages 
		languageCtrl = new VoodooControl("div", "xpath", "//tbody[@class='yui-dt-data']//div[text()='"+ fs.get("language") +"']");
		disableDivCtrl = new VoodooControl("div", "css", "#disabled_div .yui-dt-bd");
		// Move Deutsch language from Enabled to Disabled
		languageCtrl.dragNDrop(disableDivCtrl);
		// Click Save button
		new VoodooControl("input", "css", "[name='ConfigureLangs'] table:nth-child(8) input.button.primary").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		// Logout
		sugar().logout();
		// TODO: VOOD-1429
		// At Login page, click on Language option at bottom right corner
		new VoodooControl("button", "css", "#languageList button").click();
		// Verify that Deutsch language is not displayed inside language menu 
		// Used xpath because no better testability hook available for enable disable the languages 
		new VoodooControl("li", "xpath", "//div[@class='dropdown-menu bottom-up']//ul/li[contains(.,'"+ fs.get("language") +"')]").assertVisible(false);
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}