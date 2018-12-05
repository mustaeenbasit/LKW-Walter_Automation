package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22942 extends SugarTest {
	public void setup() throws Exception {
		// Creating test account record
		sugar().accounts.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Account Detail - Quotes sub-panel - Create_Verify that new quotes is created on "QUOTES" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_22942_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to "Accounts" record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Click "Create" on "Quotes" sub-panel
		StandardSubpanel quotesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().quotes.moduleNamePlural);
		quotesSubpanel.addRecord();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify 'Billing Account Name' field is displayed with the current account name
		// TODO: VOOD-1886 - Support verifying BWC relate field in edit mode
		new VoodooControl("input", "id", "billing_account_name").assertContains(sugar().accounts.defaultData.get("name"), true);
		
		// Fill all fields and save
		sugar().quotes.editView.getEditField("name").set(testName);
		sugar().quotes.editView.getEditField("date_quote_expected_closed").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		VoodooUtils.focusDefault();
		sugar().quotes.editView.save();
		quotesSubpanel.expandSubpanel();
		
		// Verify new quotes is displayed on "Quotes" sub-panel
		// TODO: VOOD-1424 - Make StandardSubpanel.verify() verify specified value is in correct column.
		new VoodooControl("a", "css", ".list.fld_name a").assertEquals(testName, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}