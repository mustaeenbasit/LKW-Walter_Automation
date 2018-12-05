package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22976 extends SugarTest {
	DataSource contactsData;
	StandardSubpanel contactSubpanel;
	
	public void setup() throws Exception {
		AccountRecord accountRecord = (AccountRecord) sugar().accounts.api.create();
		
		// Create contacts related to the Account
		contactsData = testData.get(testName);
		ArrayList<Record> contactRecords = sugar().contacts.api.create(contactsData);
		
		// Login as valid user
		sugar().login();
		
		// Go to account detail view.
		accountRecord.navToRecord();

		// Linking 6 contact records so as to have more than one page of contact records in subpanel list view
		contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecords(contactRecords);
	}

	/**
	 * Verify that corresponding contact records' list view is displayed after clicking the pagination control link on "CONTACTS" sub-panel of an account record detail view.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22976_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify there are 5 contacts in the subpanel
		for(int i=1; i<=5; i++)
			new VoodooControl("tr", "css", "div.layout_Contacts tr.single:nth-child(" + i + ")").assertExists(true);
		
		VoodooControl moreLink = contactSubpanel.getControl("moreLink");
		
		// Verify 'more Contacts' link is displayed
		moreLink.assertExists(true);
		
		// Click "More contacts" link in the Contacts subpanel
		moreLink.click();
		VoodooUtils.waitForReady();
		
		// Verify all contacts are shown
		for(int i=0, j=6; i<contactsData.size(); i++,j--)
			contactSubpanel.verify(j, contactsData.get(i), true);
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}