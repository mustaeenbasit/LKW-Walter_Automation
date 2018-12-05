package com.sugarcrm.test.GlobalSearch;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_21620 extends SugarTest {
	LeadRecord lead;
	DataSource leadFields = new DataSource();

	VoodooControl globalSearchTextField, firstSearchResult;

	public void setup() throws Exception {
		sugar().login();
		leadFields = testData.get(testName);
		lead = (LeadRecord)sugar().leads.api.create(leadFields.get(0));

		// TODO VOOD-668
		globalSearchTextField = new VoodooControl("input", "css", ".search-query");
		firstSearchResult = new VoodooControl("li", "css", "#searchForm .typeahead.dropdown-menu li");
	}

	/**
	 * Search leads with non-existing account name 
	 * @throws Exception
	 */
	@Ignore("VOOD-1704 Voodoo needs a way of triggering cron. Also, we need to add steps which is mentioned over testopia for FTS against v7.7")
	@Test
	public void GlobalSearch_21620_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		globalSearchTextField.set(leadFields.get(0).get("accountName"));

		firstSearchResult.waitForVisible();
		firstSearchResult.assertContains(leadFields.get(0).get("accountName"), true);
		firstSearchResult.assertContains(lead.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}