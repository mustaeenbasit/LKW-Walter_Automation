package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Dashlets_24430 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		customFS = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// Create Archived email for Opportunity
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Choose dashboard by name
		VoodooControl dashboard = sugar().opportunities.dashboard.getControl("dashboard");
		if(!dashboard.queryContains(customFS.get("myDashboard"), true))
			sugar().opportunities.dashboard.chooseDashboard(customFS.get("myDashboard"));

		// Archived Email
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("span", "css", ".dashboard .dashlet-row li:nth-child(5) .layout_Home .fa.fa-plus");
		VoodooControl fromFieldCtrl = new VoodooControl("input", "css", ".fld_from_address input");
		historyDashletCreateCtrl.scrollIntoView();
		historyDashletCreateCtrl.click();
		VoodooControl archiveEmail = new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']");
		archiveEmail.waitForVisible(20000);
		archiveEmail.click();
		fromFieldCtrl.waitForElement();

		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		fromFieldCtrl.waitForVisible();
		fromFieldCtrl.set(customFS.get("fromAndToAddress"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(customFS.get("fromAndToAddress"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "name", "subject").set(customFS.get("emailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * View Summary_Verify that the date value of the summary is displayed in correct format after changing the "Date Format" in "My Profile".
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_24430_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change the Date Format in User Profile.
		FieldSet userPrefs = new FieldSet();
		userPrefs.put("advanced_dateFormat", customFS.get("dateFormate")); 
		sugar().users.setPrefs(userPrefs);

		// Go to Opportunity record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Select "Historical Summary" action in action drop down list.
		sugar().opportunities.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-965 -Support for Historical Summary page
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify that the date value of archived emails is displayed by using the format set in "My Profile".
		new VoodooControl("div", "css", ".layout_Opportunities.active .dataTable tr .list.fld_date_modified div").assertContains(VoodooUtils.getCurrentTimeStamp("yyyy/MM/dd"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}