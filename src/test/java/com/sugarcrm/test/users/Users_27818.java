package com.sugarcrm.test.users;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_27818 extends SugarTest {
	VoodooControl dropHere, dragFromHere, saveBtnCtrl, userNavMenu, configureTabs, editProfile;

	public void setup() throws Exception {
		// Login as QAuser
		sugar.login(sugar.users.getQAUser());
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		editProfile = sugar.users.userPref.getControl("edit");
		editProfile.click();
		sugar.users.userPref.getControl("tab4").click();
		
		// TODO: VOOD-563
		// QAuser change module order in his profile
		new VoodooControl("option", "css", "#display_tabs_td #display_tabs option:nth-child(1)").click();
		new VoodooControl("a", "css", "#chooser_hide_tabs_down_arrow img").click();
		sugar.users.userPref.getControl("save").click(); // Save Profile
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		
		// Logout QAuser and Login as Admin
		sugar.logout();
		sugar.login();
		
		// Admin changing module order in "Display modules and subpanels"
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		configureTabs = sugar.admin.adminTools.getControl("configureTabs");
		configureTabs.click();
		
		// TODO: VOOD-1373: Need Lib support for changing order in Admin > Display modules and subpanels.
		dropHere = new VoodooControl("tr", "css", "#enabled_div div table tbody.yui-dt-data tr:nth-child(2) td");
		dragFromHere = new VoodooControl("tr", "css", "#enabled_div div table tbody.yui-dt-data tr:nth-child(3) td:nth-child(1) div");
		dragFromHere.dragNDrop(dropHere);
		saveBtnCtrl = new VoodooControl("input", "css", "table:nth-child(1) > tbody > tr > td > input.button.primary");
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		
		// Logout Admin and Login as QAuser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());
		
		// TODO: VOOD-1370 -Need Lib support to findout navbar Items and their respective position
		userNavMenu = new VoodooControl("li", "css", "div.module-list > ul > li:nth-child(2)");
		
		// Verify that module order is not changed
		userNavMenu.assertContains(sugar.contacts.moduleNamePlural, true);
		VoodooUtils.focusDefault();
		
		// Logout as QAuser
		sugar.logout();		
	}

	/**
	 * Verify that Shortcut link isn't avaolable when a user first time log in setup
	 * @throws Exception
	 */
	@Ignore("VOOD-1667")
	@Test
	public void Users_27818_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Logout as Admin
		sugar.login();
		
		// TODO: VOOD-1370 -Need Lib support to findout navbar Items and their respective position
		VoodooControl adminNavMenu = new VoodooControl("li", "css", "div.module-list > ul > li:nth-child(3)");
		adminNavMenu.assertContains(sugar.opportunities.moduleNamePlural, true);
		sugar.logout();
		
		// Reset Sally Profile
		sugar.login(sugar.users.getQAUser());
		
		// Verify that QAuser module order is not changed while changing admin module order
		userNavMenu.assertContains(sugar.contacts.moduleNamePlural, true);
		
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		editProfile.click();
		
		// TODO: VOOD-563
		VoodooControl resetUsrPref = new VoodooControl("input", "id", "reset_user_preferences_header");
		resetUsrPref.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		
		// Relogin as QAuser
		sugar.login(sugar.users.getQAUser());
		sugar.navbar.navToProfile();
		
		// Verify that in the Navigation menu, QAuser should see the module order based on admin defined in Display modules and subpanels.
		adminNavMenu.assertContains(sugar.opportunities.moduleNamePlural, true);
		
		// Edit profile, and click Save
		VoodooUtils.focusFrame("bwc-frame");
		editProfile.click();
		sugar.users.userPref.getControl("save").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		
		// Logout from QAuser and login as Admin
		sugar.logout();
		sugar.login();
		
		// Go to Administration -> Display modules and subpanels. Make changes and save
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		configureTabs.click();
		
		// Reset Module Order as default
		dragFromHere.dragNDrop(dropHere); 
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		
		// Logout from Admin and login as QAuser
		sugar.logout();
		sugar.login(sugar.users.getQAUser());
		sugar.navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		editProfile.click();
		
		// Reset user preferences
		resetUsrPref.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		
		// Relogin as QAuser
		sugar.login(sugar.users.getQAUser());
		
		// Verify that in the Navigation menu, QAuser should see the module order based on admin defined in Display modules and subpanels
		userNavMenu.assertContains(sugar.accounts.moduleNamePlural, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}