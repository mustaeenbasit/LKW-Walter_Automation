package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28811 extends SugarTest {
	VoodooControl moduleCtrl, studioFooterLink, fieldsCtrl;
	FieldSet fieldData = new FieldSet();

	public void setup() throws Exception {
		fieldData = testData.get(testName).get(0);
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleData);

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleData);
		VoodooUtils.waitForReady();

		// Studio Controls
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		studioFooterLink = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");

		// Navigate to Admin > Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Go to Accounts -> Fields -> Add Field
		// TODO: VOOD-1504
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsCtrl = new VoodooControl("td", "id", "fieldsBtn");
		fieldsCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(fieldData.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(60000);
	}

	/**
	 * Verify the custom field is removed from role based and default record view if the custom field is deleted in studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28811_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Add created custom fields to Studio -> Accounts -> Layouts -> Record View
		studioFooterLink.click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Add custom field in the default view and custom role view (myRole view)
		new VoodooControl("div", "css", "#toolbox .le_row").dragNDrop(new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel"));
		VoodooControl customFieldName = new VoodooControl("div", "css", "div[data-name='"+ fieldData.get("fieldName") + "_c']");
		customFieldName.scrollIntoViewIfNeeded(false);
		customFieldName.dragNDrop(new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel .le_row div:nth-child(1).le_field.special"));
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Logout from admin and logged in as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Accounts -> Create a new account. Enter/select something for custom field.
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("name").set(fieldData.get("accountName"));
		// Define custom field controls 
		// TODO: VOOD-1036
		VoodooControl customFieldEditViewCtrl = new VoodooControl("input", "css", ".fld_" + fieldData.get("fieldName") + "_c.edit input");
		VoodooControl customFieldDetailViewCtrl = new VoodooControl("div", "css", ".fld_" + fieldData.get("fieldName") + "_c.detail div");
		customFieldEditViewCtrl.set(testName);

		// Save and view the record
		sugar().accounts.createDrawer.save();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.getDetailField("name").assertEquals(fieldData.get("accountName"), true);
		// Verify that the Custom field with value is displayed
		customFieldDetailViewCtrl.assertEquals(testName, true);

		// Logout and login as admin
		sugar().logout();
		sugar().login();

		// Open an existing account in record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that the Custom field with value is displayed
		customFieldDetailViewCtrl.assertEquals(testName, true);

		// Go to admin -> studio -> Accounts -> Fields and delete the custom field
		// Navigate to Admin > Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1504
		new VoodooControl("a", "id", fieldData.get("fieldName") + "_c").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='fdeletebtn']").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Open an existing account in record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that the Custom field is not displayed
		customFieldDetailViewCtrl.assertExists(false);

		// Logout from admin and logged in as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Accounts module -> Go to record view of the created record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that the Custom field is not displayed
		customFieldDetailViewCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}