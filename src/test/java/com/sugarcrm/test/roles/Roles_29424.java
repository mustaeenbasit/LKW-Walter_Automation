package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_29424 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Opportunities and RLIs modules are loaded properly after access to Forecasts module is set to "disabled"
	 *
	 * @throws Exception
	 */
	@Test
	public void Roles_29424_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		customData = testData.get(testName).get(0);

		// Creating one role
		AdminModule.createRole(customData);
		VoodooUtils.focusFrame("bwc-frame");

		// View is set to owner for account module in created role.
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Forecasts_access .aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Forecasts_access select").set("Disabled");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assigning this role to qauser
		AdminModule.assignUserToRole(sugar().users.getQAUser());
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as Chris
		sugar().login(sugar().users.getQAUser());

		// Verify that Forecast module should not appear
		sugar().navbar.getControl("showAllModules").click();
		// TODO: VOOD-827
		new VoodooControl("li", "css", ".dropdown-menu [data-module='Forecasts']").assertExists(false);

		// Navigate to opportunity list view
		sugar().opportunities.navToListView();

		// Verify that the Opportunity module should be there and accessible.
		sugar().opportunities.listView.assertExists(true);

		// Navigate to Revenue Line Item list view
		sugar().revLineItems.navToListView();

		// Verify that the Revenue Line Item module should be there and accessible.
		sugar().revLineItems.listView.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}