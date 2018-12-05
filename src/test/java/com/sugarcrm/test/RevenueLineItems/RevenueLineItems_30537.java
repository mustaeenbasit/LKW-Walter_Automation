package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30537 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify that Forecast field is available in mass update only after Forecasts module is configured
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30537_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to RLI list view
		sugar().revLineItems.navToListView();

		// Check select box for one record and select "Mass Update" action
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").click();
		
		// Verifying that 'Forecasts' option is not yet enlisted in mass update fields
		// TODO: VOOD-1003
		VoodooControl availableOptions = new VoodooControl("ul", "css", "#select2-drop .select2-results");
		availableOptions.assertContains(sugar().forecasts.moduleNameSingular, false);
		
		// Clicking on first list item in order to shift control onto the page
		new VoodooControl("li", "css", "#select2-drop .select2-results li").click();
		sugar().revLineItems.massUpdate.cancelUpdate();
		
		// Go to Admin >> Forecasts and configure Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();
		
		// Navigate to RLI list view
		sugar().revLineItems.navToListView();

		// Check select box for one record and select "Mass Update" action
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").click();
		
		// Verifying that 'Forecasts' option is now enlisted in mass update fields
		availableOptions.assertContains(sugar().forecasts.moduleNameSingular, true );

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}