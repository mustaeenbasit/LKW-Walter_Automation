package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27084 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	DataSource ds;
	FieldSet customData, fs;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName+"_fields");
		customData = testData.get(testName).get(0);
		
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
	}

	/**
	 * Verify Parent dropdown dependency for multiselect fields
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27084_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();		
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		for(int i = 0; i < ds.size(); i ++) {
			new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
			VoodooUtils.waitForReady();
			new VoodooControl("select", "css", "#type").set(ds.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			new VoodooControl("input", "id", "field_name_id").set(ds.get(i).get("module_field_name"));
			if(i == 1) {
				// For multi-select field
				new VoodooControl("option", "css", "#depTypeSelect option[label='Parent Dropdown']").click();
				VoodooUtils.waitForReady();
				// select parent dropdown
				new VoodooControl("option", "css", "#parent_dd option[label='"+ds.get(0).get("module_field_name")+"']").click();	
				VoodooUtils.waitForReady();
				new VoodooControl("button", "css", "#visGridRow > td:nth-child(2) > button").click();
				VoodooUtils.waitForReady();
				// Drag options to dependent options
				VoodooControl moveToDependentCtrl1 = new VoodooControl("ul", "id", "ddd_Analyst_list"); 
				new VoodooControl("li", "css", "#childTable > li:nth-child(2)").dragNDrop(moveToDependentCtrl1);
				VoodooUtils.waitForReady();
				VoodooControl moveToDependentCtrl2 = new VoodooControl("ul", "id", "ddd_Competitor_list"); 
				new VoodooControl("li", "css", "#childTable > li:nth-child(3)").dragNDrop(moveToDependentCtrl2);
				VoodooUtils.waitForReady();
				// save dependent options
				new VoodooControl("li", "css", "#visGridWindow > div.bd > div:nth-child(6) > button:nth-child(2)").click();
				VoodooUtils.waitForReady();
			}			
			// create and save fields settings
			new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
			VoodooUtils.waitForReady(30000);
		}
		
		// TODO: VOOD-999//		
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// layout subpanel		
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		
		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();

		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		
		for(int i = 0; i < ds.size(); i++) {
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			VoodooUtils.waitForReady();
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(i).get("module_field_name")); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		sugar().accounts.navToListView();
		fs = new FieldSet();
		fs.put("name", customData.get("account_name"));
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.setFields(fs);
		sugar().alerts.waitForLoadingExpiration();
		
		// assert second dropdown not visible
		new VoodooControl("div", "css" ,".fld_"+ds.get(0).get("module_field_name")+"_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-drop-active > ul > li:nth-child(1)").click();
		new VoodooControl("div", "css" ,"div.record div[data-name='"+ds.get(1).get("module_field_name")+"_c'] .record-label").assertVisible(false);
		sugar().alerts.waitForLoadingExpiration();
		
		// assert second dropdown visible
		new VoodooControl("div", "css" ,".fld_"+ds.get(0).get("module_field_name")+"_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-drop-active > ul > li:nth-child(2)").click();
		new VoodooControl("div", "css" ,"div.record div[data-name='"+ds.get(1).get("module_field_name")+"_c'] .record-label").assertExists(true);
		
		// select multiple value
		new VoodooControl("div", "css" ,"div.record .fld_myfield2_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-display-none.select2-drop-active > ul > li:nth-of-type(1)").click();
		sugar().accounts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// edit account record and assert again
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().alerts.waitForLoadingExpiration();
		
		// assert second dropdown not visible
		new VoodooControl("div", "css" ,".fld_"+ds.get(0).get("module_field_name")+"_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-drop-active > ul > li:nth-child(1)").click();
		new VoodooControl("div", "css" ,"div.record div[data-name='"+ds.get(1).get("module_field_name")+"_c'] .record-label").assertVisible(false);
		sugar().alerts.waitForLoadingExpiration();
		
		// assert second dropdown visible
		new VoodooControl("div", "css" ,".fld_"+ds.get(0).get("module_field_name")+"_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-drop-active > ul > li:nth-child(2)").click();
		new VoodooControl("div", "css" ,"div.record div[data-name='"+ds.get(1).get("module_field_name")+"_c'] .record-label").assertExists(true);
		
		// select multiple value
		new VoodooControl("div", "css" ,"div.record .fld_myfield2_c.edit div").click();
		new VoodooControl("li", "css" ,"body > div.select2-display-none.select2-drop-active > ul > li:nth-of-type(1)").click();
		sugar().accounts.recordView.save();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}