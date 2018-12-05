package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28228 extends SugarTest {
	LeadRecord myLeadRecord;

	public void setup() throws Exception {
		// Create a lead to verify FTS
		myLeadRecord = (LeadRecord) sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that Slow click on FTS results should give the result in record being accessed
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28228_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Type in a name known to be in the database. 
		sugar().navbar.getControl("globalSearch").set(myLeadRecord.getRecordIdentifier());
		VoodooUtils.waitForReady();

		// Verify that the record should be populated the display.
		VoodooControl searchResult = sugar().navbar.search.getControl("searchResults");
		searchResult.assertVisible(true);
		searchResult.assertContains(myLeadRecord.getRecordIdentifier(), true);

		// Click on the Lead record searched 
		// TODO: VOOD-1868
		new VoodooControl("a", "css", ".search-result a").click();

		// Verify that the record should be pulled up replacing the existing page. Also verifying that the search results should be disappear
		searchResult.assertVisible(false);
		sugar().leads.recordView.assertVisible(true);
		sugar().leads.recordView.getDetailField("fullName").assertContains(myLeadRecord.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}