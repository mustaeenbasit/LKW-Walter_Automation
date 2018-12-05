package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29475 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Warning Messages Should Be Displayed while try to delete a process definition with an active process running against it
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29475_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Create a Process Definition.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "createProcessDefinition");

		// Import Process definition with Target module Contact with an activity element
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a contact record
		sugar().contacts.create();

		// Go to Process Definition and from Row Action Drop down-> Click Delete.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "viewProcessDefinition");
		sugar().processDefinitions.listView.deleteRecord(1);

		// Verify that a warning message will pop up: Warning You cannot delete this Process Definition because have pending processes running.
		FieldSet customFS = testData.get(testName).get(0);
		sugar().alerts.getWarning().assertEquals(customFS.get("warningMsg"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}