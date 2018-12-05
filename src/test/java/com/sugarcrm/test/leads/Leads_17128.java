package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17128 extends SugarTest {
	DataSource customData;

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().login();
	}

	/**
	 * "Reset to Original" will bring back to dup list and no changes to existing record in Lead
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_17128_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(customData.get(0).get("lastName"));
		sugar().leads.createDrawer.save();
		// Verify A new record with last name=Part is created.
		sugar().leads.listView.verifyField(1, "fullName",customData.get(0).get("lastName") );

		// Create another new lead record with last name as Partner
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(customData.get(1).get("lastName"));
		sugar().leads.createDrawer.save();
		// Verify a new record with last name=Partner is created.
		sugar().leads.listView.verifyField(1, "fullName",customData.get(1).get("lastName") );

		// Create 3rd new lead record with last name as p, rest fields leave blank.  Save it. 
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(customData.get(2).get("lastName"));
		sugar().leads.createDrawer.save();
		// Verify a Dup list displays 2 records in the Dup list.  
		sugar().leads.createDrawer.getControl("duplicateCount").assertContains(customData.get(0).get("Count"), true);

		// In Dup list, in the column Actions of 2nd record, click on "Select and edit".
		sugar().leads.createDrawer.selectAndEditDuplicate(2);
		// Verify Editview of the 2nd record appears.
		sugar().leads.recordView.getEditField("lastName").assertContains(customData.get(1).get("lastName"), true);

		// Enter some new values into a few fields: name, title, account, website
		sugar().leads.recordView.getEditField("lastName").set(testName);
		sugar().leads.recordView.getEditField("title").set(testName);
		sugar().leads.recordView.getEditField("accountName").set(testName);

		// Click on "Reset to Original".
		sugar().leads.createDrawer.resetToOriginal();
		// Verify Go back to editview of 3rd record
		sugar().leads.recordView.getEditField("lastName").assertContains(customData.get(2).get("lastName"), true);

		// Click Cancel
		sugar().leads.createDrawer.cancel();
		// Verify No 3rd record is created.  Only see 2 records as before. 
		Assert.assertTrue("Only 2 records as before", sugar().leads.listView.countRows() == (customData.size()-1));
		// Verify 2nd record is not changed
		sugar().leads.listView.setSearchString(customData.get(1).get("lastName"));
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.getDetailField("fullName").assertContains(customData.get(1).get("lastName"), true);
		// TODO: VOOD-710
		new VoodooControl("span", "css", ".record-edit-link-wrapper [data-name='title']").assertContains("", true);
		new VoodooControl("span", "css", ".record-edit-link-wrapper [data-name='account_name']").assertContains("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}