package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18482 extends SugarTest {
	FieldSet bugsRecord = new FieldSet();
	BugRecord myBug;

	public void setup() throws Exception {
		bugsRecord = testData.get("Bugs_18482").get(0);
		myBug = (BugRecord) sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	@Test
	// Verify the user can Cancel the inline editing from the record level
	// action drop down on List View
	public void Bugs_18482_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.bugs.navToListView();

		sugar.bugs.listView.editRecord(1);

		sugar().bugs.listView.getEditField(1,"name").set(bugsRecord.get("name"));

		sugar.bugs.listView.cancelRecord(1);

		// Assert the original default name value is present
		sugar().bugs.listView.getDetailField(1, "name").assertEquals((myBug.get("name")), true);
	}

	public void cleanup() throws Exception {}
}
