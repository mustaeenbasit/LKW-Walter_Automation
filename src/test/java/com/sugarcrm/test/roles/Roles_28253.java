package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28253 extends SugarTest {
	FieldSet roleRecord, customData;
	String dataNameDraggableField;
	VoodooControl fieldNameCtrl,saveButtonCtrl,dataTypeCtrl,addFieldCtrl,moveToNewFilter,moveToLayoutPanelCtrl,recordViewCtrl,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,accountsCtrl;

	public void setup() throws Exception {
		roleRecord = testData.get(testName).get(0);
		customData = testData.get(testName + "_customFieldData").get(0);
		sugar().login();

		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
		addFieldCtrl=new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		dataTypeCtrl=new VoodooControl("select", "id", "type");
		saveButtonCtrl=new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		fieldNameCtrl=new VoodooControl("input", "id", "field_name_id");

		// Create Sally Role
		AdminModule.createRole(roleRecord);
		// Assign QA user to role
		AdminModule.assignUserToRole(roleRecord);

		// Admin -> Dropdown Editor
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-781 -need lib support of dropdown editor functions
		new VoodooControl("a", "css", "#dropdowneditor").click();
		sugar().alerts.waitForLoadingExpiration();

		// Select account_type_dom
		new VoodooControl("a", "css", "#dropdowns tr:nth-child(1) td:nth-child(1) a").click();
		// Select Sally Role
		VoodooControl roleCtrl=new VoodooControl("select", "css", "[name='dropdown_role']");
		roleCtrl.set(roleRecord.get("roleName"));
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.waitForReady();
		VoodooControl deselectCtrl=new VoodooControl("input", "id" ,"select-none");
		deselectCtrl.click();

		// Select Reseller, Prospect and Others
		new VoodooControl("input", "css", "[name='dropdown_keys[Reseller]']:nth-child(3)").click();
		new VoodooControl("input", "css", "[name='dropdown_keys[Prospect]']:nth-child(3)").click();
		new VoodooControl("input", "css", "[name='dropdown_keys[Other]']:nth-child(3)").click();

		VoodooControl saveBtnCtrl=new VoodooControl("input", "id", "saveBtn");
		saveBtnCtrl.scrollIntoView();
		// Save
		saveBtnCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Select campaign_type_dom
		new VoodooControl("a", "css", "#dropdowns tr:nth-child(5) td:nth-child(3) a").click();
		sugar().alerts.waitForLoadingExpiration();
		// Select Sally Role
		VoodooUtils.pause(2000); // pause required for AJAX call
		roleCtrl.set(roleRecord.get("roleName"));
		sugar().admin.studio.waitForAJAX(30000);
		deselectCtrl.click();

		// Select Email, Mail and Web
		new VoodooControl("input", "css", "[name='dropdown_keys[Mail]']:nth-child(3)").click();
		new VoodooControl("input", "css", "[name='dropdown_keys[Email]']:nth-child(3)").click();
		new VoodooControl("input", "css", "[name='dropdown_keys[Web]']:nth-child(3)").click();

		saveBtnCtrl.scrollIntoView();
		// Save
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Admin -> Studio -> Accounts -> Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();

		addFieldCtrl.click();
		dataTypeCtrl.set(customData.get("dataType"));
		sugar().alerts.waitForLoadingExpiration();
		sugar().admin.studio.waitForAJAX(30000);

		// Use "campaign_type_dom"
		new VoodooControl("select", "id", "options").set(customData.get("dropDownList"));
		// Select Dependent  "Parent Dropdown"
		new VoodooControl("select", "id", "depTypeSelect").set(customData.get("dependentType"));
		// Edit Visibility
		new VoodooControl("button", "css", "#visGridRow td:nth-child(2) button").click();

		// Drag&drop "Email, Mail, Television, Radio" under "Reseller"
		VoodooControl dropHere= new VoodooControl("ul", "id", "ddd_Reseller_list");
		new VoodooControl("li", "css", "#childTable > li:nth-child(3)").dragNDrop(dropHere);
		new VoodooControl("li", "css", "#childTable > li:nth-child(4)").dragNDrop(dropHere);
		new VoodooControl("li", "css", "#childTable > li:nth-child(7)").dragNDrop(dropHere);
		new VoodooControl("li", "css", "#childTable > li:nth-child(8)").dragNDrop(dropHere);
		// Save
		new VoodooControl("button", "css", "div:nth-child(6) button:nth-child(2)").click();
		fieldNameCtrl.set(customData.get("fieldName"));
		// Save
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Studio -> Accounts- > Layouts -> Record View
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();

		// Select Sally Role
		new VoodooControl("select", "id", "roleList").set(roleRecord.get("roleName"));
		sugar().alerts.waitForLoadingExpiration();
		sugar().admin.studio.waitForAJAX(30000);

		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		// Add one row	
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		// Add Dropdown Custom field
		dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that RBV in Role based user respects LOV setting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28253_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();

		// Verify custom dropdown field isn't appearing in the account create form yet. 
		sugar().accounts.createDrawer.assertContains(customData.get("fieldName"), false);

		// Select Type
		sugar().accounts.createDrawer.getEditField("type").set(customData.get("type"));

		// Verify Once "Type=Reseller" is selected, the custom field appears
		// TODO: VOOD-1036 Need library support for Accounts/any sidecar module for newly created custom fields
		dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).assertVisible(true);

		// Expand all values in custom dropdown field
		new VoodooControl("span", "css", ".fld_dropdownfield_c.edit .select2-arrow").click();

		// Verify Sally sees "Email, Mail" 2 values
		new VoodooControl("ul", "css", "#select2-drop ul").assertContains(customData.get("fieldValue1"), true);
		new VoodooControl("ul", "css", "#select2-drop ul").assertContains(customData.get("fieldValue2"), true);

		// Verify Sally does not see "Television" and "Radio" in the list
		new VoodooControl("ul", "css", "#select2-drop ul").assertContains(customData.get("fieldValue3"), false);
		new VoodooControl("ul", "css", "#select2-drop ul").assertContains(customData.get("fieldValue4"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}