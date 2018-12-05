package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import static org.junit.Assert.assertEquals;

public class Campaigns_delete extends SugarTest {
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		myCampaign = (CampaignRecord)sugar().campaigns.api.create();
		sugar().login();
	}

	@Test
	public void Campaigns_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the campaign using the UI.
		myCampaign.delete();

		// Verify the campaign was deleted.
		assertEquals(VoodooUtils.contains(myCampaign.getRecordIdentifier(), true), false);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}