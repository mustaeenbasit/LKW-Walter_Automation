package com.sugarcrm.test.forecasts;

import java.util.Calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Forecasts_28741 extends SugarTest {
	FieldSet customFS = new FieldSet();
	String closedDate;

	public void setup() throws Exception {
		AccountRecord myAccountRecord = (AccountRecord) sugar().accounts.api.create();
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();

		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1666
		sugar().navbar.navToAdminTools();

		// Navigating to Opportunities page
		closedDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		FieldSet customFS = testData.get(testName).get(0);

		// Set fields
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAccountRecord.getRecordIdentifier());
		sugar().opportunities.createDrawer.getEditField("rli_name").set(customFS.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(closedDate);
		sugar().opportunities.createDrawer.getEditField("forecast").set(customFS.get("forecast"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(customFS.get("likelyCase"));
		sugar().opportunities.createDrawer.save();
	}

	/**
	 * Verify that forecast commit log shows date and time of each action in the log
	 * 
	 * @throws Exception
	 */
	@Test
	public void Forecasts_28741_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Get current date and time
		Calendar calObj = Calendar.getInstance();
		int booleanAmPm = calObj.get(Calendar.AM_PM); // Get current 0/1 : am/pm
		String amPm = (booleanAmPm == 0) ? "am" : "pm";


		// Navigate to Forecasts.
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		VoodooUtils.waitForReady();
		sugar().forecasts.worksheet.commit();
		String closedTime = VoodooUtils.getCurrentTimeStamp("hh:mm");

		// TODO: VOOD-929
		// Click on arrow button to show commit log
		VoodooControl showCommitLogCtrl = new VoodooControl("a", "id", "show_hide_history_log");
		showCommitLogCtrl.click();

		// Verify that Date and time entry prepends each log entry. Date and time of the commit is correct
		String verifyDateTime = closedDate+" "+closedTime+amPm;
		VoodooControl logDateCtrl = new VoodooControl("date", "css", "#history_log_results article:nth-child(1) date");
		logDateCtrl.assertContains(verifyDateTime, true);

		// Set Date Format to MM-dd-yyyy
		sugar().navbar.navToProfile();
		FieldSet fs = new FieldSet();
		fs.put("advanced_dateFormat", customFS.get("updateDateFormate"));
		sugar().users.setPrefs(fs);

		// Navigate to Forecasts and update likely and Best amount
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		new VoodooControl("div", "css", ".likely_case_field.editableColumn").scrollIntoViewIfNeeded(sugar().opportunities.listView.getControl("horizontalScrollBar"), false);
		new VoodooControl("div", "css", ".list.fld_likely_case.isEditable div div").click();
		new VoodooControl("input", "css", "input[aria-label='Likely']").set(customFS.get("updatedLikely"));
		new VoodooControl("div", "css", ".list.fld_best_case.isEditable div div").click();
		new VoodooControl("input", "css", "input[aria-label='Best']").set(customFS.get("updatedBest"));
		String updatedClosedTime = VoodooUtils.getCurrentTimeStamp("hh:mm");
		String updatedClosedDate = VoodooUtils.getCurrentTimeStamp("MM-dd-yyyy");
		sugar().forecasts.worksheet.commit();

		// Verify that Date and time are formatted based on user preference.
		showCommitLogCtrl.click();
		String verifyUpdatedDate = updatedClosedDate+" "+updatedClosedTime+amPm;
		logDateCtrl.assertContains(verifyUpdatedDate, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}