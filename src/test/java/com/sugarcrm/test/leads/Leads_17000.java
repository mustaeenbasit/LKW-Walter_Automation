package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17000 extends SugarTest {		
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Test Case 17000: Verify the white space in phone number is counted to determine dup lead record
	 * @throws Exception
	 */
	@Test
	public void Leads_17000_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");				

		FieldSet leadsFS = testData.get(testName).get(0);

		// Create 2nd new lead record, enter almost same number as in 1st record in Office Phone field, but add/delete one or multiple white space it the number, e.g. middle.
		sugar().leads.create(leadsFS);

		// Verify a new record is saved successfully in list view
		String fullNameStr = String.format("%s %s", leadsFS.get("firstName"), leadsFS.get("lastName"));
		sugar().leads.listView.verifyField(1, "fullName",	fullNameStr);

		// Verify that detail view is showing the saved record correctly
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.getDetailField("fullName").assertContains(fullNameStr, true);
		sugar().leads.recordView.getDetailField("phoneWork").assertContains(leadsFS.get("phoneWork"), true);

		// Verify that in listview, compare the Office Phone numbers between the 2 records and correctly with space or without space among the numbers
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "phoneWork", leadsFS.get("phoneWork"));
		sugar().leads.listView.verifyField(2, "phoneWork", sugar().leads.getDefaultData().get("phoneWork"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}