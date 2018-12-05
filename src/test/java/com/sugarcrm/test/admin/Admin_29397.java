package com.sugarcrm.test.admin;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29397 extends SugarTest{
	public void setup() throws Exception {
		// Login as Admin user
		sugar().login();
	}

	/**
	 * Verify that 'Asynchronous Call Failed' message should not be seen on admin -> repair for 'Remove XSS'
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_29397_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Now navigate to Admin -> repair
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("repair").click();
		VoodooUtils.waitForReady();

		// Now Select Remove XSS -> Select All from drop down list -> Click on Execute
		// TODO: VOOD-1567 - Need lib support for 'Repair' tools
		new VoodooControl("a", "css", "[href*='RepairXSS']").click();
		VoodooUtils.waitForReady();
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("select", "id", "repairXssDropdown").set(customData.get("all"));
		new VoodooControl("input", "id", "repairXssButton").click();

		// Verify that 'Asynchronous Call Failed' message should not be seen on Admin -> repair for 'Remove XSS'
		// TODO: VOOD-1045 - Library support needed for asserting the message appeared in javascript dialog box.
		VoodooUtils.waitForDialog();
		Assert.assertFalse("XSS Vulnerabilities are not removed from the database", customData.get("messageFailed").equals(VoodooUtils.iface.wd.switchTo().alert().getText()));

		// Verify that Remove XSS should be executed for all of the modules and user should get a 'Done' message
		// TODO: VOOD-1045 - Library support needed for asserting the message appeared in javascript dialog box.
		Assert.assertTrue("XSS Vulnerabilities are not removed from the database", customData.get("messageSuccess").equals(VoodooUtils.iface.wd.switchTo().alert().getText()));
		VoodooUtils.acceptDialog();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}