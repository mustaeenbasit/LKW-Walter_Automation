package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ruchi Bhatnagar <rbhatnagar@sugarcrm.com>
 */
public class GlobalSearch_20971 extends SugarTest {
	DataSource leadData = new DataSource();

	public void setup() throws Exception {
		leadData = testData.get(testName);
		DataSource accountData = testData.get(testName+"_accountData");
		sugar().leads.api.create(leadData);
		sugar().accounts.api.create(accountData);
		FieldSet fs = new FieldSet();
		fs.put("documentName", leadData.get(0).get("description"));
		sugar().documents.api.create(fs);
		sugar().login();
	}

	/**
	 * Verify user can select multiple facet criteria within multiple facet dashlets
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20971_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter search text
		sugar().navbar.getControl("globalSearch").set(leadData.get(0).get("description"));

		// Click on "View all Results"
		sugar().navbar.viewAllResults();

		// Verify results are in relevance order on the results page.
		// Verify Records having "search text" in FirstName appears in first row(last name Boost Value 1.85)
		sugar().globalSearch.getRow(1).assertContains(leadData.get(2).get("lastName"), true);

		// Verify records having "search text" in name appears in second row(Document Name: Boost Value .82)
		sugar().globalSearch.getRow(2).assertContains(leadData.get(0).get("description"), true);

		// Verify records having "search text" in description appears in third row(Decription : Boost Value .7)
		sugar().globalSearch.getRow(3).assertContains(leadData.get(0).get("lastName"), true);

		FieldSet customData = testData.get(testName+"_customData").get(0);
		// Verify counts in modules
		// Leads Module
		new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().leads.moduleNamePlural+"']").assertContains(customData.get("leadsInitialCount"), true);

		// Documents Module
		new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().documents.moduleNamePlural+"']").assertContains(customData.get("documentsInitialCount"), true);

		// Accounts Module
		new VoodooControl("li", "css", "[data-facet-criteria='"+sugar().accounts.moduleNamePlural+"']").assertContains(customData.get("accountsInitialCount"), true);

		// Click on sidecar module record.
		// TODO: VOOD-1843
		new VoodooControl("a", "css", ".nav.search-results li:nth-child(1) div span a").click();

		// Verify record displayed in sidecar
		sugar().leads.recordView.assertVisible(true);

		// Navigate to search results page again
		sugar().navbar.getControl("globalSearch").set(leadData.get(0).get("description"));
		sugar().navbar.viewAllResults();

		// Click on BWC module record
		new VoodooControl("a", "css", ".nav.search-results li:nth-child(2) div span a").click();
		VoodooUtils.waitForReady();

		// Verify record displayed in BWC
		// TODO: VOOD-1887, VOOD-1950 Uncomment L#79 when VOOD-1950 is resolved & remove L#80-#84
		// sugar().documents.detailView.getDetailField("documentName").assertEquals(leadData.get(0).get("description"), true);
		sugar().documents.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().documents.editView.getEditField("documentName").assertEquals(leadData.get(0).get("description"), true);
		VoodooUtils.focusDefault();
		sugar().documents.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}