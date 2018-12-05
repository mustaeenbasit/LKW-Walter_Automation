package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.modules.BugsModule;
import com.sugarcrm.sugar.records.BugRecord;

public class Bugs_17331 extends SugarTest {
	BugRecord myBug;
	DataSource releases;

	public void setup() throws Exception {
		sugar.login();
		myBug = (BugRecord) sugar.bugs.api.create();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);

		// Create the Releases
		releases = testData.get("Bugs_17331");
		BugsModule.createReleases(releases);
	}

	@Test
	// verify the user can enter the found in and fixed in releases fields
	public void Bugs_17331_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("found_in_release", releases.get(0).get("name"));
		newData.put("fixed_in_release", releases.get(1).get("name"));

		// Edit the bug using the UI.
		myBug.edit(newData);

		// Verify the bug was edited.
		myBug.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
