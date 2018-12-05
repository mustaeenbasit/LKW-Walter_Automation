package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28696 extends SugarTest {
	DataSource rliData = new DataSource();
	UserRecord userA, userB;

	public void setup() throws Exception {
		DataSource userData = testData.get(testName + "_Users");
		rliData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// Create Opportunity via UI
		sugar().opportunities.create();

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Create two users (userA and userB) where B reports to A
		userA = (UserRecord) sugar().users.create(userData.get(0));
		VoodooUtils.waitForReady();

		FieldSet reportsTo = userData.get(1);
		reportsTo.put("reportsTo", userData.get(0).get("userName"));
		userB = (UserRecord) sugar().users.create(reportsTo);
		VoodooUtils.waitForReady();

		// Create two "Include" RLIs in current time period - assigned to userB
		sugar().revLineItems.navToListView();
		String closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		for(int i = 0; i < rliData.size(); i++) {
			sugar().revLineItems.listView.create();
			sugar().revLineItems.createDrawer.showMore();
			sugar().revLineItems.createDrawer.getEditField("name").set(rliData.get(i).get("name"));
			sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
			sugar().revLineItems.createDrawer.getEditField("date_closed").set(closedDate);
			sugar().revLineItems.createDrawer.getEditField("forecast").set(rliData.get(0).get("forecast"));
			sugar().revLineItems.createDrawer.getEditField("likelyCase").set(rliData.get(i).get("likelyCase"));
			sugar().revLineItems.createDrawer.getEditField("relAssignedTo").set(userData.get(1).get("userName"));
			sugar().revLineItems.createDrawer.save();
		}

		// Logout from admin user
		sugar().logout();
	}

	/**
	 * Verify that deleted record is removed from sales rep's worksheet when viewed by the manager after rep 
	 * deletes committed RLI and recommits
	 * 
	 * @throws Exception
	 */

	@Test
	public void Forecasts_28696_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as userB
		sugar().login(userB);

		// Navigate to Forecasts, Commit B's worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.worksheet.commit();

		// Logout from userB
		sugar().logout();

		// Login as userA
		sugar().login(userA);

		// Commit A's manager worksheet
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Define Likely and Likely (Adjusted) controls
		// TODO: VOOD-929
		VoodooControl likelyCaseCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case div");
		VoodooControl likelyCaseAdjustedCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case_adjusted div");

		// Verify that the B (rep) row shows Likely = $300, Likely (Adjusted) = $300
		likelyCaseCtrl.assertEquals(rliData.get(0).get("likelyCaseOldValue"), true);
		likelyCaseAdjustedCtrl.assertEquals(rliData.get(0).get("likelyCaseOldValue"), true);

		// Logout from userA
		sugar().logout();

		// Login as userB
		sugar().login(userB);

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.deleteRecord(1); // Last inserted one - likeyCase 200
		sugar().revLineItems.listView.confirmDelete();

		// Navigate to Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Commit B's worksheet
		sugar().forecasts.worksheet.commit();

		// Logout from userB
		sugar().logout();

		// Login as userA
		sugar().login(userA);

		// Navigate to Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Verify that the B (rep) row shows Likely = $100, Likely (Adjusted) = $100
		likelyCaseCtrl.assertEquals(rliData.get(0).get("likelyCaseNewValue"), true);
		likelyCaseAdjustedCtrl.assertEquals(rliData.get(0).get("likelyCaseNewValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}