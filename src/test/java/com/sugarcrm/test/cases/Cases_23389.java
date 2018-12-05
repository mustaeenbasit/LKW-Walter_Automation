package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23389 extends SugarTest {
	FieldSet myData,emailSetup;
	String dashboardTitle;

	public void setup() throws Exception {
		emailSetup = testData.get("env_email_setup").get(0);
		myData = testData.get(testName).get(0);
		sugar().cases.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

		// Create Archived email for case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Choose dashboard by name
		dashboardTitle = sugar().accounts.dashboard.getControl("dashboard").getText().trim();
		if(!dashboardTitle.contains(myData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(myData.get("myDashboard"));
		}

		// Archived Email
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
	 * Edit Archived Email_Verify that modification of archived email for case can be canceled
	 * in the "Archived Email" detailed view
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_23389_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click subject of an existing archived email in "Email" sub-panel
		StandardSubpanel emailSubpanel= sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.clickLink(myData.get("emailSubject"), 1);

		// Click "Edit" button in "Archived Email" detail view and edit subject of Archived Email
		sugar().emails.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-798
		new VoodooControl("textarea", "id", "subjectfield").set(myData.get("newEmailSubject"));
		VoodooUtils.focusDefault();

		//  Click "Cancel" button
		sugar().emails.editView.cancel();
		VoodooUtils.focusDefault();

		// Navigate to Cases record view and Verify that information of the archived email for the selected case not modified
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		emailSubpanel.scrollIntoViewIfNeeded(false);

		FieldSet emailInfo = new FieldSet();
		emailInfo.put("subject", myData.get("emailSubject"));
		emailInfo.put("status", myData.get("status"));
		emailSubpanel.verify(1, emailInfo, true);

		// Set Dashboard to 'Help Dashboard"
		if(dashboardTitle.contains(myData.get("myDashboard"))) {
			sugar().accounts.dashboard.chooseDashboard(myData.get("helpDashboard"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
