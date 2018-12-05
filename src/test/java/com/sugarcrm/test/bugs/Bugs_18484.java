package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18484 extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar.bugs.api.create();

		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * 	Verify the user can preview a record on list view
	 * @throws Exception
	 */
	@Test
	public void Bugs_18484_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();

		sugar.bugs.listView.previewRecord(1);

		sugar.previewPane.showMore();

		sugar.previewPane.getPreviewPaneField("name").assertEquals(myBug.get("name"), true);
		sugar.previewPane.getPreviewPaneField("description").assertEquals(myBug.get("description"), true);
		sugar.previewPane.getPreviewPaneField("priority").assertEquals(myBug.get("priority"), true);
		sugar.previewPane.getPreviewPaneField("type").assertEquals(myBug.get("type"), true);
		sugar.previewPane.getPreviewPaneField("status").assertEquals(myBug.get("status"), true);
		sugar.previewPane.getPreviewPaneField("work_log").assertEquals(myBug.get("work_log"), true);
		sugar.previewPane.getPreviewPaneField("relAssignedTo").assertEquals(myBug.get("relAssignedTo"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
