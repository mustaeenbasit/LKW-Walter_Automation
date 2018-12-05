package com.sugarcrm.test.contacts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.util.ArrayList;

@Features(revenueLineItem = false)
public class Contacts_23564 extends SugarTest {
	DataSource opportunityRecords, accountRecords;
	ArrayList<Record> myopportunities;
	StandardSubpanel opportunitySubpanel;

	public void setup() throws Exception {
		accountRecords = testData.get(testName+"_accountData");
		opportunityRecords = testData.get(testName);

		sugar().accounts.api.create(accountRecords);
		sugar().contacts.api.create();
		sugar().login();

		// TODO: VOOD-444
		myopportunities = sugar().opportunities.create(opportunityRecords);

		// Navigate to Contacts module and relate opportunities records in subpanel
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		opportunitySubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitySubpanel.linkExistingRecords(myopportunities);
		opportunitySubpanel.scrollIntoViewIfNeeded(false);
	}

	/**
	 * Sort Opportunities_Verify that opportunities related to the contact can be sorted  by column
	 * titles in "OPPORTUNITIES" sub-panel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23564_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click column titles respectively on opportunities sub-panel and verify that
		// records are sorted according to the column titles

		opportunitySubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
		opportunitySubpanel.verify(1, opportunityRecords.get(0), true);
		opportunitySubpanel.verify(2, opportunityRecords.get(1), true);
		opportunitySubpanel.verify(3, opportunityRecords.get(2), true);

		opportunitySubpanel.sortBy("headerAccountname", false);
		VoodooUtils.waitForReady();
		opportunitySubpanel.verify(1, opportunityRecords.get(0), true);
		opportunitySubpanel.verify(2, opportunityRecords.get(1), true);
		opportunitySubpanel.verify(3, opportunityRecords.get(2), true);

		opportunitySubpanel.sortBy("headerDateclosed", false);
		VoodooUtils.waitForReady();
		opportunitySubpanel.verify(1, opportunityRecords.get(2), true);
		opportunitySubpanel.verify(2, opportunityRecords.get(1), true);
		opportunitySubpanel.verify(3, opportunityRecords.get(0), true);

		opportunitySubpanel.sortBy("headerAmount", false);
		VoodooUtils.waitForReady();
		opportunitySubpanel.verify(1, opportunityRecords.get(2), true);
		opportunitySubpanel.verify(2, opportunityRecords.get(0), true);
		opportunitySubpanel.verify(3, opportunityRecords.get(1), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
