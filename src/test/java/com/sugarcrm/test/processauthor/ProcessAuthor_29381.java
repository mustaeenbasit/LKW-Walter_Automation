package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29381 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the related field should work in Show Process (Approve/Reject/Route form) for side modules
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29381_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Importing Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + customData.get("version"));
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new task to trigger the process
		sugar().tasks.create();

		// Go to My Process > Show Process
		sugar().processes.navToListView();
		sugar().processes.myProcessesListView.showProcess(1);

		// TODO: VOOD-1706
		// Click Edit 
		new VoodooControl("a", "css", "[name='edit_button']").click();

		// Show More
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more").click();

		// Edit the related field "Assigned to"
		VoodooSelect assignedToSelectCtrl = new VoodooSelect("div", "css", ".fld_assigned_user_name.edit span div");

		// Click "search and select"
		assignedToSelectCtrl.clickSearchForMore();

		FieldSet fs = sugar().users.getQAUser();
		UserRecord qauser = new UserRecord(fs);
		// Select a record from the list
		sugar().users.searchSelect.selectRecord(qauser);
		VoodooUtils.waitForReady();

		// Verify it will fill in the related field after selection.
		assignedToSelectCtrl.assertEquals(fs.get("userName"), true);

		// Cancel
		new VoodooControl("a", "css", ".fld_cancel_button .btn-link:not(.hide)").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}