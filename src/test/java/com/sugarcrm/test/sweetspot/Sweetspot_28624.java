package com.sugarcrm.test.sweetspot;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Sweetspot_28624 extends SugarTest {	
	
	public void setup() throws Exception {
		FieldSet myLeadRecord = new FieldSet();
		FieldSet myQuoteRecord = new FieldSet();
		
		// Create 3+ Records of Leads and Quotes Module
		for (int i = 0; i < 4; i++) {
			myLeadRecord.put("lastName", testName+"_"+i);
			sugar.leads.api.create(myLeadRecord);
			myQuoteRecord.put("name", sugar.quotes.moduleNameSingular+"_"+i);
			sugar.quotes.api.create(myQuoteRecord);
		}
		sugar.login();
	}

	/**
	 * Sweet Spot directs user to correct search results page and record view when user searches term with 
	 * 3+ record results
	 * 
	 * @throws Exception
	 */
	@Test
	public void Sweetspot_28624_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Enter a search term
		sugar.sweetspot.search(testName);
		
		// Verify that search should show first three records and "View All Results..."
		for (int i = 0; i < 4; i++) {
			if (i == 3)
				sugar.sweetspot.getRecordsResult(i+1).assertContains(customData.get("viewAllResults"), true);
			else
				sugar.sweetspot.getRecordsResult(i+1).assertContains(testName, true);
		}
		
		// Click "View All Results..."
		sugar.sweetspot.getRecordsResult(4).click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1836
		VoodooControl pageTitle = new VoodooControl("div", "css", "[data-fieldname='title'] div");
		VoodooControl searchResult = new VoodooControl("a", "css", ".nav.search-results");
		
		// Verify that user is directed to global search page with the correct search results
		pageTitle.assertContains(customData.get("searchResultsFor"), true);
		for (int i = 0; i < 4; i++) {
			searchResult.assertContains(testName+"_"+i, true);
		}
		
		// Click a record on the results page
		VoodooControl firstRecord = new VoodooControl("a", "css", ".search-results li:nth-child(1) div h3 a");
		firstRecord.click();
		VoodooUtils.waitForReady();
		
		// Verify that user is directed to correct record view page
		sugar.leads.recordView.getDetailField("fullName").assertContains(testName, true);

		// For BWC Module
		// Activate Sweetspot
		sugar.sweetspot.show();
		
		// Enter a search term
		sugar.sweetspot.search(sugar.quotes.moduleNameSingular);
		
		// Verify that search should show first three records and "View All Results..."
		for (int i = 0; i < 4; i++) {
			if (i == 3)
				sugar.sweetspot.getRecordsResult(i+1).assertContains(customData.get("viewAllResults"), true);
			else
				sugar.sweetspot.getRecordsResult(i+1).assertContains(sugar.quotes.moduleNameSingular, true);
		}
		
		// Click "View All Results..."
		sugar.sweetspot.getRecordsResult(4).click();
		VoodooUtils.waitForReady();
		
		// Verify that user is directed to global search page with the correct search results
		// TODO: VOOD-1836
		pageTitle.assertContains(customData.get("searchResultsFor"), true);
		for (int i = 0; i < 4; i++) {
			searchResult.assertContains(sugar.quotes.moduleNameSingular+"_"+i, true);
		}
		
		// Click a record on the results page
		firstRecord.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user is directed to correct record view page
		sugar.quotes.detailView.getDetailField("name").assertContains(sugar.quotes.moduleNameSingular, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}