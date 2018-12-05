package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19441 extends SugarTest {
	FieldSet campaignRecord = new FieldSet();
	FieldSet testRecord = new FieldSet();

	public void setup() throws Exception {
		testRecord = testData.get(testName).get(0);
		campaignRecord = sugar().campaigns.getDefaultData();
		campaignRecord.put("type", testRecord.get("type"));	
		sugar().campaigns.api.create(campaignRecord);
		sugar().login();
	}

	/**
	 * Email Marketing management_Verify that "Cancel" function in the create email marketing view works correctly.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19441_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click Campaigns tab in the top navigation bar.
		sugar().navbar.navToModule(sugar().campaigns.moduleNamePlural);
		// Go to a campaign's detail view.
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-1028
		VoodooControl emailMrktCreateCtrl = new VoodooControl("a", "id","campaign_email_marketing_create_button");
		// Click "Create" button in the "Email Marketing" sub-panel.
		emailMrktCreateCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl cancelButton = new VoodooControl("input", "css", "[title='Cancel']");
		// Click "Cancel" button in the create email marketing view.
		cancelButton.click();
		VoodooUtils.waitForReady();
		// Verify that selected campaign detail view is displayed correctly.
		sugar().campaigns.detailView.getDetailField("name").assertEquals(campaignRecord.get("name"), true);
		// TODO: VOOD-972
		// verify that no email marketing created
		new VoodooControl("tr", "css", "#list_subpanel_emailmarketing .list.view .oddListRowS1").assertContains(testRecord.get("data_string"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}