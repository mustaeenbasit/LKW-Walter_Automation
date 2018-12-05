package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

/**
 * TC 24497 -  ENT/ULT: ENT/ULT Verify that Copy of quoted RLI record creates not-quoted copy
 * 
 * @author anisevich
 */
public class RevenueLineItems_24497 extends SugarTest {
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	FieldSet myTestData;
	
	public void setup() throws Exception {
		sugar().login();

		myTestData = testData.get(testName).get(0);

		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord)sugar().revLineItems.create();
	}
	/**
	 * TC 24497 -  ENT/ULT: ENT/ULT Verify that Copy of quoted RLI record creates not-quoted copy
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_24497_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myRLI.navToRecord();
		// Verify that Associated Quote field is empty 
		sugar().revLineItems.recordView.getDetailField("quoteName").assertVisible(false);
		
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("generateQuote").click();
		VoodooUtils.waitForReady();
		sugar().quotes.editView.cancel();  // Canceling generates the quote in the current implementation.
										 //TODO: When Quotes module is converted to Sidecar cancel has to be changed to Save. 
		myRLI.navToRecord();
		// Verify that Associated Quote field is populated
		sugar().revLineItems.recordView.getDetailField("quoteName").assertContains(myOpp.getRecordIdentifier(), true);

		// Copy RLI record created during setup
		sugar().revLineItems.recordView.openPrimaryButtonDropdown();
		sugar().revLineItems.recordView.getControl("copyButton").click();
		
		// Verify that Associated Quote for new RLI creating through copy has "No data" string in it.
		// TODO: Update once JIRA VOOD-472 is ready  
		// TODO: VOOD-1349 
		new VoodooControl("class", "css", ".fld_quote_name.nodata").assertContains(myTestData.get("no_data_str"), true);
		
		sugar().revLineItems.createDrawer.cancel();
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}