package com.sugarcrm.test.meetings;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Meetings_21076 extends SugarTest {
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * View meeting_Verify that the list for each sub-panel in the "Meeting Detail View" page can be sorted.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21076_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource notesData = testData.get(testName);
		
		// TODO: VOOD-444
		ArrayList<Record> notes = sugar().notes.create(notesData);
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);

		StandardSubpanel notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.linkExistingRecords(notes);

		// Click on header Title "Subject" to sort by Descending
		notesSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// Verify in notes subpanel records are sorted by Subject in Descending order.
		notesSubpanel.getDetailField(1,"subject").assertContains(notesData.get(0).get("subject"),true);
		notesSubpanel.getDetailField(2,"subject").assertContains(notesData.get(2).get("subject"),true);
		notesSubpanel.getDetailField(3,"subject").assertContains(notesData.get(1).get("subject"),true);

		// Click on header Title "Subject" to sort by Ascending
		notesSubpanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();

		// Verify in notes subpanel records are sorted by Subject in Ascending order.
		notesSubpanel.getDetailField(1,"subject").assertContains(notesData.get(1).get("subject"),true);
		notesSubpanel.getDetailField(2,"subject").assertContains(notesData.get(2).get("subject"),true);
		notesSubpanel.getDetailField(3,"subject").assertContains(notesData.get(0).get("subject"),true);

		// Click on header Title "Assigned User" to sort by Descending
		notesSubpanel.sortBy("headerAssignedusername", false);
		VoodooUtils.waitForReady();

		// TODO: VOOD-609
		VoodooControl record1 = new VoodooControl("a", "css", ".flex-list-view-content .single:nth-child(1) td:nth-child(5)");
		VoodooControl record2 = new VoodooControl("a", "css", ".flex-list-view-content .single:nth-child(2) td:nth-child(5)");
		VoodooControl record3 = new VoodooControl("a", "css", ".flex-list-view-content .single:nth-child(3) td:nth-child(5)");
		
		// Verify in notes subpanel records are sorted by Assigned User in Descending order.
		record1.assertContains(notesData.get(1).get("relAssignedTo"),true);
		record2.assertContains(notesData.get(0).get("relAssignedTo"),true);
		record3.assertContains(notesData.get(2).get("relAssignedTo"),true);

		// Click on header Title "Assigned User" to sort by Ascending
		notesSubpanel.sortBy("headerAssignedusername", true);
		VoodooUtils.waitForReady();
		record1.assertContains(notesData.get(2).get("relAssignedTo"),true);
		record2.assertContains(notesData.get(0).get("relAssignedTo"),true);
		record3.assertContains(notesData.get(1).get("relAssignedTo"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}