package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vishal Kumar <vkumar@sugarcrm.com>
 */
public class Campaigns_19396 extends SugarTest {

	public void setup() throws Exception {
		sugar.campaigns.api.create();
		sugar.login();
	}

	/**
	 * Search campaign_Verify that "Clear" function in the "Campaign Search" sub-panel works.
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19396_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to campaign module
		sugar.campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Enter the campaign name in search field
		VoodooControl campaignSearchCtrl =sugar.campaigns.listView.getControl("nameBasic");
		campaignSearchCtrl.set(sugar.campaigns.defaultData.get("name"));
		VoodooUtils.focusDefault();
		
		// Verifying text is cleared after click on clear button
		sugar.campaigns.listView.clearSearchForm();
		VoodooUtils.focusFrame("bwc-frame");
		campaignSearchCtrl.assertEquals("", true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}