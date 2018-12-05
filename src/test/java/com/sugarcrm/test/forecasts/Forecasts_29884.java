package com.sugarcrm.test.forecasts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_29884 extends SugarTest {
	UserRecord manager, salesRep;
	String forecasts = "";

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		DataSource userData = testData.get(testName + "_" + sugar().users.moduleNamePlural);
		sugar().login();

		// Configuring the Forecasts setting
		forecasts = sugar().forecasts.moduleNamePlural;
		sugar().navbar.navToModule(forecasts);
		sugar().forecasts.setup.saveSettings();

		// Create two users (namely Manager and SalesRep) where SalesRep reports to Manager
		manager = (UserRecord) sugar().users.create(userData.get(0));
		FieldSet reportsTo = userData.get(1);
		reportsTo.put("reportsTo", userData.get(0).get("userName"));
		salesRep = (UserRecord) sugar().users.create(reportsTo);
		sugar().logout();
	}

	/**
	 * Verify that Likely and Likely (Adjusted) amounts remain same on commit of an excluded RLI.
	 * @throws Exception
	 */

	@Test
	public void Forecasts_29884_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as SalesRep
		sugar().login(salesRep);

		// Create two "Include" RLIs in current time period.
		sugar().revLineItems.navToListView();
		String closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Controls for RLI fields
		VoodooControl rliName = sugar().revLineItems.createDrawer.getEditField("name");
		VoodooControl relOppName = sugar().revLineItems.createDrawer.getEditField("relOpportunityName");
		VoodooControl rliDateClosed = sugar().revLineItems.createDrawer.getEditField("date_closed");
		VoodooControl forecast = sugar().revLineItems.createDrawer.getEditField("forecast");
		VoodooControl rlilikely = sugar().revLineItems.createDrawer.getEditField("likelyCase");

		DataSource rliData = testData.get(testName);
		for(int i = 0; i < rliData.size(); i++) {
			sugar().revLineItems.listView.create();
			sugar().revLineItems.createDrawer.showMore();
			rliName.set(rliData.get(i).get("name"));
			relOppName.set(sugar().opportunities.getDefaultData().get("name"));
			rliDateClosed.set(closedDate);
			forecast.set(rliData.get(i).get("forecast"));
			rlilikely.set(rliData.get(i).get("likelyCase"));
			sugar().revLineItems.createDrawer.save();
		}

		// Navigate to Forecasts, Commit SalesRep Worksheet
		sugar().navbar.navToModule(forecasts);
		sugar().forecasts.worksheet.commit();		

		// Logout from SalesRep and login as Manager
		sugar().logout();
		sugar().login(manager);

		// Define Likely and Likely (Adjusted) controls
		// TODO: VOOD-929
		VoodooControl likelyCaseCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case div");
		VoodooControl likelyCaseAdjustedCtrl = new VoodooControl("div", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(2) .fld_likely_case_adjusted div");

		// Verify Likely value is equal to Adjusted Likely 
		sugar().navbar.navToModule(forecasts);
		String assertValue = rliData.get(0).get("description");
		likelyCaseCtrl.assertEquals(assertValue, true);
		likelyCaseAdjustedCtrl.assertEquals(assertValue, true);

		// TODO: VOOD-1666
		// Navigate to Profile(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		sugar().navbar.navToProfile();

		// Create RLI from Manager
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.showMore();
		rliName.set(testName);
		relOppName.set(sugar().opportunities.getDefaultData().get("name"));
		rliDateClosed.set(closedDate);
		forecast.set(rliData.get(1).get("description"));
		rlilikely.set(rliData.get(0).get("likelyCase"));
		sugar().revLineItems.createDrawer.save();

		// Navigate to Forecasts Module
		sugar().navbar.navToModule(forecasts);

		// Defining Controls for  Selection of Team Worksheet as well as Manager's personal Worksheet
		// TODO: VOOD-1385
		VoodooControl worksheetSelect = new VoodooControl("i", "css", "#forecastsTree .fa-caret-down");
		VoodooControl managerWorksheet = new VoodooControl("a", "css", "#jstree_node_myopps_Manager a");
		worksheetSelect.click();
		VoodooUtils.waitForReady();
		managerWorksheet.click();
		VoodooUtils.waitForReady();

		// Cancel the Include option from the filter
		// TODO: VOOD-1385
		new VoodooControl("a", "css", ".filter-view .select2-search-choice-close").click();
		VoodooUtils.waitForReady();
		sugar().forecasts.worksheet.commit();

		// TODO: VOOD-1666
		// Navigate to Profile(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		sugar().navbar.navToProfile();

		// Navigate to Revenue Line Items and change the Forecast to Include.
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1,"forecast").set(rliData.get(0).get("forecast"));
		sugar().revLineItems.listView.saveRecord(1);

		// Navigate to Forecasts Module.
		sugar().navbar.navToModule(forecasts);

		// Using hierarchy tree and go back to Manager's team worksheet.
		worksheetSelect.click();
		VoodooUtils.waitForReady();
		managerWorksheet.click();
		VoodooUtils.waitForReady();
		sugar().forecasts.worksheet.commit();
		worksheetSelect.click();
		VoodooUtils.waitForReady();

		// Selecting Worksheet from dropdown
		// TODO: VOOD-1385
		new VoodooControl("a", "css", ".dropdown-menu.carettop a").click();
		VoodooUtils.waitForReady();

		// Verify Manager must see both likely and likely(adjusted) same in amounts.
		// TODO: VOOD-929
		String assertTotal = rliData.get(0).get("assertTotal");
		new VoodooControl("div", "css", ".layout_ForecastManagerWorksheets tfoot td:nth-child(3)").assertEquals(assertTotal, true);
		new VoodooControl("div", "css", ".layout_ForecastManagerWorksheets tfoot td:nth-child(4)").assertEquals(assertTotal, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}