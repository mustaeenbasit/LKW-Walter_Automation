package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23415 extends SugarTest {
	StandardSubpanel contactSubpanel;
	FieldSet customFS;
	ArrayList<Record> myContactRecords;

	public void setup() throws Exception {
		sugar().cases.api.create();

		// Two contact records create with different name
		myContactRecords = new ArrayList<Record>();
		myContactRecords.add(sugar().contacts.api.create());
		customFS = new FieldSet();
		customFS.put("lastName", testName);
		myContactRecords.add(sugar().contacts.api.create(customFS));
		sugar().login();

		// Link cases with contacts in "contacts" sub-panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		contactSubpanel = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecords(myContactRecords);
	}

	/**
	 * Sort List_Verify that contacts in "Contacts" sub-panel of "Case" detail view can be sorted by column titles.
	 * @throws Exception
	 */
	@Test
	public void Cases_23415_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contactSubpanel.scrollIntoView();
		boolean flag = false;
		for(Record record : myContactRecords) {
			// Click on cases detailView > Contacts Sub-Panel > header column title
			contactSubpanel.sortBy("headerFullname", flag);
			if (flag == false) flag = true;
			else
				flag = false;
			VoodooUtils.waitForReady();

			// Verify that contacts are sorted according to the column title in "Contacts" sub-panel.
			contactSubpanel.getDetailField(1, "fullName").assertContains(record.getRecordIdentifier(), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
