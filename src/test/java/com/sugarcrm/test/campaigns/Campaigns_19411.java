package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19411 extends SugarTest {

	public void setup() throws Exception {
		FieldSet emailSetup =  new FieldSet();
		FieldSet campaignsData =  new FieldSet();
		emailSetup = testData.get("env_email_setup").get(0);
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

		VoodooUtils.focusDefault();
		// Set up an Email account for Campaign
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
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Target List management_Verify that "Edit" function in "Target List" sub-panel works correctly.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19411_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to detail view of Newsletter type Campaign 
		sugar().navbar.selectMenuItem(sugar().campaigns , "viewNewsletters");
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1072
		// Clicking on first record of target list subpanel
		VoodooControl firstTargetListCtrl = new VoodooControl("span", "css",
				"#list_subpanel_prospectlists span[sugar='slot1b']");
		firstTargetListCtrl.click();
		
		// Edit the target list and set name with default data name
		sugar().targetlists.recordView.edit();
		sugar().targetlists.recordView.getEditField("targetlistName")
				.set(sugar().targetlists.getDefaultData().get("targetlistName"));
		sugar().targetlists.recordView.save();
		
		// Verifying target list name is updated in targetlist's listview as well
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.getDetailField(1, "targetlistName")
				.assertEquals(sugar().targetlists.getDefaultData().get("targetlistName"), true);
		
		// Navigate to campaign record view
		sugar().navbar.selectMenuItem(sugar().campaigns , "viewNewsletters");
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1072
		// Sort by target list name in subpanel
		new VoodooControl("span", "css", "#list_subpanel_prospectlists th:nth-child(1) span").click();
		
		// Verifying edited targetlist name is updated in campaign record view subpanel
		firstTargetListCtrl.assertEquals(sugar().targetlists.getDefaultData().get("targetlistName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}