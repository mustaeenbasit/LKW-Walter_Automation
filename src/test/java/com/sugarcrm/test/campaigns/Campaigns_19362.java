package com.sugarcrm.test.campaigns;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Campaigns_19362 extends SugarTest {
	CampaignRecord myCampaign;
	UserRecord userJim;
	TargetListRecord myTargetList;
	StandardSubpanel userSubPanel;
	DataSource ds, userSetup, emailSetup;
	FieldSet emailSettings;

	public void setup() throws Exception {
		ds = testData.get(testName);
		userSetup = testData.get(testName+"_user");
		emailSetup = testData.get(testName+"_emailsetup");
		sugar().login();

		myCampaign = (CampaignRecord) sugar().campaigns.api.create(ds.get(0));
		userJim = (UserRecord) sugar().users.create(userSetup.get(0));
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();

		// Assign UserJim to Target list
		myTargetList.navToRecord();
		userSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		userSubPanel.linkExistingRecord(userJim);

		// Configure Email settings in Admin
		emailSettings = new FieldSet();
		emailSettings.put("userName", emailSetup.get(0).get("emailAddress"));
		emailSettings.put("password", emailSetup.get(0).get("password"));
		emailSettings.put("allowAllUsers", emailSetup.get(0).get("allowAllUsers"));
		sugar().admin.setEmailServer(emailSettings);

		VoodooUtils.focusDefault();
		// Set up an Email account for Campaign
		emailSettings.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSettings);



		myCampaign.navToRecord();
		// Assign Target list to Campaign
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".pagination > td > table > tbody > tr > td:nth-child(1) span").click();
		new VoodooControl("a", "id", "prospect_list_campaigns_select_button").click();
		VoodooUtils.pause(1000); // one second wait is necessary to make test run more consistently on fast-connection machines
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);  // move back the focus to default window
		VoodooUtils.focusDefault();
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set("Newsletter");
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Create an Email Marketing
		// TODO: VOOD-1028
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "css", "[name = name]").set(emailSetup.get(0).get("name"));
		new VoodooControl("select", "id", "status").set(emailSetup.get(0).get("status"));
		new VoodooControl("input", "id", "date_start_date").set(emailSetup.get(0).get("date_start"));
		new VoodooControl("select", "id", "template_id").set(emailSetup.get(0).get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set(emailSetup.get(0).get("inbound_email_id"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Logout and Login as Jim to setup Email
		sugar().logout();
		sugar().login(userJim);

		// TODO: VOOD-672 for all below controls
		// Set email settings individually
		sugar().navbar.navToModule("Emails");
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();

		// Email Account Setting
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("div", "xpath", "//*[@id='outboundAccountsTable']/table/tbody[2]/tr/td[2]/div[contains(.,'Gmail')]").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(userSetup.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get(0).get("emailAddress")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetup.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "reply_to_addr").set(emailSetup.get(0).get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();
		VoodooUtils.waitForReady(60000);

		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooUtils.pause(3000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();

		sugar().logout(); // UserJim
		sugar().login();
	}

	/**
	 * Verify that test emails can be sent within a newsletter
	 *
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19362_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCampaign.navToRecord();

		// Click  "Send Emails" link
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "send_emails_button").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "css", "#MassUpdate .oddListRowS1 input").click();
		new VoodooControl("input", "css", "[title='Send']").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(60000);

		// Go to Admin Page > Email Queue link
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "mass_Email").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Send Queued Campaign Emails" button.
		new VoodooControl("input", "css", "#form > table > tbody > tr > td > input.button").click();
		VoodooUtils.focusDefault();

		// Logout and Login as Jim to view received Email
		sugar().logout();
		sugar().login(userJim);

		// TODO: VOOD-685 Provide Inbound Email Module Support
		sugar().navbar.navToModule("Emails");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.pause(5000);
		new VoodooControl("button", "id", "checkEmailButton").click();
		if (!(new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").queryExists())) {
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+userSetup.get(0).get("userName")+"')]").waitForVisible();
			new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'"+userSetup.get(0).get("userName")+"')]").click();
		}
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();

		String textToCheck1 = emailSetup.get(0).get("text_to_check1");
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[contains(.,'"+textToCheck1+"')]").click();

		VoodooUtils.pause(30000); // Let mail load completely

		// Check Expected Result
		new VoodooControl("td", "css", "div#listBottom table > tbody > tr:nth-child(2) > td.displayEmailValue").waitForVisible(30000);
		new VoodooControl("td", "css", "div#listBottom table > tbody > tr:nth-child(2) > td.displayEmailValue").assertContains(textToCheck1, true);

		// TODO: VOOD-1035 - Not able to focus on displayEmailFramePreview
		// VoodooUtils.focusDefault();
		// VoodooUtils.focusFrame("displayEmailFramePreview");
		// VoodooUtils.pause(3000);
		// String textToCheck2 = emailSetup.get(0).get("text_to_check2");
		// new VoodooControl("body", "css", "body").waitForVisible();
		// new VoodooControl("body", "css", "body").assertContains(textToCheck2, true);

		// Delete this mail
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").waitForVisible(30000);
		new VoodooControl("button", "xpath", "//*[@id='_blank']/div/div[2]/button[4]").click();

		VoodooUtils.acceptDialog();
		VoodooUtils.pause(8000); // Let delete complete. no suitable waitForxxx control could be located
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}