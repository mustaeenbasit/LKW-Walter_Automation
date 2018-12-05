package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30759 extends SugarTest {
	DataSource customDS = new DataSource();
	
	public void setup() throws Exception {
		customDS = testData.get(testName);
		sugar().opportunities.api.create();

		// Create 2 RLI record with Sales stage: Closed Lost (Forecast excluded) & Sales stage: Negotiation/Review (Forecast included).
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
	 * Verify that Forecast field is set to "Excluded" in Closed Lost RLI after merge.
	 *  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30759_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to RLI list view and select both records and merge them
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.getControl("selectAllCheckbox").click();
		sugar().revLineItems.listView.openActionDropdown();
		
		// TODO: VOOD-721
		new VoodooControl("a", "css", ".list.fld_merge_button a").click();
		
		// While merging records, select Sales stage "Closed Lost" from the Secondary records.
		new VoodooControl("input", "css", ".row-div-cnt > div:nth-child(2) [name='copy_sales_stage']").click();
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='save_button']").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
		
		// Verify that the Value in Forecast field should be "Excluded" as Sales Stage of Merged RLI is Closed Lost.
		sugar().revLineItems.listView.getDetailField(1, "salesStage").assertContains(customDS.get(1).get("salesStage"), true);
		sugar().revLineItems.listView.getDetailField(1, "forecast").assertContains(customDS.get(1).get("forecast"), true);
		
		// Go to RLI record view of merged record and observe the value in "Forecast" field.
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		
		// Verify that the Values in the Forecast field should be Excluded.
		sugar().revLineItems.recordView.getEditField("forecast").assertContains(customDS.get(1).get("forecast"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}