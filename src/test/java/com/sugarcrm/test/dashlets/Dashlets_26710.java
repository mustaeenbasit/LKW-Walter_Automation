package com.sugarcrm.test.dashlets;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26710 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
	}

	/**
	 * Verify that content of the help dashlet in Help Dashboard is localized
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-591")
	@Test
	public void Dashlets_26710_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Select language - French
		new VoodooControl("a", "id", "languageList").click();
		new VoodooControl("a", "css", "#languageList a[data-lang-key='fr_FR']").click();
		sugar.alerts.waitForLoadingExpiration();

		// login as qauser. Copied from library
		sugar.loginScreen.getControl("loginUserName").waitForVisible(300000);
		sugar.loginScreen.getControl("loginUserName").set(sugar.users.getQAUser().get("userName"));
		sugar.loginScreen.getControl("loginPassword").set(sugar.users.getQAUser().get("password"));
		sugar.loginScreen.getControl("login").click();
		// Wait for the 'Loading...' alert dialog to NOT be visible
		VoodooUtils.waitForAlertExpiration(300000); 

		// Goto Accounts listview, select help dashboard and see if help text appears in French
		sugar.accounts.navToListView();
		VoodooUtils.pause(1000); // Wait for page and dashlets to load
		sugar.dashboard.getControl("dashboardTitle").waitForVisible();
		if (!sugar.dashboard.getControl("dashboardTitle").queryContains(customData.get("helpDashboardTitle"), false)) {
			sugar.dashboard.chooseDashboard(1);
		}
		new VoodooControl("div", "css", "div.help-body").assertContains(customData.get("accountsHelpMsg"), true);

		// Goto Contacts listview, select help dashboard and see if help text appears in French
		sugar.contacts.navToListView();
		VoodooUtils.pause(1000); // Wait for page and dashlets to load
		sugar.dashboard.getControl("dashboardTitle").waitForVisible();
		if (!sugar.dashboard.getControl("dashboardTitle").queryContains(customData.get("helpDashboardTitle"), false)) {
			sugar.dashboard.chooseDashboard(1);
		}
		new VoodooControl("div", "css", "div.help-body").assertContains(customData.get("contactsHelpMsg"), true);

		// Goto Contacts listview, select help dashboard and see if help text appears in French
		sugar.leads.navToListView();
		VoodooUtils.pause(1000); // Wait for page and dashlets to load
		sugar.dashboard.getControl("dashboardTitle").waitForVisible();
		if (!sugar.dashboard.getControl("dashboardTitle").queryContains(customData.get("helpDashboardTitle"), false)) {
			sugar.dashboard.chooseDashboard(1);
		}
		new VoodooControl("div", "css", "div.help-body").assertContains(customData.get("leadsHelpMsg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}