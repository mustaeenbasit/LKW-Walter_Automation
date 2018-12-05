package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22923 extends SugarTest {
	AccountRecord account;
	DataSource noteData;
	StandardSubpanel notesSubpanel;
	
	public void setup() throws Exception {
		sugar().login();
		
		noteData = testData.get("Accounts_22923");
		account = (AccountRecord)sugar().accounts.api.create();
		account.navToRecord();
	}

	/**
	 * 22923 Verify that new note without Attachment related to the account is created in-line on "Accounts" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_22923_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet noteFields = noteData.get(0);
		
		notesSubpanel = new StandardSubpanel(sugar().notes);
		notesSubpanel.create(noteFields);
		notesSubpanel.verify(1, noteFields, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}