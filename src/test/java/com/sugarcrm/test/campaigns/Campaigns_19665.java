package com.sugarcrm.test.campaigns;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.InboundEmailRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Campaigns_19665 extends SugarTest {
	FieldSet campaignsRecord;
	CampaignRecord myCampaign;
	ContactRecord myContact;
	LeadRecord myLead;
	TargetRecord myTarget;
	InboundEmailRecord myInboundEmail;

	public void setup() throws Exception {
		myContact = (ContactRecord) sugar().contacts.api.create();
		myTarget = (TargetRecord) sugar().targets.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();

		campaignsRecord = testData.get(testName).get(0);

		// SMTP settings
		FieldSet emailSetupData = testData.get(testName+"_smtp_settings").get(0);

		// Set email settings in admin		
		sugar().admin.setEmailServer(emailSetupData);

		// Create inbound email account
		myInboundEmail = (InboundEmailRecord)sugar().inboundEmail.create();
	}

	/**
	 * Verify target list are empty on campaign > email marketing detail view > subscription detail view
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_19665_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Campaign
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		myCampaign.navToRecord();		
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set(campaignsRecord.get("newsletter_type"));
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#list_subpanel_prospectlists > table tr:nth-child(3) > td:nth-child(1) > span > a").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// link target record
		StandardSubpanel targetSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetSubpanel.linkExistingRecord(myTarget);

		// link lead record
		StandardSubpanel leadSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSubpanel.linkExistingRecord(myLead);

		// link contact record
		StandardSubpanel contactSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(myContact);

		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

		// Create an email marketing
		// TODO: VOOD-1028
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "campaign_email_marketing_create_button").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "name", "name").set(campaignsRecord.get("name"));
		new VoodooControl("select", "id", "status").set(campaignsRecord.get("status"));
		new VoodooControl("input", "id", "date_start_date").set(campaignsRecord.get("date_start"));
		new VoodooControl("select", "id", "template_id").set(campaignsRecord.get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("input", "id", "from_name").set(campaignsRecord.get("from_name"));
		new VoodooControl("input", "id", "from_addr").set(campaignsRecord.get("from_addr"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady(60000);

		// Click on latest created email marketing
		new VoodooControl("a", "css", "#list_subpanel_emailmarketing > table > tbody > tr.oddListRowS1 > td:nth-child(1) > span > a").click();
		VoodooUtils.waitForReady();

		// click on target list
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#list_subpanel_allprospectlists > table > tbody > tr.oddListRowS1 > td:nth-child(1) > span > a").click();
		VoodooUtils.waitForReady();

		// unlink contact and verify no record in subpanel
		contactSubpanel.unlinkRecord(1);
		Assert.assertTrue("Contact subpanel is not empty", contactSubpanel.isEmpty());

		// unlink lead and verify no record in subpanel
		leadSubpanel.unlinkRecord(1);
		Assert.assertTrue("Lead subpanel is not empty", leadSubpanel.isEmpty());

		// unlink target and verify no record in subpanel
		targetSubpanel.unlinkRecord(1);
		Assert.assertTrue("Target subpanel is not empty", targetSubpanel.isEmpty());

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}