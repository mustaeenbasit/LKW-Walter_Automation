package com.sugarcrm.test.roles;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_delete extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();
		// Create Role via UI
		AdminModule.createRole(roleRecord);
		AdminModule.assignUserToRole(roleRecord);
	}

	@Test
	public void Roles_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the Role using the UI
		AdminModule.deleteRole(roleRecord);

		// Verify the role was deleted.
		assertEquals(VoodooUtils.contains(roleRecord.get("roleName"), true), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}