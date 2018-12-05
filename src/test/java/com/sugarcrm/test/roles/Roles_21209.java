package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21209 extends SugarTest {
	FieldSet roleRecord;

	public void setup() throws Exception {
		roleRecord = testData.get("Roles_21209").get(0);
		sugar().login();
		AdminModule.createRole(roleRecord);
		// The User is now on the ACL Matrix screen after the Role and
		// Description have been Saved
	}

	/**
	 * Verify Field permissions set to Read Owner Write on Accounts allow Inline
	 * edit of User owned records
	 * 
	 * @throws Exception
	 */
	@Test
	// TODO The VoodooControl references will be replaced by VOOD-580 Create a
	// Roles (ACL) Module LIB
	public void Roles_21209_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now on the Access matrix - Click the Accounts module and set the
		// field access
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".edit.view tr:nth-of-type(2) td a")
				.click();
		new VoodooControl("div", "id", "phone_officelink").click();
		new VoodooControl("select", "id", "flc_guidphone_office")
				.set("Read/Owner Write");
		
		new VoodooControl("div", "id", "email1link").click();
		
		// TODO: Investigate as to why select#flc_guidemail1 is not appearing after first click
		if (!(new VoodooControl("select", "id", "flc_guidemail1").queryVisible()))
				new VoodooControl("div", "id", "email1link").click();
		
		new VoodooControl("select", "id", "flc_guidemail1")
				.set("Read/Owner Write");
		
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname")
				.set("Read/Owner Write");
		new VoodooControl("div", "id", "billing_addresslink").click();
		new VoodooControl("select", "id", "flc_guidbilling_address")
				.set("Read/Owner Write");
		// Save the Role
		// TODO SP-1898
		new VoodooControl("input", "css", "div#category_data .button").click();

		// Assign a non-admin, QAuser, user to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		// Log out of Sugar as Admin and log in as QAuser to verify Owner inline
		// edit access to Accounts
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
		// Create an Account via the GUI so that QAUser is the assigned User
		// TODO VOOD-444 Support creating relationships via API
		sugar().accounts.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.editRecord(1);
		// TODO Explicit VDD Controls will be replaced when VOOD-503 - inline
		// edits is implemented
		new VoodooControl("input", "css", ".fld_name.edit input")
				.set(roleRecord.get("accName"));
		new VoodooControl("input", "css",
				".fld_billing_address_city.edit input").set(roleRecord
				.get("city"));
		new VoodooControl("input", "css",
				".fld_billing_address_country.edit input").set(roleRecord
				.get("country"));
		new VoodooControl("input", "css", ".fld_phone_office.edit input")
				.set(roleRecord.get("phone"));
		new VoodooControl("input", "css", ".fld_email.edit input")
				.set(roleRecord.get("email"));
		sugar().accounts.listView.saveRecord(1);
		sugar().accounts.listView.verifyField(1, "name",
				roleRecord.get("accName"));
		sugar().accounts.listView.verifyField(1, "billingAddressCity",
				roleRecord.get("city"));
		sugar().accounts.listView.verifyField(1, "billingAddressCountry",
				roleRecord.get("country"));
		sugar().accounts.listView.verifyField(1, "workPhone",
				roleRecord.get("phone"));
		sugar().accounts.listView.verifyField(1, "emailAddress",
				roleRecord.get("email"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
