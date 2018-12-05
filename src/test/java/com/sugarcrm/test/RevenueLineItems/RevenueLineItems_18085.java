package com.sugarcrm.test.RevenueLineItems;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18085 extends SugarTest {
	OpportunityRecord myOpportunity;
	
	public void setup() throws Exception {
		myOpportunity = (OpportunityRecord)sugar().opportunities.api.create();
		sugar().login();
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpportunity.getRecordIdentifier());
		sugar().revLineItems.create(fs);
	}

	/**
	 * Verify that Associated Quote field is populated with related quote name after the quote is generated from RLI record
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open RLI in the record view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		
		// Verify that Associated Quote field is empty
		sugar().revLineItems.recordView.getDetailField("quoteName").assertVisible(false);
		
		// Generate quote from the RLI record by selecting "Generate Quote" menu item
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Canceling generates the quote in the current implementation
		// TODO: When Quotes module is converted to Sidecar cancel has to be changed to Save.
		sugar().quotes.editView.cancel();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that associated Quote field is populated with the created quote name
		sugar().revLineItems.recordView.getDetailField("quoteName").assertContains(myOpportunity.getRecordIdentifier(), true);
		
		// Verify that "Generate Quote" menu item in Actions drop-down is disabled
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_convert_to_quote_button a").assertAttribute("class", "disabled");
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}