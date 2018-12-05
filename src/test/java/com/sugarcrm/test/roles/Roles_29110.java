package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Roles_29110 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that after repair Roles empty link "ACL" should not display in the navigation bar
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_29110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet aclRoleData = testData.get(testName).get(0);

		// Navigate to admin -> system -> repair
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("repair").click();

		// Click on Repair roles
		// TODO: VOOD-1567
		new VoodooControl("a", "css", ".other.view tr:nth-child(19) a").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Verify that empty link "ACL" should not display in the navigation bar
		sugar().navbar.assertContains(aclRoleData.get("aclName"), false);
		sugar().navbar.assertContains(aclRoleData.get("roleName"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}