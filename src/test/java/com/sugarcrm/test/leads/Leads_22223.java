package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22223 extends SugarTest {
	CampaignRecord campaignRecord;
	LeadRecord leadRecord;
	TargetListRecord targetListRecord;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		FieldSet fs = testData.get(testName).get(0);
		customData = testData.get(testName+"_email").get(0);
		campaignRecord = (CampaignRecord) sugar().campaigns.api.create(fs);
		leadRecord = (LeadRecord)sugar().leads.api.create();
		targetListRecord = (TargetListRecord)sugar().targetlists.api.create();

		sugar().login();

		// configure emails settings in admin panel
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSetup);

		// Set up an email account for campaign
		emailSetup.put("protocol", "IMAP");
		sugar().campaigns.setupEmail(emailSetup);
	}

	/**
	 * Verify that lead cannot receive email from sugar when lead is created with the "Email Opt Out" check box checked.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22223_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		leadRecord.navToRecord();
		sugar().leads.recordView.edit();

		// TODO: VOOD-896  
		new VoodooControl("input", "css", ".newEmail.input-append").set(customData.get("emailAddress"));
		new VoodooControl("a", "css", ".btn.addEmail").click();
		new VoodooControl("id", "css", ".btn.btn-edit[data-emailproperty='opt_out']").click();
		sugar().leads.recordView.save();

		// link lead record to target list record
		targetListRecord.navToRecord();
		StandardSubpanel leadSubPanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubPanel.linkExistingRecord(leadRecord);

		campaignRecord.navToRecord();

		// Link Existing Record to select the target list.
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "css", ".pagination td table tbody tr td span").click();
		new VoodooControl("a", "id","prospect_list_campaigns_select_button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("input", "id", "MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		// Create an email marketing
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='name']").set(customData.get("name"));
		new VoodooControl("select", "id", "status").set(customData.get("status"));
		new VoodooControl("input", "id", "date_start_date").set(customData.get("date_start"));
		new VoodooControl("select", "id", "template_id").set(customData.get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set(customData.get("inbound_email_id"));
		new VoodooControl("input", "css", "[title='Save']").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		// click  "Send Emails" link 
		// TODO: VOOD-1072
		new VoodooControl("a", "id", "send_emails_button").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#MassUpdate .oddListRowS1 input").click();
		new VoodooControl("input", "css", "[title='Send']").click();
		VoodooUtils.focusDefault();

		// Go to Admin Page > Email Queue link
		sugar().admin.navToAdminPanelLink("emailQueue");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1085
		// Click "Send Queued Campaign Emails" button. 
		new VoodooControl("input", "css", "#form > table > tbody > tr > td > input.button").click();

		// All BWC modules need to make sure that focusDefault() is selected before navToRecord
		VoodooUtils.focusDefault();
		campaignRecord.navToRecord();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1072
		// click on View Status button on campaigns record view page
		new VoodooControl("input", "id", "view_status_button").click();
		VoodooUtils.focusDefault();

		VoodooUtils.focusFrame("bwc-frame");
		// assert optout email  on View Status page of campaigns record 
		new VoodooControl("span", "css", "#list_subpanel_removed > table > tbody > tr.oddListRowS1 > td:nth-child(2) > span").assertEquals(customData.get("emailAddress"), true);
		new VoodooControl("span", "css", "#list_subpanel_removed > table > tbody > tr.oddListRowS1 > td:nth-child(4) > span").assertEquals(customData.get("email_optout"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}