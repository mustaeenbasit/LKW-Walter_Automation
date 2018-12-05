package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18516 extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		myBug = (BugRecord) sugar.bugs.api.create();

		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	@Test
	// Verify the user can Cancel the inline Delete from the record level action
	// drop down on List View
	public void Bugs_18516_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO : This functionality will be replaced by Story VOOD-439-Include
		// List View edit fields in Sugarfield
		sugar.bugs.navToListView();

		sugar.bugs.listView.deleteRecord(1);

		sugar.bugs.listView.cancelDelete();

		// Assert the record is still present
		new VoodooControl("span", "css", ".fld_name.list div").assertEquals((myBug.get("name")), true);
	}

	public void cleanup() throws Exception {}
}
