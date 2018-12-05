package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21615 extends SugarTest {

	public void setup() throws Exception {
		// Logging in as qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * My Account_Email address value Displayed
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21615_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create an Account Record with qauser
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		String emailAddress = sugar().accounts.getDefaultData().get("emailAddress");
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.getEditField("emailAddress").set(emailAddress);
		sugar().accounts.createDrawer.save();

		// Navigate to Home Module
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// In "My Dashboard", click on the Configure icon(Gear icon) in the existing "My Contacts" Dashlet
		// TODO: VOOD-960 - Dashlet selection 
		new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(3) .btn-group").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".btn-group.open .dashlet-toolbar a").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div", "css", ".edit.fld_module div").set(sugar().accounts.moduleNamePlural);
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify Email address is displayed in the dashlet
		new VoodooControl("span", "css", "li.row-fluid.sortable:nth-of-type(3) .fld_email").assertEquals(emailAddress, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}