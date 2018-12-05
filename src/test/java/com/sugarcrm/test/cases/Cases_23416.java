
package com.sugarcrm.test.cases;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23416 extends SugarTest {
	StandardSubpanel noteSubpanel, callSubpanel, taskSubpanel;
	ArrayList<Record> myCallRecs, myTaskRecs, myNoteRecs;

	public void setup() throws Exception {
		myCallRecs = new ArrayList<Record>();
		myTaskRecs = new ArrayList<Record>();
		myNoteRecs = new ArrayList<Record>();
		sugar().cases.api.create();

		// Create two record of Calls, Task & Notes
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		CallRecord myCall1 = (CallRecord) sugar().calls.api.create();
		CallRecord myCall2 = (CallRecord) sugar().calls.api.create(fs);
		myCallRecs.add(myCall1);
		myCallRecs.add(myCall2);

		fs.clear();
		fs.put("subject", testName+"_task");
		TaskRecord myTask1 = (TaskRecord) sugar().tasks.api.create();
		TaskRecord myTask2 = (TaskRecord) sugar().tasks.api.create(fs);
		myTaskRecs.add(myTask1);
		myTaskRecs.add(myTask2);

		fs.clear();
		fs.put("subject", testName+"_note");
		NoteRecord myNote1 = (NoteRecord) sugar().notes.api.create();
		NoteRecord myNote2 = (NoteRecord) sugar().notes.api.create(fs);
		myNoteRecs.add(myNote1);
		myNoteRecs.add(myNote2);

		// Login as Admin
		sugar().login();

		// Link cases with Calls, Task & Notes in sub-panel
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		callSubpanel = sugar().cases.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		taskSubpanel = sugar().cases.recordView.subpanels.get(sugar().tasks.moduleNamePlural);
		noteSubpanel = sugar().cases.recordView.subpanels.get(sugar().notes.moduleNamePlural);

		callSubpanel.linkExistingRecords(myCallRecs);
		taskSubpanel.linkExistingRecords(myTaskRecs);
		noteSubpanel.linkExistingRecords(myNoteRecs);
	}

	/**
	 * Sort List_Verify that activities in "Activities" sub-panels (such as Calls, Meetings, Tasks and Notes)
	 * in "Case" detail view can be sorted by column titles
	 * @throws Exception
	 */
	@Test
	public void Cases_23416_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Calls
		// Click on cases detailView > Calls Sub-Panel > header column title
		callSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1424 -Make StandardSubpanel.verify() verify specified value is in correct column.
		// Verify that calls are sorted according to the column title in "Calls" sub-panel.
		callSubpanel.getDetailField(1, "name").assertContains(testName, true);

		// Click on cases detailView > Calls Sub-Panel > header column title
		callSubpanel.sortBy("headerName",true);
		VoodooUtils.waitForReady();

		// Verify that calls are sorted according to the column title in "Calls" sub-panel.
		callSubpanel.getDetailField(1, "name").assertContains(sugar().calls.getDefaultData().get("name"), true);

		// Tasks
		// Click on cases detailView > Task Sub-Panel > header column title
		taskSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1443 -Incorrect CSS value being returned for subPanel fields.
		// Verify that Tasks are sorted according to the column title in "Tasks" sub-panel.
		new VoodooControl("a", "css", "[data-voodoo-name='"+sugar().tasks.moduleNamePlural+"'] tbody tr:nth-of-type(1) .fld_name.list a")
							.assertContains(testName, true);

		taskSubpanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();

		// Verify that Tasks are sorted according to the column title in "Tasks" sub-panel.
		new VoodooControl("a", "css", "[data-voodoo-name='"+sugar().tasks.moduleNamePlural+"'] tbody tr:nth-of-type(1) .fld_name.list a")
							.assertContains(sugar().tasks.getDefaultData().get("subject"), true);

		// Notes
		// Click on cases detailView > Notes Sub-Panel > header column title
		noteSubpanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();

		// Verify that Notes are sorted according to the column title in "Notes" sub-panel.
		new VoodooControl("a", "css", "[data-voodoo-name='"+sugar().notes.moduleNamePlural+"'] tbody tr:nth-of-type(1) .fld_name.list a")
							.assertContains(testName, true);

		noteSubpanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();

		// Verify that Notes are sorted according to the column title in "Notes" sub-panel.
		new VoodooControl("a", "css", "[data-voodoo-name='"+sugar().notes.moduleNamePlural+"'] tbody tr:nth-of-type(1) .fld_name.list a")
							.assertContains(sugar().notes.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
