package com.sugarcrm.test.cases;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.BugRecord;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23398 extends SugarTest {
	BugRecord myBugRecord;
	StandardSubpanel bugsSubPanel;

	public void setup() throws Exception {
		sugar().cases.api.create();
		myBugRecord = (BugRecord) sugar().bugs.api.create();
		sugar().login();

		// Enable bugs to show in sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Link a bug with cases record
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		bugsSubPanel = sugar().cases.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubPanel.scrollIntoView();
		bugsSubPanel.linkExistingRecord(myBugRecord);
	}

	/**
	 * Edit Bug Report_Verify that modification of bug report for case can be canceled in "Bug Tracker" detail view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23398_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		bugsSubPanel.scrollIntoView();

		// Verify that "bugs" record exist in bugs > sub-panel
		FieldSet fs = new FieldSet();
		fs.put("name", myBugRecord.getRecordIdentifier());
		bugsSubPanel.verify(1, fs, true);

		// Click the subject of an existing bug in "Bugs" sub-panel.
		bugsSubPanel.clickRecord(1);

		// Verify that RecordView of this related bug is displayed.
		VoodooControl detailNameField = sugar().bugs.recordView.getDetailField("name");
		detailNameField.assertEquals(myBugRecord.getRecordIdentifier(), true);

		// Edit the information of the bug.
		sugar().bugs.recordView.edit();
		sugar().bugs.recordView.getEditField("name").set(testName);

		// Click "Cancel" button in "Bug Tracker" / "Bugs" detail view.
		sugar().bugs.recordView.cancel();

		// Verify that the information of the bug for the selected case is not changed.
		detailNameField.assertEquals(myBugRecord.getRecordIdentifier(), true);
		detailNameField.assertEquals(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
