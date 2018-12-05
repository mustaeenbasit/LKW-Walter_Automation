package com.sugarcrm.test.studio;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24539 extends SugarTest {
	VoodooControl contactsModule,layoutSubPanelCtrl,recordViewSubPanelCtrl, fieldsBtn;
	VoodooControl moveToLayoutPanelCtrl, moveToFilter1, moveToFilter2, studioFooterLnk;
	DataSource fieldsData;
	
	public void setup() throws Exception {
		fieldsData = testData.get(testName +"_Fields");
		sugar().login();
		
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		contactsModule = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsModule.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Create a new date type field 
		VoodooControl addField = new VoodooControl("input", "css", "input[value='Add Field']");
		VoodooControl inputFieldName = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveField = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		
		addField.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#type option[value='date']").click();
		VoodooUtils.waitForReady();
		inputFieldName.set(fieldsData.get(0).get("module_field_name"));
		VoodooUtils.waitForReady();
		saveField.click();
		VoodooUtils.waitForReady(30000);
		
		// Create a new Datetime type field
		addField.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#type option[value='datetimecombo']").click();
		VoodooUtils.waitForReady();
		inputFieldName.set(fieldsData.get(1).get("module_field_name"));
		saveField.click();
		VoodooUtils.waitForReady(30000);
		
		// Create a new Integer type field
		addField.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#type option[value='int']").click();
		VoodooUtils.waitForReady();
		inputFieldName.set(fieldsData.get(2).get("module_field_name"));
		saveField.click();
		VoodooUtils.waitForReady(30000);
		
		studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		contactsModule.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add custom fields to Record view
		// TODO: VOOD-938
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		moveToFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		moveToFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dateTypeField = String.format("div[data-name=%s]",fieldsData.get(0).get("display_name"));
		String dateTimeTypeField = String.format("div[data-name=%s]",fieldsData.get(1).get("display_name"));
		String textTypeField = String.format("div[data-name=%s]",fieldsData.get(2).get("display_name"));
		new VoodooControl("div", "css", dateTypeField).dragNDrop(moveToFilter1);
		new VoodooControl("div", "css", dateTimeTypeField).dragNDrop(moveToFilter2);
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		new VoodooControl("div", "css", textTypeField).dragNDrop(moveToFilter1);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Check calculation with dayofweek function in the formula of calculated field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24539_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		VoodooUtils.focusDefault();
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		contactsModule.click();
		VoodooUtils.waitForReady();
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Edit a field, make it calculated field
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl editFormula = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInput = new VoodooControl("textarea", "id", "formulaInput");
		VoodooControl fomulaSaveButton = new VoodooControl("input", "css", "#formulaBuilderWindow #fomulaSaveButton");
		VoodooControl savebtn = new VoodooControl("input", "css", "input[name='fsavebtn']");
		
		new VoodooControl("td", "xpath", "//*[@class='yui-dt-bd']/table/tbody[2]/tr[3]/td[contains(.,'description')]").click();
		VoodooUtils.waitForReady();
		calculatedCtrl.click();
		VoodooUtils.waitForReady();
		editFormula.click();
		formulaInput.set(fieldsData.get(0).get("formula"));
		fomulaSaveButton.click();
		VoodooUtils.waitForReady(30000);
		savebtn.click();
		VoodooUtils.waitForReady(30000);
		
		// Edit newly created field, make it calculated field
		new VoodooControl("a", "id", fieldsData.get(2).get("display_name")).click();
		VoodooUtils.waitForReady();
		calculatedCtrl.click();
		VoodooUtils.waitForReady();
		editFormula.click();
		VoodooUtils.waitForReady();
		formulaInput.set(fieldsData.get(1).get("formula"));
		VoodooUtils.waitForReady();
		fomulaSaveButton.click();
		VoodooUtils.waitForReady(30000);
		savebtn.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		
		// Create record with valid date
		sugar().contacts.recordView.getEditField("lastName").set(fieldsData.get(0).get("record_name"));
		new VoodooControl("input", "css", "[aria-label='"+fieldsData.get(0).get("module_field_name")+"']").set(fieldsData.get(0).get("valid_date"));
		new VoodooControl("input", "css", "[aria-label='"+fieldsData.get(1).get("module_field_name")+"']").set(fieldsData.get(1).get("valid_date"));
		new VoodooControl("a", "css", ".btn-toolbar.pull-right .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().contacts.listView.clickRecord(1);
		VoodooUtils.waitForAlertExpiration();
		sugar().contacts.recordView.showMore();
		
		// Verify, the input in formula field is valid for valid date.
		new VoodooControl("span", "css", "[data-voodoo-name='description']").assertContains("1", true);
		new VoodooControl("span", "css", "[data-voodoo-name='"+fieldsData.get(2).get("display_name")+"']").assertContains("6", true);
		
		// Edit record with Invalid date.
		sugar().contacts.recordView.edit();
		new VoodooControl("input", "css", "[aria-label='"+fieldsData.get(0).get("module_field_name")+"']").set(fieldsData.get(0).get("invalid_date"));
		new VoodooControl("input", "css", "[aria-label='"+fieldsData.get(1).get("module_field_name")+"']").set(fieldsData.get(1).get("invalid_date"));
		new VoodooControl("a", "css", ".btn-toolbar.pull-right .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify, If the formula field input data is invalid, don't display any data in the calculated field.
		new VoodooControl("span", "css", "[data-voodoo-name='description']").assertContains("", true);
		new VoodooControl("span", "css", "[data-voodoo-name='"+fieldsData.get(2).get("display_name")+"']").assertContains("", true);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}