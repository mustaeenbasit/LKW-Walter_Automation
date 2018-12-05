package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24309 extends SugarTest {
	FieldSet recordsData;

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		recordsData = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);

	}

	/**
	 * Compose Email_Verify that "Compose Email" function in "Activities" sub-panel works successfully.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24309_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Click "Opportunities" tab on navigation bar and click the name of an opportunity in "Opportunities" list view.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Click "Compose Email" button in Activities/Emails sub-panel and compose an email for the selected opportunity.
		/*	
		 * Using control instead of '.composeEmail()' method to compose email use focus on table "#mce_0_tbl" and this is changed
		 * (like "#mce_0_tbl" to "#mce_7_tbl" on second click, "#mce_7_tbl" to "#mce_8_tbl" on third click, "#mce_8_tbl" to "#mce_9_tbl" on forth click and so on)
		 * on every click
		 */
		StandardSubpanel emailSubpanel= sugar().opportunities.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.getControl("composeEmail").click();
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.composeEmail.getControl("toAddress").set(recordsData.get("toAddress"));
		VoodooControl emailToSearchResultCtrl = new VoodooControl("div", "css", ".select2-result-label"); // Need to select searched result
		emailToSearchResultCtrl.waitForVisible();
		emailToSearchResultCtrl.click();
		sugar().opportunities.recordView.composeEmail.getControl("subject").set(recordsData.get("sentMailSubject"));
		sugar().opportunities.recordView.composeEmail.getControl("sendButton").click();
		sugar().alerts.confirmAllAlerts();
		VoodooUtils.waitForReady(30000); // Taking time to send the email

		// TODO: VOOD-965
		// Click Edit action drop down menu and click "Historical Summary"
		sugar().accounts.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// Verify that the composed email is displayed in "History" sub-panel with correct status.
		// Using xPath to verify the status of that record in the same row.
		new VoodooControl("tr", "xpath", "//*[@id='drawers']/div/div/div[1]/div/div[2]/div/table/tbody/tr[contains(.,'"+recordsData.get("sentMailSubject")+"')]").assertContains(recordsData.get("sentEmail"), true);

		// Close Historical summary drawer
		new VoodooControl("a", "css", ".history-summary-headerpane a[name='cancel_button']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}