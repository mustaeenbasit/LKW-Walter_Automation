package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30577 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().login();
		
		// Creating Euro currency
		FieldSet multiPurposeData = new FieldSet();
		multiPurposeData.put("currencyName", customData.get("currencyName"));
		multiPurposeData.put("ISOcode", customData.get("ISOcode"));
		multiPurposeData.put("conversionRate", customData.get("conversionRate"));
		multiPurposeData.put("currencySymbol", customData.get("currencySymbol"));
		sugar().admin.setCurrency(multiPurposeData);
		multiPurposeData.clear();
	}

	/**
	 * Verify that Converted Currency amount is correctly displayed in detail view of RLI record.  
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30577_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Creating RLI
		sugar().navbar.selectMenuItem(sugar().revLineItems, "createRevenueLineItem");
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		
		// Change currency to Euro (testName)
		// TODO: VOOD-983
		new VoodooControl("span", "css", "div[data-name='discount_price'] .currency.edit.fld_currency_id").click();
		new VoodooControl("li", "css", "#select2-drop li:nth-child(2)").click();
		
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customData.get("likelyCase"));
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("bestCase").set(customData.get("bestCase"));
		sugar().revLineItems.createDrawer.getEditField("worstCase").set(customData.get("worstCase"));
		sugar().revLineItems.createDrawer.save();
		
		String likelyCaseAmount = customData.get("currencySymbol") + customData.get("likelyCase") + customData.get("likelyCaseInDoller");
		String worstCaseAmount = customData.get("currencySymbol") + customData.get("worstCase") + customData.get("worstCaseInDoller");
		String bestCaseAmount = customData.get("currencySymbol") + customData.get("bestCase") + customData.get("bestCaseInDoller");
		
		// Verifying converted Currency amount is displaying correctly in ListView.
		sugar().revLineItems.listView.getDetailField(1, "likelyCase").assertEquals(likelyCaseAmount, true);
		sugar().revLineItems.listView.getDetailField(1, "worstCase").assertEquals(worstCaseAmount, true);
		sugar().revLineItems.listView.getDetailField(1, "bestCase").assertEquals(bestCaseAmount, true);
		
		// Verifying converted Currency amount is displaying correctly in RecordView.
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.getDetailField("likelyCase").assertEquals(likelyCaseAmount, true);
		sugar().revLineItems.recordView.getDetailField("worstCase").assertEquals(worstCaseAmount, true);
		sugar().revLineItems.recordView.getDetailField("bestCase").assertEquals(bestCaseAmount, true);
		sugar().revLineItems.recordView.getDetailField("unitPrice").assertEquals(likelyCaseAmount, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}