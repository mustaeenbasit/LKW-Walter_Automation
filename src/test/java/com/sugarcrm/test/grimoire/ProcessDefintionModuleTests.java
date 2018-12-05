package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessDefinitionRecord;
import com.sugarcrm.sugar.views.ProcessCreateDrawer;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for save and design, menu dropdown items, import process, listview headers, sortBy, action dropdown, edit/detail hook values
 * for record view and list view.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessDefintionModuleTests extends SugarTest {
	FieldSet defaultPDData = new FieldSet();

	public void setup() throws Exception {
		defaultPDData = sugar().processDefinitions.getDefaultData();
		sugar().login();
	}

	@Test
	public void verifySaveAndDesign() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySaveAndDesign()...");

		ProcessDefinitionRecord myDefinition = (ProcessDefinitionRecord)sugar().processDefinitions.create(defaultPDData, ProcessCreateDrawer.Save.SAVE_AND_DESIGN);
		assertTrue("The guid for the create with SAVE_AND_DESIGN was empty!", !(myDefinition.getGuid().isEmpty()));

		VoodooUtils.voodoo.log.info("verifySaveAndDesign() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().processDefinitions.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().processDefinitions);

		// Verify menu items
		sugar().processDefinitions.menu.getControl("createProcessDefinition").assertVisible(true);
		sugar().processDefinitions.menu.getControl("viewProcessDefinition").assertVisible(true);
		sugar().processDefinitions.menu.getControl("importProcessDefinition").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().processDefinitions); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyImportProcess() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyImportProcess()...");

		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/Lead_Process_Definition.bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.clickRecord(1);
		sugar().processDefinitions.recordView.getDetailField("name").assertEquals(sugar().processDefinitions.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info("verifyImportProcess() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().processDefinitions.navToListView();

		// Verify all headers in listview
		for(String header : sugar().processDefinitions.listView.getHeaders()){
			sugar().processDefinitions.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		String name = "Calls PD";
		// 2 definitions having 1 with default data and another with custom data (name)
		sugar().processDefinitions.api.create();
		FieldSet customPD = new FieldSet();
		customPD.put("name", name);
		sugar().processDefinitions.api.create(customPD);
		sugar().processDefinitions.navToListView(); // to reload data, somehow refresh page is not working on CI

		// Verify records after sort by 'name' in descending and ascending order
		sugar().processDefinitions.listView.sortBy("headerName", false);
		sugar().processDefinitions.listView.verifyField(1, "name", sugar().processDefinitions.getDefaultData().get("name"));
		sugar().processDefinitions.listView.verifyField(2, "name", name);

		sugar().processDefinitions.listView.sortBy("headerName", true);
		sugar().processDefinitions.listView.verifyField(1, "name", name);
		sugar().processDefinitions.listView.verifyField(2, "name", sugar().processDefinitions.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyActionDropdown() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionDropdown()...");

		sugar().processDefinitions.api.create();
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Verify action dropdown items
		sugar().processDefinitions.listView.getControl("edit01").assertVisible(true);
		sugar().processDefinitions.listView.getControl("delete01").assertVisible(true);
		sugar().processDefinitions.listView.getControl("design01").assertVisible(true);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").assertVisible(true);
		sugar().processDefinitions.listView.getControl("export01").assertVisible(true);
		sugar().processDefinitions.listView.getControl("dropdown01").click(); // to close action dropdown

		VoodooUtils.voodoo.log.info("verifyActionDropdown() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().processDefinitions.api.create();
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().processDefinitions.listView.getEditField(1, "name").assertEquals(defaultPDData.get("name"), true);

		// TODO: Once VOOD-444 is supported, we can verify this field in the listview. Right now it is blank and therefore
		// does not exist if there's no value, and no value is set in API create as this is a relationship field.
		// target module
		// sugar().processDefinitions.listView.getEditField(1, "targetModule").assertVisible(true);

		// status
		VoodooControl status = sugar().processDefinitions.listView.getEditField(1, "status");
		status.assertVisible(true);
		status.assertEquals(defaultPDData.get("status"), true);

		// assigned to
		VoodooSelect assignedTo = (VoodooSelect)sugar().processDefinitions.listView.getEditField(1, "relAssignedTo");
		assignedTo.set(sugar().users.getQAUser().get("userName"));
		assignedTo.assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Date Created (read only)
		VoodooControl dateCreated = sugar().processDefinitions.listView.getEditField(1, "date_entered_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		// Date modified (read only)
		VoodooControl dateModified = sugar().processDefinitions.listView.getEditField(1, "date_modified_date");
		dateModified.assertExists(true);
		dateModified.assertAttribute("class", "edit", false);

		sugar().processDefinitions.listView.cancelRecord(1);

		// verify list fields
		// name
		sugar().processDefinitions.listView.getDetailField(1, "name").assertEquals(defaultPDData.get("name"), true);

		// target
		sugar().processDefinitions.listView.getDetailField(1, "targetModule").assertExists(true);

		// assigned
		sugar().processDefinitions.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// Date Created
		sugar().processDefinitions.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().processDefinitions.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().processDefinitions.api.create();
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.clickRecord(1);
		sugar().processDefinitions.recordView.edit();
		sugar().processDefinitions.recordView.showMore();

		// Verify edit record fields
		// name
		sugar().processDefinitions.recordView.getEditField("name").assertEquals(defaultPDData.get("name"), true);

		// target
		VoodooSelect target = (VoodooSelect)sugar().processDefinitions.recordView.getEditField("targetModule");
		target.set(sugar().calls.moduleNamePlural);
		target.assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processDefinitions.recordView.getEditField("relAssignedTo").assertVisible(true);

		// description
		sugar().processDefinitions.recordView.getEditField("description").assertVisible(true);

		// date created
		sugar().processDefinitions.recordView.getEditField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processDefinitions.recordView.getEditField("date_modified_date").assertVisible(true);

		// save record
		sugar().processDefinitions.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify detail fields
		// name
		sugar().processDefinitions.recordView.getDetailField("name").assertEquals(defaultPDData.get("name"), true);

		// target
		sugar().processDefinitions.recordView.getDetailField("targetModule").assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processDefinitions.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// date created
		sugar().processDefinitions.recordView.getDetailField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processDefinitions.recordView.getDetailField("date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}