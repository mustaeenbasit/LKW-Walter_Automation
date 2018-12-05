package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_26494 extends SugarTest{
	DataSource ds;
	FieldSet fsRLI;
	OpportunityRecord myOpp;
	RevLineItemRecord myRLI;
	String dataNameDraggableField;
	VoodooControl saveFilterBtnCtrl,saveBtnCtrl,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,rliButtonCtrl;

	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);
		fsRLI = testData.get(testName+"_"+"rli").get(0);
		sugar().accounts.api.create();
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myRLI = (RevLineItemRecord) sugar().revLineItems.api.create();

		rliButtonCtrl = new VoodooControl("a", "id", "studiolink_RevenueLineItems");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}

	/** 
	 * Verify that "is equal to" works correctly with custom Float or Decimal field in filter creation
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_26494_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin->Studio->RLI->Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		rliButtonCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-938
		new VoodooControl("td", "id", "fieldsBtn").click();	
		VoodooUtils.waitForReady();

		// Create a Decimal field and float field
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl typeCtrl = new VoodooControl("select", "id", "type");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveCtrl= new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		for (int i=0; i<ds.size();i++){
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			typeCtrl.set(ds.get(i).get("dataType"));
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(ds.get(i).get("module_field_name"));
			saveCtrl.click();
			VoodooUtils.waitForReady(30000);
		}

		//  Add fields in Record, List, and Search views.
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		rliButtonCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilterDecimal =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		VoodooControl moveToNewFilterFloat = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanelDecimal = String.format("div[data-name=%s_c]",ds.get(0).get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanelDecimal).dragNDrop(moveToNewFilterDecimal);
		String dataNameDraggableFieldToRecordSubpanelFloat = String.format("div[data-name=%s_c]",ds.get(1).get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanelFloat).dragNDrop(moveToNewFilterFloat);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		rliButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		// Listview
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl dropHere = new VoodooControl("td", "css", "#Default");
		for(int i=0;i<ds.size();i++){
			new VoodooControl("li", "css", ".draggable[data-name='"+ds.get(i).get("module_field_name")+"_c']").dragNDrop(dropHere);
			VoodooUtils.waitForReady();
		}
		VoodooControl saveBtnCtrl=new VoodooControl("input", "css" ,"input[name='savebtn']");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		sugar().admin.studio.getControl("studioButton").click();
		VoodooUtils.waitForReady();
		rliButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Searchview
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		for(int i=0;i<ds.size();i++){
			new VoodooControl("li", "css", ".draggable[data-name='"+ds.get(i).get("module_field_name")+"_c").dragNDrop(moveHere);
			VoodooUtils.waitForReady();
		}
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Edit the RLI record with custom decimal and float fields with value is equal to 12.3 for decimal 12.0 for float
		myRLI.navToRecord();
//		sugar().revLineItems.navToListView();
//		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();
		sugar().revLineItems.recordView.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());

		// TODO: VOOD-1036
		VoodooControl decimalInputCtrl = new VoodooControl("input", "css", "input[name='mydecimal_c']");
		decimalInputCtrl.set(fsRLI.get("decimal_value"));
		new VoodooControl("input", "css", "[name='myfloat_c']").set(fsRLI.get("float_value"));
		sugar().revLineItems.recordView.save();

		// Create a filter, for Decimal field, select "is equal to" operator, enter value from float field, such as 12.0
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.openFilterDropdown();
		sugar().revLineItems.listView.selectFilterCreateNew();
		new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_name.detail").set(fsRLI.get("decimal_field"));
		VoodooUtils.waitForReady();
		new VoodooSelect("span", "css", "[data-filter='row']:nth-of-type(1) .fld_filter_row_operator.detail").set("is equal to");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".detail.fld_mydecimal_c input").set(fsRLI.get("float_value"));
		VoodooUtils.waitForReady();

		VoodooUtils.pause(2000); // required for list to populate
		sugar().revLineItems.listView.assertIsEmpty();

		// Now enter the correct value in filter field, such as 12.3
		new VoodooControl("input", "css", ".detail.fld_mydecimal_c input").set(fsRLI.get("decimal_value"));
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.pause(2000); // required for list to populate

		// Verify that "is equal to" works correctly with custom Float or Decimal field in filter creation
		sugar().revLineItems.listView.verifyField(1, "name", sugar().revLineItems.getDefaultData().get("name"));

		// Cancel filter
		new VoodooControl("a", "css", ".filter-close").click();
		sugar().alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}