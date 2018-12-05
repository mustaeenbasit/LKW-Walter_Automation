package com.sugarcrm.test.userinterface;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class UserInterface_28853 extends SugarTest {
	public void setup() throws Exception {
		// Login as a valid user
		sugar().login();
	}

	/**
	 * Verify the color code of Severity in View Notifications
	 * 
	 * @throws Exception
	 */
	@Test
	public void UserInterface_28853_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet colorCodeData = testData.get(testName).get(0);

		// Click on Notifications -> View Notifications(upper right of the screen next to Global search)
		// TODO: VOOD-2063
		new VoodooControl("button", "css", "div[data-name='notifications-list-button'] button").click();
		new VoodooControl("li", "css", "div[data-name='notifications-list-button'] .dropdown-menu li:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Verify that the Border color should be "#f5d9a0" and Background color should be "#fdf8ee" 
		// TODO: VOOD-2063
		VoodooControl severityMessageCtrl = new VoodooControl("span", "css", ".layout_Notifications .flex-list-view span[data-voodoo-name='severity'] .label-warning");
		severityMessageCtrl.assertCssAttribute("background-color", colorCodeData.get("background_color"), true);
		severityMessageCtrl.assertCssAttribute("border", colorCodeData.get("border"), true);
		severityMessageCtrl.assertCssAttribute("border", colorCodeData.get("borderWithPixel"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}