package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30612 extends SugarTest {
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
		
		// Adding Currency rate field in record view layout of RLI from Studio.
		VoodooControl rliStudioCtrl = new VoodooControl("a", "id", "studiolink_RevenueLineItems");
		VoodooControl rliStudioLayoutCtrl = new VoodooControl("input", "id", "layoutsBtn");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		rliStudioCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1506
		rliStudioLayoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "[data-name='base_rate']").dragNDrop(new VoodooControl("div", "css", "[data-name='tag']"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		
		// Adding Currency rate field in record view layout of RLI from Studio.
		// TODO: VOOD-1506
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		rliStudioCtrl.click();
		VoodooUtils.waitForReady();
		rliStudioLayoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "[data-name='base_rate']").dragNDropViaJS(new VoodooControl("li", "css", "[data-name='quote_name']"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Currency rate should be auto filled when changing currency in RLI.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30612_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Creating RLI
		sugar().navbar.selectMenuItem(sugar().revLineItems, "createRevenueLineItem");
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		
		// Change currency to Euro
		// TODO: VOOD-983
		new VoodooControl("span", "css", "div[data-name='discount_price'] .currency.edit.fld_currency_id").click();
		new VoodooControl("li", "css", "#select2-drop li:nth-child(2)").click();
		
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.save();
		
		// TODO: VOOD-1489
		// Verifying Currency Rate field is auto filled in list view.
		new VoodooControl("span", "css", ".list.fld_base_rate").assertEquals(customData.get("conversionRate"), true);

		// Navigate to record view
		sugar().revLineItems.listView.clickRecord(1);
		
		// Verifying Currency Rate field is auto filled in Record view.
		new VoodooControl("span", "css", ".detail.fld_base_rate").assertEquals(customData.get("conversionRate"), true);
		
		// Verifying Currency Rate field is not editable.
		sugar().revLineItems.recordView.edit();
		new VoodooControl("span", "css", ".detail.fld_base_rate").assertVisible(true);
		new VoodooControl("span", "css", ".edit.fld_base_rate").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}