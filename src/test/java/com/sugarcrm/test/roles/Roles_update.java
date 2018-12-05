package com.sugarcrm.test.roles;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_update extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();
		// Create Role via UI
		AdminModule.createRole(roleRecord);
	}

	@Test
	public void Roles_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("roleName", "Updated role");
		newData.put("roleDescription", "Updated description");

		// Edit the role using the UI.
		// TODO: VOOD-580, VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "ACLROLE_EDIT_BUTTON").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "name").set(newData.get("roleName"));
		new VoodooControl("textarea", "css", ".edit.view textarea").set(newData.get("roleDescription"));
		new VoodooControl("input", "id", "save_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		// Assign user to role
		AdminModule.assignUserToRole(roleRecord);

		// Verify new role fields
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h2", "css", ".moduleTitle h2").assertEquals(newData.get("roleName"), true);
		new VoodooControl("td", "css", "table.detail.view tr td:nth-of-type(2)").assertEquals(newData.get("roleName"), true);
		new VoodooControl("td", "css", "table.detail.view tr:nth-of-type(2) td:nth-of-type(2)").assertEquals(newData.get("roleDescription"), true);
		new VoodooControl("td", "css", "tr.oddListRowS1 a").assertEquals(roleRecord.get("userName"), true);
		VoodooUtils.focusDefault();

		// This will make sure role record is deleted accordingly
		roleRecord.put("roleName", newData.get("roleName"));
		roleRecord.put("roleDescription", newData.get("roleDescription")); 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}