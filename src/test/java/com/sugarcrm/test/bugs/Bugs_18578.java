package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Bugs_18578 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * Create Bug_Verify that a new bug is created with all fields filled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Bugs_18578_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to "Bugs" module.Click "Report Bug" link in navigation bar. 
		sugar.navbar.selectMenuItem(sugar.bugs, "createBug");

		// Fill all the fields. 
		sugar.bugs.createDrawer.getEditField("name").set(sugar.bugs.getDefaultData().get("name"));
		sugar.bugs.createDrawer.getEditField("description").set(sugar.bugs.getDefaultData().get("description"));
		sugar.bugs.createDrawer.showMore();
		sugar.bugs.createDrawer.getEditField("priority").set(sugar.bugs.getDefaultData().get("priority"));
		sugar.bugs.createDrawer.getEditField("type").set(sugar.bugs.getDefaultData().get("type"));
		sugar.bugs.createDrawer.getEditField("status").set(sugar.bugs.getDefaultData().get("status"));
		sugar.bugs.createDrawer.getEditField("source").set(fs.get("source"));
		sugar.bugs.createDrawer.getEditField("category").set(sugar.accounts.moduleNamePlural);
		sugar.bugs.createDrawer.getEditField("resolution").set(fs.get("resolution"));
		sugar.bugs.createDrawer.getEditField("work_log").set(sugar.bugs.getDefaultData().get("work_log"));
		sugar.bugs.createDrawer.getEditField("relAssignedTo").set(sugar.bugs.getDefaultData().get("relAssignedTo"));
		sugar.bugs.createDrawer.getEditField("relTeam").set(sugar.bugs.getDefaultData().get("relTeam"));

		// save the record
		sugar.bugs.createDrawer.save();

		// open the record
		sugar.bugs.listView.clickRecord(1);
		sugar.bugs.recordView.showMore();
		// Verify that  All the fields are displayed correctly in the detail view.
		sugar.bugs.recordView.getDetailField("name").assertContains(sugar.bugs.getDefaultData().get("name"), true);
		sugar.bugs.recordView.getDetailField("description").assertContains(sugar.bugs.getDefaultData().get("description"), true);
		sugar.bugs.recordView.getDetailField("priority").assertContains(sugar.bugs.getDefaultData().get("priority"), true);
		sugar.bugs.recordView.getDetailField("type").assertContains(sugar.bugs.getDefaultData().get("type"), true);
		sugar.bugs.recordView.getDetailField("status").assertContains(sugar.bugs.getDefaultData().get("status"), true);
		sugar.bugs.recordView.getDetailField("source").assertContains(fs.get("source"), true);
		sugar.bugs.recordView.getDetailField("category").assertContains(sugar.accounts.moduleNamePlural, true);
		sugar.bugs.recordView.getDetailField("resolution").assertContains(fs.get("resolution"), true);
		sugar.bugs.recordView.getDetailField("work_log").assertContains(sugar.bugs.getDefaultData().get("work_log"), true);
		sugar.bugs.recordView.getDetailField("relAssignedTo").assertContains(sugar.bugs.getDefaultData().get("relAssignedTo"), true);
		sugar.bugs.recordView.getDetailField("relTeam").assertContains(sugar.bugs.getDefaultData().get("relTeam"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}