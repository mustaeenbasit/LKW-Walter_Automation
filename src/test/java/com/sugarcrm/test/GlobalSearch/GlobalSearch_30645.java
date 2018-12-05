package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_30645 extends SugarTest {
	DataSource leadsData = new DataSource();

	public void setup() throws Exception {
		leadsData = testData.get(testName);

		// Login as QAUser
		sugar().login();

		// Create few leads records, Each one has an email address, part of the email address contains "phone"
		// TODO: VOOD-444
		sugar().leads.create(leadsData);
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that whole Email address are highlighted
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_30645_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter "phone" in global search window
		sugar().navbar.setGlobalSearch(leadsData.get(0).get("emailAddress").substring(8, 13));

		// Observe the result list
		// TODO: VOOD-1868
		VoodooControl searchResultsCtrl = new VoodooControl("li", "css", ".search-results li.search-result");
		VoodooControl emailCtrl = new VoodooControl("strong", "css", " .secondary strong");

		// Verify that the whole email address is hit and highlighted
		// TODO: VOOD-1951
		// Also verifying that the searched string is bold
		new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + emailCtrl.getHookString()).assertEquals(leadsData.get(5).get("emailAddress"), true);
		new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + ":nth-child(2)" + emailCtrl.getHookString()).assertEquals(leadsData.get(0).get("emailAddress"), true);
		new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + ":nth-child(3)" + emailCtrl.getHookString()).assertEquals(leadsData.get(1).get("emailAddress"), true);
		new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + ":nth-child(4)" + emailCtrl.getHookString()).assertEquals(leadsData.get(2).get("emailAddress"), true);
		new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + ":nth-child(5)" + emailCtrl.getHookString()).assertEquals(leadsData.get(3).get("emailAddress"), true);

		// Click on "View all results"
		sugar().navbar.viewAllResults();

		// Click on Leads option and Modified by Me on RHS
		// TODO: VOOD-1848
		new VoodooControl("li", "css", ".dashlet-row .dashlet-cell .dashlet-container:nth-child(4) .facet-results li").click();
		VoodooUtils.waitForReady();

		VoodooControl allSearchResultsCtrl = new VoodooControl("li", "css", ".layout_simple .search-results .search-result");
		// Verify that the whole email address is hit and highlighted
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + emailCtrl.getHookString()).assertEquals(leadsData.get(5).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(2)" + emailCtrl.getHookString()).assertEquals(leadsData.get(0).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(3)" + emailCtrl.getHookString()).assertEquals(leadsData.get(1).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(4)" + emailCtrl.getHookString()).assertEquals(leadsData.get(2).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(5)" + emailCtrl.getHookString()).assertEquals(leadsData.get(3).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(6)" + emailCtrl.getHookString()).assertEquals(leadsData.get(4).get("emailAddress"), true);
		new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + ":nth-child(7)" + emailCtrl.getHookString()).assertEquals(leadsData.get(6).get("emailAddress"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}