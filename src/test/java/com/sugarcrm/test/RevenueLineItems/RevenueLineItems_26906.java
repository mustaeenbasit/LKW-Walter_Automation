package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26906 extends SugarTest {
	DataSource rliDataFS;

	public void setup() throws Exception {
		rliDataFS = testData.get(testName);
		sugar().accounts.api.create();

		// 3 RLI records with sales stage not equal to Closed Won/Lost are created
		FieldSet rliRecordFS = new FieldSet();
		for(int i = 0; i < rliDataFS.size(); i++) {
			rliRecordFS.put("name", rliDataFS.get(i).get("name"));
			rliRecordFS.put("salesStage", rliDataFS.get(i).get("salesStage"));
			sugar().revLineItems.api.create(rliRecordFS);
		}

		// Login to sugar 
		sugar().login();

		// Opportunity record is created linked to the account
		sugar().opportunities.create();

		// 3 RLI records with sales stage not equal to Closed Won/Lost are linked to the opportunity (Mass update the RLI records with the Opportunity Name)
		// Navigate to RLI listview
		sugar().revLineItems.navToListView();

		// Select all RLI records
		sugar().revLineItems.listView.toggleSelectAll();
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();

		// Select "Opportunity Name" and set opportunity created 
		sugar().revLineItems.massUpdate.getControl("massUpdateField02").set(rliDataFS.get(0).get("opportunityName"));
		sugar().revLineItems.massUpdate.getControl("massUpdateValue02").set(sugar().opportunities.getDefaultData().get("name"));

		// Click on update button
		sugar().revLineItems.massUpdate.update();
	}

	/**
	 * Verify that Revenue Line Item can be successfully deleted from RLI list view 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26906_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Check several RLI records
		sugar().revLineItems.listView.toggleSelectAll();

		// Click on action drop down list and select "Delete"
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.delete();

		// Verify that a delete confirmation dialog box pop up that allow user to confirm to delete those records
		sugar().alerts.getWarning().assertVisible(true);

		// Click Confirm
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify that the records are deleted successfully
		sugar().revLineItems.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}