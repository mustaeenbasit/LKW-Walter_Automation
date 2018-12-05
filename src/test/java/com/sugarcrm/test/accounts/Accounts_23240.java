package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_23240 extends SugarTest {
	FieldSet myData, fs, currencyData;
	VoodooControl currencyCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myData = testData.get(testName).get(0);
		sugar().login();

		// Create test Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", myData.get("rate"));
		currencyData.put("currencySymbol", myData.get("symbol"));
		currencyData.put("ISOcode", myData.get("code"));
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify that opportunity created using not default currency shows both transactional and base currency 
	 * in opportunity sub-panel of accounts module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_23240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		fs = new FieldSet();
		// For Admin user, set user's default currency to test currency 
		fs.put("advanced_preferedCurrency", testName+" : "+myData.get("symbol")); 
		sugar().users.setPrefs(fs);

		// Navigate to accounts record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).addRecord();
		sugar().opportunities.createDrawer.getEditField("name").set(myData.get("opp_name"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.defaultData.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.defaultData.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.defaultData.get("rli_likely"));
		sugar().opportunities.createDrawer.save();
		sugar().alerts.closeAllWarning();
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).expandSubpanel();

		// TODO: VOOD-609
		// Verify that opportunities shown in opportunity sub-panel with transaction currency (If non-system currency is used, it will display both transactional and base currency). 
		new VoodooControl("div", "css", "div.converted").assertContains("$", true);
		new VoodooControl("label", "css", "label.original").assertContains(myData.get("symbol"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}