package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.BugsModule;
import com.sugarcrm.test.SugarTest;

public class Bugs_18577 extends SugarTest {
	DataSource ds;
	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();
		// Enable bugs module 
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		// Navigate to Admin -> Releases -> Create some Records for fixed in version and found in version. 
		BugsModule.createReleases(ds);
	}

	/**
	 * Create Bug_Verify that the "Fix in Release" information is displayed as created in bug list.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18577_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Report Bug" button on navigation shortcuts.
		sugar.navbar.selectMenuItem(sugar.bugs, "createBug");
		// Fill all the mandatory fields and select an item in "Fix in Release" dropdown list such as "7.6.0.0".
		sugar.bugs.createDrawer.getEditField("name").set(testName);
		sugar.bugs.createDrawer.showMore();
		VoodooControl descriptionCtrl = sugar.bugs.createDrawer.getEditField("description");
		descriptionCtrl.set(sugar.bugs.defaultData.get("description"));
		VoodooControl foundReleaseCtrl = sugar.bugs.createDrawer.getEditField("found_in_release");
		foundReleaseCtrl.set(ds.get(0).get("name"));
		VoodooControl fixReleaseCtrl = sugar.bugs.createDrawer.getEditField("fixed_in_release");
		fixReleaseCtrl.set(ds.get(0).get("name"));
		// Click "Save"	
		sugar.bugs.createDrawer.save();
		
		sugar.bugs.navToListView();
		// Select the created bug 
		sugar.bugs.listView.clickRecord(1);
		// Copy record 
		sugar.bugs.recordView.copy();
		// Change "Fix in Release" dropdown value to "7.7.0.0".
		fixReleaseCtrl.set(ds.get(1).get("name"));
		// Click "Save"	
		sugar.bugs.createDrawer.save();
		// Verify that The "Fix in Release" information is displayed as filled.
		fixReleaseCtrl.assertContains(ds.get(1).get("name"), true);
		// Verify that The detail of the duplicated bug is displayed.
		sugar.bugs.createDrawer.getControl("duplicateCount").assertContains("1 duplicates found.", true);
		// Ignore duplicate and save
		sugar.bugs.createDrawer.ignoreDuplicateAndSave();
		// Verify that The "Fix in Release" information is displayed 7.7.0.0
		sugar.bugs.recordView.getDetailField("fixed_in_release").assertContains(ds.get(1).get("name"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}