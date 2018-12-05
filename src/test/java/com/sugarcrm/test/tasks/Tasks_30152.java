package com.sugarcrm.test.tasks;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tasks_30152 extends SugarTest {
	NoteRecord noteRecord;

	public void setup() throws Exception {	
		sugar().accounts.api.create();
		noteRecord = (NoteRecord) sugar().notes.api.create();
		sugar().tasks.api.create();	
		sugar().login();

		// Navigating to Notes and making the note associated with Account
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("relRelatedToModule").set(sugar().accounts.moduleNameSingular);
		sugar().notes.recordView.getEditField("relRelatedToValue").set(sugar.accounts.getDefaultData().get("name"));
		sugar().notes.recordView.save();

		// Navigate to Admin > Studio > Tasks > Subpanel > Notes > Add 'Related To' to default column 
		// TODO: VOOD-542 and VOOD-1511
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "studiolink_Tasks").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "class", "studiolink").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "[data-name='parent_name']").dragNDrop(new VoodooControl("li", "css", "[data-name='date_modified']"));
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "Related to" field should not be shown blank in Notes subpanel after linking with existing Task record
	 * @throws Exception
	 */	
	@Test
	public void Tasks_30152_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().tasks.navToListView();	
		sugar().tasks.listView.clickRecord(1);
		
		StandardSubpanel notesSubpanel = sugar().tasks.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		
		// Linking existing note record with the task record 
		notesSubpanel.linkExistingRecord(noteRecord);
		
		// Verify that "Related to" field should not be shown blank while link with existing record in Task module.
		// TODO: VOOD-1489: Need Library Support for All fields moved from Hidden to Default & vice versa for All Modules
		new VoodooControl("span", "css", ".list.fld_parent_name").assertEquals(sugar().tasks.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}