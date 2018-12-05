package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18481 extends SugarTest {
	FieldSet bugsRecord;
	BugRecord myBug;

	public void setup() throws Exception {
		sugar.login();

		bugsRecord = testData.get("Bugs_18481").get(0);
		myBug = (BugRecord) sugar.bugs.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	@Test
	// Verify that the in line edit works for Bugs List View
	public void Bugs_18481_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the bug using the UI.
		// TODO : This functionality will be replaced by Story VOOD-439-Include
		// List View edit fields in Sugarfield
		sugar.bugs.navToListView();

		sugar.bugs.listView.editRecord(1);

		new VoodooControl("input", "css", ".fld_name.edit input").set(bugsRecord.get("name"));

		sugar.bugs.listView.saveRecord(1);

		// Verify the Saved record has been changed correctly
		new VoodooControl("span", "css", ".fld_name.list div").assertEquals((bugsRecord.get("name")), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
