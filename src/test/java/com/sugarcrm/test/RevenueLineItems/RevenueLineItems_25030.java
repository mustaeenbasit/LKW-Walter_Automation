package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_25030 extends SugarTest {
		
	FieldSet myTestData;
	
	public void setup() throws Exception {
		sugar().login();
		myTestData = testData.get("RevenueLineItems_25030").get(0);
	}

	/*
	 * TC 25030 - Verify that Calculated RLI Amount field is recalculated when unit price, quantity, and 
	 * discount amount are changed in the edit view.   
	 * */
	@Test
	public void RevenueLineItems_25030_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		
		sugar().revLineItems.createDrawer.showMore();
		// Enter value 5 into Unit Price field
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(myTestData.get("unitPrice"));
		sugar().revLineItems.createDrawer.getEditField("description").click(); //needed to commit previous line
		// Verification - Part 1
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(myTestData.get("calcRLIAmount1"), true);
		sugar().revLineItems.createDrawer.getEditField("worstCase").assertContains(myTestData.get("calcRLIAmount1"), true);
		sugar().revLineItems.createDrawer.getEditField("likelyCase").assertContains(myTestData.get("calcRLIAmount1"), true);
		sugar().revLineItems.createDrawer.getEditField("bestCase").assertContains(myTestData.get("calcRLIAmount1"), true);
		
		// Enter value 10 into Quantity field
		sugar().revLineItems.createDrawer.getEditField("quantity").set(myTestData.get("quantity"));
		sugar().revLineItems.createDrawer.getEditField("description").click(); //needed to commit previous line
		// Verification - Part 2
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(myTestData.get("calcRLIAmount2"), true);

		// Enter value 7 into Total Discount Amount field
		sugar().revLineItems.createDrawer.getEditField("discountPrice").set(myTestData.get("discountPrice"));
		sugar().revLineItems.createDrawer.getEditField("description").click(); //needed to commit previous line
		// Verification - Part 3
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(myTestData.get("calcRLIAmount3"), true);
		
		// Enter value 8 into Unit Price field
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(" ");
		sugar().revLineItems.createDrawer.getEditField("description").click(); //needed to commit previous line
		sugar().revLineItems.createDrawer.getEditField("unitPrice").set(myTestData.get("unitPrice1"));
		sugar().revLineItems.createDrawer.getEditField("description").click(); //needed to commit previous line
		// Verification - Part 4
		sugar().revLineItems.createDrawer.getEditField("calcRLIAmount").assertContains(myTestData.get("calcRLIAmount4"), true);
		
		sugar().revLineItems.createDrawer.showLess();
		sugar().revLineItems.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
