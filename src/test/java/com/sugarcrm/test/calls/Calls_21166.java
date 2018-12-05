package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_21166 extends SugarTest {
	NoteRecord myNotes;

	public void setup() throws Exception {
		sugar.calls.api.create();
		myNotes = (NoteRecord) sugar.notes.api.create();
		sugar.login();
	}

	/**
	 * New action dropdown list in call's subpanel
	 * @throws Exception
	 */
	@Test
	public void Calls_21166_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to a call detail view page. 
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);

		// Verify Notes subpanel (subpanel Name), 'Toggle Subpanel', 'Add Record' and 'Action Dropdown list' are shown on Notes subpanel
		StandardSubpanel noteSubpanel = sugar.calls.recordView.subpanels.get(sugar.notes.moduleNamePlural);
		noteSubpanel.getControl("subpanelName").assertEquals(sugar.notes.moduleNamePlural , true);
		noteSubpanel.getControl("addRecord").assertVisible(true);
		noteSubpanel.getControl("toggleSubpanel").assertVisible(true);

		// Click on the Action Dropdown and assert 'Link Existing Record'
		// click function already takes care of its visibility of an element otherwise, it throws a Candybean exception
		noteSubpanel.getControl("expandSubpanelActions").click();
		noteSubpanel.getControl("linkExistingRecord").click();

		// Verify that the the action is triggered, and we are on SSV
		sugar.notes.searchSelect.assertVisible(true);
		sugar.notes.searchSelect.getControl("moduleTitle").assertContains(testData.get(testName).get(0).get("moduleTitle"), true);
		
		// Select and link the Notes record
		sugar.notes.searchSelect.selectRecord(myNotes);
		sugar.notes.searchSelect.link();

		// Verify notes record in subpanel
		noteSubpanel.expandSubpanel();
		Assert.assertTrue("note record not equals one", noteSubpanel.countRows() == 1);
		noteSubpanel.expandSubpanelRowActions(1);

		// Verify 'Edit' and 'Unlink' should be available for the record in the Subpanel
		noteSubpanel.getControl("unlinkActionRow01").assertVisible(true);

		// Edit control has been triggered
		// click function already takes care of its visibility of an element otherwise, it throws Candybean exception
		noteSubpanel.getControl("editActionRow01").click(); 
		noteSubpanel.getEditField(1, "subject").set(testName);
		noteSubpanel.saveAction(1);
		// TODO: VOOD-1424
		noteSubpanel.getDetailField(1, "subject").assertEquals(testName, true);

		// Unlink and Verify that the action is triggered
		noteSubpanel.unlinkRecord(1);
		Assert.assertTrue("Number of rows did not equal zero.", noteSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}