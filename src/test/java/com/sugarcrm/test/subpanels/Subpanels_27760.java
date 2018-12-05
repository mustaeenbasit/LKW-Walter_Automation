package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_27760 extends SugarTest {
	FieldSet customData =  new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().notes.api.create();
		sugar().login();
		
		// Linking the note to the account
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("relRelatedToModule").set(customData.get("relRelatedToModule"));
		sugar().notes.recordView.getEditField("relRelatedToValue").set(customData.get("relRelatedToValue"));
		sugar().notes.recordView.save();
	}
	/**
	 * Dependent field does (too) [not] display value in (Account's notes) subpanel [or inline edit]
	 * @throws Exception
	 */
	
	@Test
	public void Subpanels_27760_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1504
		// Controls in Fields section
		VoodooControl notesCtrl = new VoodooControl("a", "id", "studiolink_Notes");
		VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl dependentCheckBox =  new VoodooControl("input", "id", "dependent");
		VoodooControl editFormulaBtn =  new VoodooControl("input", "css", "#visFormulaRow [name='editFormula']");
		VoodooControl formulaInputArea = new VoodooControl("textarea", "id", "formulaInput");
		VoodooControl formulaSaveButton = new VoodooControl("input", "id", "fomulaSaveButton");
		VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
	
		// Controls in Layout section
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewCtrl =  new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl dropdownPositionOnBusinessCardPanel =  new VoodooControl("div", "css", "[data-name='filename']");
		VoodooControl fieldPositionOnBusinessCardPanel =  new VoodooControl("div", "css", "[data-name='assigned_user_name']");
		VoodooControl testDropdownField =  new VoodooControl("div", "css", "[data-name='"+customData.get("fieldName1")+"_c']");
		VoodooControl testField =  new VoodooControl("div", "css", "[data-name='"+customData.get("fieldName2")+"_c']");
		VoodooControl layoutSaveDeployButton = new VoodooControl("input", "id", "publishBtn");
		
		// Controls in Accounts section
		VoodooControl accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl subpanelCtrl =  new VoodooControl("td", "id", "subpanelsBtn");
		VoodooControl notesSubpanelCtrl =  new VoodooControl("tr", "css", "tr:nth-child(1) td:nth-child(4)");
		VoodooControl defaultPanelCtrl =  new VoodooControl("li", "css", "[data-name='assigned_user_name']");
		VoodooControl adminSaveDeployButton = new VoodooControl("input", "id", "savebtn");

		// TODO: VOOD-1036: Need library support for Accounts/any sidecar module for newly created custom fields
		// Controls for the custom dropdown and input field on Notes and Accounts record view
		VoodooSelect testDropdownCtrl =  new VoodooSelect("span", "class", "fld_"+customData.get("fieldName1")+"_c");
		VoodooControl testFieldCtrl = new VoodooControl("input", "css", ".fld_"+customData.get("fieldName2")+"_c.edit [name='"+customData.get("fieldName2")+"_c']");
		VoodooControl integerFieldCtrl = new VoodooControl("span", "css", ".fld_"+customData.get("fieldName2")+"_c");
		
		// Navigate to Admin > Studio > Notes > Fields > Add a custom field type: DropDown > Save
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		notesCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(customData.get("dataType1"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(customData.get("fieldName1")); 
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Navigate back to Studio (Footer Pane) >  Notes > Fields > Add a custom field type: integer > Save
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		notesCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(customData.get("dataType2"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(customData.get("fieldName2")); 
		dependentCheckBox.set(Boolean.toString(true));
		editFormulaBtn.click();
		VoodooUtils.waitForReady();
		formulaInputArea.set(customData.get("formula"));
		formulaSaveButton.click();
		VoodooUtils.waitForReady();
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		// Adding dropdown menu and Integer fields to the Notes module Record View layout
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		notesCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();
		testDropdownField.dragNDrop(dropdownPositionOnBusinessCardPanel);
		testField.dragNDrop(fieldPositionOnBusinessCardPanel);
		layoutSaveDeployButton.click();
		VoodooUtils.waitForReady();
		
		// Navigating to Studio > Accounts > Subpanels > Notes and adding Dropdown 
		// menu and Integer fields to the Notes Subpanel
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		subpanelCtrl.click();
		VoodooUtils.waitForReady();
		notesSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		testField.scrollIntoView();
		VoodooUtils.waitForReady();
		testDropdownField.dragNDrop(defaultPanelCtrl);
		testField.dragNDrop(defaultPanelCtrl);
		VoodooUtils.waitForReady();
		adminSaveDeployButton.click();
		VoodooUtils.waitForReady();
		
		// Navigating to the Note record
		VoodooUtils.focusDefault();
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		
		// Selecting 'Customer' in dropdown and setting integer in the text field
		testDropdownCtrl.set(customData.get("dropDownValue"));
		testFieldCtrl.waitForVisible();
		testFieldCtrl.set(customData.get("testFieldValue"));
		sugar().notes.recordView.save();
		
		// Clicking the Account link on the Note Record view
		sugar().notes.recordView.getDetailField("relRelatedToValue").click();
		
		StandardSubpanel notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoViewIfNeeded(false);
		notesSubpanel.expandSubpanel();
		integerFieldCtrl.scrollIntoViewIfNeeded(false);
		
		// Verifying that the Note Subpanel shows up the integer value as saved above
		integerFieldCtrl.assertEquals(customData.get("testFieldValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}