package com.sugarcrm.test.forecasts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_27767 extends SugarTest {
	FieldSet forecastData,userRecord;
	UserRecord salesManager;

	public void setup() throws Exception {
		forecastData = testData.get(testName).get(0);
		userRecord = testData.get(testName + "_user").get(0);
		sugar.accounts.api.create();
		sugar.opportunities.api.create();
		sugar.login();

		salesManager = (UserRecord) sugar.users.create(userRecord);

		// TODO VOOD-444 - API Creating relationships
		// Assign Sales Administrator role to Sally
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("rolesManagement").click();

		// Click on 'Sales Administrator'
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a").click();

		// Click Select User button
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a#acl_roles_users_select_button").click();
		// Select Sally
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "xpath", "//td/a[contains(text(),'"+userRecord.get("fullName")+"')]").click();
		VoodooUtils.focusWindow(0);

		VoodooUtils.focusDefault();

		// Enable default Forecast settings
		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("timePeriodType", forecastData.get("timePeriodType"));
		sugar.forecasts.setupForecasts(forecastSettings);

		// Get date 
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR,1);    
		Date date = cal.getTime();
		String closeDate = df.format(date);

		FieldSet editedData = new FieldSet();
		editedData.put("date_closed", closeDate);
		editedData.put("bestCase", forecastData.get("bestCase"));
		editedData.put("worstCase",forecastData.get("worstCase"));
		sugar.revLineItems.create(editedData);
	}

	/**
	 * Verify that Likely dropdown inside Forecast Bar Chart is not displayed if both best 
	 * and worst scenarios are disabled in Forecast config
	 * @throws Exception
	 */
	@Test
	public void Forecasts_27767_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();

		// Cancel the Include option from the filter
		new VoodooControl("a", "css", ".filter-view .select2-search-choice-close").click();
		sugar.alerts.waitForLoadingExpiration();

		String dashBoardTitle = sugar.dashboard.getControl("dashboard").getText().trim();
		if(dashBoardTitle.contains("Help Dashboard")){
			sugar.dashboard.chooseDashboard(1);
		}

		// Verify that There should not be any dropdown in case only likely scenario is enabled.
		VoodooControl dropdownCtrl = new VoodooControl("span", "css", ".fld_dataset.edit");
		dropdownCtrl.assertVisible(false);

		sugar.logout();

		// login as a sales manager
		sugar.login(salesManager);
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();
		String helpDashBoardTitle = sugar.dashboard.getControl("dashboard").getText().trim();
		if(helpDashBoardTitle.contains("Help Dashboard")){
			sugar.dashboard.chooseDashboard(1);
		}

		// Verify that Likely dropdown is not present on manager's version of Forecast bar chart
		dropdownCtrl.assertVisible(false);

		sugar.logout();

		// login as admin
		sugar.login();

		FieldSet forecastSettings = new FieldSet();
		forecastSettings.put("scenario1", forecastData.get("scenario1"));
		forecastSettings.put("scenario2", forecastData.get("scenario2"));
		sugar.forecasts.setupForecasts(forecastSettings);

		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();

		// check if Help Dashbord is selected
		String strTitle = sugar.dashboard.getControl("dashboard").getText().trim();
		if(strTitle.contains("Help Dashboard")){
			sugar.dashboard.chooseDashboard(1);
		}

		// TODO: VOOD-1384
		VoodooControl arrowCtrl = new VoodooControl("span", "css", "[data-voodoo-name='dataset'] .select2-choice .select2-arrow");
		VoodooControl scenario1 = new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[1]");
		VoodooControl scenario2 = new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[3]");

		// Verify that now Scenarios dropdown is present and user is able to select any activated scenario and chart will display correct information
		dropdownCtrl.assertVisible(true);
		arrowCtrl.click();
		scenario1.assertContains(forecastData.get("scenario2"), true);
		scenario2.assertContains(forecastData.get("scenario1"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}