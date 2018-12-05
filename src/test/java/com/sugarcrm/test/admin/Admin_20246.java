package com.sugarcrm.test.admin;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import org.junit.Ignore;
import org.junit.Test;

public class Admin_20246 extends SugarTest {
	FieldSet fs;
	VoodooControl dashboardTitle, dashboardTitleMenuItem, emailTabDiv, emailTabDivContent, emailTabList, emailTabDashlet;

	AccountRecord myAcc;
	ContactRecord myContact;
	OpportunityRecord myOpp;
	CaseRecord myCase;

	public void setup() throws Exception {
		sugar().login();
		fs = testData.get(testName).get(0);

		myContact = (ContactRecord) sugar().contacts.api.create();
		myAcc = (AccountRecord) sugar().accounts.api.create();
		myCase = (CaseRecord) sugar().cases.api.create();

		// TODO: VOOD-963
		dashboardTitle =  new VoodooControl("a", "css", "div.preview-headerbar span.fld_name.detail a.dropdown-toggle.btn.btn-invisible.btn-link");
		dashboardTitleMenuItem = new VoodooControl("a", "css", "div.preview-headerbar .dropdown-menu li:nth-of-type(1) a");
		emailTabDiv = new VoodooControl("div", "css", "div[data-voodoo-name='history'] > div.dashlet-unordered-list > div.tab-content > div.tab-pane");
		emailTabDivContent = new VoodooControl("div", "css", "div[data-voodoo-name='history'] > div.dashlet-unordered-list > div.tab-content > div.tab-pane > div.block-footer");
		emailTabList = new VoodooControl("ul", "css", "div[data-voodoo-name='history'] > div.dashlet-unordered-list > div.tab-content > div.tab-pane > ul[data-action='pagination-body']");
		emailTabDashlet = new VoodooControl("a", "css", "div[data-voodoo-name='history'] > div.dashlet-unordered-list > div.dashlet-tabs.tab3 > div > div.dashlet-tab:nth-of-type(2) > a");

		// Navigate to 'Related Contacts Emails' in Admin Tools
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("relatedContactEmails").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Enable Contact Email in Admin
		// TODO: VOOD-1400
		new VoodooControl("input", "id", "modules[Cases]").set("true");
		new VoodooControl("input", "id", "modules[Accounts]").set("true");
		new VoodooControl("input", "id", "modules[Opportunities]").set("true");
		new VoodooControl("input", "id", "configuratorHistoryContactsEmails_admin_save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Check that emails from Contact's History subpanel could be hidden for related Account, Opportunity and Case
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore("VOOD-798 - Lib support in create/verify Archive Email from History Dashlet")
	public void Admin_20246_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}

		// Select Emails Dashlet
		emailTabDashlet.click();
		// Click +
		new VoodooControl("a", "css", "ul.dashlet-row.ui-sortable li:nth-of-type(2) a[data-original-title='Actions']").click();
		// Select 'Archive Email'
		new VoodooControl("a", "css", "a[data-dashletaction='archiveEmail']").click();
		sugar().alerts.closeAllAlerts();

		// Input values in Archive Email page
		// Date Sent
		new VoodooControl("input", "css", ".datepicker").set(fs.get("date_sent"));
		// From
		new VoodooControl("input", "name", "from_address").set(fs.get("from_address"));
		// To
		new VoodooControl("a", "css", "a[data-name='to_addresses']").click();
		new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/span/span/input").set("true");
		new VoodooControl("a", "name", "done_button").click();
		// Subject
		new VoodooControl("input", "name", "subject").set(fs.get("subject"));
		// Save
		new VoodooControl("a", "name", "archive_button").click();
		VoodooUtils.waitForReady();

		// Assert for subject
		emailTabDiv.assertContains(fs.get("subject"), true);

		sugar().contacts.recordView.subpanels.get("Cases").hover();
		sugar().contacts.recordView.subpanels.get("Documents").hover();

		sugar().contacts.recordView.subpanels.get("Emails").expandSubpanel();
		FieldSet verifyThis = new FieldSet();
		verifyThis.put("subject", fs.get("subject"));
		sugar().contacts.recordView.subpanels.get("Emails").verify(1, verifyThis, true);;

		// Verify values in Accounts after linking Accounts with Contacts
		myAcc.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.recordView.subpanels.get("Contacts").linkExistingRecord(myContact);
		VoodooUtils.refresh();
		sugar().accounts.recordView.subpanels.get("Emails").hover();
		sugar().accounts.recordView.subpanels.get("Emails").expandSubpanel();
		sugar().accounts.recordView.subpanels.get("Emails").verify(1, verifyThis, true);;

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);

