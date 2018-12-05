package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24268 extends SugarTest {
	public void setup() throws Exception {
		// Create 3 Opportunity records
		for (int i = 0; i < 3; i++) {
			sugar().opportunities.api.create();
		}
		sugar().login();
	}

	/**
	 * Mass Update Opportunities_Verify that the selected opportunities can be deleted by using "Delete" option 
	 * available in the Actions dropdown of list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24268_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Opportunity List view
		sugar().opportunities.navToListView();
		
		// Select opportunities by checking the check box in the front of opportunity record in "Opportunities" list view
		sugar().opportunities.listView.checkRecord(1);
		sugar().opportunities.listView.checkRecord(2);
		sugar().opportunities.listView.checkRecord(3);
		
		// Click "Delete" button via the Actions link on list view
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.delete();
		
		// Click "Confirm" in warning pop up
		sugar().alerts.confirmAllWarning();
		
		// Verify that the selected opportunities are deleted
		sugar().opportunities.listView.assertIsEmpty();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}