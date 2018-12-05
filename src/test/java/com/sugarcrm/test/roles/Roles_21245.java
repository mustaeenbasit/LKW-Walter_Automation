package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Roles_21245 extends SugarTest {
	FieldSet roleRecord = new FieldSet(), customData = new FieldSet();
	VoodooControl massUpdateAccess, massUpdateDropdown, saveRole;

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		customData = testData.get(testName).get(0);

		// TODO: VOOD-580, VOOD-856
		massUpdateAccess = new VoodooControl("td", "css", "#ACLEditView_Access_Contacts_massupdate");
		massUpdateDropdown = new VoodooControl("select", "css", "#ACLEditView_Access_Contacts_massupdate div select");
		saveRole = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");

		// 2 contact records creation via API
		sugar().contacts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("lastName", customData.get("last_name"));
		fs.put("firstName", customData.get("first_name"));
		sugar().contacts.api.create(fs);
		sugar().login();

		// Create role => Contacts => Mass update=All => assign to QAUser
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");
		massUpdateAccess.click();
		massUpdateDropdown.set(customData.get("mass_update_all_access"));
		saveRole.click();
		VoodooUtils.waitForReady();

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();
		sugar().logout();
	}

	/**
	 * Verify user can mass update the records while the Mass Update role is set to All/Not Set
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_21245_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String qaUsername = sugar().users.getQAUser().get("userName");

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();

		// Verify mass update link in open action dropdown (i.e massUpdateButton control click already takes care its existence)
		sugar().contacts.listView.massUpdate();
		VoodooControl massUpdateField = sugar().contacts.massUpdate.getControl(String.format("massUpdateField%02d", 2));
		VoodooControl massUpdateValue = sugar().contacts.massUpdate.getControl(String.format("massUpdateValue%02d", 2));
		massUpdateField.set(customData.get("display_name"));
		massUpdateValue.set(qaUsername);
		sugar().contacts.massUpdate.update();

		// Verify changes should be selected to records
		sugar().contacts.listView.verifyField(1, customData.get("sugar_field"), qaUsername);
		sugar().contacts.listView.verifyField(2, customData.get("sugar_field"), qaUsername);
		sugar().logout();

		// Login as Admin, Edit role => Contacts => Mass update=Not Set
		sugar().login();
		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(3) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		massUpdateAccess.click();
		massUpdateDropdown.set(customData.get("mass_update_not_set_access"));
		saveRole.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();

		// Again Login as QAUser
		sugar().login(sugar().users.getQAUser());
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.listView.openActionDropdown();

		// Verify mass update link in open action dropdown (i.e massUpdateButton control click already takes care its existence)
		sugar().contacts.listView.massUpdate();
		massUpdateField.set(customData.get("display_name"));
		massUpdateValue.set(customData.get("admin"));
		sugar().contacts.massUpdate.update();

		// Verify changes should be selected to records to make sure role change does not affect mass update records
		sugar().contacts.listView.verifyField(1, customData.get("sugar_field"), customData.get("admin"));
		sugar().contacts.listView.verifyField(2, customData.get("sugar_field"), customData.get("admin"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}