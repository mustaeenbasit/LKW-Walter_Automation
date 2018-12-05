package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29482 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the Process definition name get updated in approval form record view for BWC module
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29482_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Definition-> Create a Process Definition.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "createProcessDefinition");

		// Import Process definition => Target module Quotes => Start > new records only. Activity -> Users -> Static Assignment -> Assigned to current user.
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Trigger the event: Create a new Quote.
		sugar().quotes.create();

		// Go to Processes -> Show Process.
		sugar().navbar.selectMenuItem(sugar().processes, "viewProcesses");
		
		// Verify that the new Process name called "ProcessAuthor_29482" is shown in title.
		sugar().processes.listView.verifyField(1, "pr_definition", testName);
		
		// Go back to Process Definition list view -> click row action to select edit
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "viewProcessDefinition");
		sugar().processDefinitions.listView.editRecord(1);
		
		// Update the Process Definition name from "ProcessAuthor_29482" to "update for ProcessAuthor_29482" -> Click "Save".
		FieldSet customFS = testData.get(testName).get(0);
		sugar().processDefinitions.listView.getEditField(1, "name").set(customFS.get("updateTitle")+" "+testName);
		sugar().processDefinitions.listView.saveRecord(1);

		// Go back to Processes -> Show Process.
		sugar().navbar.selectMenuItem(sugar().processes, "viewProcesses");
		
		// Verify the Process Definition name get updated to "update for ProcessAuthor_29482" in the form.
		sugar().processes.listView.verifyField(1, "pr_definition", customFS.get("updateTitle")+" "+testName);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}