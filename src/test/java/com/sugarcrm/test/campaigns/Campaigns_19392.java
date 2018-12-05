package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19392 extends SugarTest {
	CampaignRecord myCampaign;
	UserRecord userJim;
	TargetListRecord myTargetList;

	StandardSubpanel userSubPanel;

	DataSource ds, userSetup, emailSetup;
	FieldSet emailSettings;

	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);
		userSetup = testData.get(testName+"_user");
		emailSetup = testData.get(testName+"_email");

		myCampaign = (CampaignRecord) sugar().campaigns.api.create(ds.get(0));
		userJim = (UserRecord) sugar().users.create(userSetup.get(0));
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();

		// Assign UserJim to Target list 
		myTargetList.navToRecord();
		userSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		userSubPanel.linkExistingRecord(userJim);
		
		// Configure emails settings in admin panel
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
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.pause(1000); // one second wait is necessary to make test run more consistently on fast-connection machines
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);  // move back the focus to default window

		// Create an email marketing
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
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
	}

	/**
	 * Verify that email can be sent from campaign detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19392_execute() throws Exception {
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

		// Go to Admin Page > Email Queue link
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "mass_Email").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Send Queued Campaign Emails" button. 
		new VoodooControl("input", "css", "#form > table > tbody > tr > td > input.button").click();
		VoodooControl listViewTable = new VoodooControl("table", "css", "table.list.view");
		listViewTable.waitForInvisible(120000);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Return to Campaign to view Email send
		myCampaign.navToRecord();

		// Click on View Status button on campaigns record view page
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "view_status_button").click();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Check Expected result 
		new VoodooControl("div", "css", "#list_subpanel_track_queue").assertContains(emailSetup.get(0).get("emailAddress"), false);
		new VoodooControl("div", "css", "#list_subpanel_targeted").assertContains(emailSetup.get(0).get("emailAddress"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
