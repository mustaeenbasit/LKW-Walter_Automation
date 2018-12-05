package com.sugarcrm.test.quotes;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Quotes_27012 extends SugarTest {
	FieldSet customData,separator;
	AccountRecord myAccount;
	OpportunityRecord newOpp;
	RevLineItemRecord myRLI;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		separator = testData.get(testName + "_sep").get(0);
		myAccount = (AccountRecord)sugar.accounts.api.create();
		myRLI = (RevLineItemRecord) sugar.revLineItems.api.create();
		newOpp = (OpportunityRecord) sugar.opportunities.api.create();
		sugar.login();

		// Set '1000s separator:' to "."  and   Set 'Decimal Symbol:' to ","
		sugar.users.setPrefs(separator);
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
	 * Verify that all fields on quote detail view are generated properly when convert RLI to quote when decimal separator is comma
	 * @throws Exception
	 */
	@Test
	public void Quotes_27012_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.revLineItems.recordView.openPrimaryButtonDropdown();
		sugar.revLineItems.recordView.getControl("generateQuote").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.quotes.editView.getEditField("date_quote_expected_closed").set(customData.get("date_quote_expected_closed"));

		// TODO: VOOD-1194 -sugar.quotes.editView.save method is not working
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();

		// Go to Quotes record view
		sugar.quotes.navToListView();
		sugar.quotes.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that all fields on Quote detail view are displaying correct values
		new VoodooControl("td", "css", "#content div.detail.view table tr:nth-child(7) td:nth-child(7)").assertContains("$"+customData.get("discountPrice")+",00", true);
		new VoodooControl("td", "css", "#content div.detail.view table tr:nth-child(6) td:nth-child(7)").assertContains("$"+customData.get("quantity")+",00", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}