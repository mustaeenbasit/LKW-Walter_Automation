package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_19489 extends SugarTest {
	DataSource contactData = new DataSource();
	ArrayList<Record> contactRecords;

	public void setup() throws Exception {
		contactData = testData.get(testName);
		contactRecords = sugar().contacts.api.create(contactData);
		sugar().targetlists.api.create();
		sugar().login();
	}

	/**
	 * Verify that contact list can be sort by "Office Phone" as ASC in "Contacts" sub-panel on TargetLists
	 * @throws Exception
	 */
	@Test
	public void Contacts_19489_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to TargetLists record view
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// Adding records to Contacts subpanel of TargetList
		StandardSubpanel contactSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.scrollIntoViewIfNeeded(false);
		contactSubpanel.linkExistingRecords(contactRecords);

		// Click column titles "Office Phone" twice on "CONTACTS" sub-panel and sort the records in ASC order
		contactSubpanel.sortBy("headerPhonework", true);
		VoodooUtils.waitForReady();
		for (int i = 1; i <= contactData.size(); i++) {
			contactSubpanel.getDetailField(i, "phoneWork").assertEquals(contactData.get(i-1).get("phoneWork"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}