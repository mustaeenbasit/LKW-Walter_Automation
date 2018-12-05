package com.sugarcrm.test.bugs;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Bugs_18607 extends SugarTest {
	FieldSet assertionData;
	CallRecord myCall;

	public void setup() throws Exception {
		assertionData = testData.get(testName).get(0);
		myCall = (CallRecord) sugar.calls.api.create();
		sugar.bugs.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}

	/**
	 * New action dropdown list in bug tracker's subpanel
	 * @throws Exception 
	 */
	@Test
	public void Bugs_18607_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a Bugs detail view page
		sugar.bugs.navToListView();
		sugar.bugs.listView.clickRecord(1);

		// Define all subpanel of the Bugs module under StandardSubpanel and add them into ArrayList
		ArrayList<StandardSubpanel> subpanelList = new ArrayList<StandardSubpanel>();
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.calls.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.meetings.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.tasks.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.notes.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.documents.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.contacts.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.accounts.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.cases.moduleNamePlural));
		subpanelList.add(sugar.bugs.recordView.subpanels.get(sugar.emails.moduleNamePlural));

		// Verify that 'Toggle Subpanel', 'Add Record' and 'Action Dropdown list' are shown on each subpanel
		for (int i = 0; i < subpanelList.size(); i++) {
			StandardSubpanel defaultSubpanelRecord = subpanelList.get(i);
			VoodooControl expandSubpanel = defaultSubpanelRecord.getControl("expandSubpanelActions");
			VoodooControl toggleSubpanel = defaultSubpanelRecord.getControl("toggleSubpanel");
			if (i < 8) {
				defaultSubpanelRecord.getControl("addRecord").assertExists(true);
				toggleSubpanel.assertExists(true);
				expandSubpanel.assertExists(true);

				// Click on the Action Dropdown and assert 'Link Existing Record'
				expandSubpanel.click();
				defaultSubpanelRecord.getControl("linkExistingRecord").assertEquals(assertionData.get("linkExistingRecord"), true);
				expandSubpanel.click();  // Again click on action dropdown to hide the opened dropdown)
			} else {
				// Only for Email Subpanel (Action dropdown is disable for Email subpanel)
				defaultSubpanelRecord.getControl("composeEmail").assertExists(true);
				toggleSubpanel.assertExists(true);
				expandSubpanel.assertExists(true);
			}
		}

		// Click on any action(i.e Link Existing Record) in the list
		StandardSubpanel callSubpanel = subpanelList.get(0);
		callSubpanel.linkExistingRecord(myCall);

		// Go to a Bugs detail view page and click the down arrow of action dropdown list beside a subpanel record
		callSubpanel.expandSubpanel();
		callSubpanel.expandSubpanelRowActions(1);

		// Verify 'Edit', 'Unlink' and 'Close' should be available for the record in the Subpanel
		callSubpanel.getControl("editActionRow01").assertVisible(true);
		callSubpanel.getControl("unlinkActionRow01").assertVisible(true);
		callSubpanel.getControl("closeActionRow01").assertVisible(true);

		// Click on all the actions in the list
		// Edit and Verify that the action is triggered
		callSubpanel.getControl("editActionRow01").click();
		callSubpanel.getEditField(1, "name").set(testName);
		callSubpanel.saveAction(1);

		// TODO: VOOD-1424 Once
		callSubpanel.getDetailField(1, "name").assertEquals(testName, true);

		// Close and Verify that the action is triggered
		callSubpanel.closeRecord(1);
		callSubpanel.getDetailField(1, "status").assertEquals(assertionData.get("status"), true);

		// Unlink and Verify that the action is triggered and records are empty
		callSubpanel.unlinkRecord(1);
		Assert.assertTrue("Number of rows did not equal zero.", callSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}