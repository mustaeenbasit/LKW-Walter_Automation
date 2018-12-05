package com.sugarcrm.test.opportunities;

import java.text.DecimalFormat;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_19738 extends SugarTest {
	FieldSet currencySetupData;
	RevLineItemRecord myRli;
	OpportunityRecord myOpp;
	
	public void setup() throws Exception {
		currencySetupData = testData.get(testName+"_currency").get(0);
		// Create a Opportunity record 
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		// Create a Revenue Line Item record with default currency, which is US Dollar.
		myRli = (RevLineItemRecord)sugar().revLineItems.api.create();
		sugar().login();
		// create new currency i.e. EURO
		sugar().admin.setCurrency(currencySetupData);
	}

	/**
	 * Change currency result in correct Amount to be calculated in Op record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_19738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myRli.navToRecord();
		// Edit the record, change currency to Euro. Save it.
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.showMore();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpp.get("name"));

		// Click on currency field to open drop down field, switch to EUR from USD.
		// VOOD-983
		new VoodooSelect("span", "css", "[data-fieldname='likely_case'] .fld_currency_id .select2-chosen").set(currencySetupData.get("currencySymbol") +" ("+ currencySetupData.get("ISOcode") + ")" );
		String euroAmout= sugar().revLineItems.recordView.getEditField("likelyCase").getText();
		sugar().alerts.waitForLoadingExpiration();
		sugar().revLineItems.recordView.save();

		// VOOD-1402: Need to support verification of currency
		String likelyValue = sugar().revLineItems.getDefaultData().get("likelyCase");
		DecimalFormat formatter = new DecimalFormat("##,###.00");
		String amount = formatter.format(Double.parseDouble(likelyValue));

		// Verify taht Likely, Best, Worst display both US Dollar and Euro currency amounts
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(amount, true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(amount, true);
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(amount, true);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertContains(euroAmout, true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertContains(euroAmout, true);
		sugar().revLineItems.recordView.getDetailField("worstCase").assertContains(euroAmout, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}