		// Verify values in Opportunities after linking Opportunities with Accounts
		myOpp = (OpportunityRecord) sugar().opportunities.create();
		sugar().alerts.closeAllAlerts();
		myOpp.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().alerts.closeAllAlerts();
		sugar().opportunities.recordView.subpanels.get("Contacts").linkExistingRecord(myContact);
		VoodooUtils.refresh();
		sugar().opportunities.recordView.subpanels.get("Emails").hover();
		sugar().opportunities.recordView.subpanels.get("Emails").expandSubpanel();
		sugar().opportunities.recordView.subpanels.get("Emails").verify(1, verifyThis, true);;

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);

		// Verify values in Cases after linking Cases with Contacts
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().cases.recordView.subpanels.get("Contacts").linkExistingRecord(myContact);
		VoodooUtils.refresh();
		sugar().cases.recordView.subpanels.get("Emails").hover();
		new VoodooControl("li", "xpath", "//div[@class='filtered tabbable tabs-left layout_Emails']/ul/li").assertAttribute("class", "empty");

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), false);
		emailTabDiv.assertContains("No data available", true);

		String myCaseNumber = "[CASE:"+sugar().cases.recordView.getDetailField("caseNumber").getText().trim()+"]";

		// Step 11 - Add CASE:<%number> to History dashlet of Contact and check
		myContact.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}

		// Select Emails Dashlet
		emailTabDashlet.click();
		// Click +
		new VoodooControl("a", "css", "ul.dashlets.row-fluid.layout_Home > li > ul > li:nth-of-type(2) a[data-original-title='Actions']").click();
		// Select 'Archive Email'
		new VoodooControl("a", "css", "a[data-dashletaction='archiveEmail']").click();
		sugar().alerts.closeAllAlerts();

		// Input values in Archive Email page
		// Date Sent
		new VoodooControl("input", "css", ".datepicker").set(fs.get("date_sent"));
		// From
		new VoodooControl("input", "name", "from_address").set(fs.get("from_address"));
		// To
		new VoodooControl("a", "css", "a[data-name='to_addresses']").click();
		new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/span/span/input").set("true");
		new VoodooControl("a", "name", "done_button").click();
		// Subject
		new VoodooControl("input", "name", "subject").set(myCaseNumber);
		// Save
		new VoodooControl("a", "name", "archive_button").click();
		sugar().alerts.waitForLoadingExpiration();

		// Assert for CASE
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(myCaseNumber, true);

		// Verify values in Cases
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for CASE
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(myCaseNumber, true);

		// Step 13 - Uncheck Cases in admin->contact email
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("relatedContactEmails").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1400
		new VoodooControl("input", "id", "modules[Cases]").set("false");
		new VoodooControl("input", "id", "modules[Accounts]").set("true");
		new VoodooControl("input", "id", "modules[Opportunities]").set("true");
		new VoodooControl("input", "id", "configuratorHistoryContactsEmails_admin_save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Now verify values in Cases
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for Empty Case Dashlet
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), false);
		emailTabDiv.assertContains("No data available", true);

		// Now verify values in Accounts and Opportunities

		// Verify values in Accounts
		myAcc.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);

		// Verify values in Opportunities
		myOpp.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().alerts.closeAllAlerts();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);


		// Step 15 - Uncheck Opportunities, check Cases in admin->contact email
		sugar().navbar.navToAdminTools();
		// TODO: VOOD-1400
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("relatedContactEmails").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "id", "modules[Cases]").set("true");
		new VoodooControl("input", "id", "modules[Accounts]").set("true");
		new VoodooControl("input", "id", "modules[Opportunities]").set("false");
		new VoodooControl("input", "id", "configuratorHistoryContactsEmails_admin_save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Now verify values in Cases
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for CASE
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(myCaseNumber, true);

		// Now verify values in Accounts and Opportunities

		// Verify values in Accounts
		myAcc.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);

		// Verify values in Opportunities
		myOpp.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().alerts.closeAllAlerts();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject - Empty
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), false);
		emailTabDiv.assertContains("No data available", true);

		// Step 17 - Check Opportunities, uncheck Accounts and Cases in admin->contact email
		sugar().navbar.navToAdminTools();
		// TODO: VOOD-1400
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("relatedContactEmails").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "id", "modules[Cases]").set("false");
		new VoodooControl("input", "id", "modules[Accounts]").set("false");
		new VoodooControl("input", "id", "modules[Opportunities]").set("true");
		new VoodooControl("input", "id", "configuratorHistoryContactsEmails_admin_save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Now verify values in Cases
		myCase.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject - Empty
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), false);
		emailTabDiv.assertContains("No data available", true);

		// Now verify values in Accounts and Opportunities

		// Verify values in Accounts
		myAcc.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject - Empty
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), false);
		emailTabDiv.assertContains("No data available", true);

		// Verify values in Opportunities
		myOpp.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().alerts.closeAllAlerts();

		// Select - My Dashboard
		if(!dashboardTitle.getText().contains("My Dashboard")){
			dashboardTitle.click();
			dashboardTitleMenuItem.click();
		}
		// Select Emails Dashlet
		emailTabDashlet.click();

		// Assert for subject
		VoodooUtils.waitForReady();
		emailTabDiv.assertContains(fs.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}