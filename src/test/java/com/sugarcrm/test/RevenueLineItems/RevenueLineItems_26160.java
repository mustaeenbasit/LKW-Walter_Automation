package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;

public class RevenueLineItems_26160 extends SugarTest {
	FieldSet myTestData;
	OpportunityRecord myOpp;
	RevLineItemRecord myRevenueLineItem;

	public void setup() throws Exception {
		sugar().login();
		
		myTestData = testData.get(testName).get(0);
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRevenueLineItem = (RevLineItemRecord) sugar().revLineItems.create();
	}

	/**
	 * Verify the user can Cancel the inline editing from the record level action drop down on Revenue Line Item List View
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26160_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.toggleSidebar();
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1, "name").set(myTestData.get("name"));
		sugar().revLineItems.listView.getEditField(1, "salesStage").set(myTestData.get("salesStage"));
		
		VoodooControl horizontalScrollBar = new VoodooControl("div", "css", "div.flex-list-view-content");
		horizontalScrollBar.scrollHorizontally(500); // TODO: use dynamic scroll after VOOD-1319 is implemented 
		sugar().revLineItems.listView.getEditField(1, "likelyCase").set(myTestData.get("likelyCase"));
		sugar().revLineItems.listView.cancelRecord(1);
		sugar().revLineItems.listView.toggleSidebar();
	    
		// Assert the original values are present
		sugar().revLineItems.listView.verifyField(1, "name", myRevenueLineItem.get("name"));
		sugar().revLineItems.listView.verifyField(1, "salesStage", myRevenueLineItem.get("salesStage"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
