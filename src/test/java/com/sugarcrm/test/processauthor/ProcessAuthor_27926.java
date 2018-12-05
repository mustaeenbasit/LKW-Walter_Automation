package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_27926 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify duplicate check applies for process definition
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_27926_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Process Definition List View
		sugar().navbar.navToModule(sugar().processDefinitions.moduleNamePlural);

		// Create Process Definition
		for(int i = 1; i<=2 ;i++) {
			sugar().processDefinitions.listView.create();
			sugar().processDefinitions.createDrawer.getEditField("name").set(sugar().processDefinitions.getDefaultData().get("name"));
			// Added waitForReady() method to wait for the target module to load
			VoodooUtils.waitForReady();
			sugar().processDefinitions.createDrawer.openPrimaryButtonDropdown();
			sugar().processDefinitions.createDrawer.save();

			// Verify that Process Definition is successfully created
			if(i == 1){
				sugar().processDefinitions.listView.verifyField(i,"name", sugar().processDefinitions.getDefaultData().get("name"));
			} else {

				// Verify Ignore Duplicate & Save Button is Visible.
				sugar().processDefinitions.createDrawer.getControl("ignoreDuplicateAndSaveButton").assertVisible(true);

				// Click Ignore Duplicate and Save button.
				sugar().processDefinitions.createDrawer.ignoreDuplicateAndSave();
			}
		}

		// Asserting that the Process Definition with same Name is saved.
		Assert.assertTrue("Number of rows did not equal two.", sugar().processDefinitions.listView.countRows() == 2);
		sugar().processDefinitions.listView.verifyField(1,"name", sugar().processDefinitions.getDefaultData().get("name"));
		sugar().processDefinitions.listView.verifyField(2,"name", sugar().processDefinitions.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}