package com.sugarcrm.test.tasks;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_26203 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();	
	}

	/**
	 * TC 26203: Verify the action menu is correct for tasks in edit and record view
	 * @throws Exception
	 */
	@Test
	public void Tasks_26203_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Tasks and clicking PrimaryDropdown
		sugar.navbar.navToModule(sugar.tasks.moduleNamePlural);
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(sugar.tasks.getDefaultData().get("subject"));
		sugar.tasks.createDrawer.getEditField("status").set(sugar.tasks.getDefaultData().get("status"));

		// Verify the 'Save' and 'Cancel' button on Create Drawer and save the record
		sugar.tasks.createDrawer.getControl("cancelButton").assertVisible(true);
		sugar.tasks.createDrawer.getControl("saveButton").assertVisible(true);
		sugar.tasks.createDrawer.save();

		// Navigate to the created Task record
		sugar.tasks.listView.clickRecord(1);

		// Verify PrimaryDropdown following options present: Copy, Delete
		sugar.tasks.recordView.openPrimaryButtonDropdown();
		sugar.tasks.recordView.getControl("copyButton").assertVisible(true);
		sugar.tasks.recordView.getControl("deleteButton").assertVisible(true);

		// Verify following options present: Share, Close and Create New
		// TODO: VOOD-695 and VOOD-607
		new VoodooControl("a","css", ".fld_share.detail a").assertVisible(true);
		new VoodooControl("a","css", ".fld_record-close-new.detail a").assertVisible(true);
		new VoodooControl("a","css", ".fld_record-close.detail a").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}