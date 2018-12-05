package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20085 extends SugarTest {
	VoodooControl moduleCtrl;
	DataSource customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar.login();
		
		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
				
		// Create a currency type custom field as required field and enable range search in Calls Module
		// Navigate to Admin > Studio
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='currency']").click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(customData.get(0).get("fieldName"));
		new VoodooControl("input", "css", "[name='enable_range_search']").click();
		new VoodooControl("input", "css", "[name='required']").click();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Place the currency type field in the Record view 
		sugar.admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Add the currency type to the record view of account module
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row");
		VoodooControl moveToNewFilter1 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css",  "div[data-name="+customData.get(0).get("fieldName")+"_c]").dragNDrop(moveToNewFilter1);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Place the currency type field in the Search view 
		sugar.admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl dropDefaultColumnCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name="+customData.get(0).get("fieldName")+"_c]").dragNDrop(dropDefaultColumnCtrl);

		// Save & Deploy
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify the custom currency type filed range search feature can work fine for the advanced search layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_20085_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		// Create some new Calls records and fill value of custom created currency type field
		for (int i=0; i<4; i++){
			sugar.calls.navToListView();
			sugar.calls.listView.create();
			sugar.calls.createDrawer.showMore();
			sugar.calls.createDrawer.getEditField("name").set(testName +"_"+ i);
			new VoodooControl("input", "css", "[name='customfield_c']").set(customData.get(i).get("customFieldValue"));
			sugar.calls.createDrawer.save();
		}
		
		// Create filter from the Calls listview
		sugar.calls.navToListView();
		sugar.calls.listView.openFilterDropdown();
		sugar.calls.listView.selectFilterCreateNew();

		// TODO: VOOD-1766
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']");
		VoodooSelect operatorFieldCtrl = new VoodooSelect("div", "css",  "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']");
		filterFieldCtrl.set(customData.get(0).get("fieldName"));
		
		for (int i = 0; i < 5; i++){
			// Create filter with existing dropdown filed (i.e. Type) and custom dropdown filed (i.e. dropdownfield)
			if(i == 6) {
				operatorFieldCtrl.set(customData.get(i).get("operator"));
				new VoodooControl("input", "css", "[name='"+customData.get(0).get("fieldName")+"_c_min']").set(customData.get(0).get("customFieldValue"));
				new VoodooControl("input", "css", "[name='"+customData.get(0).get("fieldName")+"_c_max']").set(customData.get(1).get("customFieldValue"));
			} else {
				operatorFieldCtrl.set(customData.get(i).get("operator"));
				new VoodooControl("input", "css", "[name='"+customData.get(0).get("fieldName")+"_c']").set(customData.get(i).get("rangeValue"));
			}
			VoodooUtils.waitForReady();

			// Verify that the range search feature of custom currency field work correctly in search layout
			if(i == 0) {
				sugar.calls.listView.verifyField(1, "name", testName + "_" + i);
				sugar.calls.listView.getControl("checkbox03").assertExists(false);
			}
			if(i == 1 || i==3) {
				sugar.calls.listView.sortBy("headerName", true);
				sugar.calls.listView.verifyField(1, "name", testName + "_" + 0);
				sugar.calls.listView.verifyField(2, "name", testName + "_" + 1);
				sugar.calls.listView.verifyField(3, "name", testName + "_" + 2);
			}
			if(i == 2) {
				sugar.calls.listView.verifyField(1, "name", testName + "_" + (i+1));
			}
			if(i == 4 || i==5 || i==6) {
				sugar.calls.listView.sortBy("headerName", true);
				sugar.calls.listView.verifyField(1, "name", testName + "_" + 0);
				sugar.calls.listView.verifyField(2, "name", testName + "_" + 1);
				sugar.calls.listView.verifyField(3, "name", testName + "_" + 2);
				sugar.calls.listView.verifyField(4, "name", testName + "_" + 3);
			}	
		}
		
		// Cancel the filter page
		sugar().calls.listView.filterCreate.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}