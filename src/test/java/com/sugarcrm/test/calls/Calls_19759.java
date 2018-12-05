package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_19759 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Check that Call could be created from quick create (+)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19759_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open QuickCreate and click "Create Call"
		sugar.navbar.quickCreateAction(sugar.calls.moduleNamePlural);
		
		// Verify cancel, save button on drawer and fill required fields
		sugar.calls.createDrawer.getControl("saveButton").assertVisible(true);
		sugar.calls.createDrawer.getControl("cancelButton").assertVisible(true);
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.getDefaultData().get("name"));
		sugar.calls.createDrawer.save();

		// Verify call record is saved and display in listview
		sugar.calls.navToListView();
		sugar.calls.listView.verifyField(1, "name", sugar.calls.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}