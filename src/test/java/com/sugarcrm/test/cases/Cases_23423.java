package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_23423 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Icons and Links_Verify that "Create Case" link in navigation shortcuts sub-panel is available.
	 */
	@Test
	public void Cases_23423_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Cases" tab on top navigation bar.
		sugar().cases.navToListView();
		
		// Click "Create Case" icon in navigation  panel
		sugar().navbar.selectMenuItem(sugar().cases, "createCase");

		// Verify existence of some controls of Cases's createDrawer
		sugar().cases.createDrawer.getEditField("name").assertExists(true);
		sugar().cases.createDrawer.getEditField("relAccountName").assertExists(true);
		sugar().cases.createDrawer.getEditField("priority").assertExists(true);
		sugar().cases.createDrawer.getControl("cancelButton").assertExists(true);
		sugar().cases.createDrawer.getControl("saveButton").assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
