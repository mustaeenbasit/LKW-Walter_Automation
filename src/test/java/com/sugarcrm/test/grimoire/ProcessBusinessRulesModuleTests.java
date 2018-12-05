package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessBusinessRulesRecord;
import com.sugarcrm.sugar.views.ProcessCreateDrawer;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for save and design, menu dropdown items, listview headers, sortBy, action dropdown, edit/detail hook values
 * for record view, list view and preview pane view.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessBusinessRulesModuleTests extends SugarTest {
	FieldSet defaultPBData = new FieldSet();

	public void setup() throws Exception {
		defaultPBData = sugar().processBusinessRules.getDefaultData();
		sugar().login();
	}

	@Test
	public void verifySaveAndDesign() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySaveAndDesign()...");

		ProcessBusinessRulesRecord myBusinessRule = (ProcessBusinessRulesRecord)sugar().processBusinessRules.create(defaultPBData, ProcessCreateDrawer.Save.SAVE_AND_DESIGN);
		assertTrue("The guid for the create with SAVE_AND_DESIGN was empty!", !(myBusinessRule.getGuid().isEmpty()));
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// TODO: VOOD-1539
		new VoodooControl("a", "css", ".fld_project_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info("verifySaveAndDesign() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().processBusinessRules.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().processBusinessRules);

		// Verify menu items
		sugar().processBusinessRules.menu.getControl("createBusinessRule").assertVisible(true);
		sugar().processBusinessRules.menu.getControl("viewBusinessRules").assertVisible(true);
		sugar().processBusinessRules.menu.getControl("importBusinessRules").assertVisible(true);
		sugar().navbar.clickModuleDropdown(sugar().processBusinessRules); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().processBusinessRules.navToListView();

		// Verify all headers in listview
		for(String header : sugar().processBusinessRules.listView.getHeaders()){
			sugar().processBusinessRules.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		String name = "New Business Rule";
		// 2 business rules having 1 with default data and another with custom data (name)
		sugar().processBusinessRules.api.create();
		FieldSet customPB = new FieldSet();
		customPB.put("name", name);
		sugar().processBusinessRules.api.create(customPB);
		sugar().processBusinessRules.navToListView(); // to reload data, somehow refresh page is not working on CI

		// Verify records after sort by 'name' in descending and ascending order
		sugar().processBusinessRules.listView.sortBy("headerName", false);
		sugar().processBusinessRules.listView.verifyField(1, "name", name);
		sugar().processBusinessRules.listView.verifyField(2, "name", defaultPBData.get("name"));

		sugar().processBusinessRules.listView.sortBy("headerName", true);
		sugar().processBusinessRules.listView.verifyField(1, "name", defaultPBData.get("name"));
		sugar().processBusinessRules.listView.verifyField(2, "name", name);

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyActionDropdown() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionDropdown()...");

		sugar().processBusinessRules.api.create();
		sugar().processBusinessRules.navToListView();
		sugar().processBusinessRules.listView.openRowActionDropdown(1);

		// Verify action dropdown items
		sugar().processBusinessRules.listView.getControl("edit01").assertVisible(true);
		sugar().processBusinessRules.listView.getControl("delete01").assertVisible(true);
		sugar().processBusinessRules.listView.getControl("design01").assertVisible(true);
		sugar().processBusinessRules.listView.getControl("export01").assertVisible(true);
		sugar().processBusinessRules.listView.getControl("dropdown01").click(); // to close action dropdown

		VoodooUtils.voodoo.log.info("verifyActionDropdown() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().processBusinessRules.api.create();
		sugar().processBusinessRules.navToListView();
		sugar().processBusinessRules.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().processBusinessRules.listView.getEditField(1, "name").assertEquals(defaultPBData.get("name"), true);

		// business rule
		VoodooSelect businessRuleType = (VoodooSelect)sugar().processBusinessRules.listView.getEditField(1, "businessRuleType");
		businessRuleType.set(defaultPBData.get("businessRuleType"));
		businessRuleType.assertEquals(defaultPBData.get("businessRuleType"), true);

		// TODO: Once VOOD-444 is supported, we can verify this field in the listview. Right now it is blank and therefore
		// does not exist if there's no value, and no value is set in API create as this is a relationship field.
		// target module
		// sugar().processBusinessRules.listView.getEditField(1, "targetModule").assertVisible(true);

		// assigned to
		VoodooSelect assignedTo = (VoodooSelect)sugar().processBusinessRules.listView.getEditField(1, "relAssignedTo");
		assignedTo.set(sugar().users.getQAUser().get("userName"));
		assignedTo.assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Date Created (read only)
		VoodooControl dateCreated = sugar().processBusinessRules.listView.getEditField(1, "date_entered_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		// Date modified (read only)
		VoodooControl dateModified = sugar().processBusinessRules.listView.getEditField(1, "date_modified_date");
		dateModified.assertExists(true);
		dateModified.assertAttribute("class", "edit", false);

		sugar().processBusinessRules.listView.cancelRecord(1);

		// verify list fields
		// name
		sugar().processBusinessRules.listView.getDetailField(1, "name").assertEquals(defaultPBData.get("name"), true);

		// business rule
		sugar().processBusinessRules.listView.getDetailField(1, "businessRuleType").assertExists(true);

		// target
		sugar().processBusinessRules.listView.getDetailField(1, "targetModule").assertExists(true);

		// assigned
		sugar().processBusinessRules.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// Date Created
		sugar().processBusinessRules.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().processBusinessRules.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		sugar().processBusinessRules.api.create();
		sugar().processBusinessRules.navToListView();

		// preview record
		sugar().processBusinessRules.listView.previewRecord(1);
		sugar().previewPane.showMore();

		// Verify preview field values
		// name
		sugar().previewPane.getPreviewPaneField("name").assertContains(defaultPBData.get("name"), true);

		// business rule
		sugar().previewPane.getPreviewPaneField("businessRuleType").assertVisible(true);

		// target
		sugar().previewPane.getPreviewPaneField("targetModule").assertVisible(true);

		// description
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);

		// assigned
		sugar().previewPane.getPreviewPaneField("relAssignedTo").assertVisible(true);

		// Date Created
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);

		// Date modified
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);


		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().processBusinessRules.api.create();
		sugar().processBusinessRules.navToListView();
		sugar().processBusinessRules.listView.clickRecord(1);
		sugar().processBusinessRules.recordView.edit();
		sugar().processBusinessRules.recordView.showMore();

		// Verify edit record fields
		// name
		sugar().processBusinessRules.recordView.getEditField("name").assertEquals(defaultPBData.get("name"), true);

		// business rule type
		VoodooSelect type = (VoodooSelect)sugar().processBusinessRules.recordView.getEditField("businessRuleType");
		type.set(defaultPBData.get("businessRuleType"));
		type.assertEquals(defaultPBData.get("businessRuleType"), true);

		// target
		VoodooSelect target = (VoodooSelect)sugar().processBusinessRules.recordView.getEditField("targetModule");
		target.set(sugar().calls.moduleNamePlural);
		target.assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processBusinessRules.recordView.getEditField("relAssignedTo").assertVisible(true);

		// description
		sugar().processBusinessRules.recordView.getEditField("description").assertVisible(true);

		// date created
		sugar().processBusinessRules.recordView.getEditField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processBusinessRules.recordView.getEditField("date_modified_date").assertVisible(true);

		// save record
		sugar().processBusinessRules.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify detail fields
		// name
		sugar().processBusinessRules.recordView.getDetailField("name").assertEquals(defaultPBData.get("name"), true);

		// business rule type
		sugar().processBusinessRules.recordView.getDetailField("businessRuleType").assertEquals(defaultPBData.get("businessRuleType"), true);

		// target
		sugar().processBusinessRules.recordView.getDetailField("targetModule").assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processBusinessRules.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// date created
		sugar().processBusinessRules.recordView.getDetailField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processBusinessRules.recordView.getDetailField("date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}
