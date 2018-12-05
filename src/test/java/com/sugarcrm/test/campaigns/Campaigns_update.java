package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.CampaignRecord;

public class Campaigns_update extends SugarTest {
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		myCampaign = (CampaignRecord)sugar().campaigns.api.create();
		sugar().login();
	}

	@Test
	public void Campaigns_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet newData = new FieldSet();
		newData.put("name", "Buy More Stuff!");

		// Edit the campaign using the UI.
		myCampaign.edit(newData);

		// Verify the campaign was edited.
		myCampaign.verify(newData);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}