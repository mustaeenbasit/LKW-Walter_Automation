package com.sugarcrm.test.accounts;

import static org.junit.Assert.*;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Accounts_22936 extends SugarTest {
	AccountRecord myAccount;
	StandardSubpanel casesSubpanel;

	public void setup() throws Exception {
		sugar().login();

		// Create a new account
		myAccount = (AccountRecord)sugar().accounts.api.create();
	}

	/**
	 * Verify that creating new case related to the account in-line is canceled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22936_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto Accounts record view
		myAccount.navToRecord();
		casesSubpanel = (StandardSubpanel) sugar().accounts.recordView.subpanels.get("Cases");
		casesSubpanel.scrollIntoViewIfNeeded(false);
		casesSubpanel.addRecord();

		// Fill all fields on the createDrawer
		FieldSet recordData = sugar.cases.getDefaultData();
		sugar.cases.createDrawer.showMore();
		sugar.cases.createDrawer.setFields(recordData);

		// Click Cancel
		sugar.cases.createDrawer.cancel();
		
		// Expand Subpanel and verify that it is empty
		casesSubpanel.expandSubpanel();
		assertTrue("Cases Subpanel is not Empty", casesSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}