package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22943 extends SugarTest {
	AccountRecord account;
	StandardSubpanel memberOrganizations;
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		account = (AccountRecord)sugar().accounts.api.create();
		account.navToRecord();
	}

	/**
	 * TC 22943: Verify that creating new account member can be canceled in Member Organizations sub-panel on account record view
	 * @throws Exception
	 */
	@Test
	public void Accounts_22943_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		ds = testData.get(testName);
		
		memberOrganizations = sugar().accounts.recordView.subpanels.get("Accounts");
		memberOrganizations.addRecord();
		
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.cancel();
		
		memberOrganizations.expandSubpanel();
		
		// Verify Organization was not created
		memberOrganizations.assertContains(ds.get(0).get("no_records_message"), true);
		memberOrganizations.assertContains(testName, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}