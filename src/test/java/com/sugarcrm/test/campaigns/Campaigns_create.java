package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Campaigns_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Campaigns_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		CampaignRecord myCampaign = (CampaignRecord)sugar().campaigns.create();
		myCampaign.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}