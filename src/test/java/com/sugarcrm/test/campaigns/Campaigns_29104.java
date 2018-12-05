package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_29104 extends SugarTest {
	FieldSet campaignsData;
	VoodooControl nextBtnCtrl, submitBtnCtrl;

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		campaignsData = testData.get(testName).get(0);
		sugar().login();

		// Configure Email settings, In bound, out bound mail settings
		sugar().admin.setEmailServer(emailSetup);
		sugar().inboundEmail.create();

		// Set up an Email account for Campaign
		VoodooUtils.focusDefault();
		campaignsData.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(campaignsData);
	}

	/**
	 * Verify "Campign log" button is NOT displayed in the top navigation bar
	 * 
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_29104_execute() throws Exception {
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
		VoodooControl viewStatusBtnCtrl = new VoodooControl("input", "id", "wiz_status_button");
		nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");
		submitBtnCtrl = new VoodooControl("input", "id", "wiz_submit_button");

		// Click on Campaign module-> Create Campaign Wizard (Email)
		sugar().navbar.navToModule(sugar().campaigns.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().campaigns);
		sugar().campaigns.menu.getControl("createCampaignWizard").click();
		VoodooUtils.focusFrame("bwc-frame");
		emailTypeCtrl.click();
		startBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Fill in the required fields: Name = Test1 and End Date = (any date)
		sugar().campaigns.editView.getEditField("name").set(campaignsData.get("name"));
		sugar().campaigns.editView.getEditField("date_end").set(campaignsData.get("date_end"));

		// Click on Next -> Next -> Save and Continue
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		submitBtnCtrl.click();
		VoodooUtils.waitForReady(60000);

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

		// Verify that Campaign Summary form should be opened
		// TODO: VOOD-1072
		new VoodooControl("h2", "css", ".moduleTitle h2").assertEquals(campaignsData.get("moduleTitle"), true);
		new VoodooControl("h4", "css", "#campaign_summary .dataField h4").assertContains(campaignsData.get("campaignSummary"), true);
		new VoodooControl("td", "css", "#campaign_summary tr:nth-child(2) td:nth-child(2)").assertContains(campaignsData.get("name"), true);
		new VoodooControl("td", "css", "#campaign_summary tr:nth-child(6) td:nth-child(2)").assertContains(campaignsData.get("date_end"), true);

		// Click on View Status button
		viewStatusBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// On View Status, Campaign button should be displayed instead of Campaign log
		sugar().navbar.assertContains(sugar().campaigns.moduleNamePlural, true);
		sugar().navbar.assertContains(campaignsData.get("campaignLog"), false);
		sugar().navbar.showAllModules();

		// Also verifying Campaign Trackers on the overflow menu 
		// TODO: VOOD-784
		new VoodooControl("ul", "css", "li.dropdown.more .dropdown-menu ul").assertContains(campaignsData.get("campaignLog"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}