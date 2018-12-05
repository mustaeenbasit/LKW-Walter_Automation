package com.sugarcrm.test.forecasts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_29702 extends SugarTest {
	DataSource rliData = new DataSource();
	UserRecord myUser;
	String qauser;

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		rliData = testData.get(testName);

		// Login as Admin
		sugar().login();

		// Go to Admin-> Forecasts and configure Forecast settings properly
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to Admin-> User Management and create a users with hierarchy level as Manager and Rep ('Rep' must reports to 'Manager')
		qauser = sugar().users.getQAUser().get("userName");
		FieldSet customFS = new FieldSet();
		customFS.put("reportsTo", qauser);
		myUser = (UserRecord) sugar().users.create(customFS);
		customFS.clear();

		// Manager must have 1-2 RLIs already saved
		String todayesDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		String relOpportunityName = sugar().opportunities.getDefaultData().get("name");
		customFS.put("date_closed", todayesDate);
		customFS.put("relOpportunityName", relOpportunityName);
		customFS.put("relAssignedTo", qauser);
		customFS.put("forecast", rliData.get(0).get("forecast"));

		for(int i = 0; i < rliData.size(); i++) {
			customFS.put("name",  rliData.get(i).get("name"));
			customFS.put("likelyCase",  rliData.get(i).get("likelyCase"));
			sugar().revLineItems.create(customFS);
		}
	}

	/**
	 * Verify that 'Commit' button of Manager's team worksheet should be enabled after committing his own worksheet.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_29702_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as Manager(i.e. QAUser)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Forecasts
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);

		// Drill down to Manager's (me) worksheet
		// TODO: VOOD-929
		VoodooControl forecastTreeDropDownArroeCtrl = new VoodooControl("a", "css", "#forecastsTree div a i");
		forecastTreeDropDownArroeCtrl.click();
		new VoodooControl("a", "css", "#jstree_node_myopps_" + qauser + " a").click();
		VoodooUtils.waitForReady();

		// Make some changes in one of the RLI amounts
		VoodooControl firstRLILikelyCtrl = sugar().forecasts.worksheet.getControl("likelyCase01");
		sugar().forecasts.worksheet.getControl("bestCase01").scrollIntoView();
		firstRLILikelyCtrl.hover();
		// TODO: VOOD-854
		new VoodooControl("i", "css", firstRLILikelyCtrl.getHookString() + " i.fa-pencil").click();
		new VoodooControl("input", "css", firstRLILikelyCtrl.getHookString() + " input").set(rliData.get(0).get("updatedLikelyCase"));
		sugar().forecasts.worksheet.getControl("currentForecastUser").click(); // need to click on the work sheet page to change focus 

		// Click Commit button
		sugar().forecasts.worksheet.commit();

		// Verify that User should see Commit button Enabled so that Manager could commit on the team's work sheet
		sugar().forecasts.worksheet.getControl("commitButton").assertAttribute("class", rliData.get(0).get("disabled"), false);
		sugar().forecasts.worksheet.commit();

		// Verify that the data is updated in grid as per the changes made by him in personal work sheet
		// TODO: VOOD-929
		new VoodooControl("h2", "id", "likely_case").assertEquals(rliData.get(0).get("totalLikelyCase"), true);
		new VoodooControl("div", "css", ".layout_ForecastManagerWorksheets .fld_likely_case div").assertEquals(rliData.get(0).get("totalLikelyCase"), true);
		new VoodooControl("td", "css", ".layout_ForecastManagerWorksheets tfoot td:nth-child(3)").assertEquals(rliData.get(0).get("totalLikelyCase"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}