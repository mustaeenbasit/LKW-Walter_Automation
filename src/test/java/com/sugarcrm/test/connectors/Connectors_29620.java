package com.sugarcrm.test.connectors;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Connectors_29620 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the D&B Test Connector is working properly
	 * @throws Exception
	 */
	@Test
	public void Connectors_29620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet settings = testData.get(testName).get(0);

		// Click the Connector link on Admin page
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("connector").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Navigate to Connector Settings Page
		// TODO: VOOD-637
		new VoodooControl("img", "css", "[name='connectorConfig']").click();
		VoodooUtils.waitForReady();

		// Navigate to D&B tab
		new VoodooControl("a", "css", "#container li:nth-child(5) a").click();
		VoodooUtils.waitForReady();

		// Enter the D&B UserName & Password Settings
		new VoodooControl("input", "id", "ext_rest_dnb_dnb_username").set(settings.get("userName"));
		new VoodooControl("input", "id", "ext_rest_dnb_dnb_password").set(settings.get("key"));

		// Click the Test Button
		new VoodooControl("input", "id", "ext_rest_dnb_test_button").click();
		VoodooUtils.waitForReady();

		// Verify the Message "Test Successful" is being displayed
		new VoodooControl("span", "id", "ext_rest_dnb_result").assertEquals(settings.get("message"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}