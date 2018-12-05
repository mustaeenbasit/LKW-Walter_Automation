package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21844 extends SugarTest {
	FieldSet leadsRecord;

	public void setup() throws Exception {
		leadsRecord = testData.get("Leads_21844").get(0);
		sugar().login();
	}

	/**
	 * Test Case 21844: Create Lead_Verify that lead is not created when using cancel function.
	 */
	@Test
	public void Leads_21844_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the Leads module
		sugar().leads.navToListView();
		// Go to CreateDrawer
		sugar().leads.listView.create();
		// Set required field
		sugar().leads.createDrawer.getEditField("firstName").set(leadsRecord.get("first_name"));
		sugar().leads.createDrawer.getEditField("lastName").set(leadsRecord.get("last_name"));
		// Click Cancel
		sugar().leads.createDrawer.cancel();

		// Verify you are not on CreateDrawer page
		sugar().leads.createDrawer.getEditField("lastName").assertVisible(false);
		// Verify you are on ListView and record is not created
		sugar().leads.listView.setSearchString(leadsRecord.get("last_name"));
		sugar().leads.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
