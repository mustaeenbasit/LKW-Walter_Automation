package com.sugarcrm.test.campaigns;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19379 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
	}

	/**
	 * Campaign Wizard_Verify that the default scheduler is set up to process bounced campaign email and campaign email..
	 * 
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19379_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.navToModule(sugar.campaigns.moduleNamePlural);
		sugar.navbar.clickModuleDropdown(sugar.campaigns);
		// TODO: VOOD-1091 - Need lib support for "View Diagnostics for Campaigns"
		sugar.campaigns.menu.getControl("viewDiagnostics").click();
		VoodooUtils.focusFrame("bwc-frame");
		// Assert that The scheduler is set up to process bounced campaign email and campaign email
		new VoodooControl("td", "css", "#schedule > table > tbody > tr:nth-child(2) > td:nth-child(1)").assertEquals(ds.get(0).get("bounced_campaign_emails_message"), true);
		new VoodooControl("td", "css", "#schedule > table > tbody > tr:nth-child(3) > td:nth-child(1)").assertEquals(ds.get(0).get("campaign_emails_message"), true);
		new VoodooControl("td", "css", "#schedule > table > tbody > tr:nth-child(2) > td:nth-child(2)").assertEquals(ds.get(0).get("scheduler_status"), true);
		new VoodooControl("td", "css", "#schedule > table > tbody > tr:nth-child(3) > td:nth-child(2)").assertEquals(ds.get(0).get("scheduler_status"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}