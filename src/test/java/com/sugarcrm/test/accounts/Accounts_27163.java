package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_27163 extends SugarTest {
	VoodooControl moduleCtrl;
	FieldSet fieldValueData = new FieldSet();

	public void setup() throws Exception {
		fieldValueData = testData.get(testName).get(0);
		sugar().login();

		// Create a custom Dropdown filed in Accounts module
		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Accounts > Fields and create a new Date type field called end_day_c 
		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl itemNameCtrl = new VoodooControl("input", "css", "input[name='drop_name']");
		VoodooControl displayLabelCtrl = new VoodooControl("input", "css", "input[name='drop_value']");
		VoodooControl addvaluesCtrl = new VoodooControl("input", "css", "#dropdownaddbtn");

		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='enum']").click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(fieldValueData.get("dropdownFieldName"));

		// Add new Dropdown List
		new VoodooControl("input", "css", "input[value='Add']").click();
		VoodooUtils.waitForReady();

		// Add three values in dropdown list
		for (int i = 0; i < 3; i++) {
			itemNameCtrl.set(fieldValueData.get("item" + i));
			displayLabelCtrl.set(fieldValueData.get("item" + i));
			addvaluesCtrl.click();
			// Wait for JS action to conclude properly. waitForReady() and waitForAJAX() not effective here.
			VoodooUtils.pause(2000);
		}

		// Save new Dropdown
		new VoodooControl("input", "id", "saveBtn").click();
		VoodooUtils.waitForReady();

		// Save the Dropdown type field
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Place the Dropdown fields on the Record view 
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Add the Dropdown type to the record view of account module
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row");
		VoodooControl moveToNewFilter1 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css", "div[data-name=" + fieldValueData.get("dropdownFieldName") + "_c]").dragNDrop(moveToNewFilter1);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();

		// Place the Dropdown fields on the Search view 
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();

		// Add the Dropdown type to the search view of account module
		VoodooControl dropDefaultColumnCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name=" + fieldValueData.get("dropdownFieldName") + "_c]").dragNDrop(dropDefaultColumnCtrl);

		// Save & Deploy
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify dropdown field in filter creation.
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_27163_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create four records with different values of existing dropdown field (i.e. Type) and custom dropdown field(i.e. dropdownfield)
		sugar().accounts.navToListView();

		// TODO: VOOD-1036
		VoodooSelect customField = new VoodooSelect("span", "css", ".fld_" + fieldValueData.get("dropdownFieldName") + "_c.edit");

		for (int i = 0; i < 4; i++) {
			sugar().accounts.listView.create();
			sugar().accounts.createDrawer.showMore();
			sugar().accounts.createDrawer.getEditField("name").set(testName + "_" + i);
			if (i < 2)
				sugar().accounts.createDrawer.getEditField("type").set(fieldValueData.get("type" + i));
			else
				customField.set(fieldValueData.get("type" + i));
			sugar().accounts.createDrawer.save();
		}

		// Create filter from the Accounts listview
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		// TODO: VOOD-1462
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']");
		VoodooSelect operatorFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']");
		VoodooControl closeSearchCtrl = new VoodooControl("a", "css", "li.select2-search-choice a");

		for (int i = 0; i < 4; i++) {
			// Create filter with existing dropdown filed (i.e. Type) and custom dropdown filed (i.e. dropdownfield)
			if (i < 2) {
				filterFieldCtrl.set(fieldValueData.get("existingDropdownFiledName"));
				operatorFieldCtrl.set(fieldValueData.get("operator"));
				new VoodooSelect("input", "css", ".fld_account_type.detail input").set(fieldValueData.get("type" + i));
			} else {
				filterFieldCtrl.set(fieldValueData.get("dropdownFieldName"));
				operatorFieldCtrl.set(fieldValueData.get("operator"));
				new VoodooSelect("input", "css", ".fld_" + fieldValueData.get("dropdownFieldName") + "_c.detail input").set(fieldValueData.get("type" + i));
			}
			VoodooUtils.waitForReady();

			// Verify that the dropdown field works well in filter creation and the search result is good.
			sugar().meetings.listView.verifyField(1, "name", testName + "_" + i);
			sugar().meetings.listView.getControl("checkbox03").assertExists(false);
			closeSearchCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Cancel the filter page
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}