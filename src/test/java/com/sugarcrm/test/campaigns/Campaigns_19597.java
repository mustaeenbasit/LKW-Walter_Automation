package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19597 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Newsletter - Campaign Wizard_Verify that creating a newsletter with lists auto-created by 
	 * using "Create NewsLetter" shortcut can be canceled at "General" step.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19597_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Define controls for Campaigns
		// TODO: VOOD-1072
		VoodooControl newsLetterTypeCtrl = new VoodooControl("input", "id", "wizardtype_nl");
		VoodooControl startBtnCtrl = new VoodooControl("input", "id", "startbutton");
		VoodooControl cancelBtnCtrl = new VoodooControl("input", "id", "wiz_cancel_button");
		VoodooControl moduleTitle = new VoodooControl("div", "css", ".moduleTitle");

		// Go to Campaign module and click Create Campaign Wizard (Newsletter)
		sugar.navbar.selectMenuItem(sugar.campaigns , "createCampaignWizard");
		VoodooUtils.focusFrame("bwc-frame");
		newsLetterTypeCtrl.click();
		startBtnCtrl.click();
		VoodooUtils.waitForReady();	

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