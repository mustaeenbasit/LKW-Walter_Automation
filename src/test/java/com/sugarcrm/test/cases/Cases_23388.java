package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23388 extends SugarTest {
	FieldSet myData,emailSetup;
	String dashboardTitle;
	CaseRecord myCase;

	public void setup() throws Exception {
		emailSetup = testData.get("env_email_setup").get(0);
		myData = testData.get(testName).get(0);
		myCase = (CaseRecord) sugar().cases.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// Navigate to Case Details and Choose dashboard by name
		myCase.navToRecord();
		dashboardTitle = sugar().accounts.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.contains(myData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(myData.get("myDashboard"));
		}

		// Create Archived email for case
		// TODO: VOOD-798. Lib support in create/verify Archive Email from History Dashlet
		VoodooControl historyDashletCreateCtrl = new VoodooControl("span", "css", ".dashboard .dashlet-row li:nth-child(2) .layout_Home .fa.fa-plus");
		VoodooControl fromFieldCtrl = new VoodooControl("input", "css", ".fld_from_address input");
		historyDashletCreateCtrl.waitForVisible();
		historyDashletCreateCtrl.click();
		new VoodooControl("a", "css", "div.thumbnail.dashlet.ui-draggable a[data-dashletaction='archiveEmail']").click();
		fromFieldCtrl.waitForElement();

		// TODO: VOOD-797. Lib support to handle compose archive email in new email composer
		new VoodooControl("i", "css", "div.input-append i.fa-calendar").click();
		new VoodooControl("td", "css", ".datepicker-days td[class='day active']").click();
		fromFieldCtrl.waitForVisible();
		fromFieldCtrl.set(myData.get("fromAndToAddress"));
		new VoodooControl("input", "css", ".fld_to_addresses input").set(myData.get("fromAndToAddress"));
		new VoodooControl("input", "css", "div.select2-result-label").click();
		new VoodooControl("input", "name", "subject").set(myData.get("emailSubject"));
		new VoodooControl("a", "css", ".fld_archive_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Edit Archived Email_Verify that the detail view of an archived email is displayed after
	 * clicking the subject of it in History (obsolete- using Email sub-panel-) sub-panel of a case detail view.
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23388_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click subject of an existing archived email in "Email" sub-panel
		StandardSubpanel emailSubpanel= sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.clickLink(myData.get("emailSubject"), 1);

		// Assert detail view of Archived Email is visible
		sugar().emails.detailView.assertVisible(true);

		// Navigate to Case details and set Dashboard to 'Help Dashboard"
		myCase.navToRecord();
		if(dashboardTitle.contains(myData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(myData.get("helpDashboard"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
