package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Admin_26399 extends SugarTest {
	CampaignRecord myCompaign;
	
	public void setup() throws Exception {
		sugar().login();
		myCompaign = (CampaignRecord)sugar().campaigns.api.create();
	}

	/**
	 *  Verify mail merge option is removed
	 */
	@Test
	public void Admin_26399_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("systemSettings").click();
		new VoodooControl("input", "css", "input[name='system_mailmerge_on']").assertExists(false);
		
		// All BWC modules need to make sure that focusDefault() is selected before navToRecord
		VoodooUtils.focusDefault();
		myCompaign.navToRecord();
				
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "mail_merge_button").assertExists(false);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}