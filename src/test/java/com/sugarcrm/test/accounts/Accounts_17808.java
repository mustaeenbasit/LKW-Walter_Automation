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

public class Accounts_17808 extends SugarTest {
	protected AccountRecord myAccount;
	protected ArrayList<Record> contacts;
	protected VoodooControl container;

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
	 * Verify clearing of search criteria from search field after changing filter - record view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17808_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-468 need replace these controls after lib file is done
		myAccount.navToRecord();
		container = new VoodooSelect("a", "css", "div.related-filter a");
		container.set(sugar().contacts.moduleNamePlural);
		sugar().alerts.waitForLoadingExpiration();
		VoodooControl searchFilter = new VoodooControl("input", "css", "div.filter-view.search input.search-name");
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		FieldSet fs = new FieldSet();
		
		// Search and verify first contact
		searchFilter.set(contacts.get(0).getRecordIdentifier());
		sugar().alerts.waitForLoadingExpiration();
		fs.put("name", contacts.get(0).getRecordIdentifier());
		contactSubpanel.verify(1, fs, true);
		
		// Search and verify second contact
		searchFilter.set(contacts.get(1).getRecordIdentifier());
		sugar().alerts.waitForLoadingExpiration();
		fs.put("name", contacts.get(1).getRecordIdentifier());
		contactSubpanel.verify(1, fs, true);
		
		// VOOD-468 & TR-5661
		// Once this TR-5661 is resolved remove Line#81,#82 and uncomment Line#75,#76,#77,#78,#79,#80
		VoodooControl filterDropdown = new VoodooControl("a", "css","span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type");
		filterDropdown.click();
		new VoodooControl("a", "xpath", "//div[@id='select2-drop']//li[5]").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that two contacts record are displayed and the text entered in the search field is cleared out
		// searchFilter.assertContains(contacts.get(1).getRecordIdentifier(), false);
		// fs.put("name", contacts.get(1).getRecordIdentifier());
		// contactSubpanel.verify(1, fs, true);
		// fs.put("name", contacts.get(0).getRecordIdentifier());
		// contactSubpanel.verify(2, fs, true);
		fs.put("name", contacts.get(1).getRecordIdentifier());
		contactSubpanel.verify(1, fs, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}