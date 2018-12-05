package com.sugarcrm.test.targets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Targets_21517 extends SugarTest {
	CampaignRecord myCampaign;
	TargetRecord myTarget;
	FieldSet campaignEmailSetup = new FieldSet();

	public void setup() throws Exception {
		FieldSet campaignData = testData.get(testName).get(0);
		campaignEmailSetup = testData.get(testName+"_emailsetup").get(0);
		FieldSet emailSetting = testData.get("env_email_setup").get(0);

		// Create a Campaign record and a Target record
		myCampaign = (CampaignRecord) sugar().campaigns.api.create(campaignData);
		myTarget = (TargetRecord) sugar().targets.api.create();

		// Login
		sugar().login();

		// Configure Email settings in Admin
		sugar().admin.setEmailServer(emailSetting);

		// Set up an Email account for Campaign
		sugar().campaigns.setupEmail(campaignEmailSetup);
	}

	/**
	 * Verify that Campaign Log is successfully created in Target DetailView
	 *
	 * @throws Exception
	 */
	@Test
	public void Targets_21517_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Target list of 'Test' type
		FieldSet targetlist = new FieldSet();
		targetlist.put("listType",campaignEmailSetup.get("listType"));
		sugar().targetlists.create(targetlist);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Assign Target to Target list
		sugar().targetlists.listView.clickRecord(1);
		StandardSubpanel targetSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetSubPanel.linkExistingRecord(myTarget);

		// Assign Target list to Campaign
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".pagination > td > table > tbody > tr > td:nth-child(1) span").click();
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set(campaignEmailSetup.get("newsletter"));
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Create an Email Marketing
		// TODO: VOOD-1028
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name = name]").set(campaignEmailSetup.get("name"));
		new VoodooControl("select", "id", "status").set(campaignEmailSetup.get("status"));
		new VoodooControl("input", "id", "date_start_date").set(campaignEmailSetup.get("date_start"));
		new VoodooControl("select", "id", "template_id").set(campaignEmailSetup.get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set(campaignEmailSetup.get("inbound_email_id"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady(120000);
		VoodooUtils.focusDefault();

		// Send Campaign mail
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

		// Click 'Send Test' link
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "send_test_button").click();
		VoodooUtils.waitForReady(60000);

		// Select Marketing message
		new VoodooControl("input", "id", "massall").click();

		// Click Send
		new VoodooControl("input", "css", "[title='Send']").click();
		VoodooUtils.waitForReady(120000);
		VoodooUtils.focusDefault();

		// Go to Target > CampaignLog Subpanel
		sugar().targets.navToListView();
		sugar().targets.listView.clickRecord(1);

		// TODO: VOOD-1344 -Need library support for 'Campaign Log' subpanel in Record view of any module
		sugar().targets.recordView.subpanels.get(sugar().emails.moduleNamePlural).scrollIntoView();
		VoodooControl campaignLogSubpanelCtrl = new VoodooControl("li", "css", "div[data-voodoo-name='CampaignLog'] li.subpanel");
		if(campaignLogSubpanelCtrl.getAttribute("class").contains(campaignEmailSetup.get("closed"))){
			new VoodooControl("div", "css", campaignLogSubpanelCtrl.getHookString() + " .subpanel-header").click();
			VoodooUtils.waitForReady();
		}

		// Verify CampaignLog
		new VoodooControl("a", "css", campaignLogSubpanelCtrl.getHookString() + " div[data-voodoo-name='subpanel-list'] div.flex-list-view-content table tr td:nth-of-type(2) a").assertContains(myCampaign.getRecordIdentifier(), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}