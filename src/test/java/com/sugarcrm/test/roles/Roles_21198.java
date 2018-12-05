package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21198 extends SugarTest {
	DataSource roleFields;
		
	public void setup() throws Exception {
		sugar().login();
		
		roleFields = testData.get("Roles_21198");
		AdminModule.createRole(roleFields.get(0));
	}

	/**
	 * 21198 Verify that ACL settings are not applied to system admin user
	 * @throws Exception
	 */
	@Test
	public void Roles_21198_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "acl_roles_users_select_button").click();
		
		VoodooUtils.focusWindow(1);
		
		// Can't use assertContains(string, shouldContain); as there is an Administrator word in the table header and xpath //td/a is more reliable here
 		new VoodooControl("a", "xpath", "//td/a[contains(text(),'Administrator')]").assertExists(false);
		VoodooUtils.closeWindow();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
