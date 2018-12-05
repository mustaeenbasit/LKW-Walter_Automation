package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21554 extends SugarTest {
	FieldSet userTeam = new FieldSet();
	
	public void setup() throws Exception {
		// Initializing Accounts data
		DataSource accounts = testData.get(testName);
		sugar().accounts.api.create(accounts);

		// Logging in as admin
		sugar().login();

		// Create summation report of account records.
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");

		// TODO: VOOD-822 - Need lib support of reports module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "#report_type_div [name='summationImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nxtButtonCtrl = new VoodooControl("input", "id", "nextBtn");
		nxtButtonCtrl.click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		nxtButtonCtrl.click();
		new VoodooControl("tr", "id", "Accounts_count").click();
		nxtButtonCtrl.click();
		new VoodooControl("option", "css", "#chart_type option:nth-child(2)").click();
		new VoodooControl("input", "css", "#chart_options_div table input:nth-child(2)").click();
		new VoodooControl("input", "id", "save_report_as").set(testName);

		// Assigning Private team
		userTeam = testData.get(testName + "_team").get(0);
		new VoodooControl("input", "css", "#ReportsWizardForm_team_name_input_div_0 input").set(userTeam.get("team"));
		new VoodooControl("div", "css", "#ReportsWizardForm_ReportsWizardForm_team_name_collection_0_results div").click();
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Customizing dashboard: Verify that the name of summation report in "My Saved Reports" list of "Report" module
	 * is displayed as an item of "Add a Chart" drop down list on "Dashboard: Home" 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21554_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Home" navigation tab.
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Click Edit button to go to Dashboard EditView
		sugar().home.dashboard.edit();

		// Add a new row and add a dashlet on Dashboard EditView
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4,1);

		// Verifying saved report is displayed as an item in "My Saved Reports" category on the Charts tab.
		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(userTeam.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", ".fld_saved_report_id a .select2-arrow").click();
		new VoodooControl("div", "css", ".select2-with-searchbox ul li:nth-child(3) div").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}