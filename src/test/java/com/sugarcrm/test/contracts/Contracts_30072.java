package com.sugarcrm.test.contracts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_30072 extends SugarTest {
	DataSource accountsData = new DataSource();

	public void setup() throws Exception {
		accountsData = testData.get(testName);
		sugar().accounts.api.create(accountsData);

		// Login
		sugar().login();

		// Updating records with email addresses
		// TODO: VOOD-1282 : No primary email inserted through REST call in create method inside RecordsModule.java 
		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerName", true);
		sugar().accounts.listView.clickRecord(1);
		
		for(int i = 0 ; i < accountsData.size() ; i++) {
			sugar().accounts.recordView.edit();
			sugar().accounts.recordView.showMore();
			sugar().accounts.recordView.getEditField("emailAddress").set(accountsData.get(i).get("emailAddress"));
			sugar().accounts.recordView.save();
			sugar().accounts.recordView.gotoNextRecord();
		}
		
		// Enable Contracts module
		sugar().admin.enableModuleDisplayViaJs(sugar().contracts);
	}

	/**
	 * Verify Email field appears enabled in bwc pop-up search and Account records are searchable w.r.t Emails
	 * @throws Exception
	 */
	@Test
	public void Contracts_30072_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Go to Contracts
		sugar().navbar.selectMenuItem(sugar().contracts, "createContract");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-805 : Need lib support for BWC popup select window
		new VoodooControl("button", "css", "#btn_account_name").click();
		VoodooUtils.waitForReady();
		
		// Switching focus to the pop up window
		VoodooUtils.focusWindow(1);
		
		// Making search for Accounts as per Email addresses.
		new VoodooControl("input", "css", "#email_advanced").set(accountsData.get(accountsData.size()-1).get("emailAddress"));
		new VoodooControl("input", "id", "search_form_submit").click();
		
		// Verify that Account records are searchable w.r.t. Emails.
		new VoodooControl("td", "css", ".list.view tr:nth-child(3) td").assertEquals(accountsData.get(accountsData.size()-1).get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}