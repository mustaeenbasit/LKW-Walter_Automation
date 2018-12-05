package com.sugarcrm.test.bugs;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.BugsModule;
import com.sugarcrm.sugar.records.BugRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Bugs_18596 extends SugarTest {
	BugRecord ascBug, descBug;
	DataSource releases, bugsData;
	FieldSet releaseName = new FieldSet();

	public void setup() throws Exception {
		sugar.login();

		// Create the Releases
		releases = testData.get(testName+"_rel");
		BugsModule.createReleases(releases);

		// Create two bugs with different releases
		bugsData = testData.get(testName);
		ascBug = (BugRecord) sugar.bugs.api.create(bugsData.get(0));
		descBug = (BugRecord) sugar.bugs.api.create(bugsData.get(1));

		// Enable the Bugs module
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);

		// Update created bugs - set a release
		releaseName.put("fixed_in_release",releases.get(0).get("name"));
		ascBug.edit(releaseName);
		releaseName.put("fixed_in_release",releases.get(1).get("name"));
		descBug.edit(releaseName);
	}
	/**
	 * Test Case 18596: Sort Bug_Verify that bugs can be sorted by "Fixed in Release" condition.
	*/
	@Test
	public void Bugs_18596_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl firHeader = new VoodooControl("span", "css", ".orderByfixed_in_release_name span");

		// Go to the Bugs list view
		sugar.bugs.navToListView();

		// Sort by "Fixed in Release" descending
		// TODO: VOOD-680
		// sugar.bugs.listView.sortBy("headerfixed_in_release_name", false);
		firHeader.waitForVisible();
		firHeader.click();
		sugar.alerts.waitForLoadingExpiration();
		// and verify
		sugar.bugs.listView.verifyField(1, "name", descBug.get("name"));

		// Sort the same field ascending
		// TODO: VOOD-680
		// sugar.bugs.listView.sortBy("headerfixed_in_release_name", true);
		firHeader.click();
		sugar.alerts.waitForLoadingExpiration();
		// and verify
		sugar.bugs.listView.verifyField(1, "name", ascBug.get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}