package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19391 extends SugarTest {
	DataSource inboundData, emailMarketingData, ds, targetListData;
	FieldSet customData;
	FieldSet emailSetupData;
	CampaignRecord myCampaign;
	TargetRecord myTarget;
	TargetListRecord myTargetListRecord;
	StandardSubpanel targetsSubPanel,targatListSubPanel;
	public void setup() throws Exception {
		sugar().login();
		
		ds = testData.get(testName);
		targetListData = testData.get(testName + "_target_list");
		
		// Create a Campaign
		myCampaign = (CampaignRecord) sugar().campaigns.api.create(ds.get(0));
		
		// create a target
		myTarget = (TargetRecord)sugar().targets.api.create();
		
		// create a target list
		myTargetListRecord = (TargetListRecord)sugar().targetlists.api.create();
		
		// smtp settings
		emailSetupData = testData.get(testName+"_smtp_settings").get(0);
		inboundData = testData.get(testName +"_inbound_email");
		emailMarketingData = testData.get(testName + "_email_marketings");
		
		// configure email settings in admin panel
		sugar().admin.setEmailServer(emailSetupData);
		
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("inboundEmail").click();
		sugar().navbar.selectMenuItem(sugar().inboundEmail, "newBounceMailAccount");
		
		// create bounce handling account
		// TODO: VOOD-1082 (Lib support needed to create bounce handling account)
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "name").set(inboundData.get(0).get("name"));
		new VoodooControl("input", "id", "email_user").set(inboundData.get(0).get("email"));
		new VoodooControl("input", "id", "email_password").set(inboundData.get(0).get("password"));
		new VoodooControl("input", "id", "from_addr").set(inboundData.get(0).get("email"));
		// save settings
		new VoodooSelect("input", "id", "button").click();
		VoodooUtils.pause(16000);
		VoodooUtils.focusDefault(); 
	}

	/**
	 * Send Test_Verify that test can sent from email-based campaign.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19391_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// navigate to target module record view 
		myTarget.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().targets.recordView.edit();
		
		// add an email account in target record
		new VoodooControl("input", "css", "[placeholder='Add email']").set(inboundData.get(0).get("email"));
		sugar().targets.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// navigate to target list module record view 
		myTargetListRecord.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().targetlists.recordView.edit();
		
		// add a list type in target list record 
		sugar().targetlists.recordView.getEditField("listType").set(targetListData.get(0).get("listType"));
		sugar().targetlists.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// link existing target record to target list record 
		targetsSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetsSubPanel.linkExistingRecord(myTarget);
	
		// navigate to campaign record 
		myCampaign.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		
		// link existing target list record to campaign record 
		// TODO: VOOD-1028
		new VoodooControl("span", "css", "#list_subpanel_prospectlists > table > tbody > tr.pagination > td > table > tbody > tr > td:nth-child(1) > ul > li > span").click();
		new VoodooSelect("a", "id", "prospect_list_campaigns_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "css", "#MassUpdate > table.list.view > tbody > tr:nth-child(3) > td:nth-child(3) > a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Create an email marketing and link with campaign record 
	   	// TODO: VOOD-1028
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").waitForVisible();
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("input", "css", "input[name='name']").set(emailMarketingData.get(0).get("name"));
		new VoodooControl("select", "id", "status").set(emailMarketingData.get(0).get("status"));
		new VoodooControl("input", "id", "date_start_date").set(emailMarketingData.get(0).get("date_start"));
		new VoodooControl("select", "id", "template_id").set(emailMarketingData.get(0).get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("input", "id", "from_name").set(emailMarketingData.get(0).get("from_name"));
		new VoodooControl("input", "id", "from_addr").set(emailMarketingData.get(0).get("from_address"));
		new VoodooControl("select", "id", "inbound_email_id").set(inboundData.get(0).get("name"));
		new VoodooControl("input", "css", "[title='Save']").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// click on "Send Test" link 
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "send_test_button").click();

		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		new VoodooControl("input", "id", "massall").click();
		new VoodooControl("input", "css", "#MassUpdate [title='Send']").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Assert Campaign status page is displayed, and the tests are sent out
		new VoodooControl("span", "css", "#list_subpanel_targeted tr.oddListRowS1 > td:nth-child(4) > span").assertContains(targetListData.get(0).get("activity_type"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}