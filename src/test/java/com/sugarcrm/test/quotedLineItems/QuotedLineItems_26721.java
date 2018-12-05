package com.sugarcrm.test.quotedLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.QuotedLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26721 extends SugarTest {
	DataSource currencyFs;
	QuotedLineItemRecord myQLI;

	public void setup() throws Exception {
		myQLI = (QuotedLineItemRecord)sugar().quotedLineItems.api.create();
		currencyFs = testData.get(testName);
		sugar().login();

		// Create Currencies
		sugar().admin.setCurrency(currencyFs.get(0));
		sugar().admin.setCurrency(currencyFs.get(1));

		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().logout();

		// "Show Preferred Currency" is checked in the user preference
		// Select User Preferred currency, different from system default (base) currency.
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").click();
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_showpreferedCurrency").click();
		sugar().users.userPref.getControl("advanced_preferedCurrency").set((testName + "_" + 0) +" : "+currencyFs.get(0).get("currencySymbol"));
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that Quoted Line Items detail view shows amounts in the transactional currency.
	 *
	 * @throws Exception
	 */
	@Ignore("Under Investigation: Seems to breaking other tests.Temporarily blocked. "
			+ "Update 10/15/2015 Ashish Raina: "
			+ "Reason is Product Bug TR-10788 which surfaces *After* this script completes")

	@Test
	public void QuotedLineItems_26721_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl currencyFieldCtrl = new VoodooControl("div", "css", ".currency-field");
		VoodooControl discountPriceCtrl = new VoodooControl("span", "css", "span[data-voodoo-name='discount_price'] span[data-voodoo-name='currency_id']");
		myQLI.navToRecord();

		// Verify the user preferred currency.
		currencyFieldCtrl.assertContains(currencyFs.get(0).get("currencySymbol"), true);
		sugar().quotedLineItems.recordView.edit();
		discountPriceCtrl.click();
		new VoodooControl("div", "css", ".select2-results :nth-child(1) div").click();
		sugar().quotedLineItems.recordView.save();

		// Verify the user preferred currency.
		currencyFieldCtrl.assertContains(currencyFs.get(0).get("currencySymbol"), true);
		myQLI.navToRecord();
		sugar().quotedLineItems.recordView.edit();
		discountPriceCtrl.click();
		new VoodooControl("div", "css", ".select2-results :nth-child(3) div").click();
		sugar().quotedLineItems.recordView.save();

		// Verify the transactional currency and user preferred currency for each number field.
		currencyFieldCtrl.assertContains(currencyFs.get(0).get("currencySymbol"), true);
		currencyFieldCtrl.assertContains(currencyFs.get(0).get("currencySymbol"), true);

		// "Show Preferred Currency" is unchecked in the user preference
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("edit").click();
		sugar().users.userPref.getControl("tab4").click();
		sugar().users.userPref.getControl("advanced_showpreferedCurrency").click();
		sugar().users.userPref.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}