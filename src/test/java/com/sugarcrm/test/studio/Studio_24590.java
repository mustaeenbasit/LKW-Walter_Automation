package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24590 extends SugarTest {
	DataSource ds, rliDataSource;
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	ContactRecord myContactsRecord;
	OpportunityRecord myOpportunityRecord;
	RevLineItemRecord myRLI1;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		rliDataSource = testData.get(testName + "_rli_record");
		myContactsRecord = (ContactRecord) sugar().contacts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", rliDataSource.get(0).get("relOpportunityName"));
		myOpportunityRecord = (OpportunityRecord) sugar().opportunities.api.create(fs);
		myRLI1 = (RevLineItemRecord)sugar().revLineItems.api.create(rliDataSource.get(0));
		
		FieldSet rliRelate = new FieldSet();
		// TODO: VOOD-718
		rliRelate.put("relOpportunityName", myOpportunityRecord.getRecordIdentifier());
		myRLI1.edit(rliRelate);
	}

	/**
	 * Auto updating calculated field that contain rollupAve function when related record created.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24590_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		// studio 
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl saveFieldCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl calculatedCtrl = new VoodooControl("input", "id", "calculated"); 
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputCtrl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");

		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();

		// Create a calculated field in Contacts module
		fieldCtrl.click();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(ds.get(0).get("module_field_name"));
		calculatedCtrl.click();
		editFormulaCtrl.click();

		// Set formula contain rollupAve function, such as rollupAve($opportunities,"amount")
		VoodooUtils.pause(500);
		formulaInputCtrl.set(ds.get(0).get("formula_string"));
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		// Save custom field
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
	
		studioFooterCtrl.click();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		
		// Add the calculated custom field on record view layouts
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",ds.get(0).get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Verify that custom field is empty when no opportunity record is linked  
		myContactsRecord.navToRecord();
		new VoodooControl("span", "css", "[data-voodoo-name='myfield_c']").assertContains("",true);

		// 	Get the opportunities subpanel to link existing opportunity record  
		StandardSubpanel opportunitiesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.linkExistingRecord(myOpportunityRecord);
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that custom "relate" field is now populated with opportunity likely amount  
		new VoodooControl("span", "css", "[data-voodoo-name='myfield_c']").assertContains(ds.get(0).get("custom_field_value"), true);
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}