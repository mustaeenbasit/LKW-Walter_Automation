package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19570 extends SugarTest {
	FieldSet campaignsData;

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		campaignsData = testData.get(testName).get(0);
		sugar().login();

		// Define controls for Campaigns
		// TODO: VOOD-1072
		VoodooControl newsLetterTypeCtrl = new VoodooControl("input", "id", "wizardtype_nl");
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
		VoodooControl submitBtnCtrl = new VoodooControl("input", "id", "wiz_submit_button");

		// Configure Email settings, In bound, out bound mail settings
		sugar().admin.setEmailServer(emailSetup);

		// Set up an Email account for Campaign
		VoodooUtils.focusDefault();
		emailSetup.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetup);

		// Get Current Date
		String endDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Go to Campaign module and Create Campaign Wizard (Newsletter)
		sugar().navbar.selectMenuItem(sugar().campaigns , "createCampaignWizard");
		VoodooUtils.focusFrame("bwc-frame");
		newsLetterTypeCtrl.click();
		startBtnCtrl.click();
		VoodooUtils.waitForReady();	

		// Fill in the required fields: Name = Test1 and End Date = (any date) on Campaign Header and Navigate to Campaign Subscription Page
		sugar().campaigns.editView.getEditField("name").set(testName);
		sugar().campaigns.editView.getEditField("date_end").set(endDate);
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		submitBtnCtrl.click();

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
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();	
	}

	/**
	 * Newsletter - Duplicate email marketing message_Verify that email marketing message can be duplicated
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19570_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to detail view of Newsletter type Campaign 
		sugar().navbar.selectMenuItem(sugar().campaigns , "viewNewsletters");
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying the information entered in Email Marketing is same as entered and navigate to Email Marketing detail view
		// TODO: VOOD-1072
		VoodooControl emailMarketing = new VoodooControl("span","css", "#list_subpanel_emailmarketing .oddListRowS1 span");
		emailMarketing.assertEquals(campaignsData.get("marketingEmailName"),true);
		emailMarketing.click();

		// Click copy on Email Marketing page and save duplicate Email Marketing message
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "input[name='Duplicate']").click();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("input", "css", "input[name='name']").set(testName);
		new VoodooControl("input", "css", "input[title='Save']").click();
		VoodooUtils.waitForReady(30000);

		// Verify the duplicate Email Marketing message is saved
		new VoodooControl("a", "css", "#list_subpanel_emailmarketing .listViewThLinkS1").click();
		VoodooControl duplicateEmailMarketing = new VoodooControl("span","css", "#list_subpanel_emailmarketing .oddListRowS1 span");
		duplicateEmailMarketing.assertEquals(testName, true);
		duplicateEmailMarketing.click();

		// Verify detail view of duplicate EmailMarketing message is displayed
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".moduleTitle h2").assertEquals(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}