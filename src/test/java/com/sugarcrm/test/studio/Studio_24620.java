package com.sugarcrm.test.studio;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Studio_24620  extends SugarTest {
	
	VoodooControl contactsSubPanelCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldCtrl, studioFooterCtrl;
	DataSource customData;
	ContactRecord myContactsRecord;
	DataSource ds, rliDataSource;
	ArrayList<Record> opportunitiesLinkRecords = new ArrayList<Record>();
	RevLineItemRecord myRLI1, myRLI2;
	OpportunityRecord opp1, opp2;
	FieldSet fs = new FieldSet();
	FieldSet rliRelate = new FieldSet();
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
		rliDataSource = testData.get(testName + "_rli_record");
		
		myContactsRecord = (ContactRecord) sugar().contacts.api.create();
		// create Opportunity record using api
		fs.put("name", rliDataSource.get(0).get("relOpportunityName"));
		opp1 = (OpportunityRecord)sugar().opportunities.api.create(fs);
		fs.put("name", rliDataSource.get(1).get("relOpportunityName"));
		opp2 = (OpportunityRecord)sugar().opportunities.api.create(fs);
		
		myRLI1 = (RevLineItemRecord)sugar().revLineItems.api.create(rliDataSource.get(0));
		myRLI2 = (RevLineItemRecord)sugar().revLineItems.api.create(rliDataSource.get(1));
		
		// TODO: VOOD-718
		rliRelate.put("relOpportunityName", opp1.getRecordIdentifier());
		myRLI1.edit(rliRelate);
		rliRelate.put("relOpportunityName", opp2.getRecordIdentifier());
		myRLI2.edit(rliRelate);
		
		// create a hash map of Opportunity record 
		opportunitiesLinkRecords.add(opp1);
		opportunitiesLinkRecords.add(opp2);
	}

	/**
	 *  Auto updating calculated field that contain rollupAve function when related record is deleted
	 *  @throws Exception
	 */
	@Test
	public void Studio_24620_execute() throws Exception {
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
		fieldNameCtrl.set(ds.get(0).get("field_name"));
		calculatedCtrl.click();
		editFormulaCtrl.click();
		// Set formula contain rollupAve function, such as rollupAve($opportunities,"amount")
		formulaInputCtrl.set(ds.get(0).get("formula_string"));
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();
		// save custom field
		saveFieldCtrl.click();
		VoodooUtils.waitForReady();
	
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		
		// Add the calculated custom field on detailview layouts
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]","testfield"); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// navigate to current record 
		myContactsRecord.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		// 	opportunities subpanel to link existing record  
		StandardSubpanel opportunitiesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural);
		opportunitiesSubpanel.linkExistingRecords(opportunitiesLinkRecords);
		sugar().alerts.getSuccess().closeAlert();
		
		// navigate back to contact record to assert the average value of related opportunities' amount will show in the calculated field
		//myContactsRecord.navToRecord();
		VoodooControl tempFieldCtrl = new VoodooControl("span", "css", "[data-voodoo-name='testfield_c']");
		tempFieldCtrl.assertContains(ds.get(0).get("average"), true);
		// unlink a opportunity record
		opportunitiesSubpanel.unlinkRecord(1);

		myContactsRecord.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		// assert the calculated field value auto updated to the new value
		tempFieldCtrl.assertContains(ds.get(0).get("average_one"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}