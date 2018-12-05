package com.sugarcrm.test.roles;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_create extends SugarTest {
	FieldSet roleRecord = new FieldSet();

	public void setup() throws Exception {
		roleRecord = testData.get("env_role_setup").get(0);
		sugar().login();
	}

	@Test
	public void Roles_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		AdminModule.createRole(roleRecord);
		AdminModule.assignUserToRole(roleRecord);

		// Verify role record in detail view
		// TODO: VOOD-580, VOOD-856
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("h2", "css", ".moduleTitle h2").assertEquals(roleRecord.get("roleName"), true);
		new VoodooControl("td", "css", "table.detail.view tr td:nth-of-type(2)").assertEquals(roleRecord.get("roleName"), true);
		new VoodooControl("td", "css", "table.detail.view tr:nth-of-type(2) td:nth-of-type(2)").assertEquals(roleRecord.get("roleDescription"), true);
		new VoodooControl("td", "css", "tr.oddListRowS1 a").assertEquals(roleRecord.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}