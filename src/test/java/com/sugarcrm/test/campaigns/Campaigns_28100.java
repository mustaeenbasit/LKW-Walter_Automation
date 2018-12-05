package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_28100 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Deleting Target Lists associated to a Campaign should not create duplicate records in Campaign Summary
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_28100_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet systemSettingData = testData.get(testName).get(0);

		// Go to Campaigns -> Create Campaign (Wizard) -> Newsletter -> Start
		sugar().navbar.selectMenuItem(sugar().campaigns, "createCampaignWizard");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "startbutton").click();

		// Fill in the required fields: Name = Test1 and End Date = (any date)
		sugar().campaigns.editView.getEditField("name").set(systemSettingData.get("name"));
		sugar().campaigns.editView.getEditField("date_end").set(systemSettingData.get("date_end"));

		//  Next -> Next -> Next
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();
		nextBtnCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1072
		// Fill in Subscription List Name: a1
		new VoodooControl("input", "id", "subscription_name").set(systemSettingData.get("subscription_name"));

		// Fill in Unsubscription List Name: a2
		VoodooControl unsubscriptionFieldCtrl = new VoodooControl("input", "id", "unsubscription_name");
		unsubscriptionFieldCtrl.set(systemSettingData.get("unsubscription_name"));

		// Fill in Test List Name: a3
		new VoodooControl("input", "id", "test_name").set(systemSettingData.get("test_name"));

		// Click Finish
		VoodooControl finishCtrl = new VoodooControl("input", "id", "wiz_submit_finish_button");
		finishCtrl.click();

		// Verify the Subscription Lists, they are correct
		VoodooControl subscriptionListsCtrl = new VoodooControl("tr", "xpath", "//*[@id='campaign_targets']/table/tbody/tr[contains(.,'"+systemSettingData.get("subscription_name")+"')]");
		VoodooControl unsubscriptionListsCtrl = new VoodooControl("tr", "xpath", "//*[@id='campaign_targets']/table/tbody/tr[contains(.,'"+systemSettingData.get("unsubscription_name")+"')]");
		VoodooControl testListsCtrl = new VoodooControl("tr", "xpath", "//*[@id='campaign_targets']/table/tbody/tr[contains(.,'"+systemSettingData.get("test_name")+"')]");
		VoodooControl newUnsubscriptionListsCtrl = new VoodooControl("tr", "xpath", "//*[@id='campaign_targets']/table/tbody/tr[contains(.,'"+systemSettingData.get("unsubscription_name_new")+"')]");

		// 'a1', 'a2' and 'a3' exist in the Subscription Lists
		subscriptionListsCtrl.assertExists(true);
		unsubscriptionListsCtrl.assertExists(true);
		testListsCtrl.assertExists(true);
		VoodooUtils.focusDefault();

		// Go to Target Lists and select list a2 -> Delete -> Confirm
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.deleteRecord(2);
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();
		
		// Go to Campaigns -> click on the previous created Campaign named Test1
		sugar().campaigns.navToListView();
		sugar().campaigns.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Click on Launch Wizard
		// TODO: VOOD-1028
		new VoodooControl("input", "id", "launch_wizard_button").click();

		// Verify that there should be only two Subscription Lists: a1 and a3
		subscriptionListsCtrl.assertExists(true);
		unsubscriptionListsCtrl.assertExists(false);
		testListsCtrl.assertExists(true);

		// Go to Subscriptions
		// TODO: VOOD-1072
		new VoodooControl("a", "id", "nav_step4").click();
		VoodooUtils.focusFrame("bwc-frame"); // Need to focus on BWC frame again

		// Fill in Unsubscription List Name: a4 (the field was correctly empty)
		unsubscriptionFieldCtrl.set(systemSettingData.get("unsubscription_name_new"));

		// Click Finish
		finishCtrl.click();

		// Verify that there should be three Subscription Lists (one Subscription, one Unsubscription, one Test)
		subscriptionListsCtrl.assertExists(true);
		unsubscriptionListsCtrl.assertExists(false);
		testListsCtrl.assertExists(true);
		newUnsubscriptionListsCtrl.assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}