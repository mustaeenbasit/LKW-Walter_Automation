package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24270 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}

	/**
	 * Detail View Opportunity_Verify that account detail view can be displayed by clicking the "Account Name" column 
	 * of opportunity record in "Opportunities" list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24270_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunities Listview
		sugar().opportunities.navToListView();
		
		// Click account name of an opportunity record in "Opportunity" list view
		sugar().opportunities.listView.getDetailField(1, "relAccountName").click();
		
		// Verify that account record view can be displayed by clicking the "Account Name" column of opportunity record in "Opportunities" list
		sugar().accounts.recordView.getDetailField("name").assertContains(sugar().opportunities.getDefaultData().get("relAccountName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}