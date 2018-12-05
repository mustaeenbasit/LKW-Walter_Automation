package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19440 extends SugarTest {
	FieldSet emailTemplateData;

	public void setup() throws Exception {
		emailTemplateData = testData.get(testName).get(0);
		FieldSet campaignData = new FieldSet();
		campaignData.put("type", emailTemplateData.get("type"));
		sugar.campaigns.api.create(campaignData);
		sugar.login();

		// Clicking Email Template from Campaign Dropdown
		sugar.navbar.selectMenuItem(sugar.campaigns, "createEmailTemplate");
		VoodooUtils.focusFrame("bwc-frame");

		// Create an email marketing
		// TODO: VOOD-1028
		new VoodooControl("input", "css", "[name = name]").set(emailTemplateData.get("name"));
		new VoodooControl("select", "css", "[name='type']").set(emailTemplateData.get("emailType"));
		new VoodooControl("input", "id", "description").set(emailTemplateData.get("description"));
		new VoodooControl("button", "id", "remove_team_name_collection_0").click();
		new VoodooControl("input", "id", "EditView_team_name_collection_0").set(emailTemplateData.get("teamName"));
		new VoodooControl("button", "id", "primary_team_name_collection_0").click();
		new VoodooControl("input", "id", "subjectfield").set(emailTemplateData.get("subject"));
		new VoodooControl("select", "id", "toggle_textonly").click();
		new VoodooControl("input", "id", "body_text_plain").set(emailTemplateData.get("bodyText"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Email Marketing management_Verify email template is following the team access control, during email marketing creation
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19440_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1028
		VoodooControl emailMrktCreateCtrl = new VoodooControl("a", "id","campaign_email_marketing_create_button");
		emailMrktCreateCtrl.click();

		// Verify that Email Template is shown
		// TODO: VOOD-1028
		VoodooControl emailTemplateCtrl = new VoodooControl("select", "id", "template_id");
		emailTemplateCtrl.assertContains(emailTemplateData.get("name"), true);
		new VoodooControl("input", "css", "[title='Cancel']").click();
		VoodooUtils.focusDefault();
		sugar.logout();

		// Logging through the QAuser
		sugar.login(sugar.users.getQAUser());
		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		emailMrktCreateCtrl.click();

		// Verifying that Email Template is not shown to the QAuser
		emailTemplateCtrl.assertContains(emailTemplateData.get("name"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}