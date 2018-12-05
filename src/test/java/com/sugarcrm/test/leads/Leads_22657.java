package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22657 extends SugarTest {
	LeadRecord myLead;
	CallRecord myCall;

	public void setup() throws Exception {
		myLead = (LeadRecord)sugar().leads.api.create();
		myCall = (CallRecord)sugar().calls.api.create();
		sugar().login();

		// TODO: VOOD-444 - Once resolved dependencies should be via API
		// Relate Call to the Lead
		myCall.navToRecord();
		sugar().calls.recordView.edit();
		sugar().calls.recordView.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		sugar().calls.recordView.getEditField("relatedToParentName").set(myLead.getRecordIdentifier());
		sugar().calls.recordView.save();
	}

	/** 
	 * Verify that scheduled Call can be removed from Lead detail view	
	 *	@throws Exception
	 */
	@Test
	public void Leads_22657_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myLead.navToRecord();

		// Unlink Call from Leads
		StandardSubpanel callsSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.expandSubpanel();
		callsSubpanel.unlinkRecord(1);

		// Verify Call unlinked only from Leads, not actually delete record
		Assert.assertTrue("Number of rows did not equal zero.", callsSubpanel.countRows() == 0);

		// Verify Call record
		myCall.navToRecord();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}