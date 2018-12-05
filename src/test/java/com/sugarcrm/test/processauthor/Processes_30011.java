package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Processes_30011 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that "Process Definition Name" is not saved as empty while saving it from canvas
	 * 
	 * @throws Exception
	 */
	@Test
	public void Processes_30011_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet warningMessageData = testData.get(testName).get(0);

		// Navigate to Process Definition
		sugar().processDefinitions.navToListView();

		// Create Process Definition -> Enter all Required Fields
		sugar().processDefinitions.listView.create();
		sugar().processDefinitions.createDrawer.showMore();
		sugar().processDefinitions.createDrawer.getEditField("name").set(testName);

		// Click Save & Design
		sugar().processDefinitions.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();
		sugar().alerts.getSuccess().closeAlert();

		// Define controls
		// TODO: VOOD-1539
		VoodooControl projectTitleCtrl = new VoodooControl("div", "id", "ProjectTitle");
		VoodooControl titleTextCtrl = new VoodooControl("input", "id", "txt-title");

		// From the top right corner of the canvas Edit the Process definition name as Blank and Save
		projectTitleCtrl.click();
		// TODO: CB-252, VOOD-1437
		titleTextCtrl.set("" + '\uE007');

		// Verify that 'Process Definition Name should not be saved as blank as it is required field', Warning message should be prompted
		sugar().alerts.getWarning().assertContains(warningMessageData.get("warningMessage"), true);

		// Fill Process definition name and save
		// TODO: CB-252, VOOD-1437
		titleTextCtrl.set(testName + '\uE007');
		projectTitleCtrl.assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}