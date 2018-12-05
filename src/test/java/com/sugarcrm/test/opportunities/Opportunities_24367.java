package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_24367 extends SugarTest {
	AccountRecord myAccRecord;
	
	public void setup() throws Exception {
		FieldSet currencyData = testData.get(testName+"_currency").get(0);
		myAccRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();
		
		// Add new Currency
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that the default currency in "My Account" is not changed after changing the currency of an opportunity.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24367_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to opportunity recordView for Edit record
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAccRecord.getRecordIdentifier());
		
		// TODO: VOOD-983
		// Select a currency different from current one of the opportunity (such as selecting "Euro" currency) and then click Save
		new VoodooControl("span", "css", ".fld_worst_case.edit .currency.edit.fld_currency_id").click();
		VoodooControl currencySelect = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)");
		currencySelect.waitForVisible();
		currencySelect.click();
		
		// Save record
		sugar().opportunities.recordView.save();
				
		// Click the User account name link at the top right of the page to access the User Profile.
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		sugar().users.userPref.getControl("edit").click();
		sugar().users.userPref.getControl("tab4").click();
		
		// Verify that the "Currency" field in User Profile >advanced tab is not changed.
		String currencyText = "US Dollar : $";
		sugar().users.userPref.getControl("advanced_preferedCurrency").assertContains(currencyText, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}