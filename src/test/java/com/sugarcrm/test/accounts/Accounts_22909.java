package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22909 extends SugarTest {
	DataSource contactRecords;
	ArrayList<Record> myContacts;
	StandardSubpanel contactsSubpanel;
	
	public void setup() throws Exception {
		contactRecords = testData.get(testName);
		sugar().accounts.api.create();
		myContacts = sugar().contacts.api.create(contactRecords);
		sugar().login();
		
		// Navigate to Accounts module and relate contacts records in subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		contactsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecords(myContacts);
	}

	/**
	 * Account Detail - Contacts sub-panel - Sort_Verify that contact records related to the accounts 
	 * can be sorted  by column titles on "CONTACTS" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22909_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click column titles respectively on "CONTACTS" sub-panel and verify that 
		// records are sorted according to the column titles
		contactsSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		contactsSubpanel.verify(1, contactRecords.get(2), true);
		contactsSubpanel.verify(2, contactRecords.get(1), true);
		contactsSubpanel.verify(3, contactRecords.get(0), true);
		
		contactsSubpanel.sortBy("headerPrimaryaddresscity", false);
		VoodooUtils.waitForReady();
		contactsSubpanel.verify(1, contactRecords.get(2), true);
		contactsSubpanel.verify(2, contactRecords.get(1), true);
		contactsSubpanel.verify(3, contactRecords.get(0), true);
		
		contactsSubpanel.sortBy("headerPrimaryaddressstate", false);
		VoodooUtils.waitForReady();
		contactsSubpanel.verify(1, contactRecords.get(2), true);
		contactsSubpanel.verify(2, contactRecords.get(1), true);
		contactsSubpanel.verify(3, contactRecords.get(0), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}