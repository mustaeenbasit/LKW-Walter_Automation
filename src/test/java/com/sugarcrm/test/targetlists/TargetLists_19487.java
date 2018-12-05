package com.sugarcrm.test.targetlists;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19487 extends SugarTest{
	DataSource contacts = new DataSource(); 
	TargetListRecord myTargetListRecord;
	ArrayList<Record> contactRecordsList = new ArrayList<Record>();

	public void setup() throws Exception {
		contacts = testData.get(testName);
		myTargetListRecord = (TargetListRecord)sugar().targetlists.api.create();
		contactRecordsList = sugar().contacts.api.create(contacts);
		sugar().login();
	}

	/**
	 * Target List - Contacts management_Verify that contact list can be sort by "Name" as ASC in "Contacts" sub-panel.
	 */
	@Test
	public void TargetLists_19487_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTargetListRecord.navToRecord();
		StandardSubpanel contactsSubPanel = (StandardSubpanel)sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		// link contact records to targetlist record
		contactsSubPanel.linkExistingRecords(contactRecordsList);

		// Sorting the Contacts in descending order in subpanel
		contactsSubPanel.sortBy("headerFullname", false);
		VoodooUtils.waitForReady();
		// Verifying that Contacts are visible in Contacts subpanel in sorted order
		for (int i = contacts.size() ; i < 0 ; i--){
			String fullName = "Mr. " + contacts.get(i).get("firstName") +" "+ contacts.get(i).get("lastName");
			// TODO: VOOD-1424: Make StandardSubpanel.verify() verify specified value is in correct column.
			contactsSubPanel.getDetailField(i+1, "fullName").assertEquals(fullName, true);
		}

		// Sorting the Contacts in ascending order in subpanel
		contactsSubPanel.sortBy("headerFullname", true);
		VoodooUtils.waitForReady();
		for (int i = 0 ; i < contacts.size() ; i++){
			String fullName = "Mr. " + contacts.get(i).get("firstName") +" "+ contacts.get(i).get("lastName");
			// TODO: VOOD-1424: Make StandardSubpanel.verify() verify specified value is in correct column.
			contactsSubPanel.getDetailField(i+1, "fullName").assertEquals(fullName, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
