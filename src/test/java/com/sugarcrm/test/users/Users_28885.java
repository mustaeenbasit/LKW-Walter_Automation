package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_28885 extends SugarTest{
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that team and user should not be create/displayed for SNIP while clicking on Upgrade team on repair
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_28885_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Admin-> Repair-> Upgrade Teams
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("repair").click();
		
		// Verify that team for SNIP user should not be created
		// TODO: VOOD-1567
		new VoodooControl("a", "css", "#contentTable tr:nth-child(2) td:nth-child(1) a").click();
		new VoodooControl("td", "css", "#contentTable td").assertContains(customData.get("assertion_text"), false);
		VoodooUtils.focusDefault();
		
		// Go to Teams module
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify team should not be create for SNIP user
		sugar.teams.listView.assertContains(customData.get("SNIP_user"), false);
		VoodooUtils.focusDefault();
		
		// Go to Users module
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that SNIP user should not be displayed in the list view of Users module
		sugar.users.listView.assertContains(customData.get("SNIP_user"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}