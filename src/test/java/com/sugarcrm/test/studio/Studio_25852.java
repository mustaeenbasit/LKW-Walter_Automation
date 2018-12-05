package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_25852 extends SugarTest {
	DataSource customData = new DataSource();
	VoodooControl layoutsButtonCtrl, recordViewButtonCtrl, moveToLayoutPanelCtrl, moveToNewFilter, fieldsBtn, opportunitySubPanelCtrl, studioFooterLnk;
	OpportunityRecord myOpportunity1, myOpportunity2;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Custom dropdown and Mass update
	 *
	 * @throws Exception
	 */
	@Test
	public void Studio_25852_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		customData = testData.get(testName);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// Add a dropdown list
		new VoodooControl("a", "id", "dropdowneditor").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='adddropdownbtn']").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-938
		new VoodooControl("input", "css", "input[name='dropdown_name']").set(customData.get(0).get("name"));
		VoodooControl dropdownNameCtrl = new VoodooControl("input", "css", "input[name='drop_name']");
		VoodooControl dropdownValueCtrl = new VoodooControl("input", "css", "input[name='drop_value']");
		VoodooControl dropdownAddBtnCtrl = new VoodooControl("input", "css", "#dropdownaddbtn");
		dropdownNameCtrl.set(customData.get(0).get("item_name"));
		dropdownValueCtrl.set(customData.get(0).get("display_label"));
		dropdownAddBtnCtrl.click();
		VoodooUtils.pause(2000);  // hard-code wait required, waitForReady not doing the trick here
		dropdownNameCtrl.set(customData.get(1).get("item_name"));
		dropdownValueCtrl.set(customData.get(1).get("display_label"));
		dropdownAddBtnCtrl.click();
		VoodooUtils.pause(2000);  // hard-code wait required, waitForReady not doing the trick here
		dropdownNameCtrl.set(customData.get(2).get("item_name"));
		dropdownValueCtrl.set(customData.get(2).get("display_label"));
		dropdownAddBtnCtrl.click();
		VoodooUtils.pause(2000);  // hard-code wait required, waitForReady not doing the trick heres
		new VoodooControl("input", "css", "#saveBtn").click();
		VoodooUtils.waitForReady();

		studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		opportunitySubPanelCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		opportunitySubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		// Add field and save
		// TODO: VOOD-938
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='enum']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#options option[value='myCustom_dom']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get(0).get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		// TODO: Investigate Jenkins failure at the below line.
		// new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();

		// TODO: Remove when above issue fix.
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();

		// Add new field to Opportunities Record view
		// TODO: VOOD-938
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]", customData.get(0).get("module_field_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Add new field to Opportunities List view
		// TODO: VOOD-938
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]", customData.get(0).get("field_display_name"));
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();

		// Add new field to Opportunities Search view
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
		defaultSearchCtrl.waitForVisible();
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSearchCtrl);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create opportunity with value one
		sugar().accounts.api.create();
		myOpportunity1 = (OpportunityRecord) sugar().opportunities.api.create();
		myOpportunity2 = (OpportunityRecord) sugar().opportunities.api.create();
		myOpportunity1.navToRecord();
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(sugar().opportunities.defaultData.get("relAccountName"));
		new VoodooSelect("span", "css", "[data-voodoo-name='test_field_c']").set(customData.get(0).get("item_name"));
		sugar().opportunities.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Create opportunity with value two
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(2);
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(sugar().opportunities.defaultData.get("relAccountName"));
		VoodooUtils.waitForReady();
		new VoodooSelect("span", "css", "[data-voodoo-name='test_field_c']").set(customData.get(1).get("item_name"));
		sugar().opportunities.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		sugar().opportunities.navToListView();
		// Search for opportunities with value one.
		// VOOD-486
		new VoodooControl("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type").click();
		new VoodooControl("a", "css", ".search-filter-dropdown li:nth-child(1)").waitForVisible();
		new VoodooControl("a", "css", ".search-filter-dropdown li:nth-child(1)").click();
		new VoodooControl("a", "css", ".select2-choice.select2-default").click();
		// xpath is needed to select a field from dropdown
		new VoodooControl("div", "xpath", "//*[@class='select2-results']/li[starts-with(.,'test')]").click();
		new VoodooControl("a", "css", ".select2-choice.select2-default").click();
		new VoodooControl("div", "css", "#select2-drop .select2-results li:nth-of-type(1)").click();
		new VoodooControl("ul", "css", ".select2-choices").waitForVisible();
		new VoodooSelect("ul", "css", ".select2-choices").set(customData.get(0).get("item_name"));
		sugar().alerts.waitForLoadingExpiration();

		// Verify that search return one opportunity.
		new VoodooControl("tr", "css", "[data-voodoo-name='recordlist'] .table.dataTable tbody tr:nth-child(2)").assertExists(false);

		// Select one record. Actions Mass Update - change value to three
		sugar().opportunities.listView.checkRecord(1);
		sugar().opportunities.listView.openActionDropdown();
		sugar().opportunities.listView.massUpdate();
		// TODO VOOD-1003 
		new VoodooSelect("div", "css", "div.filter-field").set(customData.get(0).get("field"));
		new VoodooSelect("div", "css", ".filter-value.controls").set(customData.get(2).get("item_name"));
		VoodooUtils.waitForReady();
		// Not working: sugar().opportunities.massUpdate.update();
		new VoodooControl("a", "css", ".fld_update_button.massupdate a").click();
		VoodooUtils.waitForReady();

		// Select record of value three.
		new VoodooSelect("ul", "css", ".select2-choices").set(customData.get(2).get("item_name"));
		sugar().alerts.waitForLoadingExpiration();
		// Verify that record updated correctly.
		new VoodooControl("span", "css", ".fld_test_field_c.list").assertContains(customData.get(2).get("item_name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}