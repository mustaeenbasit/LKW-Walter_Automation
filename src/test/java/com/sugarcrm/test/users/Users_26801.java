package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Users_26801 extends SugarTest {
	UserRecord userA, userB;
	FieldSet customFS;

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		DataSource userData = testData.get(testName + "_" + sugar().users.moduleNamePlural);
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Create two users (userA and userB) where B and B reports to QAuser
		FieldSet reportsTo = userData.get(0);
		reportsTo.put("reportsTo", sugar().users.getQAUser().get("userName"));
		userA = (UserRecord) sugar().users.create(reportsTo);
		reportsTo.clear();
		reportsTo = userData.get(1);
		reportsTo.put("reportsTo", sugar().users.getQAUser().get("userName"));
		userB = (UserRecord) sugar().users.create(reportsTo);
		
		// Create "Include" RLIs in current time period - assigned to userB
		sugar().revLineItems.navToListView();
		String closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(closedDate);
		sugar().revLineItems.createDrawer.getEditField("forecast").set(customFS.get("forecast"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.getEditField("relAssignedTo").set(userData.get(1).get("userName"));
		sugar().revLineItems.createDrawer.save();
	}

	/**
	 * Verify that inactive user does not appear in manager's Forecast worksheet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_26801_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from Admin user and login as QAuser(Manager of userA and userB)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to Forecasts, Commit B's worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		
		// Verify that Active user appear in manager's Forecast worksheet
		sugar().forecasts.worksheet.verifyField(1, "name", sugar().users.qaUser.get("userName"));
		sugar().forecasts.worksheet.verifyField(2, "name", userB.getRecordIdentifier());
		sugar().forecasts.worksheet.verifyField(3, "name", userA.getRecordIdentifier());
		
		// Logout from QAuser(Manager of of userA and userB) and login as Admin
		sugar().logout();
		sugar().login();
		
		// Go to Admin->Users
		userB.navToRecord();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1053,994
		// Set the user as Inactive and click Save.
		VoodooControl statusCtrl = new VoodooControl("select", "css", "[name='status']");
		statusCtrl.click();
		VoodooUtils.waitForReady();
		statusCtrl.set(customFS.get("userStatus"));
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", ".ft .button-group span:nth-child(2) .first-child button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Logout from Admin user and login as QAuser(Manager of userA and userB)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigate to Forecasts, Commit QAuser Worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		
		// Verify that inactive user does not appear in manager's Forecast Worksheet
		sugar().forecasts.worksheet.verifyField(1, "name", sugar().users.qaUser.get("userName"));
		sugar().forecasts.worksheet.verifyField(2, "name", userA.getRecordIdentifier());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}