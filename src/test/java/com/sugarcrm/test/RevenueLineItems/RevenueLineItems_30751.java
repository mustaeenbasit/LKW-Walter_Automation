package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30751 extends SugarTest {

	public void setup() throws Exception {
		// Creating 2 RLI records
		DataSource rliData = testData.get(testName);
		sugar().revLineItems.api.create(rliData);
		sugar().login();
	}

	/**
	 *  [ RLI ] Verify that Forecast field is not available in the Merge when forecast is not configured
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30751_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Go to RLI module > Select All > OpenActionDropdown
		sugar().revLineItems.navToListView();		
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();

		// Click 'Merge'
		// TODO: VOOD-689: Need to add a lib support for the "Merge" menu item to the dropdown action in the list view
		VoodooControl mergeCtrl = new VoodooControl("a", "css", ".fld_merge_button.list");
		mergeCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 'Forecast' field is not present in Merge Record Drawer before forecast is configured
		// TODO: VOOD-681: Create a Lib for Merge Duplicates
		// TODO: VOOD-999: Need a unique, consistent, and does not get translated when the language is changed (Valid testability hooks) 
		VoodooControl forecastCtrl = new VoodooControl("label", "css", "label[title='Forecast']");
		forecastCtrl.assertVisible(false);
		VoodooControl cancelCtrl = new VoodooControl("span", "css", "[data-voodoo-name='cancel_button'] a:not(.hide)");
		cancelCtrl.click();

		// Configure Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to Admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666: Not able to proceed after 'sugar.forecasts.setup.saveSettings();' as it gives 'Page was not ready within 120000ms' error
		sugar().navbar.navToAdminTools();

		//  Go to RLI module > Select All > OpenActionDropdown
		sugar().revLineItems.navToListView();		
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();

		// Click 'Merge'
		mergeCtrl.click();
		VoodooUtils.waitForReady();

		// Verify 'Forecast' field is present in Merge Record Drawer after forecast is configured
		forecastCtrl.assertVisible(true);
		cancelCtrl.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}