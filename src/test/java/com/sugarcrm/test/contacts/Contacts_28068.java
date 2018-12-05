package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_28068 extends SugarTest {
	VoodooControl contactsStudioCtrl,studioCtrl;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that search in Contact Module using Filter for custom drop-down fields works properly.
	 * @throws Exception
	 */
	@Test
	public void Contacts_28068_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFieldAndFilter = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504
		// Creating a Custom field of type Drop-Down in the Contacts module
		contactsStudioCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsStudioCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customFieldAndFilter.get("fieldType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customFieldAndFilter.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "label_value_id").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		contactsStudioCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();

		// Adding the custom Drop-down field to the Record View layout
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		VoodooControl insertNewRow = new VoodooControl("div", "css", "#toolbox .le_row");
		VoodooControl toPanel = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel");
		insertNewRow.dragNDrop(toPanel);
		VoodooControl customDropDown = new VoodooControl("div", "css", "#toolbox div[data-name='customdropdown_c']");
		VoodooControl filler = toPanel.getChildElement("div", "css", ".le_row div:nth-child(1).le_field.special");
		customDropDown.scrollIntoViewIfNeeded(false);
		customDropDown.dragNDrop(filler);
		VoodooControl saveAndDeploy = new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn");
		saveAndDeploy.click();
		VoodooUtils.waitForReady();

		// Adding the Custom Drop-down field to the List view layout
		// TODO: VOOD-1507
		sugar().admin.studio.clickStudio();
		contactsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl addToDefaultList = new VoodooControl("td", "css", "#editor-content #Default");
		VoodooControl customDropDownInHidden = new VoodooControl("li", "css", "#Hidden li[data-name='customdropdown_c']");
		customDropDownInHidden.scrollIntoViewIfNeeded(false);
		customDropDownInHidden.dragNDrop(addToDefaultList);
		saveAndDeploy = new VoodooControl("input", "css", ".list-editor #savebtn");
		saveAndDeploy.click();
		VoodooUtils.waitForReady();

		// Adding the Custom Drop-down field to the Search Layout
		// TODO: VOOD-1509,VOOD-1510
		sugar().admin.studio.clickStudio();
		contactsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		customDropDownInHidden.scrollIntoViewIfNeeded(false);
		customDropDownInHidden.dragNDrop(addToDefaultList);
		saveAndDeploy.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Editing a Contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();

		// TODO: VOOD-1036
		new VoodooSelect("div", "css", ".fld_customdropdown_c .select2").set(customFieldAndFilter.get("filterOnValue"));
		sugar().contacts.recordView.save();
		sugar().contacts.navToListView();

		// Create filter for the custom drop-down type field
		sugar().contacts.listView.openFilterDropdown();
		sugar().contacts.listView.selectFilterCreateNew();

		// TODO: VOOD-1462
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(customFieldAndFilter.get("fieldName"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(customFieldAndFilter.get("filterOperator"));
		new VoodooSelect("ul", "css", ".layout_Contacts .detail.fld_customdropdown_c .select2-choices").set(customFieldAndFilter.get("filterOnValue"));
		sugar().contacts.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().contacts.listView.filterCreate.save();

		// After applying filter, Verify that the same contact is displayed in the list view i.e created in Set-Up
		// TODO: VOOD-1036
		sugar().contacts.listView.verifyField(1, "fullName",sugar().contacts.getDefaultData().get("fullName"));
		new VoodooControl("span", "css", ".list.fld_customdropdown_c").assertContains(customFieldAndFilter.get("filterOnValue"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}