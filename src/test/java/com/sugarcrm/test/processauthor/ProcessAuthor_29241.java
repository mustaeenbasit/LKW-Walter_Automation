package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29241 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Approve/Reject form should NOT be displayed to the another user when assigned to current user
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29241_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Process Definition-> Create a Process Definition.
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "createProcessDefinition");

		// Import Process definition => Target module Leads => Start > new records only. Activity -> Users -> Static Assignment -> Assigned to current user.
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Trigger the event: Create a new Leads.
		sugar().leads.create();

		// Go to Processes -> Show Process.
		sugar().navbar.selectMenuItem(sugar().processes, "viewProcesses");

		// Verify that the new Process name called "ProcessAuthor_29241" is shown in title.
		sugar().processes.listView.verifyField(1, "pr_definition", testName);
		
		// Go to showProcess detail view and copy current URL and stored it to a String
		sugar().processes.myProcessesListView.showProcess(1);
		String currentURL = VoodooUtils.getUrl();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Processes -> Show Process.
		sugar().navbar.selectMenuItem(sugar().processes, "viewProcesses");
		
		// Append/Paste the URL copied
		VoodooUtils.go(currentURL);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1706
		// Verify that the data not available and page doesn't exist message should displayed. ( i.e Approve/Reject form should NOT be displayed to the Regular user)
		FieldSet customFS = testData.get(testName).get(0);
		new VoodooControl("a", "css", "[name='approve_button']").assertExists(false);
		new VoodooControl("a", "css", "[name='reject_button']").assertExists(false);
		new VoodooControl("h1", "css", "#content .error-message h1").assertContains(customFS.get("dataNotAvailableMsg"), true);
		new VoodooControl("h1", "css", "#content .error-message p").assertContains(customFS.get("pageNotFoundMsg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}