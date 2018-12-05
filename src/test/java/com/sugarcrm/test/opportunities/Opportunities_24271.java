package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24271 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Detail View Opportunity_Verify that opportunity detail view is displayed by clicking the "Opportunity" column 
	 * of opportunity record in "Opportunities" list.
	 *  
	 */
	@Test
	public void Opportunities_24271_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities listview
		sugar().opportunities.navToListView();
		
		// Click the name of an opportunity record in "Opportunity" list view
		sugar().opportunities.listView.getDetailField(1, "name").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// The detail information of the opportunity is displayed
		sugar().opportunities.recordView.getDetailField("name").assertContains(sugar().opportunities.getDefaultData().get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}