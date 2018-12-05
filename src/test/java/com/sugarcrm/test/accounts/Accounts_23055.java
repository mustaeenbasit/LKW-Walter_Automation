package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23055 extends SugarTest {
	FieldSet noteDefaultData;
	AccountRecord myAcc;
	NoteRecord noteFromSubpanel;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		noteDefaultData = sugar().notes.getDefaultData();
		sugar().login();
		myAcc = (AccountRecord) sugar().accounts.api.create();
		noteFromSubpanel = new NoteRecord(noteDefaultData);
		notesSubpanel = sugar().accounts.recordView.subpanels.get("Notes");
	}

	/**
	 * Test Case 23055: Account Detail - Verify that "Create Note" is correctly canceled on "Account" sub-panel.
	 */
	@Test
	public void Accounts_23055_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to the lead record
		myAcc.navToRecord();
		// Click Create in Notes subpanel
		notesSubpanel.addRecord();

		// Fill al required fields
		sugar().notes.createDrawer.showMore();
		sugar().notes.createDrawer.setFields(noteDefaultData);
		// and cancel creation
		sugar().notes.createDrawer.cancel();

		// Verify note isn't created
		notesSubpanel.waitForVisible();
		notesSubpanel.assertContains(noteFromSubpanel.getRecordIdentifier(), false);
		sugar().notes.navToListView();
		sugar().notes.listView.setSearchString(noteFromSubpanel.getRecordIdentifier());
		sugar().notes.listView.getControl("emptyListViewMsg").waitForVisible();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}