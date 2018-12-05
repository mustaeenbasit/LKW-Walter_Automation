package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21936 extends SugarTest {
	DataSource leadData, customData = new DataSource();
	FieldSet systemSettings = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName+"_customData");
		leadData = testData.get(testName);
		sugar().leads.api.create(leadData);

		sugar().login();
		// Set max no of list view items to 3 per page
		systemSettings.put("maxEntriesPerPage", customData.get(0).get("maxEntries"));
		sugar().admin.setSystemSettings(systemSettings);
	}

	/**
	 * Search Leads_Verify that values entered in the "Search" fields are not changed 
	 * after clicking "More..." link in the "Leads" list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_21936_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Leads
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);

		// Perform search function to get an filtered leads list that is more than one page.
		sugar().leads.listView.setSearchString(leadData.get(0).get("lastName"));

		// Click "More..." link in the bottom of a page
		sugar().leads.listView.showMore();

		// Asserting Record count in list view after clicking More link
		int count1 = sugar().leads.listView.countRows();
		Assert.assertTrue("No of records is not 6 after clicking More link", count1 == leadData.size()-4);

		// Verify value in the "Search" field is not changed
		VoodooControl searchFilterCtrl = sugar().leads.listView.getControl("searchFilter");
		searchFilterCtrl.assertEquals(leadData.get(0).get("lastName"), true);

		// Verify result is still filtered by search conditions.
		// i.e verifying the data in count1(i.e. 6) no of records.
		String fullName = sugar().leads.getDefaultData().get("salutation")+" "+leadData.get(0).get("lastName");
		for (int i = 0; i < count1; i++) {
			sugar().leads.listView.getDetailField(i+1, "fullName").assertEquals(fullName, true);
		}

		// Click "More..." link in the bottom of a page once again
		sugar().leads.listView.showMore();

		// Asserting Record count in list view after clicking More link second time
		int count2 = sugar().leads.listView.countRows();
		Assert.assertTrue("No of records is not 8 after clicking More link", count2 == leadData.size()-2);

		// Verify value in the "Search" field is not changed
		searchFilterCtrl.assertEquals(leadData.get(0).get("lastName"), true);

		// Verify result is still filtered by search conditions.
		// i.e verifying the data in count2(i.e 8) no of records.
		for (int i = 0; i < count2; i++) {
			sugar().leads.listView.getDetailField(i+1, "fullName").assertEquals(fullName, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}