package com.sugarcrm.test.projects;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_30073 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Enable Projects module from Admin > Display modules and subpanels
		sugar().admin.enableModuleDisplayViaJs(sugar().projects);
		
		// Go to Admin > Configure Navigation Bar Quick Create
		// TODO: VOOD-1150 : Need Library support for Admin > Configure Navigation Bar Quick Create.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("configurationShortcutBar").click();
		VoodooUtils.waitForReady();
		
		// Move Projects modules from right to left (i.e. Enabled Modules). 
		// TODO: TR-12046: Need unique and persistent testability hooks for "Modules" in Admin > Configure Navigation Bar Quick Create
		new VoodooControl("tr", "css", "#disabled_div .yui-dt-data tr:nth-child(4)").dragNDrop(new VoodooControl("tr", "css", "#enabled_div .yui-dt-data tr"));
		
		// Save settings
		new VoodooControl("input", "css", "[value='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that application is linking account record in subpanel of Project during creation of Project through "Quick Create"
	 * @throws Exception
	 */
	@Test
	public void Projects_30073_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		// Create project using Quick Create
		sugar().navbar.openQuickCreateMenu();
		
		// TODO: VOOD-828 and SFA-3348
		new VoodooControl("button", "css", ".navbar [data-voodoo-name='quickcreate'] [data-module='Project']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		sugar().projects.editView.getEditField("name").set(sugar().projects.getDefaultData().get("name"));
		sugar().projects.editView.getEditField("date_estimated_start").set(sugar().projects.getDefaultData().get("date_estimated_start"));
		sugar().projects.editView.getEditField("date_estimated_end").set(sugar().projects.getDefaultData().get("date_estimated_end"));
		sugar().projects.editView.getEditField("status").set(sugar().projects.getDefaultData().get("status"));
		VoodooUtils.focusDefault();
		sugar().projects.editView.save();
		
		// Navigate to Projects Module 
		sugar().projects.navToListView();
		sugar().projects.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		
		// Verifying that Account Name is displayed in Project's Account subpanel
		// TODO: VOOD-972: Not working methods in BWC subpanels: verify, assertContains, create
		new VoodooControl("a", "css", "#list_subpanel_accounts tr:nth-child(3) span a").assertEquals(sugar().accounts.defaultData.get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}