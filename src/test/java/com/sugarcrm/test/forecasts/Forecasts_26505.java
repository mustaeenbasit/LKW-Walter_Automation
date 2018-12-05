package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_26505 extends SugarTest {
	AccountRecord myAccount;
	OpportunityRecord myOpportunity;
	RevLineItemRecord myRevLineItem;
	 
	public void setup() throws Exception {
		sugar.login();

		// TODO: VOOD-928. Why com.sugarcrm.test.grimore.Forecast_setup.java is failing,
		// due to which below initialization has become necessary. This is to be 
		// removed once VOOD-928 is fixed
		sugar.forecasts.resetForecastSettings();
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").click();
		VoodooUtils.waitForAlertExpiration();
	}

	/**
	 * 04/09/2014, Alex N: Created TC from SFA-2382 (https://sugarcrm.atlassian.net/browse/SFA-2382)
	 * Verify that Forecasts does not allow to commit with invalid date field
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void Forecasts_26505_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Setup Accounts and linked Opportunities records, and
		// at least one included RLI record for the current time period 
		myAccount = (AccountRecord)sugar.accounts.api.create();

		// To create a new rec + relationship between myAccount and myOpportunity through api
		FieldSet myFieldSet1 = new FieldSet();
		myFieldSet1.put("relAccountName", myAccount.getRecordIdentifier());		
		myOpportunity = (OpportunityRecord)sugar.opportunities.api.create(myFieldSet1);
		VoodooUtils.waitForAlertExpiration();
		
		// To create a new rec + relationship between myOpportunity and myRevLineItem through api
		FieldSet myFieldSet2 = new FieldSet();
		myFieldSet2.put("relOpportunityName", myOpportunity.getRecordIdentifier());
		myFieldSet2.put("date_closed", VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy")); 		
		myRevLineItem = (RevLineItemRecord)sugar.revLineItems.api.create(myFieldSet2);
		VoodooUtils.waitForAlertExpiration();

		sugar.navbar.navToModule("Forecasts");
		VoodooUtils.pause(3000); // Allow page to load
		
		// TODO: VOOD-929

		// remove 'include' from filter
		new VoodooControl ("a", "css", "li.select2-search-choice a.select2-search-choice-close").waitForVisible();
		new VoodooControl ("a", "css", "li.select2-search-choice a.select2-search-choice-close").click();
		VoodooUtils.pause(3000); // Allow data to reload

		// Change 'stage' and 'expected close data' values
		new VoodooControl("span", "css", ".fld_sales_stage").click();
		new VoodooControl("li", "css" ,"div#select2-drop ul li").click();
		new VoodooControl("span", "css", ".fld_date_closed").click();
		new VoodooControl("input", "css", "span.fld_date_closed div.clickToEdit input").set("1");
		new VoodooControl("body", "css", "body").click(); // To remove focus from the date field		

		// Commit button is grayed out if the date field is invalid 
		new VoodooControl("a", "css", ".btn.btn-primary").assertAttribute("class", "disabled", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
