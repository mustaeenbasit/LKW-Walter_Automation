package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_26217 extends SugarTest {
	FieldSet customData, customFields;
	String listName;
	AccountRecord myAccount;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		customFields = testData.get(testName+"_fields").get(0);
		listName = customData.get("list_name") + System.currentTimeMillis();		
		sugar().login();
	}

	/**
	 * Verify that apostrophe in custom Dropdown works properly 
	 * 
	 * @throws Exception
	 */
	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add new Dropdown
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("dropdownEditor").click();
		sugar().admin.studio.waitForAJAX();

		// TODO: VOOD-781 -need lib support of dropdown editor functions
		VoodooControl dropName = new VoodooControl("input", "id", "drop_name");
		VoodooControl dropValue = new VoodooControl("input", "id", "drop_value");
		VoodooControl dropDownAddBtn = new VoodooControl("input", "id", "dropdownaddbtn");
		VoodooControl saveBtn = new VoodooControl("input", "id", "saveBtn");
		VoodooControl customDropDown = new VoodooControl("a", "xpath", "//*[@id='dropdowns']/table/tbody/tr[contains(.,'"+listName+"')]/td[contains(.,'"+listName+"')]/a");

		// Add one new dropdown. Item Name: test, Display Label: t'est
		// TODO: VOOD-781
		new VoodooControl("input", "css", "[name='adddropdownbtn']").click();
		sugar().admin.studio.waitForAJAX();
		new VoodooControl("input", "id", "dropdown_name").set(listName);
		dropName.set(customData.get("dropdown_item_name1"));
		dropValue.set(customData.get("dropdown_display_label1"));
		dropDownAddBtn.click();
		VoodooUtils.waitForReady();
		// Wait for JS action to conclude properly. waitForReady() not effective here.
		VoodooUtils.pause(2000); 

		// Save
		sugar().admin.studio.waitForAJAX();
		saveBtn.click();
		sugar().admin.studio.waitForAJAX();
		customDropDown.waitForVisible();

		// Re-open the list. Add another item. Item Name: bloop, Display Label: bl'oop
		customDropDown.click();
		sugar().admin.studio.waitForAJAX();
		dropName.set(customData.get("dropdown_item_name2"));
		dropValue.set(customData.get("dropdown_display_label2"));
		dropDownAddBtn.click();

		// Wait for JS action to conclude properly. waitForReady() not effective here.
		VoodooUtils.pause(2000); 

		// Save and Re-open the list
		sugar().admin.studio.waitForAJAX();
		saveBtn.click();
		sugar().admin.studio.waitForAJAX();
		customDropDown.click();
		sugar().admin.studio.waitForAJAX();

		// Verify that  items are displayed as test[t'est] & bloop[bl'oop]
		new VoodooControl("li", "xpath", "//*[@id='ul1']/li[contains(.,'"+customData.get("dropdown_item_name1")+"')]").assertContains(customData.get("dropdown_display_label1"), true);	
		new VoodooControl("li", "xpath", "//*[@id='ul1']/li[contains(.,'"+customData.get("dropdown_item_name2")+"')]").assertContains(customData.get("dropdown_display_label2"), true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
