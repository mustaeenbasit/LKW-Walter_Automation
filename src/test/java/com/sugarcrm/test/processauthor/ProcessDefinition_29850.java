package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessDefinition_29850 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		
		// Import Process Definition (In Start event, select new records only, In action-> right click -> select change field, In the settings of action -> Assigned to Current user and Name MyTestPD)
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify that "Assigned to" field should not be displayed blank
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessDefinition_29850_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new account and assign to qauser
		FieldSet customFS = testData.get(testName).get(0);
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.getEditField("relAssignedTo").assertContains(customFS.get("relAssignedTo"), true);
		sugar().accounts.createDrawer.save();

		// Verify that the Account record should be created with name MyTestPD and Assigned to field should not be displayed blank.
		sugar().accounts.listView.verifyField(1, "name", customFS.get("verifyAccountNameText"));
		sugar().accounts.listView.verifyField(1, "relAssignedTo", customFS.get("relAssignedTo"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}