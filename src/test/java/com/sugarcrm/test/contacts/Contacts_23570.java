package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23570 extends SugarTest {
	DataSource contactRecords;
	StandardSubpanel directReportSubpanel;
	ArrayList<Record> myContacts;

	public void setup() throws Exception {
		contactRecords = testData.get(testName);
		sugar().login();

		// Create Contact by entering their email address
		// TODO: VOOD-444
		myContacts = sugar().contacts.create(contactRecords);

		// Navigate to Contacts module and relate contacts records in "DIRECT REPORTES" sub-panel
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		directReportSubpanel = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReportSubpanel.linkExistingRecords(myContacts);

	}

	/**
	 * Sort Direct Reports_Verify that contacts to be directly reported to the related contact can be sorted
	 * by column titles in "DIRECT REPORTES" sub-panel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23570_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click column titles respectively on "DIRECT REPORTES" sub-panel and verify that
		// records are sorted according to the column titles
		directReportSubpanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		directReportSubpanel.verify(1, contactRecords.get(0), true);
		directReportSubpanel.verify(2, contactRecords.get(1), true);
		directReportSubpanel.verify(3, contactRecords.get(2), true);

		directReportSubpanel.sortBy("headerEmail", false);
		VoodooUtils.waitForReady();
		directReportSubpanel.verify(1, contactRecords.get(0), true);
		directReportSubpanel.verify(2, contactRecords.get(1), true);
		directReportSubpanel.verify(3, contactRecords.get(2), true);

		directReportSubpanel.sortBy("headerPhonework", false);
		VoodooUtils.waitForReady();
		directReportSubpanel.verify(1, contactRecords.get(0), true);
		directReportSubpanel.verify(2, contactRecords.get(1), true);
		directReportSubpanel.verify(3, contactRecords.get(2), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
