package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27740 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that quick create opportunity record with linked RLI works fine 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27740_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl rliNameField = sugar().opportunities.createDrawer.getEditField("rli_name");

		// Quick create opportunity
		sugar().navbar.quickCreateAction(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.createDrawer.getControl("cancelButton").waitForVisible();

		// Verify Opportunity create drawer is displayed with RLI sub-panel
		sugar().opportunities.createDrawer.assertExists(true);
		rliNameField.assertVisible(true);

		// Create OPP + RLI record
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.defaultData.get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.defaultData.get("relAccountName"));
		rliNameField.set(sugar().opportunities.defaultData.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.defaultData.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.defaultData.get("rli_likely"));
		sugar().opportunities.createDrawer.save();

		// Verify RLI record are created successfully and User is returned to "Home" module
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().closeAlert();
		sugar().home.dashboard.getControl("dashboardTitle").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}