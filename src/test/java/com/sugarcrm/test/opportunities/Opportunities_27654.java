package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_27654 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that click on Opportunities in the admin panel bring user to Opportunities Settings configuration screen
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27654_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-2082
		// Verify Help text for Opportunities
		new VoodooControl("td", "css", "#contentTable div.dashletPanelMenu.wizard table:nth-of-type(17) tr:nth-of-type(2) td").assertEquals(testData.get(testName).get(0).get("help_text"), true);
		sugar().admin.adminTools.getControl("opportunityManagement").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Opportunities Settings page is displayed
		sugar().opportunities.createDrawer.assertExists(true);
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}