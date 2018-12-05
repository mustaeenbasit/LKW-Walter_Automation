package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19492 extends SugarTest {
	ArrayList<Record> contactRecords;

	public void setup() throws Exception {
		DataSource myContacts = testData.get(testName);
		sugar.targetlists.api.create();
		contactRecords = sugar.contacts.api.create(myContacts);
		sugar.login();
	}

	/**
	 * Verify that the "Next" pagination function in the "Contacts" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19492_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);

		// Linking Contact Records to TargetList
		StandardSubpanel contactsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecords(contactRecords);

		// Verifying total No. of Rows in Contacts Subpanel is equal to 5.
		int contactRows = contactsSubpanel.countRows();
		Assert.assertTrue("Total No. of Rows not Equal to 5", contactRows == 5);

		// Viewing more Contacts Records in the the Target Subpanel
		contactsSubpanel.showMore();
		VoodooUtils.waitForReady();
		contactRows = contactsSubpanel.countRows();
		Assert.assertTrue("Total No. of Rows not Equal to 7", contactRows == 7);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}