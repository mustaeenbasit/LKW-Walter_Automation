package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.InboundEmailRecord;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Campaigns_19662 extends SugarTest {
	DataSource emailSetup;
	FieldSet emailSetupData;
	CampaignRecord myCampaign;
	InboundEmailRecord myInboundEmail;
	
	public void setup() throws Exception {
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		emailSetup = testData.get(testName+"_email_marketings");
		emailSetupData = testData.get("env_email_setup").get(0);
		sugar().login();
		
		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetupData);
		
		// Create inbound email account
		myInboundEmail = (InboundEmailRecord)sugar().inboundEmail.create();
	}

	/**
	 * Verify that all check boxes are checked on campaign send mail when select-all checkbox is checked
	 *  
	 * */
	@Test
	public void Campaigns_19662_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Edit Campaign		
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);		
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set(emailSetup.get(0).get("campaign_type"));
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		sugar().alerts.waitForLoadingExpiration();		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Create an email marketing
	   	// TODO: VOOD-1028
		for(int i = 0; i < emailSetup.size(); i++) {
			new VoodooControl("a", "id", "campaign_email_marketing_create_button").waitForVisible();
			new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "css", "input[name='name']").set(emailSetup.get(i).get("name"));
			new VoodooControl("select", "id", "status").set(emailSetup.get(i).get("status"));
			new VoodooControl("input", "id", "date_start_date").set(emailSetup.get(i).get("date_start"));
			new VoodooControl("select", "id", "template_id").set(emailSetup.get(i).get("template_id"));
			new VoodooControl("input", "id", "all_prospect_lists").click();
			new VoodooControl("input", "id", "from_name").set(emailSetup.get(i).get("from_name"));
			new VoodooControl("input", "id", "from_addr").set(emailSetup.get(i).get("from_address"));
			new VoodooControl("input", "css", "[title='Save']").click();
			VoodooUtils.waitForReady(60000);
		}
		
		// Click on "Send Emails" link 
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "send_emails_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "massall").click();
		
		// Verify main checkbox is checked
		new VoodooControl("input", "css", "input#massall[checked]").assertExists(true);		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}