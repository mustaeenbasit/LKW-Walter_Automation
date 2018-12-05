package com.sugarcrm.test.admin;
import org.junit.Test;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_20316 extends SugarTest {
	FieldSet moduleData, currencyData, fieldData;

	VoodooControl modulebuilderCtrl;
	VoodooControl breadCrumbCtrl;
	VoodooControl defaultSubPanelCtrl;
	VoodooControl deployCtrl;

	VoodooControl fieldCtrl;
	VoodooControl addNewFieldCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl displayLabelCtrl;
	VoodooControl fieldSaveButtonCtrl;
	VoodooControl studioFooterCtrl;

	VoodooControl accountsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl listViewButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl subpanelViewButtonCtrl;
	VoodooControl subpanelTestButtonCtrl;
	VoodooControl publishButtonCtrl;
	VoodooControl historyDefaultCtrl;
	VoodooControl deletePackageCtrl;
	
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl defaultPanelListViewCtrl;
	VoodooControl hiddenPanelListViewCtrl;
	VoodooControl saveBtnCtrl;

	VoodooControl resetButtonCtrl;
	VoodooControl resetClickCtrl;
	VoodooControl relationshipsCtrl;
	VoodooControl fieldsCtrl;
	VoodooControl layoutsCtrl;
	VoodooControl labelsCtrl;
	VoodooControl extensionsCtrl;

	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		
		moduleData = testData.get(testName).get(0);	
		currencyData = testData.get(testName+"_currency").get(0);
		
		// As currency does not get deleted, its name is made unique by making it based upon test name
		currencyData.put("name", testName); 

		fieldData = testData.get(testName+"_field").get(0);
		
		String customModuleLink = "studiolink_" + moduleData.get("data_module");

		// TODO: VOOD-938
		modulebuilderCtrl = new VoodooControl("a", "id" ,"moduleBuilder");
		breadCrumbCtrl = new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)");
		deployCtrl = new VoodooControl("input", "css" ,"input[name='deploybtn']");

		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		addNewFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		displayLabelCtrl = new VoodooControl("input", "id", "label_value_id");
		fieldSaveButtonCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		subpanelViewButtonCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelTestButtonCtrl = new VoodooControl("td", "xpath", "//*[@id='Buttons']/table/tbody/tr[contains(.,'"+moduleData.get("plural_label")+"')]/td[contains(.,'"+moduleData.get("plural_label")+"')]");
		deletePackageCtrl =  new VoodooControl("input", "css", ".yui-dt-liner input[value='Delete Package']");

		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(1)"); 
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) > div.le_row > .le_field.special:nth-of-type(2)"); 
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		historyDefaultCtrl = new VoodooControl("td", "id", "historyDefault");
		publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");

		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		
		// Module Builder
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		modulebuilderCtrl.click();

		// create package
		// TODO: VOOD-933
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady();

		// create module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(moduleData.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "id" ,"type_company").click();		
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();

		// Add relationship 1-Many
		new VoodooControl("input", "css" ,"input[name='viewrelsbtn']").click();
		new VoodooControl("input", "css" ,"input[name='addrelbtn']").click();
		VoodooUtils.pause(3000); // Wait because of product related issue involving saving after navigating to certain studio pages.
		new VoodooControl("input", "css" ,"input[name='saverelbtn']").click();
		VoodooUtils.waitForReady();

		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		breadCrumbCtrl.waitForVisible();
		breadCrumbCtrl.click();
		VoodooUtils.waitForReady();

		// Deploy
		deployCtrl.waitForVisible();
		deployCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1010
		// This pause is required for deploy
		VoodooUtils.pause(60000);
		studioFooterCtrl.click();

		// New module is a subpanel under Accounts module
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#Buttons table tbody tr:nth-of-type(3) td:nth-of-type(3)").click();
		VoodooUtils.waitForReady();
		defaultSubPanelCtrl = new VoodooControl("ul", "id" ,"ul0");
		new VoodooControl("a", "id" ,"subslot11").dragNDrop(defaultSubPanelCtrl);
		VoodooUtils.pause(3000); // Wait because of product related issue involving saving after navigating to certain studio pages.
		new VoodooControl("input", "id" ,"savebtn").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();

		// Navigate to Admin > Studio > New Module > Fields 	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", customModuleLink).click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		addNewFieldCtrl.click();

		// Select Data Type
		new VoodooControl("select", "id", "type").set(fieldData.get("dataType"));
		VoodooUtils.waitForReady();
		
		fieldNameCtrl.set(fieldData.get("name"));
		// Field Save button
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		studioFooterCtrl.click();
		new VoodooControl("a", "id", customModuleLink).click();

		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add new field to Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",fieldData.get("name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);
		// Save and Deploy
		publishButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add new field to List View		
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();

		new VoodooControl("a", "id", customModuleLink).click();
		VoodooUtils.waitForReady();
		
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		listViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		defaultPanelListViewCtrl.waitForVisible();

		String dataNameDraggableLi = String.format("li[data-name=%s_c]",fieldData.get("name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add new Currency Field to Accounts -> Tests subpanel
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();

		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		subpanelViewButtonCtrl.click();
		VoodooUtils.waitForReady();

		subpanelTestButtonCtrl.click();
		VoodooUtils.waitForReady();

		defaultPanelListViewCtrl.waitForVisible();
		dataNameDraggableLi = String.format("li[data-name=%s_c]",fieldData.get("name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultPanelListViewCtrl);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// All done. Now change focus to default.
		VoodooUtils.focusDefault();

		// Create custom currency
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-840
		new VoodooControl("a", "css", "#currencies_management").click();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO VOOD-840
		new VoodooControl("input", "css", "table.edit.view input[name='name']").set(currencyData.get("name"));
		new VoodooControl("input", "css", "table.edit.view input[name='conversion_rate']").set(currencyData.get("rate"));
		new VoodooControl("input", "css", "table.edit.view input[name='symbol']").set(currencyData.get("symbol"));
		new VoodooControl("input", "css", "table.edit.view input[name='iso4217']").set(currencyData.get("code"));
		new VoodooControl("input", "css", "input[value='Save']").click();
		VoodooUtils.pause(1000); // Wait for processing to complete
		
		VoodooUtils.focusDefault();
		
		// account record from API
		myAccount= (AccountRecord) sugar().accounts.api.create();
	}

	/**
	 * Verify that the Currency Symbol on SubPanel Listview for custom module is correctly displayed 
	 * @throws Exception
	 */
	@Test
	public void Admin_20316_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String dataSubpanelLink = moduleData.get("data_module").toLowerCase()+"_accounts";
		String dataFieldName = String.format("fld_%s_c",fieldData.get("name"));
		
		myAccount.navToRecord();

		// Click new Subpanel
		new VoodooControl("div", "css", "div[data-subpanel-link='"+dataSubpanelLink+"']").click();
		
		// Click + button of Subpanel
		new VoodooControl("a", "css", "div.filtered.tabbable.tabs-left.layout_test1_Test > ul > li > div.subpanel-header > div.subpanel-controls.btn-toolbar.pull-right > span.actions.btn-group.pull-right.panel-top > span > a").click();
		
		// New module Edit Record - Create a record in new module
		new VoodooControl("input", "name", "name").set(moduleData.get("singular_label"));
		new VoodooControl("span", "css", "span.currency.edit.fld_currency_id span.select2-chosen").click();
		new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[contains(.,'"+currencyData.get("code")+"')]").click();
		new VoodooControl("a", "css", "div.layout_test1_Test a[name='save_button']").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Control returns to Accounts record view. Now expand new module Subpanel.
		if (new VoodooControl("li", "css", "div[data-subpanel-link='"+dataSubpanelLink+"'] > ul > li").getAttribute("class").contains("closed"))
			new VoodooControl("a", "css", "div[data-subpanel-link='"+dataSubpanelLink+"'] > ul > li a.btn.btn-invisible").click();

		// Assert Currency Symbol in subpanel of Accounts
		VoodooUtils.voodoo.log.info(">>>>>>>>>>>>>> " + new VoodooControl("span", "css", "span."+dataFieldName+".list").getText());
		new VoodooControl("span", "css", "span."+dataFieldName+".list").hover();
		new VoodooControl("span", "css", "span."+dataFieldName+".list").assertContains(currencyData.get("symbol"), true);

		// Nav to new module
		sugar().navbar.showAllModules();
		new VoodooControl("li", "css", "div.dropdown-menu.scroll ul[data-container='overflow'] li[data-module='"+moduleData.get("data_module")+"']").click(); 
		sugar().alerts.waitForLoadingExpiration();

		// Assert Currency Symbol in list view of custom module
		new VoodooControl("span", "css", "span."+dataFieldName+".list").assertContains(currencyData.get("symbol"), true);
		
		// Goto recordView 
		new VoodooControl("a", "css", ".fld_name.list a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Assert Currency Symbol in record view of custom module
		new VoodooControl("span", "css", "span."+dataFieldName+".detail").assertContains(currencyData.get("symbol"), true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
