package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19672 extends SugarTest {
	VoodooControl configSubPanelCtrl;
	FieldSet emailSetupData, myInboundData, emailMarketingRecord,customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		emailMarketingRecord = testData.get(testName+"_email_marketings").get(0); // Email marketing
		emailSetupData = testData.get("env_email_setup").get(0); // SMTP settings
		myInboundData = testData.get(testName+"_inbound").get(0); // In-bound email setup

		// Create two target record
		TargetRecord myTarget1 = (TargetRecord) sugar().targets.api.create();
		FieldSet fs = new FieldSet();
		fs.put("firstName", testName);
		TargetRecord myTarget2 = (TargetRecord) sugar().targets.api.create(fs);

		// Create campaign record
		sugar().campaigns.api.create();
		sugar().login();

		// Clear Search form
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.basicSearch("");

		// Setup email settings
		sugar().admin.setEmailServer(emailSetupData);

		// TODO: VOOD-938
		// Setup a proper number for sub-panel pagination
		configSubPanelCtrl = new VoodooControl("a", "id", "configphp_settings");		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		configSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "list_max_entries_per_subpanel").set("1");
		new VoodooControl("input", "id", "ConfigureSettings_save_button").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady(120000);

		// TODO: VOOD-1082
		VoodooControl targetEmail = new VoodooControl("input", "css", ".newEmail.input-append");
		VoodooControl emailOptedOut = new VoodooControl("button", "css", "button[data-emailproperty='opt_out']");
		VoodooControl clickToAddEmail = new VoodooControl("a", "css", "a.btn.addEmail");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");

		// Go to target recordView and add opted out email
		myTarget1.navToRecord();
		sugar().targets.recordView.edit();
		targetEmail.set(emailMarketingRecord.get("from_addr"));
		clickToAddEmail.click();
		emailOptedOut.click();
		sugar().targets.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Go to target recordView and add second opted out email
		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(2);
		sugar().targets.recordView.edit();
		targetEmail.set(emailSetupData.get("userName"));
		clickToAddEmail.click();
		emailOptedOut.click();
		sugar().targets.recordView.save();

		// TODO: VOOD-1082
		// Edit campaign record
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		sugar().alerts.waitForLoadingExpiration();
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set("Newsletter");
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// Link target record
		new VoodooControl("a", "css", "#list_subpanel_prospectlists > table tr:nth-child(3) > td:nth-child(1) > span > a").click();
		StandardSubpanel targetSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetSubPanel.scrollIntoView();
		targetSubPanel.linkExistingRecord(myTarget1);
		targetSubPanel.linkExistingRecord(myTarget2);

		// Setup email
		VoodooUtils.focusDefault();
		emailSetupData.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetupData);

		// Create an email marketing
		// TODO: VOOD-1028
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "name").set(emailMarketingRecord.get("name"));
		new VoodooControl("select", "id", "status").set(emailMarketingRecord.get("status"));
		new VoodooControl("input", "id", "date_start_date").set(emailMarketingRecord.get("date_start"));

		// create new template
		new VoodooControl("a", "css", "#contentTable table tr:nth-child(2) > td:nth-child(4) > slot > a").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "name", "name").set(customData.get("template_name"));
		new VoodooControl("input", "css", "input[value='Insert URL Reference']").click();
		new VoodooControl("input", "id", "SAVE").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set("SugarCRM");

		new VoodooControl("input", "id", "from_name").set(emailMarketingRecord.get("from_name"));
		new VoodooControl("input", "id", "from_addr").set(emailMarketingRecord.get("from_addr"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady(60000);

		// TODO: VOOD-1028
		// update optout tracker link
		new VoodooControl("input", "id", "launch_wizard_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#nav_step3 > a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "tracker_name").waitForVisible();
		new VoodooControl("input", "id", "tracker_name").set(customData.get("optout_name"));
		new VoodooControl("input", "id", "is_optout").click();
		new VoodooControl("input", "css", "input[value='Create Tracker']").click();
		VoodooUtils.pause(3000); //TODO: TR-8001 Studio Buttons remain disabled longer than intended
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "wiz_submit_finish_button").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// TODO: VOOD-1028
		// Click on campaign Send-email
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", "#detail_header_action_menu .sugar_action_button span").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "id", "send_emails_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "css", "input[title='Send']").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// TODO:  VOOD-1085
		// Send queue mail
		VoodooControl emailQueueCtrl = new VoodooControl("a", "id", "mass_Email");
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		emailQueueCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "massall_top").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[title='Send Queued Campaign Emails']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Campaign Status View Pagination_Opt Out
	 * */
	@Test
	public void Campaigns_19672_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		new VoodooControl("input", "id", "view_status_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("div", "css", "#list_subpanel_removed").scrollIntoView();
		new VoodooControl("td", "css", "#list_subpanel_removed > table > tbody > tr:nth-child(3) > td:nth-child(2)").assertContains(emailSetupData.get("userName"), true);

		// Verify that all pagination button works properly 
		// click on next(pagination) button
		new VoodooControl("img", "css", "#list_subpanel_removed > table > tbody > tr.pagination > td > table > tbody > tr > td:nth-child(2) > button:nth-child(4) > img").click();
		new VoodooControl("td", "css", "#list_subpanel_removed > table > tbody > tr:nth-of-type(3) > td:nth-child(2)").assertContains(emailSetupData.get("userName"), true);

		// Click on end button
		new VoodooControl("img", "css", "#list_subpanel_removed > table > tbody > tr.pagination > td > table > tbody > tr > td:nth-child(2) > button:nth-child(5) > img").click();
		new VoodooControl("td", "css", "#list_subpanel_removed > table > tbody > tr:nth-of-type(3) > td:nth-child(2)").assertContains(emailSetupData.get("userName"), true);

		// click on prev button
		new VoodooControl("img", "css", "#list_subpanel_removed > table > tbody > tr.pagination > td > table > tbody > tr > td:nth-child(2) > button:nth-child(2) > img").click();
		new VoodooControl("td", "css", "#list_subpanel_removed > table > tbody > tr:nth-of-type(3) > td:nth-child(2)").assertContains(emailMarketingRecord.get("from_addr"), true);

		//click on start button
		new VoodooControl("img", "css", "#list_subpanel_removed > table > tbody > tr.pagination > td > table > tbody > tr > td:nth-child(2) > button:nth-child(1) > img").click();
		new VoodooControl("td", "css", "#list_subpanel_removed > table > tbody > tr:nth-of-type(3) > td:nth-child(2)").assertContains(emailMarketingRecord.get("from_addr"), true);

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}