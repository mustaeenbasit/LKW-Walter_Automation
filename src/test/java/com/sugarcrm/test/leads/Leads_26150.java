package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;

public class Leads_26150 extends SugarTest {
	LeadRecord myLead;

	public void setup() throws Exception {
		sugar().login();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Verify Navigation and display of the Leads Preview pane
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_26150_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.previewRecord(1);
		myLead.verifyPreview();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
