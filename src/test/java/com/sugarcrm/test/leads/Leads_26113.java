package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_26113 extends SugarTest {
	FieldSet currencySet = new FieldSet();
	FieldSet customData = new FieldSet();			
	VoodooControl leadsBtnCtrl, rliBtnCtrl, resetButtonCtrl, resetClickCtrl,
	relationshipsCtrl, fieldsCtrl, labelsCtrl, layoutsCtrl, extensionsCtrl, layoutBtnCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		currencySet = testData.get(testName+"_currency_settings").get(0);

		// create a lead record
		sugar().leads.api.create();

		// create a opportunities record
		sugar().opportunities.api.create();

		// Login
		sugar().login();

		// Create a new currency i.e Euro
		sugar().admin.setCurrency(currencySet);

		// navigate to admin tools
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// navigate to studio panel
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		leadsBtnCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		// navigate to leads module inside studio  
		leadsBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Define controls for studio
		// TODO: VOOD-1504. VOOD-1506
		VoodooControl fieldBtnCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "input[name='addfieldbtn'][value='Add Field']");
		VoodooControl fieldTypeCtrl = new VoodooControl("select", "id", "type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl viewBtnRecordviewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		layoutBtnCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl rowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		VoodooControl layoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		VoodooControl fieldCtrl = new VoodooControl("div", "css", "[data-name='rli_lead_currency_c']");
		VoodooControl rowFilterCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		VoodooControl publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");
		rliBtnCtrl = new VoodooControl("a", "id", "studiolink_RevenueLineItems");

		// Add Currency type custom field in Leads 
		fieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		// set field type as Currency
		fieldTypeCtrl.set(customData.get("fieldType"));
		VoodooUtils.waitForReady();
		// set currency name as RLI_Lead_currency.
		fieldNameCtrl.set(customData.get("fieldName"));
		// click save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// navigate to leads layout panel to add currency custom field
		// TODO: VOOD-1506
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		leadsBtnCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		viewBtnRecordviewCtrl.click();
		VoodooUtils.waitForReady();
		rowCtrl.dragNDrop(layoutPanelCtrl);
		VoodooUtils.waitForReady();
		fieldCtrl.dragNDrop(rowFilterCtrl);
		VoodooUtils.waitForReady();
		publishButtonCtrl.click();
		VoodooUtils.waitForReady();

		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();

		// navigate to RLI module inside studio
		rliBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add Currency type custom field in Leads 
		fieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();

		// set field type as Currency
		fieldTypeCtrl.set(customData.get("fieldType"));
		VoodooUtils.waitForReady();

		// set currency name as RLI_Lead_currency.
		fieldNameCtrl.set(customData.get("fieldName"));
		VoodooUtils.waitForReady();

		// click save
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		rliBtnCtrl.click();
		VoodooUtils.waitForReady();

		// navigate to RLI layout panel to add currency custom field
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		viewBtnRecordviewCtrl.click();
		VoodooUtils.waitForReady();
		rowCtrl.dragNDrop(layoutPanelCtrl);
		VoodooUtils.waitForReady();
		fieldCtrl.dragNDrop(rowFilterCtrl);
		VoodooUtils.waitForReady();
		publishButtonCtrl.click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1026
		// In Studio->Leads->Layouts->Convert Lead, enable RLI module and check box under Copy Data column.
		leadsBtnCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "convertSelectNewModule").set(customData.get("moduleName"));
		new VoodooControl("input", "css", "[name='addModule']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='RevenueLineItems-copyData']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='saveLayout']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();
	}

	/**
	 * non-USD currency filed in Lead is copid correctly during Lead conversion
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_26113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().leads.navToListView();

		// Open one not-converted Lead record view
		sugar().leads.listView.clickRecord(1);

		// edit the record 
		sugar().leads.recordView.edit();

		// TODO: VOOD-1036
		VoodooControl customFieldCtrl = new VoodooControl("input", "css", "[name='rli_lead_currency_c']");

		// enter 100 in RLI_Lead_currency field, but select EUR
		customFieldCtrl.set(customData.get("currencyAmount"));
		new VoodooSelect("span", "css", "[data-voodoo-name='currency_id'] .select2-container.select2 .select2-chosen").set(customData.get("currencyName"));

		// Verify that the custom currency field displays 90 EUR
		customFieldCtrl.assertContains(customData.get("changedCurrencyAmount"), true);

		// Click Save
		sugar().leads.recordView.save();

		// Click on the arrow drop down to view the action links
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// Click on Convert action link
		// TODO : VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();

		// Confirm with Accounts info
		new VoodooControl("input", "css","div[data-module='Accounts'] .fld_name.edit input").set(testName);
		new VoodooControl("a", "css","div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		//Confirm with Opportunities info		
		new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input").set(sugar().opportunities.getDefaultData().get("name"));
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Confirm with the RLI info
		// In RLI session, fill in required fields, and Associate RLI.
		new VoodooControl("input", "css","div[data-module='RevenueLineItems'] .fld_name.edit input").set(testName);
		new VoodooSelect("select", "css", ".fld_opportunity_name.edit").set(sugar().opportunities.getDefaultData().get("name"));
		new VoodooControl("input", "css", ".datepicker.required").set(customData.get("date"));
		new VoodooControl("input", "css", "[name='likely_case']").set(customData.get("likelyValue"));
		new VoodooControl("a", "css","div[data-module='RevenueLineItems'] .fld_associate_button.convert-panel-header a").click();

		// Click on Save and Convert.
		new VoodooControl("a", "css", ".convert-headerpane.fld_save_button a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Navigate to the RLI list view
		sugar().revLineItems.navToListView();

		// Open the RLI record.
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that the custom currency field displays 90 EUR.
		new VoodooControl("span", "css", ".detail.fld_rli_lead_currency_c").assertContains(customData.get("changedCurrencyAmount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}