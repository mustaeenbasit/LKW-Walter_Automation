package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_27109 extends SugarTest {
	FieldSet customData,separator;
	AccountRecord myAccount;
	OpportunityRecord newOpp;
	RevLineItemRecord myRLI;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar.accounts.api.create();
		myRLI = (RevLineItemRecord) sugar.revLineItems.api.create();
		newOpp = (OpportunityRecord) sugar.opportunities.api.create();
		sugar.login();

		newOpp.navToRecord();
		sugar.opportunities.recordView.edit();
		FieldSet editedData = new FieldSet();
		editedData.put("relAccountName", myAccount.getRecordIdentifier());
		sugar.opportunities.recordView.setFields(editedData);
		sugar.opportunities.recordView.save();

		myRLI.navToRecord();
		sugar.revLineItems.recordView.edit();
		editedData.clear();
		editedData.put("relOpportunityName", newOpp.getRecordIdentifier());
		editedData.put("unitPrice", customData.get("unit_price"));
		editedData.put("quantity", customData.get("quantity"));
		editedData.put("discountPrice", customData.get("discountPrice"));
		sugar.revLineItems.recordView.setFields(editedData);
		sugar.revLineItems.recordView.save();
	}

	/**
	 * Verify that calculation is correct when generating quote from RLI
	 * @throws Exception
	 */
	@Test
	public void Quotes_27109_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.revLineItems.recordView.openPrimaryButtonDropdown();
		sugar.revLineItems.recordView.getControl("generateQuote").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.editView.getEditField("date_quote_expected_closed").set(customData.get("date_quote_expected_closed"));

		// TODO: VOOD-1194 -sugar.quotes.editView.save method is not working
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();

		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that total of Quote should be 985.25
		new VoodooControl("td", "css", "#content div.detail.view table tr:nth-child(8) td:nth-child(7)").assertContains(customData.get("discounted_subtotal"), true);

		// verify discounted price
		new VoodooControl("td", "css", "#content div.detail.view table tr:nth-child(7) td:nth-child(7)").assertContains(customData.get("discountPrice"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}