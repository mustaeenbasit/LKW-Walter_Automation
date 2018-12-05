package com.sugarcrm.test.forecasts;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28970 extends SugarTest {
	DataSource userData, loginAndOppData;
	UserRecord userA, userB;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		loginAndOppData  = testData.get(testName);
		userData = testData.get(testName + "_" + sugar.users.moduleNamePlural);
		sugar.login();

		// Configuring the Forecasts setting
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.setup.saveSettings();

		// Create two users (userA and userB) where B reports to A (Both are reporting to admin2)
		userA = (UserRecord) sugar.users.create(userData.get(0));
		userB = (UserRecord) sugar.users.create(userData.get(1));

		// Create three "Include" Opportunities in current time period 1) - OPP1 assigned to admin2, 2) - OPP2 assigned to userA and 3) - OPP3 assigned to userB
		sugar.opportunities.navToListView();
		String closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		// TODO: VOOD-1359
		VoodooSelect forecastCtrl = new VoodooSelect("span", "css", ".fld_commit_stage.edit");
		for(int i = 0; i < loginAndOppData.size(); i++) {
			sugar.opportunities.listView.create();
			sugar.opportunities.createDrawer.showMore();
			sugar.opportunities.createDrawer.getEditField("name").set(loginAndOppData.get(i).get("name"));
			sugar.opportunities.createDrawer.getEditField("relAccountName").set(sugar.accounts.getDefaultData().get("name"));
			sugar.opportunities.createDrawer.getEditField("relAssignedTo").set(loginAndOppData.get(i).get("relAssignedTo"));
			sugar.opportunities.createDrawer.getEditField("rli_expected_closed_date").set(closedDate);
			sugar.opportunities.createDrawer.getEditField("rli_name").set(loginAndOppData.get(i).get("rli_name"));
			sugar.opportunities.createDrawer.getEditField("rli_likely").set(loginAndOppData.get(i).get("rli_likely"));
			forecastCtrl.set(loginAndOppData.get(0).get("forecast"));
			sugar.opportunities.createDrawer.getEditField("rli_assigned_user").set(loginAndOppData.get(i).get("rli_assigned_user"));
			sugar.opportunities.createDrawer.save();
		}
	}

	/**
	 * Verify that in the Reportee's Forecast sheet  Account Name should be display.
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1677: While creating a user(via the UI) with 'Reports to' user field gives 'Cannot read property 'waitForReady' of undefined' error.")
	@Test
	public void Forecasts_28970_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from admin user and login as userB
		sugar.logout();
		sugar.login(userB);

		// Navigate to Forecasts, Commit B's worksheet
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.worksheet.commit();

		// Logout from userB and login as userA
		sugar.logout();
		sugar.login(userA);

		// Commit A's manager worksheet
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.worksheet.commit();

		FieldSet userLogin = new FieldSet();
		userLogin.put("userName", loginAndOppData.get(0).get("userName"));
		userLogin.put("password", loginAndOppData.get(0).get("password"));

		// Logout from admin user and Login into the application by user admin2
		sugar.logout();
		sugar.login(userLogin);

		// Navigate to Forecasts
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);

		// From Forecasts page, select UserA from the user drop-down tree
		// TODO: VOOD-929
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		// Using xPath to find specific user from the multiple users shown
		new VoodooControl("a", "css", "#jstree_node_" + userData.get(0).get("userName") + " a").click();
		VoodooUtils.waitForReady();

		// Select UserB from the Opportunities worksheet
		new VoodooControl("a", "css", ".flex-list-view-content .list.fld_name div[data-original-title='" + userData.get(1).get("userName") + " " + userData.get(1).get("lastName") + "'] a").click();
		VoodooUtils.waitForReady();

		// Verify that From Userb reportee's forecast worksheet Account Name should be display for the record
		sugar.forecasts.worksheet.getControl("acctName01").assertEquals(sugar.accounts.getDefaultData().get("name"), true);
		sugar.forecasts.worksheet.getControl("rliName01").assertEquals(loginAndOppData.get(2).get("rli_name"), true);
		sugar.forecasts.worksheet.getControl("oppName01").assertEquals(loginAndOppData.get(2).get("name"), true);

		// Logout and login as admin user
		// Currently script is working with 'admin2' user(have administrative privileges) and will execute cleanup() method with the same user.
		// But I want to execute cleanup() method with 'admin' user, so logout from 'admin2' and login as 'admin'.
		sugar.logout();
		sugar.login();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}