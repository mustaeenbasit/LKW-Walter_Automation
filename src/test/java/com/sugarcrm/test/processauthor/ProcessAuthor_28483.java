package com.sugarcrm.test.processauthor;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_28483 extends SugarTest {
	DataSource accountRecords;

	public void setup() throws Exception {
		accountRecords = testData.get(testName);
		sugar().accounts.api.create(accountRecords);
		sugar().login();
	}

	/**
	 * Verify a process definition that will be executed every time while doing mass update in list view
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_28483_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String processDefName = String.format("%s%s", testName, accountRecords.get(1).get("name"));
		// Importing process Definition file
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + processDefName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify that Process Definition has been Enabled
		sugar().processDefinitions.listView.verifyField(1, "status", accountRecords.get(0).get("name"));

		// Navigating to Accounts List View
		sugar().accounts.navToListView();

		// Performing Mass Update on Account Records
		sugar().accounts.listView.toggleSelectAll();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(accountRecords.get(3).get("name"));
		sugar().accounts.massUpdate.getControl("massUpdateValue02").set(accountRecords.get(4).get("name"));
		sugar().accounts.massUpdate.update();

		// Navigating to Process Management
		sugar().navbar.selectMenuItem(sugar().processes, "processManagement");

		// TODO: VOOD-1698
		// Counting Rows in Process Management list view
		int recordCount = 0;
		while (new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single:nth-child(" + (recordCount+1) + ")").queryExists()) {
			if(new VoodooControl("div", "css", "[data-voodoo-name='casesList-list'] .single:nth-child(" + (recordCount+1) + 
					") td:nth-child(2) div").queryContains(processDefName, true))
				recordCount++;
			else
				break;
		}

		// Verify Total records listed in Process Management are equivalent to 5
		Assert.assertTrue("Process count is not equal to 5", recordCount==accountRecords.size());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}