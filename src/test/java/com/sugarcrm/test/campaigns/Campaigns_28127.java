package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_28127 extends SugarTest {
	FieldSet systemSettingData = new FieldSet();
	DataSource emailMarketingData = new DataSource();

	public void setup() throws Exception {
		systemSettingData = testData.get(testName).get(0);
		emailMarketingData = testData.get(testName + "_email_marketings");
		FieldSet campaignType = new FieldSet();
		campaignType.put("type", systemSettingData.get("type"));
		sugar().campaigns.api.create(campaignType);
		sugar().login();
	}

	/**
	 * Verify that "Next page" link in Send Emails under Campaigns is functioning accordingly.
	 *
	 * @throws Exception
	 */
	@Test
	public void Campaigns_28127_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set System settings
		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerPage", systemSettingData.get("numberOfEntries"));
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);

		// Link the Campaign with x+1(for e.g 3) number of items in the Email Marketing subpanel.
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);

		// Create three email marketing and link with campaign record 
		// TODO: VOOD-1028
		VoodooControl marketingEmailCtrl = new VoodooControl("a", "id", "campaign_email_marketing_create_button");
		VoodooControl nameCtrl = new VoodooControl("input", "css", "input[name='name']");
		VoodooControl statusCtrl = new VoodooControl("select", "id", "status");
		VoodooControl startDateCtrl = new VoodooControl("input", "id", "date_start_date");
		VoodooControl templateCtrl = new VoodooControl("select", "id", "template_id");
		VoodooControl prospectListsCtrl = new VoodooControl("input", "id", "all_prospect_lists");
		VoodooControl fromNameCtrl = new VoodooControl("input", "id", "from_name");
		VoodooControl fromAddressCtrl = new VoodooControl("input", "id", "from_addr");
		VoodooControl marketingEmailSaveBtnCtrl = new VoodooControl("input", "css", "[title='Save']");

		for (int i = 0; i <= 2; i++) {
			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");

			marketingEmailCtrl.waitForVisible();
			marketingEmailCtrl.click();

			VoodooUtils.focusDefault();
			VoodooUtils.focusFrame("bwc-frame");

			nameCtrl.set(emailMarketingData.get(i).get("name"));
			statusCtrl.set(emailMarketingData.get(i).get("status"));
			startDateCtrl.set(emailMarketingData.get(i).get("date_start"));
			templateCtrl.set(emailMarketingData.get(i).get("template_id"));
			prospectListsCtrl.click();
			fromNameCtrl.set(emailMarketingData.get(i).get("from_name"));
			fromAddressCtrl.set(emailMarketingData.get(i).get("from_address"));
			marketingEmailSaveBtnCtrl.click();
			sugar().alerts.waitForLoadingExpiration();
		}

		// In Campaign detail view, select 'Send Emails' option form Edit dropdown
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "send_emails_button").click();

		// Verify that only two records are exist and also verifying the number of records at pagination row
		new VoodooControl("tr", "css", ".oddListRowS1:nth-child(3)").assertExists(true);
		new VoodooControl("tr", "css", ".evenListRowS1").assertExists(true);
		new VoodooControl("tr", "css", ".oddListRowS1:nth-child(5)").assertExists(false);
		new VoodooControl("span", "css", ".pageNumbers").assertEquals(systemSettingData.get("firstPageNumbers"), true);

		// Click on the next page button or last button
		new VoodooControl("button", "css", "button[name='listViewNextButton']").click();

		// Verify that it should navigate to the next page with the remaining item(s).
		new VoodooControl("tr", "css", ".oddListRowS1:nth-child(3)").assertExists(true);
		new VoodooControl("span", "css", ".pageNumbers").assertEquals(systemSettingData.get("nextPageNumbers"), true);

		// Click on the Cancel button at 'Campaign: Send Emails' page
		new VoodooControl("input", "css", "input[title='Cancel']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}