package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class Calls_20084 extends SugarTest {
	DataSource callsDS = new DataSource();
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		callsDS = testData.get(testName);
		myData = testData.get(testName + "_value").get(0);
		sugar().calls.api.create(callsDS);
		sugar().login();

		// TODO: VOOD-1504 - Support Studio Module Fields View
		// TODO: VOOD-1508 - Support Studio Module PopUp Layouts View
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl enableRangeCtrl = new VoodooControl("input", "css", "input[name='enable_range_search']");
		VoodooControl isRequiredCtrl = new VoodooControl("input", "css", "input[name='required']");
		VoodooControl detailViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel .le_row");
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special");
		VoodooControl callsFieldCtrl = sugar().studio.studioModule.getControl("fields");
		VoodooControl layoutPanelCtrl = sugar().studio.studioModule.getControl("layouts");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl rowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		VoodooControl publishButtonCtrl = new VoodooControl("input", "id", "publishBtn");
		VoodooControl searchButtonCtrl = new VoodooControl("td", "id", "searchBtn");
		VoodooControl filterSearchButtonCtrl = new VoodooControl("td", "id", "FilterSearchBtn");
		VoodooControl defaultSearchCtrl = new VoodooControl("ul", "id", "ul0");
		VoodooControl saveLayoutCtrl = new VoodooControl("td", "id", "savebtn");

		// Navigate to calls module in studio panel
		sugar().studio.navToStudioModule(sugar().calls);

		// click on field component inside calls module panel
		sugar().studio.clickItem(callsFieldCtrl);

		// click on Add Field button on Fields page
		sugar().studio.clickItemOnPage(addFieldCtrl);

		// Add an Integer type custom field
		sugar().studio.setItem(dataTypeDropdownCtrl, myData.get("data_type"));
		sugar().studio.setItem(nameFieldCtrl, myData.get("field_name"));
		sugar().studio.setItem(enableRangeCtrl, Boolean.toString(true));
		sugar().studio.setItem(isRequiredCtrl, Boolean.toString(true));
		sugar().studio.clickItemOnPage(saveBtnCtrl);
		sugar().studio.breadCrumb.clickItemInBreadCrumb(sugar().calls.moduleNamePlural);
		sugar().studio.clickItem(layoutPanelCtrl);
		sugar().studio.clickItem(detailViewSubPanelCtrl);
		sugar().studio.moveItem(rowCtrl, moveToLayoutPanelCtrl);

		// Add custom field on record view
		String dataNameDraggableFieldToRecord = String.format("div[data-name=%s_c]", myData.get("field_name"));
		VoodooControl customFieldCtrl = new VoodooControl("div", "css", dataNameDraggableFieldToRecord);
		sugar().studio.moveItem(customFieldCtrl, moveToNewFilter);
		sugar().studio.clickItemOnPage(publishButtonCtrl);

		sugar().studio.breadCrumb.clickItemInBreadCrumb(sugar().calls.moduleNamePlural);
		sugar().studio.clickItem(layoutPanelCtrl);

		sugar().studio.clickItem(searchButtonCtrl);
		sugar().studio.clickItem(filterSearchButtonCtrl);

		// TODO: VOOD-1510 - Support Studio Module Search View
		// Add custom field on search view
		String fieldDraggableLi = String.format("li[data-name=%s_c]", myData.get("field_name"));
		VoodooControl dragableFieldCtrl = new VoodooControl("li", "css", fieldDraggableLi);
		sugar().studio.moveItem(dragableFieldCtrl, defaultSearchCtrl);
		sugar().studio.clickItem(saveLayoutCtrl);

		//Update the Custom integer field for all Call records
		sugar().calls.navToListView();
		sugar().calls.listView.sortBy("headerName", true);
		sugar().calls.listView.clickRecord(1);
		for (int i = 0; i < callsDS.size(); i++) {
			sugar().calls.recordView.edit();
			// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
			new VoodooControl("input", "css", "input[name='" + myData.get("field_name") + "_c']").set(myData.get("field_value_" + (i + 1)));
			sugar().calls.recordView.save();
			if (i < callsDS.size())
				sugar().calls.recordView.gotoNextRecord();
		}
	}

	/**
	 * Verify the custom integer type field range search feature can work fine for the search layout
	 *
	 * @throws Exception
	 */

	@Test
	public void Calls_20084_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//Navigate to Calls List view, open filter and create new filter
		sugar().calls.navToListView();
		sugar().calls.listView.sortBy("headerName", true);
		sugar().calls.listView.openFilterDropdown();
		sugar().calls.listView.selectFilterCreateNew();

		// TODO: VOOD-1766 - Support range operators like “is between” in filters
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div[data-filter='field']");
		VoodooSelect operatorFieldCtrl = new VoodooSelect("div", "css", "div[data-filter='operator']");
		VoodooControl valueMinField = new VoodooControl("div", "css", "div[data-filter='value'] .fld_" + myData.get("field_name") + "_c_min input");
		VoodooControl valueMaxField = new VoodooControl("div", "css", "div[data-filter='value'] .fld_" + myData.get("field_name") + "_c_max input");
		VoodooControl newNameField = new VoodooControl("div", "css", "input[placeholder='Enter new filter name…']");

		// Select the "CUSTOM" field from the drop down list and set min,max in range search.
		filterFieldCtrl.set(myData.get("field_label"));
		operatorFieldCtrl.set(myData.get("field_operator"));
		valueMinField.set(myData.get("field_value_min"));
		valueMaxField.set(myData.get("field_value_max"));
		newNameField.click();
		VoodooUtils.waitForReady();

		// Verify that it shows all of call records within range min to max
		int maxRange = Integer.parseInt(myData.get("field_value_max"));
		int minRange = Integer.parseInt(myData.get("field_value_min"));
		int count = maxRange - minRange + 1;
		Assert.assertEquals("Assert Count number of rows failed", count, sugar().calls.listView.countRows());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}