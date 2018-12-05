package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20317 extends SugarTest {
	FieldSet currencyMngt, currencyData;
	VoodooControl oppSubPanelCtrl,layoutSubPanelCtrl,recordViewSubPanelCtrl;

	public void setup() throws Exception {
		currencyMngt = testData.get(testName+"_currency").get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// Create Opportunity record
		sugar().opportunities.create();

		// Added 'Converted Amount' field to list view and Record view.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		oppSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		oppSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		new VoodooControl("div", "css", "div[data-name='amount_usdollar']").dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		// List view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		oppSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='amount_usdollar']").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Create Currency custom currency 'Euro'
		currencyData = new FieldSet();
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyMngt.get("rate"));
		currencyData.put("currencySymbol", currencyMngt.get("symbol"));
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 * Verify editing currency type in opportunity will still display currency value correctly in list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20317_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change the currency type from USD to Euro.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		
		// TODO: VOOD-983 Need lib support for currency dropdowns in RLI
		new VoodooControl("div", "css", ".currency.edit.fld_currency_id div").click();
		new VoodooControl("li", "css", "#select2-drop ul li:nth-child(2)").click();
		sugar().revLineItems.recordView.save();

		// Verify currency type in the Record view.
		new VoodooControl("div", "css", ".fld_total_amount.detail.disabled div").assertContains(currencyMngt.get("changed_value"), true);

		// Verify in the list view, Converted Amount should be USD 10,000. 
		sugar().opportunities.navToListView();
		new VoodooControl("span", "css", ".fld_amount_usdollar").assertContains(currencyMngt.get("converted_value"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}