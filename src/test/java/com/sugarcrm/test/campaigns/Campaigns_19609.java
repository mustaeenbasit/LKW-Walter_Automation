package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Campaigns_19609 extends SugarTest {
	FieldSet userSettings, campData;
	
	public void setup() throws Exception {
		campData = testData.get(testName).get(0);
		userSettings = new FieldSet();
		userSettings.put("advanced_dateFormat", campData.get("dateFormat"));
		sugar.login();
	}

	/*
	 * Newsletter - Campaign Wizard_Verify that the Start Date & End Date of 
	 * campaign is saved as created after changing the default Date Format.
	*/
	@Test
	public void Campaigns_19609_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Changing the date format of the user to format (2010-12-23)
		sugar.users.setPrefs(userSettings);
		
		FieldSet campaignDataFS = new FieldSet();
		campaignDataFS.put("type", campData.get("type"));
		campaignDataFS.put("date_start", campData.get("startDate"));
		campaignDataFS.put("date_end", campData.get("endDate"));
		
		// Create Newsletter record
		sugar.campaigns.create(campaignDataFS);	
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verifying that the Start and End dates created are displayed in specified format
		sugar.campaigns.detailView.getDetailField("date_start").assertEquals(campData.get("startDate"), true);
		sugar.campaigns.detailView.getDetailField("date_end").assertEquals(campData.get("endDate"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}