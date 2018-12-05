package com.sugarcrm.test.tasks;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21190 extends SugarTest {
	FieldSet fieldsData;
	VoodooControl tasksCtrl;

	public void setup() throws Exception {
		fieldsData = testData.get(testName).get(0);
		sugar.login();

		// TODO: VOOD-542
		tasksCtrl = new VoodooControl("a", "id", "studiolink_Tasks");
		VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl defaultCtrl = new VoodooControl("input", "css", "input[name='required']");
		VoodooControl dependentCtrl = new VoodooControl("input", "id", "dependent");
		VoodooControl editFormulaCtrl = new VoodooControl("input", "css", "tr[id='visFormulaRow'] input[name='editFormula']");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl formulaInputCtrl = new VoodooControl("input", "id", "formulaInput");
		VoodooControl formulaSaveCtrl = new VoodooControl("input", "id", "fomulaSaveButton");
		VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl recordViewLayoutCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");

		// Navigate to Admin > studio > Accounts > Fields > Add a custom field type: Date
		// TODO: VOOD-1504
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		tasksCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(fieldsData.get("dataTypeDate"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(fieldsData.get("fieldNameDate"));
		defaultCtrl.set(Boolean.toString(true));
		dependentCtrl.set(Boolean.toString(true));
		VoodooUtils.waitForReady();
		editFormulaCtrl.click();
		formulaInputCtrl.set(fieldsData.get("formulaValue"));
		VoodooUtils.waitForReady();
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: TR-10026 - Save Button is not functional while saving the formula for the calculated field.
		// Remove the following lines(L#57 - L#63 after TR-10026 is fixed 
		VoodooControl closeFormulaPopUpCtrl = new VoodooControl("a", "css", "#formulaBuilderWindow .container-close");
		if (closeFormulaPopUpCtrl.queryVisible()) {
			closeFormulaPopUpCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Save
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to Admin > studio > Task > Fields > Add a custom field type: Text
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(fieldsData.get("dataTypeText"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(fieldsData.get("fieldNameText"));
		defaultCtrl.set(Boolean.toString(true));
		dependentCtrl.set(Boolean.toString(true));
		editFormulaCtrl.click();
		formulaInputCtrl.set(fieldsData.get("formulaValue"));
		formulaSaveCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: TR-10026 - Save Button is not functional while saving the formula for the calculated field.
		// Remove the following lines(L#82 - L#87 after TR-10026 is fixed 
		if (closeFormulaPopUpCtrl.queryVisible()) {
			closeFormulaPopUpCtrl.click();
			VoodooUtils.waitForReady();
		}

		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Adding the above created custom Date and Text fields to record view layout
		// TODO: VOOD-1506
		VoodooUtils.waitForReady();
		new VoodooControl("img", "css", ".bodywrapper img").click();
		layoutCtrl.click();
		recordViewLayoutCtrl.click();
		VoodooControl defaultFieldsDate = new VoodooControl("li", "css", "[data-name='assigned_user_name']");
		VoodooControl defaultFieldsText = new VoodooControl("li", "css", "[data-name='parent_name']");
		String customDateHook = String.format("div[data-name=%s_c]",fieldsData.get("fieldNameDate")); 
		String customTextHook = String.format("div[data-name=%s_c]",fieldsData.get("fieldNameText")); 
		new VoodooControl("li", "css", customDateHook).dragNDrop(defaultFieldsDate);
		new VoodooControl("li", "css", customTextHook).dragNDrop(defaultFieldsText);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}
	/**
	 * Check that hidden required Date and Text fields doesn't block form validation
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tasks_21190_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Create Drawer of Tasks 
		sugar.tasks.navToListView();
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(fieldsData.get("subjectName"));
		sugar.tasks.createDrawer.showMore();

		// Verify the visibility of custom fields and their required attribute
		// TODO: VOOD-1036	
		new VoodooControl("div", "css", "[data-name='"+fieldsData.get("fieldNameDate")+"_c']").assertVisible(true);
		new VoodooControl("div", "css", "[data-name='"+fieldsData.get("fieldNameText")+"_c']").assertVisible(true);
		new VoodooControl("input", "css", ".fld_"+fieldsData.get("fieldNameDate")+"_c.edit input").assertAttribute("class", fieldsData.get("customRequired"),true);
		new VoodooControl("input", "css", ".fld_"+fieldsData.get("fieldNameText")+"_c.edit input").assertAttribute("class", fieldsData.get("customRequired"),true);

		// Verify the custom field becomes disable once priority changes from High to Medium
		sugar.tasks.createDrawer.getEditField("priority").set("Medium");
		new VoodooControl("input", "css", ".fld_"+fieldsData.get("fieldNameDate")+"_c").assertAttribute("class", fieldsData.get("customDisabled"),true);
		new VoodooControl("input", "css", ".fld_"+fieldsData.get("fieldNameText")+"_c").assertAttribute("class", fieldsData.get("customDisabled"),true);

		// Verify the record saves without any error 
		sugar.tasks.createDrawer.save();
		int count = sugar.tasks.listView.countRows();
		Assert.assertTrue("Record count is not equal to one", count == 1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}