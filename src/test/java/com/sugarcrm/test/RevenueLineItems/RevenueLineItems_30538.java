package com.sugarcrm.test.RevenueLineItems;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.*;

public class RevenueLineItems_30538 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
		// Go to Admin-> Forecasts and configure Forecast settings properly
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();
	}

	/**
	 * Verify that Forecast field value cannot be changed for closed Opps/RLIs using mass update.
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30538_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource rliRecord = testData.get(testName);
		FieldSet fs = testData.get(testName + "_1").get(0);

		sugar().revLineItems.create(rliRecord);
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set("Forecast");
		sugar().revLineItems.massUpdate.getControl("massUpdateValue02").set("Include");
		sugar().revLineItems.massUpdate.update();

		// Verify that Warning message visible and comes up saying: Warning One or more of the selected records has a status of Closed Won or Closed Lost and cannot be mass updated.
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertEquals(fs.get("warning_msg"), true);

		// Verify that closed records are unchecked
		Assert.assertEquals("Checkbox is checked when it should not ", false, sugar().revLineItems.listView.getControl("checkbox01").isChecked());
		Assert.assertEquals("Checkbox is checked when it should not ", false, sugar().revLineItems.listView.getControl("checkbox02").isChecked());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}