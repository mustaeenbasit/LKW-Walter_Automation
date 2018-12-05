package com.sugarcrm.test.contacts;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23565 extends SugarTest {
	ContactRecord contactRecord;
	StandardSubpanel opportunitySubpanel;

	public void setup() throws Exception {
		DataSource ds = testData.get(testName);
		FieldSet itemsPerPage = testData.get(testName + "_1").get(0);

		// Create 1 contact record and 9 opportunity records
		contactRecord = (ContactRecord) sugar().contacts.api.create();

		// create opp records
		FieldSet data = new FieldSet();
		ArrayList<Record> linkRecords = new ArrayList<Record>();
		for (int i = 0; i < ds.size(); i++) {
			data.put("name", ds.get(i).get("oppName"));
			linkRecords.add(sugar().opportunities.api.create(data));
		}
		sugar().login();

		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerSubPanel", itemsPerPage.get("itemsPerPage"));
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);

		// Link all opportunity records to contact
		contactRecord.navToRecord();
		opportunitySubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitySubpanel.linkExistingRecords(linkRecords);
	}

	/**
	 * Verify opportunities records are listed per Subpanel items per page
	 * setting under Contacts > Opportunities subpanel
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_23565_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contactRecord.navToRecord();
		opportunitySubpanel.expandSubpanel();
		VoodooControl fourthRecord = new VoodooControl("tbody", "css", ".layout_Opportunities .flex-list-view tbody tr:nth-child(4)");
		VoodooControl seventhRecord = new VoodooControl("tbody", "css", ".layout_Opportunities .flex-list-view tbody tr:nth-child(7)");

		// Verify under opportunities subpanel, 3 records are displayed
		// TODO: VOOD-609 & VOOD-999
		new VoodooControl("tbody", "css", ".layout_Opportunities .flex-list-view tbody tr:nth-child(3)").assertExists(true);
		fourthRecord.assertExists(false);
		new VoodooControl("button", "css", "[data-action='show-more']").click();

		// Verify 3 more records are displayed
		fourthRecord.assertExists(true);
		seventhRecord.assertExists(false);
		new VoodooControl("button", "css", "[data-action='show-more']").click();

		// Verify 3 more records are displayed
		seventhRecord.assertExists(true);
		new VoodooControl("tbody", "css", ".layout_Opportunities .flex-list-view tbody tr:nth-child(10)").assertExists(false);

		// Verify link 'More opportunities' is not displayed anymore
		new VoodooControl("button", "css", "[data-action='show-more']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
