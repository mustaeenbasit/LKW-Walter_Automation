package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20064 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl, studioFooterCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the range search feature works fine for custom date type filed
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20064_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubPanelCtrl.click();
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldCtrl.click();
		
		// TODO: VOOD-938
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input:nth-of-type(1)").click();
		new VoodooControl("option", "css", "select[name='type'] option:nth-of-type(4)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "input[name=required]").click();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooterCtrl.click();
		accountsSubPanelCtrl.click();
		
		// TODO: VOOD-938
		// layout subpanel
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();

		// TODO: VOOD-938
		// Record view
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableRecordViewSubpanel = String.format("div[data-name=%s]",customData.get("module_field_name")+"_c"); 
		
		// Expected Result: the date type custom field can be added to the record view layout
		VoodooControl customFieldForRecordViewCtrl = new VoodooControl("div", "css", dataNameDraggableRecordViewSubpanel);
		customFieldForRecordViewCtrl.assertVisible(true);
		customFieldForRecordViewCtrl.dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Search view
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		studioFooterCtrl.click();
		accountsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
		defaultSearchCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",customData.get("module_field_name")+"_c"); 
		
		// Expected Result: the date type custom field can be added to the search view layout
		VoodooControl customFieldForSearchCtrl = new VoodooControl("li", "css", dataNameDraggableLi);
		customFieldForSearchCtrl.assertVisible(true);
		
		customFieldForSearchCtrl.dragNDrop(defaultSearchCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Multiple account records
		DataSource ds = testData.get(testName+"_1");
		
		// TODO: VOOD-935
		VoodooControl customFieldCtrl = new VoodooControl("input", "css", ".input-append.date input");
		for (int i = 0; i < ds.size(); i++) {
			sugar().accounts.navToListView();
			sugar().accounts.listView.create();
			sugar().accounts.createDrawer.getEditField("name").set(ds.get(i).get("account_name"));
			customFieldCtrl.set(ds.get(i).get("date_account"));
			sugar().accounts.createDrawer.save();
			VoodooUtils.waitForReady();
		}

		sugar().alerts.closeAllSuccess();

		// Filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();
		
		// TODO: VOOD-1462
		VoodooControl selectCtrl = new VoodooControl("a", "css", ".select2-container.select2 .select2-choice.select2-default");
		selectCtrl.click();
		// Xpath used for dynamic field
		VoodooControl customFieldLiCtrl =  new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[contains(.,'test aj')]");
		customFieldLiCtrl.click();
		VoodooControl filterConditionCtrl = new VoodooControl("a", "css", ".controls.span4 .select2-container .select2-choice.select2-default");
		filterConditionCtrl.click();

		// First filter with 'before'
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-of-type(2)").click();
		new VoodooControl("input", "css", ".datepicker.inherit-width").set(customData.get("date_filter"));
		new VoodooControl("button", "css", "button[data-action='add']").click();
		selectCtrl.click();
		customFieldLiCtrl.click();
		filterConditionCtrl.click();
		
		// second filter with 'after'
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-of-type(3)").click();
		new VoodooControl("input", "css", ".filter-definition-container .filter-body:nth-of-type(2) .datepicker").set(customData.get("date_filter"));
		VoodooUtils.waitForReady();
		// Verify, No data-record expected with given conditions
		new VoodooControl("div", "css", "div[data-voodoo-name='list-bottom'] .block-footer").assertContains(customData.get("expected_value"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}

