package com.sugarcrm.test.campaigns;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19599 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Newsletter - Campaign Wizard_Verify that creating a newsletter with lists auto-created by using 
	 * "Create NewsLetter" shortcut can be canceled at "Subscriptions" step
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19599_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Define controls for Campaigns
		// TODO: VOOD-1072
		VoodooControl newsLetterTypeCtrl = new VoodooControl("input", "id", "wizardtype_nl");
		VoodooControl startBtnCtrl = new VoodooControl("input", "id", "startbutton");
		VoodooControl cancelBtnCtrl = new VoodooControl("input", "id", "wiz_cancel_button");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "wiz_next_button");
		VoodooControl moduleTitle = new VoodooControl("div", "css", ".moduleTitle");

		// Get Current Date
		Date date = new Date();
		SimpleDateFormat dateFormat  = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = dateFormat.format(date);

		// Go to Campaign module and Create Campaign Wizard (Newsletter)
		sugar.navbar.selectMenuItem(sugar.campaigns , "createCampaignWizard");
		VoodooUtils.focusFrame("bwc-frame");
		newsLetterTypeCtrl.click();
		startBtnCtrl.click();
		VoodooUtils.waitForReady();	

		// Fill in the required fields: Name = Test1 and End Date = (any date) on Campaign Header and Navigate to Campaign Subscription Page
		sugar.campaigns.editView.getEditField("name").set(testName);
		sugar.campaigns.editView.getEditField("date_end").set(currentDate);
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();

		// Click Cancel and verify if user is navigated to Campaign start page
		cancelBtnCtrl.click();
		moduleTitle.assertEquals(fs.get("moduleTitle"), true);
		newsLetterTypeCtrl.assertVisible(true);
		startBtnCtrl.assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}