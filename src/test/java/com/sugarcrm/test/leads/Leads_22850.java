package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Leads_22850 extends SugarTest {
	FieldSet currencyData = new FieldSet();
	FieldSet fs = new FieldSet();
	VoodooControl layoutSubPanelCtrl, leadsButtonCtrl, layoutSaveBtn;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().leads.api.create();
		sugar().login();
		
		// Create test Currency
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", fs.get("rate"));
		currencyData.put("currencySymbol", fs.get("symbol"));
		currencyData.put("ISOcode", fs.get("code"));
		sugar().admin.setCurrency(currencyData);
						
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1026
		leadsButtonCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSaveBtn = new VoodooControl("input", "css", "input[name='saveLayout']");
		
		// studio > leads > layouts > Convert Lead > add RLI module 
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#layoutsBtn tr:nth-child(1) td").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='convertSelectNewModule']").set(fs.get
				("rliOption"));		
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='addModule']").click();	
		VoodooUtils.waitForReady();
		layoutSaveBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Opportunity Create Form from Lead Convert has currency drop down
	 * @throws Exception
	 */
	@Test
	public void Leads_22850_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);
		
		// TODO: VOOD-585
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_lead_convert_button.list a").click();
		
		// Associate Accounts to lead.		
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").
			set(fs.get("accountName"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		
		// Associate Opportunity to lead.
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input")
			.set(fs.get("opportunityName"));
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button."
				+ "convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Associate Revenue Line Items to lead.
		new VoodooControl("input", "css", "div[data-module='RevenueLineItems'] .fld_name.edit input")
			.set(fs.get("revenueLineItemName"));
		new VoodooControl("a", "css", "div[data-module='RevenueLineItems'] .fld_associate_button."
				+ "convert-panel-header a").click();
		sugar().alerts.getError().closeAlert();
		
		new VoodooControl("span", "css", "div[data-name='opportunity_name'] div a span.select2-arrow").click();
		new VoodooControl("div", "css", "#select2-drop ul:nth-child(3) li div").click();
		new VoodooControl("input", "css", ".layout_Opportunities input[name='Opportunities_select']").click();

		// Click on Show more...
		new VoodooControl("button", "css", "#collapseRevenueLineItems button.btn-link.btn-invisible.more").click();
		VoodooControl likely_case = new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child(2)");

		// Verify the different currency
		new VoodooControl("span", "css", "#collapseRevenueLineItems div[data-name='likely_case'] a "
				+ "span.select2-arrow").click();
		likely_case.assertElementContains(fs.get("code"), true);
		likely_case.click();		
		new VoodooControl("span", "css", "#collapseRevenueLineItems div[data-name='best_case'] a "
				+ "span.select2-arrow").click();
		likely_case.assertElementContains(fs.get("code"), true);
		likely_case.click();		
		new VoodooControl("span", "css", "#collapseRevenueLineItems div[data-name='worst_case'] a "
				+ "span.select2-arrow").click();
		likely_case.assertElementContains(fs.get("code"), true);
		likely_case.click();
		new VoodooControl("a", "css", "span.fld_cancel_button.convert-headerpane a").click();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}