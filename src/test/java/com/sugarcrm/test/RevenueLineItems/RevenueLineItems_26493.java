package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26493 extends SugarTest {
	FieldSet currencyMngt;
	VoodooControl currencyCtrl, currencySaveCtrl;
	
	public void setup() throws Exception {
		FieldSet currencyData = testData.get(testName+"_currency").get(0);
		AccountRecord myAcc = (AccountRecord)sugar().accounts.api.create();
		OpportunityRecord myOpportunityRecord = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().revLineItems.api.create();
		sugar().login();
		
		// Add new Currency
		sugar().admin.setCurrency(currencyData);
		
		// Link account with opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(myAcc.getRecordIdentifier());
		sugar().opportunities.recordView.save();
		
		// Created RLI link to the opportunity 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpportunityRecord.getRecordIdentifier());
		sugar().revLineItems.recordView.save();
	}

	/**
	 * Verify that currency drop-down is not present in the list views and sub-panels 
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26493_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to to RLI list view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);

		// Verify that Not possible to change currency of RLI. Currency drop-down is not present in the RLI list view
		new VoodooControl("span", "css", ".currency.edit.fld_currency_id").assertExists(false);
		
		// Cancel In-line edit
		sugar().revLineItems.listView.cancelRecord(1);
		
		// Go to opportunity module and open opportunity in the record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Verify that Not possible to change currency of the RLI in RLI sub-panel of opportunity record view. Currency drop-down is not available 
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubPanel.scrollIntoView();
		rliSubPanel.expandSubpanel();
		rliSubPanel.editRecord(1);
		
		// Verify that Not possible to change currency of RLI. Currency drop-down is not present in the RLI list view
		new VoodooControl("span", "css", ".currency.edit.fld_currency_id").assertExists(false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}