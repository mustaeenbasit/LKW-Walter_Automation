package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30758 extends SugarTest {
	DataSource customDS = new DataSource();
	
	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().opportunities.api.create();

		// Create 2 RLI record with Sales stage: Closed Won (Forecast included) & Sales stage: Prospecting (Forecast excluded).
		sugar().revLineItems.api.create(customDS);
		sugar().login();

		// TODO: VOOD-444
		// Associate account and opportunity record with rli
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		for(int i = 0; i < customDS.size(); i++) {
			sugar().revLineItems.recordView.edit();
			sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
			sugar().revLineItems.recordView.save();
			sugar().revLineItems.recordView.gotoNextRecord();
		}
		
		// Enable default Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		VoodooUtils.waitForReady();
		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();
	}

	/**
	 * Verify that Forecast field is set to "Included" in Closed won RLI after merge.
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30758_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI list view and select both records and merge them
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.getControl("selectAllCheckbox").click();
		sugar().revLineItems.listView.openActionDropdown();
		
		// TODO: VOOD-721
		new VoodooControl("a", "css", ".list.fld_merge_button a").click();
		
		// Click on all radio buttons for the right record which is merging record.
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_sales_stage']").click();
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='save_button']").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
		
		// Verify that the Value in Forecast field should be "Included" as Sales Stage of Merged RLI is Closed Won.
		sugar().revLineItems.listView.getDetailField(1, "salesStage").assertContains(customDS.get(1).get("salesStage"), true);
		sugar().revLineItems.listView.getDetailField(1, "forecast").assertContains(customDS.get(1).get("forecast"), true);
		
		// Go to RLI record view of merged record and observe the value in "Forecast" field.
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("forecast").assertContains(customDS.get(1).get("forecast"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}