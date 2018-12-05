package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21093 extends SugarTest {
	FieldSet myData = new FieldSet();

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		myData = testData.get(testName).get(0);

		// Create a Case
		sugar().cases.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// Navigate to Case Details and Choose dashboard by name
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboard");
		String myDashboard = myData.get("myDashboard");
		if(!dashboardTitle.queryContains(myDashboard, true))
			sugar().cases.dashboard.chooseDashboard(myDashboard);

		// Create Archived email for case
		// TODO: VOOD-798 - Lib support in create/verify Archive Email from History Dashlet
		new VoodooControl("span", "css", ".dashlet-row li:nth-child(2) .fa.fa-plus").click();
		new VoodooControl("a", "css", "[data-dashletaction='archiveEmail']").click();

		// TODO: VOOD-797 - Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		String fromAndToAddress = myData.get("fromAndToAddress");
		new VoodooControl("input", "css", ".fld_from_address input").set(fromAndToAddress);
		new VoodooControl("input", "css", ".fld_to_addresses input").set(fromAndToAddress);
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "css", "[name='subject']").set(myData.get("emailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * View Summary_Verify that the summary of "History" for case can be displayed,
	 * when using "View Summary" function.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21093_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify archived email exists in "Email" sub-panel
		StandardSubpanel emailSubpanel= sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.assertContains(myData.get("emailSubject"), true);

		// Click Historical Summary
		// TODO: VOOD-738 - Need to add a lib support for the actions in the record view 
		sugar().cases.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify that archived email is displayed in Historical Summary
		// TODO: VOOD-965 - Support for Historical Summary page
		new VoodooControl("div", "css", ".history-summary-headerpane.fld_title div").assertEquals(myData.get("historyDashlet") + " " + sugar().cases.getDefaultData().get("name"), true);
		new VoodooControl("span", "css", ".drawer.active .single .fld_name").assertEquals(myData.get("emailSubject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}