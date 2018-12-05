package com.sugarcrm.test.contacts;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23568 extends SugarTest {
	DataSource bugsData = new DataSource();
	int bugsRecordsNo = 0;
	StandardSubpanel bugsSubPanel;
	
	public void setup() throws Exception {
		bugsData = testData.get(testName);
		sugar().contacts.api.create();
		
		// Create 3 bugs with specific data values
		ArrayList<Record> bugsRecords = sugar().bugs.api.create(bugsData);
		bugsRecordsNo = bugsData.size();
		
		// Login
		sugar().login();
		
		// Enable Bugs module and subpanel as well
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
		
		// Navigating to bugs module
		sugar().bugs.navToListView();
		
		// Sorting bugs on list view in Ascending order
		sugar().bugs.listView.sortBy("headerName", true);
		sugar().bugs.listView.clickRecord(1);

		// Assign bugs to specific users and accounts
 		// TODO: VOOD-444
		for (int i = 0 ; i <= (bugsRecordsNo - 1) ; i++) {
			sugar().bugs.recordView.edit();
			sugar().bugs.recordView.getEditField("relAssignedTo").set(bugsData.get(i).get("relAssignedTo"));
			sugar().bugs.recordView.save();
			sugar().bugs.recordView.gotoNextRecord();
		}
		
		// Navigating to Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		bugsSubPanel = sugar().contacts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		
		// Linking the above bugs with the Contact Record
		bugsSubPanel.linkExistingRecords(bugsRecords);
	}

	/** 
	 * Verify that bugs related to the contact can be sorted by column titles in "BUGS" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23568_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sorting in descending order w.r.t Subject 
		bugsSubPanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in descending order w.r.t "Subject" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "name").assertEquals(bugsData.get(bugsRecordsNo - i).get("name"), true);
		
		// Sorting in ascending order w.r.t Subject
		bugsSubPanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in ascending order w.r.t "Subject" column
		for (int i = 0 ; i <= (bugsRecordsNo - 1) ; i++)
			bugsSubPanel.getDetailField(i+1, "name").assertEquals(bugsData.get(i).get("name"), true);
		
		VoodooControl colStatus = new VoodooControl("th", "css", ".orderBystatus");
		
		// Sorting in descending order w.r.t Status
		bugsSubPanel.sortBy("headerStatus", false);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Status" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "status").assertEquals(bugsData.get(bugsRecordsNo - i).get("status"), true);

		// Sorting in ascending order w.r.t Status 
		bugsSubPanel.sortBy("headerStatus", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Status" column
		for (int i = 0 ; i <= (bugsRecordsNo - 1) ; i++)
			bugsSubPanel.getDetailField(i+1, "status").assertEquals(bugsData.get(i).get("status"), true);
		
		///VoodooControl colType = new VoodooControl("th", "css", ".orderBytype");
		
		// Sorting in descending order w.r.t Type
		bugsSubPanel.sortBy("headerType", false);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in descending order w.r.t "Type" column
		for (int i = 0 ; i <= (bugsRecordsNo - 1) ; i++)
			bugsSubPanel.getDetailField(i+1, "type").assertEquals(bugsData.get(i).get("type"), true);

		// Sorting in ascending order w.r.t Type
		bugsSubPanel.sortBy("headerType", true);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in ascending order w.r.t "Type" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "type").assertEquals(bugsData.get(bugsRecordsNo - i).get("type"), true);

		// Sorting in descending order w.r.t Priority
		bugsSubPanel.sortBy("headerPriority", false);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in descending order w.r.t "Priority" column
		for (int i = 0 ; i <= (bugsRecordsNo - 1) ; i++)
			bugsSubPanel.getDetailField(i+1, "priority").assertEquals(bugsData.get(i).get("priority"), true);

		// Sorting in ascending order w.r.t Priority
		bugsSubPanel.sortBy("headerPriority", true);
		VoodooUtils.waitForReady();

		// Verify that records are sorted in ascending order w.r.t "Priority" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "priority").assertEquals(bugsData.get(bugsRecordsNo - i).get("priority"), true);

		// Sorting in descending order w.r.t Assigned To user
		bugsSubPanel.sortBy("headerAssignedusername", false);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in descending order w.r.t "Assigned User" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "relAssignedTo").assertEquals(bugsData.get(bugsRecordsNo - i).get("relAssignedTo"), true);

		// Sorting in ascending order w.r.t Assigned User 
		bugsSubPanel.sortBy("headerAssignedusername", true);
		VoodooUtils.waitForReady();
		
		// Verify that records are sorted in ascending order w.r.t "Assigned User" column
		for (int i = 1 ; i <= bugsRecordsNo ; i++)
			bugsSubPanel.getDetailField(i, "relAssignedTo").assertEquals(bugsData.get(i - 1).get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}