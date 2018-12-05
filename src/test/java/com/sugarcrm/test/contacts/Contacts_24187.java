package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_24187 extends SugarTest {
	FieldSet customData = new FieldSet(), emailMarketing = new FieldSet();
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
		emailMarketing = testData.get(testName+"_marketing").get(0);

		// Set email settings in admin
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().admin.setEmailServer(emailSetup);

		// Create contact with email address.
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", customData.get("email"));
		myContact = (ContactRecord) sugar().contacts.api.create(fs);

		// TODO: VOOD-1044	Need lib support for "Email Setup for Campaigns"
		// Set up an email account for campaign
		sugar().campaigns.navToListView();
		sugar().navbar.selectMenuItem(sugar().campaigns, "setUpEmail");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl nextButton = new VoodooControl("input", "css", "#wiz_next_button");
		nextButton.click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "id", "ssl").click();
		new VoodooControl("select", "id", "protocol").set(customData.get("protocol"));
		VoodooUtils.pause(2000); // TODO: TR-8001 Studio Buttons remain disabled longer than intended
		nextButton.click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", "#wiz_submit_button").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Campaign log_Verify that the Campaign Log record is displayed in "Campaign Log" sub-panel of
	 * "Contact Detail View" page.
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_24187_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		TargetListRecord myTargetList = (TargetListRecord) sugar().targetlists.api.create();
		myTargetList.navToRecord();
		StandardSubpanel contactSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(myContact);

		FieldSet newData = new FieldSet();
		newData.put("type", customData.get("type"));
		sugar().campaigns.api.create(newData);

		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

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
		new VoodooControl("input", "css", "[name = name]").set(emailMarketing.get("name"));
		new VoodooControl("select", "id", "status").set(emailMarketing.get("status"));
		new VoodooControl("input", "id", "date_start_date").set(emailMarketing.get("date_start"));
		new VoodooControl("select", "id", "template_id").set(emailMarketing.get("template_id"));
		new VoodooControl("input", "id", "all_prospect_lists").click();
		new VoodooControl("select", "id", "inbound_email_id").set(emailMarketing.get("inbound_email_id"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.focusDefault();

		// Send marketing email
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.openPrimaryButtonDropdown();

		// TODO: VOOD-1072
		new VoodooControl("a", "id", "send_emails_button").click();
		new VoodooControl("input", "css", "#MassUpdate .oddListRowS1 input").click();
		new VoodooControl("input", "css", "[title='Send']").click();
		VoodooUtils.focusDefault();

		// Go to Admin > Email Queue. Select the campaign record then click "Send Queued Campaign Emails" button.
		sugar().admin.navToAdminPanelLink("emailQueue");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#MassUpdate tr.oddListRowS1 td.nowrap input").click();
		new VoodooControl("input", "css", "#form input.button").click();
		VoodooUtils.focusDefault();

		// Verify Campaign Log record is displayed in "Campaign" sub-panel
		myContact.navToRecord();

		// TODO: VOOD-1344
		new VoodooControl("div", "css", ".layout_CampaignLog").click();
		new VoodooControl("span", "css" ,"div.layout_CampaignLog span.fld_campaign_name1.list").assertEquals(sugar().campaigns.defaultData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}