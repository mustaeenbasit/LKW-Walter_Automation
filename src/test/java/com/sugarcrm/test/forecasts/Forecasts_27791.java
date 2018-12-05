package com.sugarcrm.test.forecasts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_27791 extends SugarTest {
	FieldSet userData, oppData, multipurposeFS;
	UserRecord myUser;
	VoodooControl commitButtonEnabled, commitButtonDisabled;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		userData = testData.get(testName).get(0);
		oppData = testData.get(testName+"_"+"opp").get(0);
		sugar.login();

		// Create a users and set as  sales rep 
		myUser = (UserRecord) sugar.users.create(userData);

		// Assign Sales Administrator role to Sally
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("rolesManagement").click();

		// Click on 'Sales Administrator'
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		// Using xPath to find specific Role by name
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a").click();

		// Click Select User button
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a#acl_roles_users_select_button").click();
		VoodooUtils.focusWindow(1);
		// Using xPath to find specific User by name, i.e. Sally
		new VoodooControl("a", "xpath", "//td/a[contains(text(),'"+userData.get("fullName")+"')]").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		// Enable default Forecast settings
		multipurposeFS = new FieldSet();
		multipurposeFS.put("timePeriodType", "Yearly (Quarterly sub-periods)");
		multipurposeFS.put("scenario1", oppData.get("best"));
		multipurposeFS.put("scenario2", "Worst");
		multipurposeFS.put("worksheet1", oppData.get("probability"));
		multipurposeFS.put("worksheet2", oppData.get("salesStage"));
		sugar.forecasts.setupForecasts(multipurposeFS);
		multipurposeFS.clear();

		// Logout from the admin user
		sugar.logout();
	}

	/**
	 * Verify that probability and commit stage updated after sales stage is changed in sales rep worksheet
	 * @throws Exception
	 */
	@Test
	public void Forecasts_27791_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as sales rep/Sally
		sugar.login(myUser);

		// Set Date Format to mm/dd/yyyy
		sugar.navbar.navToProfile();
		multipurposeFS.put("advanced_dateFormat", oppData.get("dateFormat"));
		sugar.users.setPrefs(multipurposeFS);
		multipurposeFS.clear();

		/* Parse Date, Month and Year to set Date of current year and first quarter as default 'Time Period Type' is Yearly (Quarterly sub-periods),
		therefore it is easy to find the opportunity in forecast module as Closed Date of opportunity is set to "01-01-'Current Year'" 
		 */
		// Get first day date of the year
		DateFormat myDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR,1);
		Date date = cal.getTime();
		String closeDate = myDateFormat.format(date);

		// Create an Opp+RLI record with Closed Date: "01-01-'Current Year'"
		multipurposeFS.put("rli_expected_closed_date", closeDate);
		sugar.opportunities.create(multipurposeFS);

		// Go to RLI and set the Best/Worst case values
		sugar.revLineItems.navToListView();
		sugar.revLineItems.listView.clickRecord(1);
		sugar.revLineItems.recordView.edit();
		sugar.revLineItems.recordView.showMore();
		sugar.revLineItems.recordView.getEditField("bestCase").set(oppData.get("bestCaseValue"));
		sugar.revLineItems.recordView.getEditField("worstCase").set(oppData.get("worstCaseValue"));
		sugar.revLineItems.recordView.save();

		// Go to Forecast module to see sales rep worksheet
		sugar.navbar.navToModule(sugar.forecasts.moduleNamePlural);

		// Controls for Probability, Likely and Best field
		// TODO: VOOD-1385
		VoodooControl salesStageCtrl = new VoodooSelect("span", "css", ".fld_sales_stage");
		VoodooControl probabilityfldCtrl = new VoodooControl("span", "css", ".fld_probability");
		VoodooControl likelyCtrl = new VoodooSelect("span", "css", ".fld_likely_case.list");
		VoodooControl bestCtrl = new VoodooSelect("span", "css", ".fld_best_case.list");
		VoodooControl worstCtrl = new VoodooSelect("span", "css", ".fld_worst_case.list");
		VoodooControl commitStageCtrl = new VoodooSelect("span", "css", ".fld_commit_stage");

		// Cancel the Include option from the filter
		new VoodooControl("a", "css", ".filter-view .select2-search-choice-close").click();
		sugar.alerts.waitForLoadingExpiration();

		// change in sales stage to any but not Closed
		salesStageCtrl.set(oppData.get("qualificationSalesStage"));

		// Verify that the Probability is updated to corresponding value and Best, Worst and Likely fields
		probabilityfldCtrl.assertContains(oppData.get("qualificationProbability"), true);
		likelyCtrl.assertContains(oppData.get("likelyValue"), true);
		bestCtrl.assertContains(oppData.get("bestCaseValue"), true);
		worstCtrl.assertContains(oppData.get("worstCaseValue"), true);
		commitStageCtrl.assertContains(oppData.get("commitStageExcluded"), true);

		// Change sales stage to Closed Won
		salesStageCtrl.set(oppData.get("closedWonSalesStage"));

		// Verify that the Probability is updated to corresponding value. commit_stage to Included and Best/Worst fields being synced with Likely
		probabilityfldCtrl.assertContains(oppData.get("closedWonProbability"), true);
		likelyCtrl.assertContains(oppData.get("likelyValue"), true);
		bestCtrl.assertContains(oppData.get("likelyValue"), true);
		worstCtrl.assertContains(oppData.get("likelyValue"), true);
		commitStageCtrl.assertContains(oppData.get("commitStageIncluded"), true);

		// Change sales stage to Closed Lost
		salesStageCtrl.set(oppData.get("closedLostSalesStage"));

		// Verify that the Probability is updated to corresponding value. commit_stage to Excluded and Best/Worst fields being synced with Likely
		probabilityfldCtrl.assertContains(oppData.get("closedLostProbability"), true);
		likelyCtrl.assertContains(oppData.get("likelyValue"), true);
		bestCtrl.assertContains(oppData.get("likelyValue"), true);
		worstCtrl.assertContains(oppData.get("likelyValue"), true);
		commitStageCtrl.assertContains(oppData.get("commitStageExcluded"), true);

		// Click on the commit button
		new VoodooControl("a", "xpath", "//a[@name='commit_button'][@class='btn btn-primary']").click();
		sugar.alerts.getSuccess().waitForVisible();
		sugar.alerts.getSuccess().closeAlert();

		sugar.logout();
		sugar.login();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}