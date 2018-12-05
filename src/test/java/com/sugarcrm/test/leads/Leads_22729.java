package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_22729 extends SugarTest {
	LeadRecord lead;

	public void setup() throws Exception {
		lead = (LeadRecord)sugar().leads.api.create();
		sugar().leads.api.create();
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * 22729 Verify that lead can be canceled merging with records which are selected in leads list searched by "Filter Condition" from the detail view of the lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_22729_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		//  Go to "Leads" module -> record view of a lead
		lead.navToRecord();

		// TODO: VOOD-695, VOOD-738, VOOD-691, VOOD-578
		// Click Find Duplicates
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_find_duplicates_button a").click();

		// TODO: VOOD-513, VOOD-653, VOOD-681
		// Search for the Leads with the same name
		new VoodooControl("input", "css", "input.search-name[placeholder='Search by first name, last name...']").set(lead.getRecordIdentifier());
		sugar().alerts.waitForLoadingExpiration();

		// Select all and click Merge Duplicates 
		new VoodooControl("input", "css", ".toggle-all").set("true");
		VoodooControl mergeDuplicateCtrl = new VoodooControl("a", "name", "merge_duplicates_button");
		mergeDuplicateCtrl.click();
		sugar().alerts.cancelAllAlerts();

		// Click Cancel
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='cancel_button']").click();

		// Verify it returns back to "Find Duplicate" page
		new VoodooControl("span", "css", ".fld_title.find-duplicates-headerpane").assertContains("Find Duplicates", true);
		mergeDuplicateCtrl.assertExists(true);

		// Click cancel again to return to record view
		new VoodooControl("a", "css", ".find-duplicates-headerpane a[name='cancel_button']").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}