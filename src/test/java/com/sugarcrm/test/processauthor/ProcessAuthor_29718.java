package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29718 extends SugarTest {
	public void setup() throws Exception {
		// Need to have atleast one Process definition created (Disabled by default)
		sugar().processDefinitions.api.create();

		// Login as Admin/valid User
		sugar().login();
	}
	/**
	 * Verify that Process status should not change on click of in-line Cancel. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29718_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource processDefinitionsData = testData.get(testName);

		// Go to Process Definition
		sugar().processDefinitions.navToListView();

		for(int i = 0; i < processDefinitionsData.size(); i++) {
			// Click in-line action drop-down of any of the record either Enabled or Disabled
			sugar().processDefinitions.listView.openRowActionDropdown(1);

			// Click Enable/Disable link and click Confirm button on the pop-up message
			sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
			sugar().alerts.getWarning().assertContains(processDefinitionsData.get(i).get("warningMessage"), true);
			sugar().alerts.getWarning().confirmAlert();

			// User should see status getting changed accordingly i.e. Enabled/Disabled
			VoodooControl statusListViewCtrl = sugar().processDefinitions.listView.getEditField(1, "status");
			statusListViewCtrl.assertEquals(processDefinitionsData.get(i).get("pdStatus"), true);

			// Now click to in-line Edit the same record in the list view
			sugar().processDefinitions.listView.editRecord(1);

			// Click Cancel link without editing/updating anything
			sugar().processDefinitions.listView.cancelRecord(1);

			// User should see the same status as before
			statusListViewCtrl.assertEquals(processDefinitionsData.get(i).get("pdStatus"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}