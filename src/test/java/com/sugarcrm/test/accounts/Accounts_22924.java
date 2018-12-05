package com.sugarcrm.test.accounts;

import static org.junit.Assert.*;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Accounts_22924 extends SugarTest {
	AccountRecord myAccount;
	StandardSubpanel notesSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that creating new note without Attachment related to the account is canceled in-line on "Accounts" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		notesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get("Notes");
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.addRecord();

		// Fill all fields on the createDrawer
		FieldSet recordData = sugar.notes.getDefaultData();
		sugar.notes.createDrawer.showMore();
		sugar.notes.createDrawer.setFields(recordData);

		// Click Cancel
		sugar.notes.createDrawer.cancel();
		
		// Expand Subpanel and verify that it is empty
		notesSubpanel.expandSubpanel();
		assertTrue("Notes Subpanel is not Empty", notesSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}