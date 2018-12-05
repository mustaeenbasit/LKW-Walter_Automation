package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Campaigns_19393 extends SugarTest {
	DataSource campaignsData = new DataSource();
	
	public void setup() throws Exception {
		campaignsData = testData.get(testName);
		sugar.campaigns.api.create(campaignsData);
		sugar.login();
	}

	/**
	 * Search campaign_Verify that basic search function in the "Campaign Search" sub-panel
	 * works correctly. (Name)
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19393_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Navigate to campaign module
		sugar.campaigns.navToListView();
		
		// Entering a campaign name's first letter in search field
		sugar.campaigns.listView.basicSearch(Character.toString(campaignsData.get(1).get("name").charAt(0)));
		
		// Verifying that the searched record is displayed on the list view page
		sugar.campaigns.listView.verifyField(1, "name", campaignsData.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}