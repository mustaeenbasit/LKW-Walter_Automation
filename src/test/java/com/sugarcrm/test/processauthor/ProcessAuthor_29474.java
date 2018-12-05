package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29474 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Log-In as admin
		sugar().login();
	}

	/**
	 * Verify Warning Messages Should Be Displayed while try to disable a process definition with an active process running against it
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29474_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Importing Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new quote to trigger the process
		sugar().quotes.create();

		// Select the newly created process definition > Disable
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();

		// Verify Warning message should be displayed for the process definition 
		sugar().alerts.getWarning().assertVisible(true);

		// Verify warning text
		FieldSet customData = testData.get(testName).get(0);
		sugar().alerts.getWarning().assertContains(customData.get("warningText"), true);

		// Click Cancel
		sugar().alerts.getWarning().cancelAlert();

		// Verify it will return to the process definition list view
		// TODO: VOOD-1887, Once this VOOD is resolved below 
		// line should suffice for the list view visibility check. 
		// sugar().processDefinitions.listView.assertVisible(true);
		sugar().processDefinitions.listView.getDetailField(1, "name").assertEquals(testName, true);

		// Verify process definition is still enabled.
		sugar().processDefinitions.listView.getDetailField(1, "status").assertEquals(customData.get("status1"), true);

		// Click Confirm
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify process definition is disabled.
		sugar().processDefinitions.listView.getDetailField(1, "status").assertEquals(customData.get("status2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}