package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class StandardSubpanelCampaign extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myLead.navToRecord();

		StandardSubpanel campaignLogSub = sugar().leads.recordView.subpanels.get(sugar().campaigns.moduleNamePlural);
		campaignLogSub.toggleSubpanel();

		// TODO: VOOD-1344
		new VoodooControl("div", "css", campaignLogSub.getHookString() + " .block-footer").assertElementContains("No data available.", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}