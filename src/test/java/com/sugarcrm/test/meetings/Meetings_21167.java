package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_21167 extends SugarTest {	
	public void setup() throws Exception {
		sugar().meetings.api.create();
		sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * New action dropdown list in meeting's subpanel
	 * @throws Exception
	 */
	@Test
	public void Meetings_21167_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		StandardSubpanel notesSubpanel = sugar().meetings.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);

		// Verify Notes subpanel (subpanel Name), 'Add Record'(+) and 'Action Dropdown list'(ˇ)
		notesSubpanel.getControl("subpanelName").assertEquals(sugar().notes.moduleNamePlural , true);
		notesSubpanel.getControl("toggleSubpanel").assertVisible(true);
		notesSubpanel.getControl("addRecord").assertVisible(true);
		notesSubpanel.getControl("expandSubpanelActions").click();

		// Click on Link Existing Record action from dropdown menu list
		notesSubpanel.getControl("linkExistingRecord").click(); // click function already takes care of its visibility of an element otherwise, it throws a Candybean exception

		// Verify action is triggered & select, link Notes record
		sugar().notes.searchSelect.assertVisible(true);
		sugar().notes.searchSelect.selectRecord(1);
		sugar().notes.searchSelect.link();

		// Verify notes record in subpanel
		notesSubpanel.expandSubpanel();
		Assert.assertTrue("note record not equals one", notesSubpanel.countRows() == 1);
		notesSubpanel.expandSubpanelRowActions(1);

		// Verify Edit and 'Unlink' should be available for the record in the Subpanel
		notesSubpanel.getControl("editActionRow01").assertVisible(true);

		// Verify unlink action is triggered & no records in subpanels list
		notesSubpanel.getControl("unlinkActionRow01").click(); // click function already takes care of its visibility of an element otherwise, it throws Candybean exception
		sugar().alerts.getWarning().confirmAlert();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		Assert.assertTrue("note record not equals zero", notesSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}