package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28863 extends SugarTest {
	DataSource userData, rliData;
	UserRecord userA, userB;

	public void setup() throws Exception {
		sugar.opportunities.api.create();
		userData = testData.get(testName + "_" + sugar.users.moduleNamePlural);
		rliData = testData.get(testName);
		sugar.login();

		// Configuring the Forecasts setting
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.forecasts.setup.saveSettings();

		// Create two users (userA and userB) where B reports to A
		userA = (UserRecord) sugar.users.create(userData.get(0));

		FieldSet reportsTo = userData.get(1);
		reportsTo.put("reportsTo", userData.get(0).get("userName"));
		userB = (UserRecord) sugar.users.create(reportsTo);

		// Create two "Include" RLIs in current time period 1) - RLI1 assigned to A - Likely = $100, 2) - RLI2 assigned to B - Likely = $200
		sugar.revLineItems.navToListView();
		String closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		for(int i = 0; i < rliData.size(); i++) {
			sugar.revLineItems.listView.create();
			sugar.revLineItems.createDrawer.showMore();
			sugar.revLineItems.createDrawer.getEditField("name").set(rliData.get(i).get("name"));
			sugar.revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar.opportunities.getDefaultData().get("name"));
			sugar.revLineItems.createDrawer.getEditField("date_closed").set(closedDate);
			sugar.revLineItems.createDrawer.getEditField("forecast").set(rliData.get(0).get("forecast"));
			sugar.revLineItems.createDrawer.getEditField("likelyCase").set(rliData.get(i).get("likelyCase"));
			sugar.revLineItems.createDrawer.getEditField("relAssignedTo").set(userData.get(i).get("userName"));
			sugar.revLineItems.createDrawer.save();
		}
	}

	/**
	 * B (rep) commits to A (manager): Verify that Likely adjusted does not get locked for manager's rep row before manager updates it.
	 * 
	 * @throws Exception
	 */

	@Test
	public void Forecasts_28863_execute() throws Exception {
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

		// Define Likely and Likely (Adjusted) controls
		// TODO: VOOD-929
		VoodooControl likelyCaseCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case div");
		VoodooControl likelyCaseAdjustedCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case_adjusted div");
		VoodooControl likelyCaseFieldCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1)  .fld_likely_case div");


		// Verify that the B (rep) row shows Likely = $200, Likely (Adjusted) = $200
		likelyCaseCtrl.assertEquals(rliData.get(0).get("likelyCaseOldValue"), true);
		likelyCaseAdjustedCtrl.assertEquals(rliData.get(0).get("likelyCaseOldValue"), true);

		// Logout from userA and login as B
		sugar.logout();
		sugar.login(userB);

		// Navigate to Forecasts
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);

		// Increase RLI2 Likely by 100
		likelyCaseFieldCtrl.scrollIntoViewIfNeeded(false);
		likelyCaseFieldCtrl.hover();
		// TODO: VOOD-854
		new VoodooControl("i", "css", likelyCaseFieldCtrl.getHookString() + " i.fa-pencil").click();
		new VoodooControl("input", "css", likelyCaseFieldCtrl.getHookString() + " input").set(rliData.get(0).get("updateLikelyCase"));
		sugar.forecasts.worksheet.getControl("currentForecastUser").click(); // need to click on the work sheet page to change focus 

		// Commit B's worksheet
		sugar.forecasts.worksheet.commit();

		// Logout from userB and login as A
		sugar.logout();
		sugar.login(userA);

		// Navigate to Forecasts
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);

		// Verify that the B (rep) row shows Likely = $300, Likely (Adjusted) = $300
		likelyCaseCtrl.assertEquals(rliData.get(0).get("likelyCaseNewValue"), true);
		likelyCaseAdjustedCtrl.assertEquals(rliData.get(0).get("likelyCaseNewValue"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}