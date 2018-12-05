package com.sugarcrm.test.grimoire;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ProcessEmailTemplatesRecord;
import com.sugarcrm.sugar.views.ProcessCreateDrawer;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests for save and Design, menu dropdown items, listview headers, sortBy, action dropdown, edit/detail hook values
 * for record view and list view.
 *
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessEmailTemplatesModuleTests extends SugarTest {
	FieldSet defaultPEData = new FieldSet();

	public void setup() throws Exception {
		defaultPEData = sugar().processEmailTemplates.getDefaultData();
		sugar().login();
	}

	@Test
	public void verifySaveAndDesign() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySaveAndDesign()...");

		ProcessEmailTemplatesRecord myEmailTemplate = (ProcessEmailTemplatesRecord)sugar().processEmailTemplates.create(defaultPEData, ProcessCreateDrawer.Save.SAVE_AND_DESIGN);
		assertTrue("The guid for the create with SAVE_AND_DESIGN was empty!", !(myEmailTemplate.getGuid().isEmpty()));
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// TODO: VOOD-1539
		new VoodooControl("a", "css", ".fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info("verifySaveAndDesign() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().processEmailTemplates.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().processEmailTemplates);

		// Verify menu items
		sugar().processEmailTemplates.menu.getControl("createProcessEmailTemplate").assertVisible(true);
		sugar().processEmailTemplates.menu.getControl("viewProcessEmailTemplates").assertVisible(true);
		sugar().processEmailTemplates.menu.getControl("importProcessEmailTemplates").assertVisible(true);
		sugar().navbar.clickModuleDropdown(sugar().processEmailTemplates); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyListHeaders() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeaders()...");

		sugar().processEmailTemplates.navToListView();

		// Verify all headers in listview
		for(String header : sugar().processEmailTemplates.listView.getHeaders()){
			sugar().processEmailTemplates.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertExists(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeaders() test complete.");
	}

	@Test
	public void sortOrderByName() throws Exception {
		VoodooUtils.voodoo.log.info("Running sortOrderByName()...");

		String name = "New Email Template";
		// 2 email templates having 1 with default data and another with custom data (name)
		sugar().processEmailTemplates.api.create();
		FieldSet customPE = new FieldSet();
		customPE.put("name", name);
		sugar().processEmailTemplates.api.create(customPE);
		sugar().processEmailTemplates.navToListView(); // to reload data, somehow refresh page is not working on CI

		// Verify records after sort by 'name' in descending and ascending order
		sugar().processEmailTemplates.listView.sortBy("headerName", false);
		sugar().processEmailTemplates.listView.verifyField(1, "name", sugar().processEmailTemplates.getDefaultData().get("name"));
		sugar().processEmailTemplates.listView.verifyField(2, "name", name);

		sugar().processEmailTemplates.listView.sortBy("headerName", true);
		sugar().processEmailTemplates.listView.verifyField(1, "name", name);
		sugar().processEmailTemplates.listView.verifyField(2, "name", sugar().processEmailTemplates.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info("sortOrderByName() test complete.");
	}

	@Test
	public void verifyActionDropdown() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyActionDropdown()...");

		sugar().processEmailTemplates.api.create();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);

		// Verify action dropdown items
		sugar().processEmailTemplates.listView.getControl("edit01").assertVisible(true);
		sugar().processEmailTemplates.listView.getControl("delete01").assertVisible(true);
		sugar().processEmailTemplates.listView.getControl("design01").assertVisible(true);
		sugar().processEmailTemplates.listView.getControl("export01").assertVisible(true);
		sugar().processEmailTemplates.listView.getControl("dropdown01").click(); // to close action dropdown

		VoodooUtils.voodoo.log.info("verifyActionDropdown() complete.");
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().processEmailTemplates.api.create();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.editRecord(1);

		// Verify inline edit fields
		// name
		sugar().processEmailTemplates.listView.getEditField(1, "name").assertEquals(defaultPEData.get("name"), true);

		// target
		sugar().processEmailTemplates.listView.getEditField(1, "targetModule").assertExists(true);

		// assigned to
		VoodooSelect assignedTo = (VoodooSelect)sugar().processEmailTemplates.listView.getEditField(1, "relAssignedTo");
		assignedTo.set(sugar().users.getQAUser().get("userName"));
		assignedTo.assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Date Created (read only)
		VoodooControl dateCreated = sugar().processEmailTemplates.listView.getEditField(1, "date_entered_date");
		dateCreated.assertExists(true);
		dateCreated.assertAttribute("class", "edit", false);

		// Date modified (read only)
		VoodooControl dateModified = sugar().processEmailTemplates.listView.getEditField(1, "date_modified_date");
		dateModified.assertExists(true);
		dateModified.assertAttribute("class", "edit", false);

		sugar().processEmailTemplates.listView.cancelRecord(1);

		// verify list fields
		// name
		sugar().processEmailTemplates.listView.getDetailField(1, "name").assertEquals(defaultPEData.get("name"), true);

		// target
		sugar().processEmailTemplates.listView.getDetailField(1, "targetModule").assertExists(true);

		// assigned
		sugar().processEmailTemplates.listView.getDetailField(1, "relAssignedTo").assertVisible(true);

		// Date Created
		sugar().processEmailTemplates.listView.getDetailField(1, "date_entered_date").assertVisible(true);

		// Date modified
		sugar().processEmailTemplates.listView.getDetailField(1, "date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().processEmailTemplates.api.create();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.clickRecord(1);
		sugar().processEmailTemplates.recordView.edit();
		sugar().processEmailTemplates.recordView.showMore();

		// Verify edit record fields
		// name
		sugar().processEmailTemplates.recordView.getEditField("name").assertEquals(defaultPEData.get("name"), true);

		// target
		VoodooSelect target = (VoodooSelect)sugar().processEmailTemplates.recordView.getEditField("targetModule");
		target.set(sugar().calls.moduleNamePlural);
		target.assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processEmailTemplates.recordView.getEditField("relAssignedTo").assertVisible(true);

		// description
		sugar().processEmailTemplates.recordView.getEditField("description").assertExists(true);

		// date created
		sugar().processEmailTemplates.recordView.getEditField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processEmailTemplates.recordView.getEditField("date_modified_date").assertVisible(true);

		// save record
		sugar().processEmailTemplates.recordView.save();
		if(sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Verify detail fields
		// name
		sugar().processEmailTemplates.recordView.getDetailField("name").assertEquals(defaultPEData.get("name"), true);

		// target
		sugar().processEmailTemplates.recordView.getDetailField("targetModule").assertEquals(sugar().calls.moduleNamePlural, true);

		// assigned To
		sugar().processEmailTemplates.recordView.getDetailField("relAssignedTo").assertEquals("Administrator", true);

		// date created
		sugar().processEmailTemplates.recordView.getDetailField("date_entered_date").assertVisible(true);

		// date modified
		sugar().processEmailTemplates.recordView.getDetailField("date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	public void cleanup() throws Exception {}
}