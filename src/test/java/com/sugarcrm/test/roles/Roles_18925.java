package com.sugarcrm.test.roles;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Assert;
import org.junit.Test;

public class Roles_18925 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify "Account name" field is displayed once (not twice) in the Roles management "Contacts" permissions table
	 *
	 * @throws Exception
	 */
	@Test
	public void Roles_18925_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("rolesManagement");
		VoodooUtils.focusFrame("bwc-frame");

		// Click on 'Contacts'
		// xpath needed here because there is neither proper id (or class) available for 'Customer Support Administrator' nor it has stable position.
		// TODO: VOOD-580
		new VoodooControl("a", "css", "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//*[@class='edit view']/tbody/tr/td[contains(.,'" + sugar().contacts.moduleNamePlural + "')]/a").click();

		// Verify that "Account name" field should be displayed once
		Assert.assertTrue("Account name field is not displaying once", new VoodooControl("a", "id", "d_account_name_anchor").count() == 1);

		// cancel the contacts role based view
		// TODO: VOOD-580
		new VoodooControl("input", "id", "ACLROLE_CANCEL_BUTTON").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}