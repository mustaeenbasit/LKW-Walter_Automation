package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28260 extends SugarTest {
	VoodooControl compaignDropDownLink, roleDropDownCtrl, deSelectAllCtrl, mailDropDownCtrl, saveDropDownEditCtrl;
	FieldSet customFS = new FieldSet();


	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		FieldSet rolesData = testData.get("env_role_setup").get(0);

		// Login as a valid admin user
		sugar().login();

		// Create New Role and assign QAuser into it
		AdminModule.createRole(rolesData);

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(rolesData);

		// Go to DDE -> account_type_dom
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-781
		// Used xPath here to select specific drop down dom
		roleDropDownCtrl = new VoodooControl("select", "css", "select[name='dropdown_role']");
		new VoodooControl("a", "xpath", "//a[@class='mbLBLL' and text()='account_type_dom']").click();
		VoodooUtils.waitForReady(30000);

		// Select Role and Save it
		roleDropDownCtrl.set(rolesData.get("roleName"));
		VoodooUtils.waitForReady();	
		deSelectAllCtrl = new VoodooControl("input", "id", "select-none");
		deSelectAllCtrl.click(); // Deselect All
		new VoodooControl("input", "css", "#Prospect input:nth-child(3)").click();
		new VoodooControl("input", "css", "#Reseller input:nth-child(3)").click();
		new VoodooControl("input", "css", "#Other input:nth-child(3)").click();
		saveDropDownEditCtrl = new VoodooControl("input", "id", "saveBtn");
		saveDropDownEditCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Click on "campaign_type_dom" link
		compaignDropDownLink = new VoodooControl("a", "xpath", "//a[@class='mbLBLL' and text()='campaign_type_dom']");
		compaignDropDownLink.click();
		VoodooUtils.waitForReady();

		// Select Role
		roleDropDownCtrl.set(rolesData.get("roleName"));
		VoodooUtils.waitForReady();	
		deSelectAllCtrl.click(); // Deselect All
		mailDropDownCtrl = new VoodooControl("input", "css", "#Mail input:nth-child(3)");
		mailDropDownCtrl.click();
		new VoodooControl("input", "css", "#Email input:nth-child(3)").click();
		new VoodooControl("input", "css", "#Web input:nth-child(3)").click();
		saveDropDownEditCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Define Account modules controls in studio
		// TODO: VOOD-542, VOOD-1504 and VOOD-1506
		VoodooControl accountsModuleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldsCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl layoutCtrl=new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewCtrl=new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl roleListCtrl = new VoodooControl("select", "id", "roleList");
		VoodooControl saveAndDeployCtrl = new VoodooControl("input", "id", "publishBtn");

		// Go to studio -> Accounts -> Fields
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		accountsModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsCtrl.click();
		VoodooUtils.waitForReady();

		// Creates a dropdown type custom field.  Use "compaign_type_dom".  Dependent is "Parent Dropdown".  Select "Type" as parent Dropdown
		// TODO: VOOD-1504
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customFS.get("fieldType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customFS.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", "#options").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("option", "css", "#options option[label='campaign_type_dom']").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("select", "css", "#depTypeSelect").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("select", "css", "#depTypeSelect option[label='Parent Dropdown']").click();
		VoodooUtils.waitForReady();

		// Click on  "Edit Visibility"
		new VoodooControl("input", "css", "#visGridRow td:nth-child(2) button").click();
		VoodooUtils.waitForReady();

		//  Drag&drop "Email, Mail, Television, Radio" under "Reseller" then Save it. 
		VoodooControl dropHere = new VoodooControl("ul", "css", "#ddd_Reseller_list");
		new VoodooControl("li", "css", "#childTable li[val='Mail']").dragNDrop(dropHere);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#childTable li[val='Email']").dragNDrop(dropHere);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#childTable li[val='Radio']").dragNDrop(dropHere);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#childTable li[val='Television']").dragNDrop(dropHere);
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", "#visGridWindow div:nth-child(6) button:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Save creating new field
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// admin goes to studio->Accounts->Layouts->Record View, select Sally Role, adds one row and adds above custom field. Save&Deploy.
		sugar().admin.studio.clickStudio();
		accountsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();

		// Select firstRole Role, Add this field. Save & Deploy.
		roleListCtrl.set(rolesData.get("roleName"));
		VoodooUtils.waitForReady();

		// Drag and drop the custom created field
		// TODO: VOOD-1506
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]", (customFS.get("fieldName") + "_c")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);

		// Save & Deploy
		saveAndDeployCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Logout from Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that updates in the LOV in child dropdown get reflected in user's RBV
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28260_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Account > click on create button
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();

		// Verify that the custom dropdown field isn't appearing in the account create form yet.
		sugar().accounts.createDrawer.assertContains(customFS.get("fieldName"), false);

		sugar().accounts.createDrawer.getEditField("type").set(customFS.get("typeReseller"));

		// Verify that Once "Type=Reseller" is selected, the custom field appears.
		VoodooControl customDropDown = new VoodooControl("div", "css", "[data-name='testfield_c']");
		customDropDown.assertExists(true);

		// Verify that the Sally sees "Email, Mail" 2 values.  Should not see "Television" and "Radio" in the list.
		VoodooControl customDropDownValue = new VoodooControl("ul", "css", "#select2-drop .select2-results");
		customDropDown.click();
		customDropDownValue.assertContains("Email", true);
		customDropDownValue.assertContains("Mail", true);
		customDropDownValue.assertContains("Television", false);
		new VoodooControl("li", "css", "#select2-drop .select2-results > li").click(); // Close dropdown

		// Cancel creating createDrawer
		sugar().accounts.createDrawer.cancel();

		// Logout from QAuser and Login as Admin
		sugar().logout();
		sugar().login();

		// Go back to admin->DropDown Editor.
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");

		// Select compaign_type_dom. Select myRole, only select Print, Mail.  Save it.
		compaignDropDownLink.click();
		VoodooUtils.waitForReady();

		// Select Role
		roleDropDownCtrl.set(customFS.get("roleNameWithAsterisk"));
		VoodooUtils.waitForReady();	
		deSelectAllCtrl.click(); // Deselect All
		mailDropDownCtrl.click();
		new VoodooControl("input", "css", "#Print input:nth-child(3)").click();
		saveDropDownEditCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Logout from Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("type").set("Reseller");

		// Verify that Once "Type=Reseller" is selected, the custom field appears.
		customDropDown.assertExists(true);

		// Verify that the Sally sees "Email, Mail" 2 values.  Should not see "Television" and "Radio" in the list.
		customDropDown.click();
		customDropDownValue.assertContains("Email", false);
		customDropDownValue.assertContains("Mail", true);
		customDropDownValue.assertContains("Television", false);
		new VoodooControl("li", "css", "#select2-drop .select2-results li").click(); // Close dropdown

		// Cancel creating createDrawer
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}