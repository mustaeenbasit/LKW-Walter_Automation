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

public class Campaigns_19544 extends SugarTest {
	CampaignRecord myCampaign;
	UserRecord userJim;
	TargetListRecord myTargetList;
	
	StandardSubpanel userSubPanel;

	DataSource ds, userSetup, emailSetup;
	FieldSet emailSettings;
	
	VoodooControl myEmailMenuCaret;
	VoodooControl myEmailTemplateList;
	VoodooControl myEmailTemplateCreate;
	VoodooControl myEmailType;

	public void setup() throws Exception {
		sugar().login();
		
		ds = testData.get(testName);
		userSetup = testData.get(testName+"_user");
		emailSetup = testData.get(testName+"_email");

		myEmailMenuCaret = new VoodooControl("button", "css", "li[data-module='Emails'] button[data-toggle='dropdown']");
		myEmailTemplateList = new VoodooControl("a", "css", "li[data-module='Emails'] div.dropdown-menu a[data-navbar-menu-item='LNK_EMAIL_TEMPLATE_LIST']");
		myEmailTemplateCreate = new VoodooControl("a", "css", "li[data-module='Emails'] div.dropdown-menu a[data-navbar-menu-item='LNK_NEW_EMAIL_TEMPLATE']");
		myEmailType = new VoodooControl("option", "css", "select[name='type'] option[value='campaign']");

		userJim = (UserRecord) sugar().users.create(userSetup.get(0));
		myCampaign = (CampaignRecord) sugar().campaigns.api.create(ds.get(0));
		myTargetList = (TargetListRecord)sugar().targetlists.api.create();
		
		// Assign UserJim to Target list 
		myTargetList.navToRecord();
		userSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		userSubPanel.linkExistingRecord(userJim);

		// 1. configure emails settings in admin panel
		emailSettings = new FieldSet();
		emailSettings.put("userName", emailSetup.get(0).get("emailAddress"));
		emailSettings.put("password", emailSetup.get(0).get("password"));
		emailSettings.put("allowAllUsers", emailSetup.get(0).get("allowAllUsers"));
		sugar().admin.setEmailServer(emailSettings);
		
		// 2. Set up an email account for campaign
		VoodooUtils.focusDefault();
		emailSettings.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSettings);
		
		// 3. Create a Newsletter HTML template
		sugar().navbar.selectMenuItem(sugar().emails, "createTemplate");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(emailSetup.get(0).get("template_id"));
		new VoodooControl("textarea", "id", "description").set(emailSetup.get(0).get("template_id"));
		new VoodooControl("textarea", "id", "subjectfield").set(emailSetup.get(0).get("template_id"));
		myEmailType.click();
		VoodooUtils.focusFrame("body_text_ifr");
		new VoodooControl("body", "id", "tinymce").set(emailSetup.get(0).get("bodyText"));
		VoodooUtils.focusDefault();
		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "SAVE").click();
		VoodooUtils.waitForReady(30000);
		
		VoodooUtils.focusDefault();

		// 4. Goto Campaign Record, Assign Target list to Campaign, create an Newsletter template and Create an email marketing
		// Goto to Campaign record
		myCampaign.navToRecord();

		VoodooUtils.focusDefault();
		
		// 4a. Assign Target list to Campaign
 		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".pagination > td > table > tbody > tr > td:nth-child(1) span").click();
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);  // move back the focus to default window

		// 4b. Create an email marketing
	   	// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "css", "[name = name]").set(emailSetup.get(0).get("name"));
		new VoodooControl("select", "id", "status").set(emailSetup.get(0).get("status"));
		new VoodooControl("input", "id", "date_start_date").set(emailSetup.get(0).get("date_start"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set(emailSetup.get(0).get("inbound_email_id"));
		new VoodooControl("select", "id", "template_id").set(emailSetup.get(0).get("template_id"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that the html text and plain text are displayed as created when going to the related campaign email detail view from "Message Sent/Attempted" sub-panel of campaign status page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19544_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Goto Campaign Record
		myCampaign.navToRecord();

		VoodooUtils.focusDefault();
				
		// click  "Send Emails" link 
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
		
		VoodooUtils.focusDefault();
		
		// Return to Campaign to view Email send
		myCampaign.navToRecord();

		// click on View Status button on campaigns record view page
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "view_status_button").click();
				
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Goto sent Email
		new VoodooControl("a", "xpath", "//*[@id='list_subpanel_targeted']/table/tbody/tr[3]/td[6]/span/a").click();

		// Check Expected result
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.voodoo.log.info(">>>>>>>>>" + (new VoodooControl("h1", "css", "div#html_div > h1").getText()));
		new VoodooControl("h1", "css", "div#html_div > h1").assertContains(emailSetup.get(0).get("htmlText"), true);
		new VoodooControl("p", "css", "div#html_div p:nth-of-type(2)").assertContains(emailSetup.get(0).get("plainText"), true);

		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}