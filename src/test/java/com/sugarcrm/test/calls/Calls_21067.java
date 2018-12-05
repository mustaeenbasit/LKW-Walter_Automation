package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_21067 extends SugarTest {
	DataSource notesData = new DataSource();
	StandardSubpanel notesSubpanelCtrl;

	public void setup() throws Exception {
		notesData = testData.get(testName);
		ArrayList<Record> noteRecords = new ArrayList<Record>();

		// Create a Call record
		sugar().calls.api.create();

		// Login
		sugar().login();

		// Create Notes Record with different 'Assigned To' user
		// TODO: VOOD-444
		noteRecords.addAll(sugar().notes.create(notesData));

		// Link all Notes record to the Call record
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		notesSubpanelCtrl = sugar().calls.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanelCtrl.linkExistingRecords(noteRecords );
	}

	/**
	 * View call_Verify that the list for each sub-panel in the "Call Detail View" page can be sorted.
	 * @throws Exception
	 */
	@Test
	public void Calls_21067_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Call" navigation tab and click "View Calls" link on Navigation shortcuts
		sugar().navbar.selectMenuItem(sugar().calls, "view"+sugar().calls.moduleNamePlural);

		// Click any call subject link in "Call List" view to view the detail information
		sugar().calls.listView.clickRecord(1);

		// Sort the list by columns 'Subject' for Notes sub-panel
		notesSubpanelCtrl.sortBy("headerName", false);;
		VoodooUtils.waitForReady();

		// Verify that the list is sorted by the 'Subject' column for Notes sub-panel
		for(int i = 0; i < notesData.size(); i++) {
			notesSubpanelCtrl.getDetailField(i+1, "subject").assertEquals(notesData.get(i).get("subject"), true);
		}

		// Sort the list by columns 'Assigned To' for Notes sub-panel
		notesSubpanelCtrl.sortBy("headerAssignedusername", false);
		VoodooUtils.waitForReady();

		// Verify that the list is sorted by the 'Assigned To' column for Notes sub-panel
		for(int i = 0; i < notesData.size(); i++) {
			// TODO: VOOD-1380
			new VoodooControl("a", "css", ".flex-list-view-content tr:nth-child(" + (i+1) + ") .fld_assigned_user_name.list").assertEquals(notesData.get(i).get("relAssignedTo"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}