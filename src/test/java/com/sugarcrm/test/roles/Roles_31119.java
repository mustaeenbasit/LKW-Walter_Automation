package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_31119 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify Record data is displayed in the listview when View permission are set on Roles
	 * @throws Exception
	 */
	@Test
	public void Roles_31119_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating Role
		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		AdminModule.createRole(roleRecord);

		// TODO VOOD-856
		// Set the Contact View cell to None
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css",	"#ACLEditView_Access_Contacts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Contacts_view div select").set(roleRecord.get("roleNone"));

		// Set the Revenue Line Item View cell to None
		new VoodooControl("div", "css",	"#ACLEditView_Access_RevenueLineItems_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_RevenueLineItems_view div select").set(roleRecord.get("roleNone"));

		// Save the Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();

		// Assign this role to qauser
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);

		// Log out of Sugar as Admin
		sugar().logout();

		// Log in as QAuser
		sugar().login(sugar().users.getQAUser());

		// Navigate to Contact List View
		sugar().contacts.navToListView();

		// Verify the contact name in listview
		sugar().contacts.listView.getDetailField(1, "fullName").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);
		
		// TODO: VOOD:911
		// Verify the contact name is not a hyperlink in list view
		new VoodooControl("a", "css", ".fld_full_name.list a").assertExists(false);

		// Navigate to RLI
		sugar().revLineItems.navToListView();
		
		// TODO: VOOD:911
		// Verify the RLI name in listview
		new VoodooControl("span", "css", ".fld_name.list").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);

		// Verify the RLI name is not a hyperlink in list view
		new VoodooControl("a", "css", ".fld_name.list a").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
