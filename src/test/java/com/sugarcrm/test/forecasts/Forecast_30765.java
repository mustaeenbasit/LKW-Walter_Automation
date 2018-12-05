package com.sugarcrm.test.forecasts;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Forecast_30765 extends SugarTest {
	DataSource users = new DataSource();
	UserRecord sarah, sally;

	public void setup() throws Exception {
		users = testData.get(testName + "_users");
		sugar().accounts.api.create();

		// Login as Admin
		sugar().login();

		FieldSet firstUser = users.get(0);
		FieldSet secondUser = users.get(1);

		// Create two users "Sarah" and "Sally". Sally reports to Sarah
		// TODO: VOOD-1200, VOOD-444
		sarah = (UserRecord) sugar().users.create(firstUser);
		sally = (UserRecord) sugar().users.create(secondUser);

		// Go to Admin-> Forecasts and configure Forecast settings
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		sugar().logout();
	}


	/**
	 * Verify that Commit date of edited RLIs are visible to higher rank users
	 *
	 * @throws Exception
	 */
	@Test
	public void Forecast_30765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Login as user Sally
		sugar().login(sally);

		// TODO VOOD-444
		sugar().opportunities.create();

		// Get current date
		Date date = new Date();
		String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + 6));
		date = cal.getTime();
		// date 2 quarters from now
		String dateAfterSixMonth = new SimpleDateFormat("MM/dd/yyyy").format(date);
		// get current year
		int currentYear = cal.get(Calendar.YEAR);

		// create an RLI with the following data: Name: TestRLI, Forecast: Include, Expected Close date, Likely: $10,000
		FieldSet revLineItemRecord = new FieldSet();
		revLineItemRecord.put("forecast", fs.get("forecast"));
		revLineItemRecord.put("date_closed", dateAfterSixMonth);
		sugar().revLineItems.create(revLineItemRecord);

		// Move to forecasts module and observe Sally's forecast
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.worksheet.setTimePeriod(currentYear + fs.get("quarter_three"));

		// The RLI created by Sally in step 1 is visible into forecast worksheet.
		sugar().forecasts.worksheet.getControl("rliName01").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);
		// Commit it.
		sugar().forecasts.worksheet.commit();

		sugar().logout();

		// Login as Sarah
		sugar().login(sarah);

		// Move to forecasts module and observe Sarah's forecast
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.worksheet.setTimePeriod(currentYear + fs.get("quarter_three"));

		// TODO: VOOD-1385
		// user name in forecast worksheet
		VoodooControl userNameCtrl = new VoodooControl("a", "css", "[data-type='userLink'] [data-original-title='" + sally.get("fullName") + "'] a");
		VoodooUtils.waitForReady();
		// Click on the user `Sally`
		userNameCtrl.click();

		// Verify that the RLI created by Sally in step 1 is visible to Sarah.
		sugar().forecasts.worksheet.getControl("rliName01").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);

		sugar().logout();

		// login back as Sally
		sugar().login(sally);
		// Navigate to RLI module
		sugar().revLineItems.navToListView();
		// Edit the RLI record created in step 1 i.e. Expected Close date has a date such that it is in the present quarter
		sugar().revLineItems.listView.editRecord(1);
		sugar().revLineItems.listView.getEditField(1, "date_closed").set(currentDate);
		sugar().revLineItems.listView.saveRecord(1);

		// Move to forecasts module and observe Sally's forecast
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		// Navigate to Forecasts module and commit changes for the quarter for which the RLI was originally created in step 1.
		sugar().forecasts.worksheet.setTimePeriod(currentYear + fs.get("quarter_three"));
		sugar().forecasts.worksheet.commit();

		// Commit changes for the present quarter
		sugar().forecasts.worksheet.setTimePeriod(currentYear + fs.get("quarter_one"));
		sugar().forecasts.worksheet.commit();

		// Verify that the RLI created by Sally in step 1 is visible to Sally in the present quarter.
		sugar().forecasts.worksheet.getControl("rliName01").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);

		sugar().logout();

		// re-login as Sarah
		sugar().login(sarah);
		// Navigate to Forecasts module
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.worksheet.setTimePeriod(currentYear + fs.get("quarter_one"));
		//  Verify that the RLI moved to the present quarter should be visible to Sarah.
		userNameCtrl.click();
		VoodooUtils.waitForReady();
		// Verify that the RLI created by Sally in step 1 is visible to Sarah.
		sugar().forecasts.worksheet.getControl("rliName01").assertEquals(sugar().revLineItems.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}