package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_21621 extends SugarTest {
	DataSource contactsAndLeads = new DataSource();
	ArrayList<Record> contacts = new ArrayList<Record>();
	ArrayList<Record> leads = new ArrayList<Record>();

	public void setup() throws Exception {
		contactsAndLeads = testData.get(testName);
		contacts = sugar().contacts.api.create(contactsAndLeads);
		leads = sugar().leads.api.create(contactsAndLeads);
		sugar().login();

		VoodooControl contactEmailEditField = sugar().contacts.recordView.getEditField("emailAddress");
		VoodooControl leadsEmailEditField = sugar().leads.recordView.getEditField("emailAddress");
		int dataSize = contactsAndLeads.size();

		// TODO: VOOD-1282, VOOD-444
		// Navigate to the contact's record view and add an email address to it 
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		for(int i = dataSize-1; i >= 0; i--) {
			sugar().contacts.recordView.edit();
			contactEmailEditField.set(contactsAndLeads.get(i).get("emailAddress"));
			sugar().contacts.recordView.save();
			// move to next record
			if(i == dataSize-1)
				sugar().contacts.recordView.gotoNextRecord();
		}

		// Navigate to the lead's record view and add email address
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		for(int j = dataSize-1; j >= 0; j--) {
			sugar().leads.recordView.edit();
			leadsEmailEditField.set(contactsAndLeads.get(j).get("emailAddress"));
			sugar().leads.recordView.save();
			// move to next record
			if(j == dataSize-1)
				sugar().leads.recordView.gotoNextRecord();
		}
	}

	/**
	 * Search leads, contacts by email addresses in the global search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_21621_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: CB-252 - Need CB support to simulate input of any keyboard key i.e tab, esc etc.
		// Input an email address in the global search input box and hit Enter key
		String email1 = contactsAndLeads.get(0).get("emailAddress");
		sugar().navbar.getControl("globalSearch").set(email1 + '\uE007');
		VoodooUtils.waitForReady();

		String fullName = String.format("%s %s",contactsAndLeads.get(0).get("firstName"), contactsAndLeads.get(0).get("lastName"));
		VoodooControl contactSearchResult = sugar().globalSearch.getRow(contacts.get(0));
		VoodooControl leadSearchResult = sugar().globalSearch.getRow(leads.get(0));

		// Assert that those contact and lead records which email address is same with the input string
		contactSearchResult.assertContains(fullName, true);
		contactSearchResult.assertContains(email1, true);
		leadSearchResult.assertContains(fullName, true);
		leadSearchResult.assertContains(email1, true);

		// Assert that the other 2 records with different email address are not displayed
		sugar().globalSearch.getRow(contacts.get(1)).assertVisible(false);
		sugar().globalSearch.getRow(leads.get(1)).assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}