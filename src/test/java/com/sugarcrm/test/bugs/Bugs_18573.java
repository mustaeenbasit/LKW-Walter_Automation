package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_18573 extends SugarTest {
	FieldSet bugRecord;
	BugRecord myBug;

	public void setup() throws Exception {
		bugRecord = testData.get("Bugs_18573").get(0);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		myBug = (BugRecord) sugar.bugs.api.create();
		myBug.navToRecord();
	}

	/**
	 * Verify that the edited records are displayed in the detail view.
	 */
	@Test
	public void Bugs_18573_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs record view
		myBug.edit(bugRecord);

		// Verify the modified records is displayed in the detail view
		myBug.verify(bugRecord);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}