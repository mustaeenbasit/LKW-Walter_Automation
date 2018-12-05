package com.sugarcrm.test.campaigns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19545 extends SugarTest {
	FieldSet campaignsData = new FieldSet();

	public void setup() throws Exception {
		campaignsData = testData.get(testName).get(0);
		TargetRecord myTarget = (TargetRecord) sugar().targets.api.create();
		sugar().targetlists.api.create();

		// Updating Email Address for Target Record.
		sugar().login();
		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);
		sugar().targets.recordView.edit();
		sugar().targets.recordView.getEditField("emailAddress").set(campaignsData.get("emailAddress"));
		sugar().targets.recordView.save();

		// Updating TargetList type to 'Test' and adding Target to TargetList
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);
		sugar().targetlists.recordView.edit();
		sugar().targetlists.recordView.getEditField("listType").set(campaignsData.get("targetListType"));
		sugar().targetlists.recordView.save();
		StandardSubpanel targetSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetSubpanel.scrollIntoViewIfNeeded(false);
		targetSubpanel.linkExistingRecord(myTarget);
	}

	/**
	 * Verify that the Date Sent of campaign email which is viewed from Message
	 * Sent/Attempted sub-panel of campaign status page is the same as Time Zone
	 * setting in My Account.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19545_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define controls for Campaigns
		// TODO: VOOD-1072, VOOD-1028
		VoodooControl emailTypeCtrl = new VoodooControl("input", "id", "wizardtype_em");
		VoodooControl startBtnCtrl = new VoodooControl("input", "id", "startbutton");
		VoodooControl nameCtrl = new VoodooControl("input", "id", "name");
		VoodooControl statusCtrl = new VoodooControl("select", "id", "status");
		VoodooControl startDateCtrl = new VoodooControl("input", "id", "date_start");
		VoodooControl templateCtrl = new VoodooControl("select", "id", "template_id");
		VoodooControl prospectListsCtrl = new VoodooControl("input", "id", "all_prospect_lists");
		VoodooControl fromNameCtrl = new VoodooControl("input", "id", "from_name");
		VoodooControl fromAddressCtrl = new VoodooControl("input", "id", "from_addr");
		VoodooControl useMailAccountCtrl = new VoodooControl("select", "id", "inbound_email_id");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");
		VoodooControl submitBtnCtrl = new VoodooControl("id", "css", "#wiz_submit_button");
		VoodooControl searchBtnCtrl = new VoodooControl("id", "css", "#target_list_button");
		VoodooControl targetListCtrl = new VoodooControl("a", "css", ".oddListRowS1 a");

		// Email Setup field set
		FieldSet emailSetup = testData.get("env_email_setup").get(0);

		// Configure Email settings, In bound, out bound mail settings
		sugar().admin.setEmailServer(emailSetup);
		sugar().inboundEmail.create();

		// Set up an Email account for Campaign
		emailSetup.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetup);

		// Get End Date
		String endDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Go to Campaign module and Create Campaign Wizard (Email)
		sugar().navbar.selectMenuItem(sugar().campaigns , "createCampaignWizard");
		VoodooUtils.focusFrame("bwc-frame");
		emailTypeCtrl.click();
		startBtnCtrl.click();
		VoodooUtils.waitForReady();	

		// Fill in the required fields: Name and End Date
		sugar().campaigns.editView.getEditField("name").set(testName);
		sugar().campaigns.editView.getEditField("date_end").set(endDate);

		// Click on Next -> Next -> Save and Continue
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		targetListCtrl.click();
		VoodooUtils.focusWindow(0);

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		submitBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Enter the Email marketing details -> Click on Next -> Click on Finish
		nameCtrl.set(campaignsData.get("marketingEmailName"));
		statusCtrl.set(campaignsData.get("status"));
		startDateCtrl.set(campaignsData.get("date_start"));
		templateCtrl.set(campaignsData.get("template_id"));
		prospectListsCtrl.click();
		fromNameCtrl.set(campaignsData.get("from_name"));
		fromAddressCtrl.set(campaignsData.get("from_address"));
		useMailAccountCtrl.set(campaignsData.get("inbound_email_id"));
		nextBtnCtrl.click();
		submitBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Send Test Email 
		VoodooUtils.focusDefault();
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		
		new VoodooControl("a", "id", "send_test_button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "massall").click();		

		// Calculate current date
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		Date date = new Date();
		String currentDateTime = dateFormat.format(date).toLowerCase();
		
		new VoodooControl("input", "css", "[title='Send']").click();
		VoodooUtils.waitForReady();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Asserting Recipient Name and Activity Date under Sent/Attempted sub-panel. 
		String targetName = sugar().targets.getDefaultData().get("firstName")+" "+sugar().targets.getDefaultData().get("lastName");
		new VoodooControl("span", "css", ".oddListRowS1 span").assertEquals(targetName, true);
		new VoodooControl("span", "css", ".oddListRowS1 td:nth-child(5) span").assertContains(currentDateTime, true);
		new VoodooControl("span", "css", ".oddListRowS1 td:nth-child(6) span").click();
		VoodooUtils.pause(5000); // waitForReady() is giving Candybean inspector error
		// VoodooUtils.waitForReady(30000);
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Date Sent in Email detail view
		// TODO: VOOD-792		
		new VoodooControl("slot", "css", ".detail.view td:nth-child(4) slot").assertEquals(currentDateTime, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 