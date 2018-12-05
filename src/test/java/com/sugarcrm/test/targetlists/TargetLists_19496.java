package com.sugarcrm.test.targetlists;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19496 extends SugarTest{
	TargetListRecord myTargetListRecord;
	LeadRecord myLeadRecord;

	public void setup() throws Exception {
		myTargetListRecord = (TargetListRecord)sugar().targetlists.api.create();
		myLeadRecord = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Target List - Leads management_Verify that "Select" leads function in the "Leads" sub-panel works correctly.
	 * 
	 */
	@Test
	public void TargetLists_19496_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myTargetListRecord.navToRecord();
		StandardSubpanel leadsSubPanel = (StandardSubpanel)sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubPanel.linkExistingRecord(myLeadRecord);
		VoodooUtils.waitForReady();
		// TODO: VOOD-1424: Make StandardSubpanel.verify() verify specified value is in correct column.
		leadsSubPanel.getDetailField(1, "fullName").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
