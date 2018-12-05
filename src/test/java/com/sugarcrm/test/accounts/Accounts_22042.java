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

public class Accounts_22042 extends SugarTest {
	DataSource ds = new DataSource();
	ArrayList<Record> contactRecords = new ArrayList<Record>();
	AccountRecord accountRecord;

	public void setup() throws Exception {
		ds = testData.get(testName);
		accountRecord = (AccountRecord)sugar().accounts.api.create();
		contactRecords = sugar().contacts.api.create(ds);
		sugar().login();
		sugar().accounts.navToListView();
	}

	/**
	 * User sort subpanel column of detail view and leave then back same page directly
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_22042_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		accountRecord.navToRecord();

		StandardSubpanel contactsSubpanel = (StandardSubpanel)sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.scrollIntoViewIfNeeded(false);
		contactsSubpanel.linkExistingRecords(contactRecords);
		// Click Name column on "CONTACTS" sub-panel and sort the
		// records according to the titles
		contactsSubpanel.sortBy("headerFullname", true);
		VoodooUtils.waitForReady();
		contactsSubpanel.verify(1, ds.get(0), true);
		contactsSubpanel.verify(2, ds.get(1), true);

		// Leave that page then go back directly
		sugar().contacts.navToListView();
		accountRecord.navToRecord();

		// Verify that Pre-sorted order be reserved.
		contactsSubpanel.verify(1, ds.get(0), true);
		contactsSubpanel.verify(2, ds.get(1), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}