package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Users_24711 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as admin
		sugar().login();

		// Create a role 
		AdminModule.createRole(roleRecord);

		// Assign QAuser to the Role
		AdminModule.assignUserToRole(roleRecord);
	}

	/**
	 * Verify that Default privilege in ACL list is displayed as All in user detail view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24711_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open users (qauser) record view
		sugar().users.navToListView();
		sugar().users.listView.basicSearch(sugar().users.getQAUser().get("userName"));
		sugar().users.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click Access Tab
		// TODO: VOOD-563
		new VoodooControl("a", "id", "tab3").click();

		// Verify Access for modules = Enabled, Normal, All, All ...
		// Access Table Rows count = 35, first 2 rows are headers
		FieldSet tableData = testData.get(testName).get(0);
		// TODO: VOOD-563
		for (int tr = 0; tr < Integer.parseInt(tableData.get("trCount")); tr++) {
			// First Column data = "Enabled"
			new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child(2) .aclEnabled b").assertEquals(tableData.get("enabled"), true);
			// Second Column data = "Normal"
			new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child(3) .aclNormal b").assertEquals(tableData.get("normal"), true);
			// Remaining 7 Columns data = "All"
			for (int td = 0; td < Integer.parseInt(tableData.get("tdCount")); td++)
				new VoodooControl("b", "css", "#user_detailview_tabs tr:nth-child("+ (tr+2)+ ") td:nth-child("+ (td+4)+ ") .aclAll b").assertEquals(tableData.get("all"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}