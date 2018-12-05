package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23566 extends SugarTest {
	DataSource casesData = new DataSource();
	int casesRecordsNo = 0;
	StandardSubpanel casesSubPanel;

	public void setup() throws Exception {
		casesData = testData.get(testName);
		sugar().contacts.api.create();

		// Create 3 cases with specific data values
		ArrayList<Record> casesRecords = sugar().cases.api.create(casesData);
		casesRecordsNo = casesData.size();

		// For creating 3 account records (for case records later to be sorted by)
		DataSource accountsData = new DataSource();

		for (int i = 0 ; i <= (casesRecordsNo - 1) ; i++) {
			FieldSet accountData = new FieldSet();
			accountData.put("name", casesData.get(i).get("relAccountName"));
			accountsData.add(i, accountData);
		}

		// Creating account records
		sugar().accounts.api.create(accountsData);
		accountsData.clear();

		// Login
		sugar().login();
		sugar().cases.navToListView();

		// Sorting cases on list view in Ascending order
		sugar().cases.listView.sortBy("headerName", true);
		sugar().cases.listView.clickRecord(1);

		// Assign Cases to specific users and accounts
		// TODO: VOOD-444
		for (int i = 0 ; i <= (casesRecordsNo - 1) ; i++) {
			sugar().cases.recordView.edit();
			sugar().cases.recordView.showMore();
			VoodooUtils.waitForReady();
			sugar().cases.recordView.getEditField("relAssignedTo").set(casesData.get(i).get("relAssignedTo"));
			sugar().cases.recordView.getEditField("relAccountName").set(casesData.get(i).get("relAccountName"));
			sugar().cases.recordView.save();
			sugar().cases.recordView.gotoNextRecord();
		}

		// Navigating to Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		casesSubPanel = sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural);

		// Linking the above cases with the Contact Record
		casesSubPanel.linkExistingRecords(casesRecords);
	}

	/** 
	 * Verify that cases related to the contact can be sorted by column titles in "CASES" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Contacts_23566_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sorting in descending order w.r.t Subject
		casesSubPanel.sortBy("headerName",false);
		VoodooUtils.waitForReady();
		// Verify that records are sorted in descending order w.r.t "Subject" column
		for (int i = 1 ; i <= casesRecordsNo ; i++)
			casesSubPanel.getDetailField(i, "name").assertEquals(casesData.get(casesRecordsNo - i).get("name"), true);

		// Sorting in ascending order w.r.t Subject 
		casesSubPanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Subject" column
		for (int i = 0 ; i <= (casesRecordsNo - 1) ; i++)
			casesSubPanel.getDetailField(i+1, "name").assertEquals(casesData.get(i).get("name"), true);

		// Sorting in descending order w.r.t Account Name
		casesSubPanel.sortBy("headerAccountname", false);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Account Name" column
		for (int i = 0 ; i <= (casesRecordsNo - 1) ; i++)
			casesSubPanel.getDetailField(i+1, "relAccountName").assertEquals(casesData.get(i).get("relAccountName"), true);

		// Sorting in ascending order w.r.t Account Name
		casesSubPanel.sortBy("headerAccountname", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Account Name" column
		for (int i = 1 ; i <= casesRecordsNo ; i++)
			casesSubPanel.getDetailField(i, "relAccountName").assertEquals(casesData.get(casesRecordsNo - i).get("relAccountName"), true);

		// Sorting in descending order w.r.t Status
		casesSubPanel.sortBy("headerStatus", false);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Status" column
		for (int i = 1 ; i <= casesRecordsNo ; i++)
			casesSubPanel.getDetailField(i, "status").assertEquals(casesData.get(casesRecordsNo - i).get("status"), true);

		// Sorting in ascending order w.r.t Status
		casesSubPanel.sortBy("headerStatus", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Status" column
		for (int i = 0 ; i <= (casesRecordsNo - 1) ; i++)
			casesSubPanel.getDetailField(i+1, "status").assertEquals(casesData.get(i).get("status"), true);

		// Sorting in descending order w.r.t Assigned To user
		casesSubPanel.sortBy("headerAssignedusername", false);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Assigned To user" column
		for (int i = 1 ; i <= casesRecordsNo ; i++)
			casesSubPanel.getDetailField(i, "relAssignedTo").assertEquals(casesData.get(casesRecordsNo - i).get("relAssignedTo"), true);

		// Sorting in ascending order w.r.t Assigned To user 
		casesSubPanel.sortBy("headerAssignedusername", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Assigned To user" column
		for (int i = 1 ; i <= casesRecordsNo ; i++)
			casesSubPanel.getDetailField(i, "relAssignedTo").assertEquals(casesData.get(i - 1).get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
