package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_update extends SugarTest {
	BugRecord myBug;

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);

		myBug = (BugRecord) sugar.bugs.api.create();
	}

	@Test
	public void Bugs_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("description", "A changed Bug description");

		// Edit the bug using the UI.
		myBug.edit(newData);

		// Verify the bug was edited.
		myBug.verify();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
