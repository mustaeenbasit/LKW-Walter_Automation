package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_28264 extends SugarTest {
	DataSource recordsDataDS = new DataSource();
	UserRecord myFirstUser, mySecondUser;
	VoodooControl accountTypeDomCtrl, roleDropDownCtrl, accountsModuleCtrl, layoutCtrl, recordViewCtrl, saveAndDeployCtrl, roleListCtrl;

	public void setup() throws Exception {
		recordsDataDS = testData.get(testName);
		FieldSet rolesData = testData.get("env_role_setup").get(0);
		FieldSet recordNameFS = new FieldSet();

		// Login as a valid admin user
		sugar().login();

		// Create two custom regular user
		// TODO: VOOD-1200
		// Create user 'firstUser'
		recordNameFS.put("userName", recordsDataDS.get(0).get("userName"));
		recordNameFS.put("lastName", recordsDataDS.get(0).get("userName"));
		myFirstUser = (UserRecord) sugar().users.create(recordNameFS);
		// Create user 'secondUser'
		recordNameFS.put("userName", recordsDataDS.get(1).get("userName"));
		recordNameFS.put("lastName", recordsDataDS.get(1).get("userName"));
		mySecondUser = (UserRecord) sugar().users.create(recordNameFS);

		// Creates 2 Roles - Sally Role and Max Role
		// Create Role: 'firstRole' and assign to 'myFirstUser'
		rolesData.put("roleName", recordsDataDS.get(0).get("roleName"));
		AdminModule.createRole(rolesData);
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(myFirstUser);

		// Create Role: 'secondRole' and assign to 'mySecondUser'
		rolesData.put("roleName", recordsDataDS.get(1).get("roleName"));
		AdminModule.createRole(rolesData);
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(mySecondUser);

		// Define Account modules controls in studio
		// TODO: VOOD-542, VOOD-1504 and VOOD-1506
		accountsModuleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldsCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutCtrl=new VoodooControl("td", "id", "layoutsBtn");
		recordViewCtrl=new VoodooControl("td", "id", "viewBtnrecordview");
		roleListCtrl = new VoodooControl("select", "id", "roleList");
		saveAndDeployCtrl = new VoodooControl("input", "id", "publishBtn");

		// Go to studio -> Accounts -> Fields, Create a new DropDown Type field and uses account_type_dom.
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		accountsModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsCtrl.click();
		VoodooUtils.waitForReady();
		// Add field and save
		// TODO: VOOD-1504
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(recordsDataDS.get(0).get("fieldType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(recordsDataDS.get(0).get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Go to studio -> Accounts -> Layouts -> Record View
		sugar().admin.studio.clickStudio();
		accountsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();

		// Select firstRole Role, Add this field. Save & Deploy.
		roleListCtrl.set(recordsDataDS.get(0).get("roleName"));
		VoodooUtils.waitForReady();

		// Drag and drop the custom created field
		// TODO: VOOD-1506
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]", (recordsDataDS.get(0).get("fieldName") + "_c")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);

		// Save & Deploy
		saveAndDeployCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Go to DDE -> account_type_dom
		sugar().admin.studio.clickDropdownEditor();
		// TODO: VOOD-781
		// Used xPath here to select specific drop down dom
		accountTypeDomCtrl = new VoodooControl("a", "xpath", "//a[@class='mbLBLL' and text()='account_type_dom']");
		roleDropDownCtrl = new VoodooControl("select", "css", "select[name='dropdown_role']");
		accountTypeDomCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Select 'firstRole' Role, only keep 3 values in the list. Save it
		roleDropDownCtrl.set(recordsDataDS.get(0).get("roleName"));
		VoodooUtils.waitForReady();
		// TODO: VOOD-781
		new VoodooControl("input", "id", "select-none").click();
		new VoodooControl("input", "css", "#Analyst input:nth-child(3)").click();
		new VoodooControl("input", "css", "#Competitor input:nth-child(3)").click();
		new VoodooControl("input", "css", "#Customer input:nth-child(3)").click();
		new VoodooControl("input", "id", "saveBtn").click();
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Verify that different users ia able to see the correct LOV when log in within one browser
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28264_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to DDE -> account_type_dom
		accountTypeDomCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that asterisk is in the front of the 'firstRole' Role.
		roleDropDownCtrl.assertContains(recordsDataDS.get(0).get("roleNameWithAsterisk"), true);
		VoodooUtils.waitForReady();

		// Go to studio -> Accounts -> Layouts -> Record View
		sugar().admin.studio.clickStudio();
		accountsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();

		// Select 'secondRole' Role
		roleListCtrl.set(recordsDataDS.get(1).get("roleName"));
		VoodooUtils.waitForReady();

		// Copy from 'firstRole' Role
		// TODO: VOOD-1706
		new VoodooControl("input", "id", "copyBtn").click();
		new VoodooControl("button", "css", ".button-group button").click();
		VoodooUtils.waitForReady();

		// Save & Deploy
		saveAndDeployCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Admin logs out, 'mySecondUser' log into instance within the same browser
		sugar().logout();
		mySecondUser.login();

		// 'mySecondUser' tries to create a new Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();

		// TODO: VOOD-1036 and VOOD-1463
		VoodooControl customDropDownFieldCtrl = new VoodooControl("li", "css", ".select2-drop.select2-drop-active ul .select2-result");
		VoodooControl firstValueOfDropDownCtrl = new VoodooControl("div", "css", customDropDownFieldCtrl.getHookString() + ":nth-child(1) div");
		// TODO: VOOD-1036
		VoodooControl customFieldCtrl = new VoodooControl("span", "css", ".fld_testfield_c");
		customFieldCtrl.click();

		// Verify that the 'mySecondUser' clicks on the custom drop down field, sees all the values in the drop down list
		int dropDownValuesCount = recordsDataDS.size();
		Assert.assertTrue("All values are NOT visible in the drop down list", customDropDownFieldCtrl.countAll() == (dropDownValuesCount + 1));
		// TODO: VOOD-1463
		for(int i = 0; i < dropDownValuesCount; i++) {
			new VoodooControl("div", "css", customDropDownFieldCtrl.getHookString() + ":nth-child(" + (i+2) + ") div").assertContains(recordsDataDS.get(i).get("dropdownValues"), true);		
		}

		// Select any value to close the drop down 
		firstValueOfDropDownCtrl.click();

		// Cancel the Account create drawer
		sugar().accounts.createDrawer.cancel();

		// 'mySecondUser' logs out, 'myFirstUser' logs into within the same browser
		sugar().logout();
		myFirstUser.login();

		// 'myFirstUser' tries to create a new Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		customFieldCtrl.click();

		// Verify that the 'myFirstUser' clicks on the custom drop down field, sees only 3 values in the drop down list 
		Assert.assertTrue("Not exactly 3 values are visible in the drop down list", customDropDownFieldCtrl.countAll() == 3);
		// TODO: VOOD-1463
		for(int i = 0; i < 3; i++) {
			new VoodooControl("div", "css", customDropDownFieldCtrl.getHookString() + ":nth-child(" + (i+1) + ") div").assertContains(recordsDataDS.get(i).get("dropdownValues"), true);		
		}

		// Select any value to close the drop down 
		firstValueOfDropDownCtrl.click();

		// Cancel the Account create drawer
		sugar().accounts.createDrawer.cancel();

		// 'myFirstUser' logs out, QAUser logs into the sugar instance within the same browser. 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// QAUser tries to create a new Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.showMore();

		// Verify that QAUser doesn't have such custom drop down field
		// TODO: VOOD-1036
		new VoodooControl("span", "css", ".fld_testfield_c").assertExists(false);

		// Cancel the Account create drawer
		sugar().accounts.createDrawer.cancel();

		// QAUser logs out, Admin logs into within the same browser.  
		sugar().logout();
		sugar().login();

		// Admin goes to DDE -> account_type_dom. Clicks on Role drop down. 
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");
		accountTypeDomCtrl.click();
		VoodooUtils.waitForReady();
		roleDropDownCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that a star in the front of 'firstRole', not in 'secondRole'. 
		roleDropDownCtrl.assertContains(recordsDataDS.get(0).get("roleNameWithAsterisk"), true);
		roleDropDownCtrl.assertContains(recordsDataDS.get(1).get("roleName"), true);
		roleDropDownCtrl.assertContains(recordsDataDS.get(1).get("roleNameWithAsterisk"), false);

		// Clicks on 'secondRole' 
		roleDropDownCtrl.set(recordsDataDS.get(1).get("roleName"));
		VoodooUtils.waitForReady();

		// Verify that all default values enabled and appear
		// TODO: VOOD-781
		VoodooControl defaultDropDownValuesCtrl = new VoodooControl("li", "css", ".listContainer .draggable");
		Assert.assertTrue("All values are NOT visible", defaultDropDownValuesCtrl.countAll() == (dropDownValuesCount + 1));
		for(int i = 0; i <= dropDownValuesCount; i++){
			Assert.assertTrue("All values are NOT enabled, when they should be", new VoodooControl("input", "css", defaultDropDownValuesCtrl.getHookString() + ":nth-child(" + (i+1) + ") input:nth-child(3)").isChecked());
		}

		// Clicks on 'firstRole'
		roleDropDownCtrl.set(recordsDataDS.get(0).get("roleNameWithAsterisk"));
		VoodooUtils.waitForReady();

		// Verify that only 3 values are enabled, others are crossed out
		// TODO: VOOD-781
		Assert.assertFalse("Not exactly 3 values are checked, when they should not", new VoodooControl("input", "css", defaultDropDownValuesCtrl.getHookString() + ":nth-child(1) input:nth-child(3)").isChecked());
		for(int i = 1; i <= dropDownValuesCount; i++){
			if (i < 4) {
				Assert.assertTrue("Not exactly 3 values are checked in the drop down list", new VoodooControl("input", "css", defaultDropDownValuesCtrl.getHookString() + ":nth-child(" + (i+1) + ") input:nth-child(3)").isChecked());
			} else {
				Assert.assertFalse("Values other then selected 3 values are also checked in the drop down list, when they should not", new VoodooControl("input", "css", defaultDropDownValuesCtrl.getHookString() + ":nth-child(" + (i+1) + ") input:nth-child(3)").isChecked());
			}
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}