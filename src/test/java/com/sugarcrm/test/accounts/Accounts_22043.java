package com.sugarcrm.test.accounts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22043 extends SugarTest {
	DataSource ds = new DataSource();
	ArrayList<Record> contactDataList = new ArrayList<Record>();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();
		contactDataList = (ArrayList<Record>)sugar().contacts.api.create(ds);
		sugar().login();
	}

	/**
	 * User sort subpanel column of detail view and leave then back same page after refresh whole site 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22043_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Accounts" module. 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactsSubPanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);

		// link contacts records in contacts subpanel
		contactsSubPanel.linkExistingRecords(contactDataList);
		// Click column titles respectively on "CONTACTS" sub-panel and sort the 
		// records according to the column titles Desc
		contactsSubPanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		contactsSubPanel.verify(1, ds.get(2), true);
		contactsSubPanel.verify(2, ds.get(1), true);
		contactsSubPanel.verify(3, ds.get(0), true);

		// Leave the Account page then go back after refresh whole site
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		VoodooUtils.refresh();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();

		// Verify that Sort status is retained as previous
		contactsSubPanel.verify(1, ds.get(2), true);
		contactsSubPanel.verify(2, ds.get(1), true);
		contactsSubPanel.verify(3, ds.get(0), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}