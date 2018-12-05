package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_20090 extends SugarTest {
	VoodooControl meetingsStudioCtrl;

	public void setup() throws Exception {
		DataSource meetingName = testData.get(testName+"_meetingData");
		sugar().meetings.api.create(meetingName);
		sugar().login();
	}

	/**
	 * Verify simple search still works with Enter key after adding custom field
	 * @throws Exception
	 */
	@Test
	public void Meetings_20090_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// click on studio link  
		sugar().admin.adminTools.getControl("studio").click();

		// Creating a Custom field of type text in the Meetings module
		meetingsStudioCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		meetingsStudioCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(fs.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "label_value_id").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();

		// Adding the custom Text Field into Search View
		sugar().admin.studio.clickStudio();
		meetingsStudioCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl customTextFieldCtrl = new VoodooControl("div", "css", "[data-name='"+fs.get("fieldName")+"_c']");
		customTextFieldCtrl.scrollIntoViewIfNeeded(false);
		VoodooControl addToDefaultList = new VoodooControl("td", "id", "Default");
		customTextFieldCtrl.dragNDrop(addToDefaultList);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();

		// Adding the Custom Text field to the List view layout
		sugar().admin.studio.clickStudio();
		meetingsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		addToDefaultList = new VoodooControl("td", "css", "#editor-content #Default");
		VoodooControl customDropDownInHidden = new VoodooControl("li", "css", "[data-name='"+fs.get("fieldName")+"_c']");
		customDropDownInHidden.scrollIntoViewIfNeeded(false);
		customDropDownInHidden.dragNDrop(addToDefaultList);
		VoodooControl saveAndDeploy = new VoodooControl("input", "css", ".list-editor #savebtn");
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// TODO: VOOD-1036
		VoodooControl customFieldListViewCtrl = new VoodooControl("input", "css", "[name='"+fs.get("fieldName")+"_c']");
		sugar().meetings.navToListView();
		sugar().meetings.listView.editRecord(1);
		customFieldListViewCtrl.set(fs.get("customText"));
		sugar().meetings.listView.saveRecord(1);

		// Create a Filter for searching through Custom Field Added
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();

		// TODO: VOOD-1460,1478
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(fs.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(fs.get("operator"));
		new VoodooControl("input", "css", "[name='"+fs.get("fieldName")+"_c']").set(fs.get("filterBy"));
		VoodooUtils.waitForReady();

		// Verify that the only the record searched for, is populated
		sugar().meetings.listView.getControl("checkbox03").assertVisible(false);

		// Closing the Filter
		// TODO: VOOD-1460,1478
		new VoodooControl("a", "css", "[data-voodoo-name='filter-actions'] .filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}