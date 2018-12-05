package com.sugarcrm.test.grimoire;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class ReportsModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.navToModule("Reports");
		sugar().navbar.clickModuleDropdown(sugar().reports);
		// Verify that all menus are present
		sugar().reports.menu.getControl("createReport").assertVisible(true);
		sugar().reports.menu.getControl("myFavoriteReports").assertVisible(true);
		sugar().reports.menu.getControl("viewReports").assertVisible(true);
		sugar().reports.menu.getControl("manageAdvancedReports").assertVisible(true);

		// TODO: VOOD-822
		VoodooControl moduleTitle = new VoodooControl("h2", "css", ".moduleTitle h2");

		// Verify that "Create Report" menu is functional
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		moduleTitle.assertContains("Report Wizard", true);
		VoodooUtils.focusDefault();

		// Verify that "My Favorite Reports" menu is functional
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("myFavoriteReports").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		moduleTitle.assertElementContains("My Favorite Reports", true);
		VoodooUtils.focusDefault();

		// Verify that "View Reports" menu is functional
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("viewReports").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		moduleTitle.assertElementContains("Search", true);
		VoodooUtils.focusDefault();

		// Verify that "Manage Advanced Reports" menu is functional
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("manageAdvancedReports").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		moduleTitle.assertContains("Search Advanced Reports", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	public void cleanup() throws Exception {}
}