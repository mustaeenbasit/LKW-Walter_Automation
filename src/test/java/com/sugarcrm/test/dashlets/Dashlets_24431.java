package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_24431 extends SugarTest {

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().leads.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * View Summary_Verify that time value of the summary is displayed in correct format after changing the "Time Format" in "My Account"
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_24431_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change the Date Format in User Profile.
		FieldSet customFS = testData.get(testName).get(0);
		FieldSet userPrefs = new FieldSet();
		userPrefs.put("advanced_dateFormat", customFS.get("dateFormate")); 
		sugar().users.setPrefs(userPrefs);

		// Create Archived email for leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Choose dashboard by name
		VoodooControl dashboard = sugar().leads.dashboard.getControl("dashboard");
		if(!dashboard.queryContains(customFS.get("myDashboard"), true))
			sugar().leads.dashboard.chooseDashboard(customFS.get("myDashboard"));

		// Archived Email
		// TODO: VOOD-798 - Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("span", "css", ".dashboard .dashlet-row li:nth-child(2) .layout_Home .fa.fa-plus");
		VoodooControl fromFieldCtrl = new VoodooControl("input", "css", ".fld_from_address input");
		historyDashletCreateCtrl.waitForVisible();
		historyDashletCreateCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl archiveEmail = new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']");
		archiveEmail.waitForElement();
		archiveEmail.click();
		fromFieldCtrl.waitForElement();

		// TODO: VOOD-797 - Lib support to handle compose archive email in new email composer
		// Verify that when you create the archive, the date time is based on the user profile format.
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".fld_date_sent.edit .datepicker").assertAttribute("placeholder", customFS.get("placeholder"), true);

		// Fill required data
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		fromFieldCtrl.waitForVisible();
		fromFieldCtrl.set(customFS.get("fromAndToAddress"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(customFS.get("fromAndToAddress"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "name", "subject").set(customFS.get("emailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();

		// Get current date time and replace uppercase(AM/PM) to lowercase(am/pm)
		String currentDate = VoodooUtils.getCurrentTimeStamp("yyyy/MM/dd hh:mma");
		currentDate = currentDate.replace("AM", "am").replace("PM","pm");
		VoodooUtils.waitForReady();

		// Relative Time value is displayed, eg. 2 minutes ago.  
		new VoodooControl("a", "css", "[data-voodoo-name='history'] .dashlet-tabs-row div:nth-child(2) a").click();
		VoodooControl timeCtrl = new VoodooControl("time", "css", "[data-voodoo-name='history'] .tab-pane.active .unstyled.listed .details time[title='" + currentDate + "']");
		timeCtrl.assertContains(customFS.get("message"), true);

		// The date time shown in tooltip will use the user profile format.
		timeCtrl.hover();
		timeCtrl.waitForVisible();
		timeCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}