package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.test.SugarTest;

public class Accounts_23106 extends SugarTest {
	AccountRecord myAcc;
	NoteRecord relNote;
	StandardSubpanel notesSubpanel;
	FieldSet notesSubjectData = new FieldSet();

	public void setup() throws Exception {
		myAcc = (AccountRecord) sugar().accounts.api.create();
		relNote = (NoteRecord)sugar().notes.api.create();
		notesSubjectData.put("subject", sugar().notes.getDefaultData().get("subject"));
		sugar().login();

		// Link a note to an account
		myAcc.navToRecord();
		notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.clickLinkExisting();
		
		// TODO VOOD-726
		// Select the first note record from the list
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "link_button").click();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Account Detail - Cases sub-panel - Verify that a related note can be unlinked
	 */
	@Test
	public void Accounts_23106_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		// Go to an account record - done in the setup()
		// Check if a case is linked
		notesSubpanel.verify(1, notesSubjectData, true);

		// Unlink a case and verify
		notesSubpanel.unlinkRecord(1);
		notesSubpanel.waitForVisible();

		// TODO: VOOD-735
		notesSubpanel.assertContains(notesSubjectData.get("subject"), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}