package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19563 extends SugarTest {
	DataSource campaignsRecord;
		
	public void setup() throws Exception {
		sugar.login();
		campaignsRecord = testData.get(testName);
		
		// Create Campaign Records.
		FieldSet fs = new FieldSet();
		for(int i = 0; i < campaignsRecord.size(); i++){
			fs.put("type", campaignsRecord.get(i).get("type"));
			sugar.campaigns.api.create(fs);
		}
	}

	/**
	 * Verify that View Newsletters from campaign menu only displays newsletter type campaigns.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19563_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.campaigns.navToListView();
		sugar.navbar.clickModuleDropdown(sugar.campaigns);
		sugar.campaigns.menu.getControl("viewNewsletters").click();

		sugar.campaigns.listView.clickRecord(1); // Go to the campaign record view
		VoodooUtils.focusFrame("bwc-frame");
		
		// Define next button on campaign detailview
		VoodooControl goToNextRecord = new VoodooControl("img","css","span button:nth-child(2)");
		
		// Get string from pagination.
		String getStr = new VoodooControl("span", "css", ".paginationWrapper .pagination").getText();
		String trimStr = getStr.trim();
		// Number of total records exist.
		char totalRec = trimStr.charAt(trimStr.length()- 2);
		for (int i=0; i < totalRec-1; i++){
			sugar.campaigns.detailView.getDetailField("type").assertContains(campaignsRecord.get(0).get("assertText"), true);
			goToNextRecord.click(); // Click on the "Next" button on Campaign detail view
			}
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}