package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28482 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify a process definition that will be executed every time an existing record is updated
	 *
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28482_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource accountNames = testData.get(testName);
		String processDefName = String.format("%s%s", testName, accountNames.get(0).get("version"));

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + processDefName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that process definition has been Enabled
		sugar().processDefinitions.listView.verifyField(1, "status", accountNames.get(0).get("processDefinitionStatus"));

		// Update the same account record 3 times to trigger the Process thrice
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		VoodooControl accountNameEdit = sugar().accounts.listView.getEditField(1, "name");
		for (int i = 0; i < accountNames.size(); i++) {
			sugar().accounts.listView.editRecord(1);
			accountNameEdit.set(accountNames.get(i).get("name"));
			sugar().accounts.listView.saveRecord(1);
		}

		// Assert that exactly 3 processes are displayed in the Process Management list view
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// Assert that Row Count is 3
		Assert.assertTrue("Process count is not equal to 3", sugar().processes.listView.countRows() == accountNames.size());

		sugar().processes.listView.getDetailField(1, "rec_name").assertEquals(accountNames.get(2).get("name"), true);
		sugar().processes.listView.getDetailField(2, "rec_name").assertEquals(accountNames.get(1).get("name"), true);
		sugar().processes.listView.getDetailField(3, "rec_name").assertEquals(accountNames.get(0).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}