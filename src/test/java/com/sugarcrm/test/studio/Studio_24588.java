package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24588 extends SugarTest {
	VoodooControl leadsButtonCtrl;
	FieldSet customData;
	LeadRecord myLead;

	public void setup() throws Exception {
		// TODO: VOOD-542
		leadsButtonCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 

		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		VoodooControl saveAndDeployBtnCtrl = new VoodooControl("input", "id", "publishBtn");

		VoodooControl addNewFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl formulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl fomulaSaveButtonCtrl = new VoodooControl("input", "id", "fomulaSaveButton");

		VoodooControl fieldSaveButtonCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");

		customData = testData.get(testName).get(0);
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().contacts.api.create();
		sugar().login();

		// Navigate to Admin > Studio > Leads > Fields 		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Add field and save
		addNewFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(customData.get("module_field_name"));
		calculatedCtrl.click();
		VoodooUtils.waitForReady();
		formulaCtrl.click();

		// Add related($contacts,"title")
		String formulaWithCloseParenthesis = String.format("%srelated($contacts,\"title\")", formulaInputCtrl.getAttribute("value"));		
		formulaInputCtrl.set(formulaWithCloseParenthesis);

		// Save Formula
		fomulaSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Save Field
		fieldSaveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Leads Layouts
		sugar().admin.studio.clickStudio();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add new field to Leads List view
		listViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Goto Leads Recordview Layout
		sugar().admin.studio.clickStudio();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add new field to Leads Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		saveAndDeployBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Auto updating calculated field that contain related function when new 
	 * related record created.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24588_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link lead record to the contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel leardSubPanel = sugar().contacts.recordView.subpanels.get("Leads");
		leardSubPanel.scrollIntoView();
		leardSubPanel.linkExistingRecord(myLead);

		// Verify if the new formula field in recordView contains correct value
		leardSubPanel.clickRecord(1);
		String customInputField = String.format(".fld_%s_c .ellipsis_inline",customData.get("module_field_name")); 
		VoodooControl formulaResultCtrl = new VoodooControl("div", "css", customInputField);
		String contactTitle = sugar().contacts.getDefaultData().get("title");
		formulaResultCtrl.assertEquals(contactTitle, true);

		// Verify if the new formula field in listView contains correct value
		sugar().leads.navToListView();
		formulaResultCtrl.assertEquals(contactTitle, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}