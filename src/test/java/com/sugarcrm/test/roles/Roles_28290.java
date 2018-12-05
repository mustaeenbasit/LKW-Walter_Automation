package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28290 extends SugarTest {
	FieldSet customData;
	VoodooControl accountsCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
		
		// Create a new Role.
		AdminModule.createRole(customData);
		
		// Assign QAuser to the Role
		AdminModule.assignUserToRole(customData);
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Go to Studio-> Accounts-> Fields, create a dropdown field
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("select", "id", "type").set(customData.get("dataType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(testName);
		
		// Select 'campaign_type' as the dropdown value
		new VoodooControl("select", "id", "options").set(customData.get("dom"));
		VoodooUtils.waitForReady();
		
		// Select "Parent Dropdown" from the Dependent dropdown
		new VoodooControl("select", "id", "depTypeSelect").set(customData.get("depTypeSelect"));
		VoodooUtils.waitForReady();
		
		// Select Type as parent dropdown
		new VoodooControl("select", "id", "parent_dd").set(customData.get("parent_dd"));
		
		// Edit Visibility to make 3 values available under Type = Reseller, such as Email, Web, and Mail
		new VoodooControl("button", "css", "#visGridRow > td:nth-child(2) > button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "li[val='Mail']").dragNDrop(new VoodooControl("ul", "id", "ddd_Reseller_list"));
		new VoodooControl("li", "css", "li[val='Email']").dragNDrop(new VoodooControl("ul", "id", "ddd_Reseller_list"));
		new VoodooControl("li", "css", "li[val='Web']").dragNDrop(new VoodooControl("ul", "id", "ddd_Reseller_list"));
		new VoodooControl("input", "css", "#visGridWindow > div.bd > div:nth-child(6) > button:nth-child(2)").click();
		
		// Save and Deploy
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// Studio->Accounts->Layouts ->Record View 
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		
		// Select qauser Role
		new VoodooControl("select", "id", "roleList").set(customData.get("roleName"));
		VoodooUtils.waitForReady();
		
		// Add custom dropdown field to Record View
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		
		// Add Custom dropdown field 
		String dataNameDraggableField = String.format("div[data-name=%s_c]",testName.toLowerCase());
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		sugar().logout();
	}

	/**
	 * Verify that ACLRoles action "EditRole" honors option hidden_to_role_assignment
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_28290_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Qauser goes to Accounts and create a new Account
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.create();
		
		// In 'Type' field select Customer
		VoodooControl type = sugar().accounts.createDrawer.getEditField("type");
		type.set(customData.get("customer"));
		VoodooUtils.waitForReady();
		
		// Verify that custom dropdown doesn't appear
		new VoodooControl("span", "css", "[data-fieldname='"+testName.toLowerCase()+"_c']").assertVisible(false);
		
		// Now select Customer in 'Type' field
		type.set(customData.get("reseller"));
		VoodooUtils.waitForReady();
		
		// Verify that custom dropdown appears
		new VoodooControl("span", "css", "[data-fieldname='"+testName.toLowerCase()+"_c']").assertVisible(true);
		
		// Click on the dropdown and select Email, save
		new VoodooSelect("span", "css", "[data-fieldname='"+testName.toLowerCase()+"_c']").set(customData.get("value"));
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();
		
		// Verify that account record is saved and custom dropdown field has value of Email
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.listView.clickRecord(1);
		new VoodooControl("span", "css", ".fld_"+testName.toLowerCase()+"_c.detail").assertContains(customData.get("value"), true);
		
		// Edit the account, select Type = Customer
		sugar().accounts.recordView.edit();
		type.set(customData.get("customer"));
		VoodooUtils.waitForReady();
		
		// Verify that custom dropdown field disappears
		new VoodooControl("span", "css", "[data-fieldname='"+testName.toLowerCase()+"_c']").assertVisible(false);
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}