package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29423 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify user is able to navigate to other modules after trying to delete a Process Definition with a Process in "IN PROGRESS" State
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29423_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Definition-> Create a Process Definition.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "createProcessDefinition");

		// Import Process definition => Target module Accounts => Activity Settings-> Users: Set Assignment Method as "Static Assignment" & Applies to "Current user" -> Save
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Now Navigate to Accounts Module and create a New account Record.
		sugar().accounts.create();

		// Now Navigate back to Process Definition and from Row Action Drop down-> Click Delete.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "viewProcessDefinition");
		sugar().processDefinitions.listView.deleteRecord(1);

		// Verify that the Warning message should be displayed Process Definition cannot be deleted as Process is in running state"
		FieldSet customFS = testData.get(testName).get(0);
		sugar().alerts.getWarning().assertEquals(customFS.get("errorMsg"), true);

		// Now Navigate to any module(for e.g. Contacts/Leads/Quotes) or try to Logout Application.
		sugar().contacts.navToListView();

		// User should be able to Navigate to any Module(for e.g Contacts/Leads/Quotes) and even able to logout from the application
		sugar().logout();
		sugar().loginScreen.assertExists(true);
		
		// Login as Admin
		sugar().login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}