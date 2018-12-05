package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_21381 extends SugarTest {
	DataSource dropdownFieldData;

	public void setup() throws Exception {
		dropdownFieldData = testData.get(testName);
		sugar().login();
	}

	/**
	 * Studio_DropDown_Editor_Create and Edit on edit view page of dropdown list field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_21381_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Fields of one module from the left tree view list.
		// TODO: VOOD-938
		new VoodooControl("td", "css", "#ygtvt1").click();
		new VoodooControl("a", "css", ".ygtvcell #ygtvlabelel3").click();
		VoodooUtils.waitForReady();

		// Click "Add field" button.
		new VoodooControl("button", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();

		// Select "DropDown" as data type. Enter value in field name.
		new VoodooControl("select", "css", "#type option[label='DropDown']").click();
		VoodooUtils.waitForReady();

		// Click "Add" button at Drop Down List field.
		new VoodooControl("input", "css", "input[value='Add']").click();
		VoodooUtils.waitForReady();

		// Verify create page for dropdown list is displayed.
		new VoodooControl("em", "css", ".yui-nav .selected em").assertContains(dropdownFieldData.get(0).get("editDropdown"), true);

		//  Enter a valid name and add at least one item and click "Save" button.
		// TODO: VOOD-938
		new VoodooControl("input", "css", "input[name='dropdown_name']").set(dropdownFieldData.get(0).get("name"));
		VoodooControl itemNameCtrl = new VoodooControl("input", "css", "input[name='drop_name']");
		VoodooControl displayValueCtrl = new VoodooControl("input", "css", "input[name='drop_value']");
		VoodooControl dropdownListValuesAddBtnCtrl = new VoodooControl("input", "id", "dropdownaddbtn");
		VoodooControl dropdownListSaveBtnCtrl = new VoodooControl("input", "id", "saveBtn");
		itemNameCtrl.set(dropdownFieldData.get(0).get("item_name"));
		displayValueCtrl.set(dropdownFieldData.get(0).get("display_label"));
		dropdownListValuesAddBtnCtrl.click();
		// Wait for JS action to conclude properly. waitForReady() and waitForAJAX() not effective here.
		VoodooUtils.pause(2000); 
		dropdownListSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify the dropdown list is saved correctly
		// TODO: VOOD-938
		new VoodooControl("select", "css", "select[name='options']").click();
		VoodooControl customDropdownListCtrl = new VoodooControl("select", "css", "select[name='options'] option[value='"+dropdownFieldData.get(0).get("name")+"']");
		customDropdownListCtrl.assertExists(true);

		// Select the newly created dropdown list and click "Edit" button.
		customDropdownListCtrl.click();
		new VoodooControl("input", "css", "[value='Edit']").click();
		VoodooUtils.waitForReady();

		// Verify Edit view page for the newly created dropdown list is displayed with correct information.
		// TODO: VOOD-938
		new VoodooControl("input", "css", "input[id='dropdown_name']").assertAttribute("value", ""+dropdownFieldData.get(0).get("name")+"", true);
		new VoodooControl("span", "css", "span[id='span_"+dropdownFieldData.get(0).get("display_label")+"']").assertContains(dropdownFieldData.get(0).get("display_label"), true);

		// Edit dropdown
		// TODO: VOOD-938
		itemNameCtrl.set(dropdownFieldData.get(1).get("item_name"));
		displayValueCtrl.set(dropdownFieldData.get(1).get("display_label"));
		dropdownListValuesAddBtnCtrl.click();
		// Wait for JS action to conclude properly. waitForReady() and waitForAJAX() not effective here.
		VoodooUtils.pause(2000); 

		// Click "Save" button
		dropdownListSaveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify dropdown list is edited successfully.
		new VoodooControl("select", "css", "[name='default[]']").click();
		new VoodooControl("option", "css", "[name='default[]'] option:nth-child(1)").assertAttribute("value", ""+dropdownFieldData.get(0).get("item_name")+"", true);
		new VoodooControl("option", "css", "[name='default[]'] option:nth-child(2)").assertAttribute("value", ""+dropdownFieldData.get(1).get("item_name")+"", true);

		// Cancel the Edit field form
		new VoodooControl("input", "css", "input[name='cancelbtn']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}