package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18642 extends SugarTest {
	FieldSet forecastAndMessageFS;

	public void setup() throws Exception {
		forecastAndMessageFS = testData.get(testName).get(0);
		FieldSet rliName = new FieldSet();

		// Create two RLI record
		for (int i = 0; i < 2; i++) {
			rliName.put("name", forecastAndMessageFS.get("rliName")+"_"+i);
			sugar().revLineItems.api.create(rliName);
			rliName.clear();
		}

		// Login as admin user
		sugar().login();

		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Mass update the both RLI records to INCLUDED in the forecast module
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set(forecastAndMessageFS.get("forecastField"));
		sugar().revLineItems.massUpdate.getControl("massUpdateValue02").set(forecastAndMessageFS.get("forecastValue"));
		sugar().revLineItems.massUpdate.update();

		// Logout from the admin user
		sugar().logout();
	}

	/**
	 * ENT/ULT: Verify that warning message is displayed when RLI that is marked as "Include" in a current draft and/or committed Forecast gets deleted.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18642_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as sales rep user (QAUser)
		sugar().login(sugar().users.getQAUser());

		// Go to RLI module and open first RLI created during setup in the record view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);

		// Delete this RLI
		sugar().revLineItems.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify the The following warning message is displayed: "This record was included in a Forecast. It will be removed and you will need to re-commit your Forecast." 
		sugar().alerts.getWarning().assertContains(forecastAndMessageFS.get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		// Go to RLI module list view and delete the second RLI created during setup
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();
		sugar().alerts.getAlert().confirmAlert();

		// Verify the The following warning message is displayed: "This record was included in a Forecast. It will be removed and you will need to re-commit your Forecast." 
		sugar().alerts.getWarning().assertContains(forecastAndMessageFS.get("warningMessage"), true);
		sugar().alerts.getWarning().closeAlert();

		// Logout from sales rep user and login as admin (Need to logout as script not perform logout from other user and login to admin user automatically before moves to cleanup() method)
		sugar().logout();
		sugar().login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}