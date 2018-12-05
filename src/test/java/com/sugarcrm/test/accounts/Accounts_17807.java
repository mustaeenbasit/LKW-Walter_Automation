package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_17807 extends SugarTest {
	AccountRecord myAccount;
	ArrayList<Record> contacts;
	VoodooControl container;

	public void setup() throws Exception {
		// create account and contacts
		myAccount = (AccountRecord) sugar().accounts.api.create();
		String relAcc = myAccount.getRecordIdentifier();		
		contacts = sugar().contacts.api.create(testData.get(testName));
		sugar().login();
		
		// relate contacts to account
		for (int i = 0; i < contacts.size(); i++) {
			sugar().contacts.navToListView();
			sugar().contacts.listView.clickRecord(2);
			sugar().contacts.recordView.edit();
			sugar().contacts.recordView.getEditField("relAccountName").set(relAcc);
			sugar().alerts.getAlert().confirmAlert();
			sugar().contacts.recordView.save();
		}
	}

	/**
	 * Verify dynamic filtering of related records data - record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17807_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-468 need replace these controls after lib file is done
		myAccount.navToRecord();
		container = new VoodooSelect("a", "css", "div.related-filter a");
		container.set(sugar().contacts.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that only the Contacts subpanel is displayed on the data view
		sugar().accounts.recordView.subpanels.get(sugar().accounts.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().leads.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural).assertAttribute("class", "hide");
		sugar().accounts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).assertAttribute("class", "hide");

		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		FieldSet fs = new FieldSet();
		
		// Verify that two contacts record are displayed
		fs.put("name", contacts.get(1).getRecordIdentifier());
		contactSubpanel.verify(1, fs, true);
		fs.put("name", contacts.get(0).getRecordIdentifier());
		contactSubpanel.verify(2, fs, true);
		
		// TODO: VOOD-468
		VoodooControl searchFilter = new VoodooControl("input", "css", "div.filter-view.search input.search-name");
		String filter1 = contacts.get(0).get("firstName");
		searchFilter.set(filter1);
		sugar().alerts.waitForLoadingExpiration();
		
		// As you enter matching text in the search field, the related contacts are being filtered to match search criteria dynamically.
		fs.put("name", contacts.get(0).getRecordIdentifier());
		contactSubpanel.verify(1, fs, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}