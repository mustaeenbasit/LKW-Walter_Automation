package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.DocumentRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_21258_BWC extends SugarTest {
	DocumentRecord myDocumentRecord;
	StandardSubpanel documentsSubpanel;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		myDocumentRecord = (DocumentRecord) sugar().documents.api.create();
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and select a module -> Set Edit Role to be All
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Document Edit cell to All
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Documents_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Documents_edit div select").set(roleRecord.get("roleAll"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

		// Link a Document with an Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		documentsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().documents.moduleNamePlural);
		documentsSubpanel.linkExistingRecord(myDocumentRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify edit and related actions are shown in the list view and detail view while the Edit role is set to All or Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21258_BWC_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the select module -> Click on "Create" link of module from top navigation bar
		sugar().navbar.selectMenuItem(sugar().documents, "createDocument");
		VoodooUtils.focusFrame("bwc-frame");

		// user should be able to create
		sugar().documents.editView.getEditField("documentName").set(testName);
		VoodooControl fileNameFieldCtrl = sugar().documents.editView.getEditField("fileNameFile");
		VoodooFileField importFileDocument = new VoodooFileField(fileNameFieldCtrl.getTag(), fileNameFieldCtrl.getStrategyName(), fileNameFieldCtrl.getHookString());

		// Getting the file for Document
		importFileDocument.set("src/test/resources/data/" + testName + ".csv");
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().documents.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the Document record is created successfully
		VoodooControl documentNameCtrl = sugar().documents.detailView.getDetailField("documentName");
		documentNameCtrl.assertContains(testName, true);

		// Check the drop down action on the detail view
		sugar().documents.detailView.openPrimaryButtonDropdown();

		FieldSet documentsData = testData.get(testName).get(0);

		// Verify that the Edit and related actions should be shown in the detail view action list: Edit, Duplicate
		sugar().documents.detailView.getControl("editButton").assertEquals(documentsData.get("edit"), true);
		sugar().documents.detailView.getControl("deleteButton").assertEquals(documentsData.get("delete"), true);
		sugar().documents.detailView.getControl("copyButton").assertEquals(documentsData.get("copy"), true);

		// Close the opened primary action drop down'
		sugar().documents.detailView.openPrimaryButtonDropdown();
		VoodooUtils.focusDefault();

		// Navigate to Documents list view
		sugar().documents.navToListView();
		sugar().documents.listView.toggleSelectAll();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that for BWC modules: The quick edit action (the pencil icon) should be shown next all the records
		sugar().documents.listView.getControl("edit01").assertVisible(true);
		VoodooUtils.focusDefault();

		// Verify that the Mass Update should be available
		sugar().documents.listView.openActionDropdown();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.listView.getControl("massUpdateButton").assertContains(documentsData.get("massUpdate"), true);

		// Close the row action drop down and de-select all the record
		sugar().documents.listView.getControl("actionDropdown").click();
		VoodooUtils.focusDefault();
		sugar().documents.listView.toggleSelectAll();

		// Check quick create list by clicking + sign on the top right -> user should be able to create
		sugar().navbar.quickCreateAction(sugar().documents.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// user should be able to create
		sugar().documents.editView.getEditField("documentName").set(documentsData.get("quickCreateDocument"));
		// Getting the file for Document
		importFileDocument.set("src/test/resources/data/" + testName + ".csv");
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().documents.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that the Document record is created successfully
		documentNameCtrl.assertContains(documentsData.get("quickCreateDocument"), true); 
		VoodooUtils.focusDefault();

		// Check related module sub-panel 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		documentsSubpanel.expandSubpanel();
		documentsSubpanel.expandSubpanelRowActions(1);

		// Verify that the Edit pencil button should be shown on the sub-panel for this module(i.e. documents)
		documentsSubpanel.getControl("editActionRow01").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}