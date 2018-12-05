package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28928 extends SugarTest {
	DataSource rliData;

	public void setup() throws Exception {
		sugar.opportunities.api.create();
		rliData = testData.get(testName);
		sugar.login();

		// Configuring the Forecasts setting
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar.navbar.navToAdminTools();

		// Create a "Include" RLIs(Need to create from UI as forecast field is edit-able after forecast settings are configured)
		FieldSet rliRecordData = new FieldSet();
		rliRecordData.put("forecast", rliData.get(0).get("forecast"));
		rliRecordData.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		sugar.revLineItems.create(rliRecordData);
	}

	/**
	 * Verify that expected closed date should set to its previous value while enter invalid date.
	 * 
	 * @throws Exception
	 */

	@Test
	public void Forecasts_28928_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Forecasts
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);

		// Edit Expected Closed date and enter invalid date e.g.1111111
		VoodooControl closedDateCtrl = sugar.forecasts.worksheet.getControl("expectedClose01");
		closedDateCtrl.hover();
		// TODO: VOOD-854
		new VoodooControl("i", "css", closedDateCtrl.getHookString() + " i.fa-pencil").click();
		new VoodooControl("input", "css", closedDateCtrl.getHookString() + " input").set(rliData.get(0).get("updateCloseDate"));

		// Click on outside or navigate to other page
		sugar.forecasts.worksheet.getControl("currentForecastUser").click(); 

		// Verify that the Expected closed date set to its previous value
		closedDateCtrl.assertEquals(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}