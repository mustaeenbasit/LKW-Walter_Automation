package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Teams_28219 extends SugarTest {
	FieldSet userData = new FieldSet();
	UserRecord Max;

	public void setup() throws Exception {
		userData = testData.get(testName).get(0);
		sugar.login();

		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("noPrivateTeamUpdate", "true");
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);
	}

	/**
	 * Verify that renamed the private team name will be modified after change the user profile
	 *
	 * @throws Exception
	 */
	@Test
	public void Teams_28219_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create User Max		
		Max = (UserRecord) sugar.users.create(userData);

		// Admin > team management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		// Edit Private Team
		// TODO: VOOD-518
		new VoodooControl("td", "css", "#MassUpdate tr:nth-child(3) td:nth-child(3)").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "#contentTable input[name='name']").set(userData.get("firstName") + "-" + userData.get("userName"));
		sugar.alerts.waitForLoadingExpiration();
		// Save
		new VoodooControl("input", "id", "btn_save").click();
		sugar.alerts.waitForLoadingExpiration(30000);
		VoodooUtils.focusDefault();

		// Admin > user management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("userManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Edit User Max
		// TODO: VOOD-563
		new VoodooControl("a", "css", "#MassUpdate tr:nth-child(3) td:nth-child(2) a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "[name='department']").set(userData.get("userName"));
		// Save
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();

		// Admin > team management  
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Team Name should be same as renamed
		new VoodooControl("td", "css", "#MassUpdate > table > tbody > tr:nth-child(3) td:nth-child(4)").assertContains(userData.get("firstName") + "-" + userData.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}