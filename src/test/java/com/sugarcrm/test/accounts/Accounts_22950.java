package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22950 extends SugarTest {
	AccountRecord account;
	StandardSubpanel bugsSubpanel;
	DataSource bugFields, textMessages;
	
	public void setup() throws Exception {
		bugFields = testData.get(testName);
		textMessages = testData.get(testName + "_text_messages");
		
		sugar().login();
		account = (AccountRecord)sugar().accounts.api.create();
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);
	}

	/**
	 *	22950 Verify that creating new bug related to the account in-line is canceled
	 *	@throws Exception
	 */
	@Test
	public void Accounts_22950_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		account.navToRecord();
		bugsSubpanel = sugar().accounts.recordView.subpanels.get("Bugs");

		bugsSubpanel.addRecord();
		
		for(String controlName : bugFields.get(0).keySet())
			sugar().bugs.createDrawer.getEditField(controlName).set(bugFields.get(0).get(controlName));
		
		sugar().bugs.createDrawer.cancel();

		bugsSubpanel.expandSubpanel();
		
		// verify the bug was not created
		for(String fieldValue : bugFields.get(0).values())
			bugsSubpanel.assertContains(fieldValue, false);
			
		bugsSubpanel.assertContains(textMessages.get(0).get("no_records_message"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}