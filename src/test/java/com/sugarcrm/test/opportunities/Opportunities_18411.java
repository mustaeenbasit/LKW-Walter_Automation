package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_18411 extends SugarTest {
	DataSource oppRecords;
	
	public void setup() throws Exception {
		oppRecords = testData.get(testName+"_oppRecords");
		sugar().accounts.api.create();
		sugar().login();
		
		// Create 3 Opportunity records with status- Closed Won, Closed Lost and Prospecting
		sugar().opportunities.create(oppRecords);
	}

	/**
	 * Verify closed status Opportunity record is not deleted in list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_18411_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet warning = testData.get(testName).get(0);

		// Go to Opportunity module list view
		sugar().opportunities.navToListView();
		
		// Select records, including a closed won/lost opportunity record
		sugar().opportunities.listView.toggleSelectAll();
		
		// From the list view action menu, select Delete
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		
		// Verify warning message "One or more of the selected records has a status of Closed Won or Closed Lost and cannot be deleted."
		sugar().alerts.getWarning().assertContains(warning.get("warning"), true);
		
		// Close the warning
		sugar().alerts.closeAllWarning();
		
		// Verify that only non Closed Won/Closed Lost Opportunities are checked
		Assert.assertTrue("Opportunity with stage Prospecting is unchecked", sugar().opportunities.listView.getControl("checkbox01").isChecked());
		Assert.assertFalse("Opportunity with stage Closed Lost is checked", sugar().opportunities.listView.getControl("checkbox02").isChecked());
		Assert.assertFalse("Opportunity with stage Closed Win is checked", sugar().opportunities.listView.getControl("checkbox03").isChecked());
		sugar().opportunities.listView.verifyField(1, "name", oppRecords.get(2).get("name"));
		
		// Select Delete again
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		
		// Click Confirm
		sugar().alerts.confirmAllWarning();
		
		// Verify that delete is successful for non Closed Won/Closed Lost Opportunity and corresponding RLI
		sugar().opportunities.listView.verifyField(1, "name", oppRecords.get(1).get("name"));
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.verifyField(1, "relOpportunityName", oppRecords.get(1).get("name"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}