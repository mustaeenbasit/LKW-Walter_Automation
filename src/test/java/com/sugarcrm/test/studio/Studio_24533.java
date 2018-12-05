package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24533 extends SugarTest {
	VoodooControl accountsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl addNewFieldCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl formulaInputCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl formulaResultCtrl;

	VoodooControl calculatedCtrl;
	VoodooControl formulaCtrl;
	VoodooControl fomulaSaveButtonCtrl;
	
	VoodooControl fieldSaveButtonCtrl;
	VoodooControl studioFooterCtrl;
	VoodooControl accountsBreadcrumbCtrl;
	VoodooControl layoutsBreadcrumbCtrl;
	VoodooControl listViewButtonCtrl;
	VoodooControl historyDefaultCtrl;
	VoodooControl publishButtonCtrl;

	VoodooControl resetButtonCtrl;
	VoodooControl resetClickCtrl;
	VoodooControl relationshipsCtrl;
	VoodooControl fieldsCtrl;
	VoodooControl layoutsCtrl;
	VoodooControl labelsCtrl;
	VoodooControl extensionsCtrl;

	FieldSet customData;
	AccountRecord myAccount;

	public void setup() throws Exception {
		sugar().login();
		
		// TODO: VOOD-938
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		addNewFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)");
		
		calculatedCtrl = new VoodooControl("input", "id", "calculated");
		formulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		fomulaSaveButtonCtrl = new VoodooControl("input", "id", "fomulaSaveButton");

		fieldSaveButtonCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		accountsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[4]");
		layoutsBreadcrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[5]");
		listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		historyDefaultCtrl = new VoodooControl("td", "id", "historyDefault");
		publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");

		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");		
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		customData = testData.get(testName).get(0);
		
		// Navigate to Admin > Studio > Accounts > Fields	
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		
		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add checkbox field
		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		
		addNewFieldCtrl.click();
		new VoodooControl("select", "id", "type").set("Checkbox");
		VoodooUtils.waitForReady();
		
		fieldNameCtrl.set(customData.get("module_checkbox_name"));

		// Save button
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add Calculated Field with Formula
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Goto Fields
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		addNewFieldCtrl.click();
		VoodooUtils.waitForReady();
		
		fieldNameCtrl.set(customData.get("module_field_name"));
		calculatedCtrl.click();
		formulaCtrl.click();
		VoodooUtils.waitForReady();
		
		String myFormula = "ifElse(contains($name,\"*\"),\" \",ifElse($mycheckbox_c,strToUpper($name),strToLower($name)) )";
		formulaInputCtrl.set(myFormula);
		VoodooUtils.waitForReady();

		// Save formula
		fomulaSaveButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Save button
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Layouts section
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsButtonCtrl.click();		
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add two new fields to Accounts Record view
		recordViewButtonCtrl.click();	
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		
		// Drag Checkbox field to recordview
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_checkbox_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);

		// Drag Formula field to recordview
		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 		
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);

		publishButtonCtrl.click();
		VoodooUtils.waitForReady(30000);

		VoodooUtils.focusDefault();
	}

	/**
	 * Check calculation with ifElse function in the formula of calculated field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24533_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Create an example rec in Accounts module
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("name"));
		myAccount = (AccountRecord)sugar().accounts.create(fs);
		VoodooUtils.waitForAlertExpiration();
		
		myAccount.navToRecord();
		
		// TODO: VOOD-935
		String customChecboxInputField = String.format(".fld_%s_c.edit input",customData.get("module_checkbox_name")); 
		String customInputField = String.format(".fld_%s_c.detail .ellipsis_inline",customData.get("module_field_name")); 
		formulaResultCtrl = new VoodooControl("div", "css", customInputField);
		
		// Valid Name, checkbox false, myformula should be small
		formulaResultCtrl.assertEquals(customData.get("name").toLowerCase(), true);
		
		sugar().accounts.recordView.edit();

		// Valid Name, checkbox true, myformula should be caps
		new VoodooControl("input", "css", customChecboxInputField).set("true");
		sugar().accounts.recordView.save();
		formulaResultCtrl.assertEquals(customData.get("name").toUpperCase(), true);
		
		sugar().accounts.recordView.edit();

		// Invalid Name, checkbox true, myformula should be nil
		sugar().accounts.recordView.getEditField("name").set(customData.get("name")+"*");		
		sugar().accounts.recordView.save();

		formulaResultCtrl.assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
