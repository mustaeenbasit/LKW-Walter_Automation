package com.sugarcrm.test.campaigns;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooDate;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19395 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.campaigns.api.create(ds);
		sugar.login();
	}

	/**
	 * Search campaign_Verification of Advanced Search function on the Campaign Page
	 * @throws Exception
	 */
	@Test
	public void Campaigns_19395_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to campaign module
		sugar.campaigns.navToListView();

		// Click Clear for basic search
		sugar.campaigns.listView.clearSearchForm();

		// Click on advance search
		VoodooUtils.focusFrame("bwc-frame");
		sugar.campaigns.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// Perform advanced search based on campaigns type
		// TODO: VOOD-975
		new VoodooControl("option", "css", "#campaign_type_advanced [value='Email']").click();

		// Click the search button
		VoodooControl advancedSearchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		advancedSearchBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assert the search result
		sugar.campaigns.listView.verifyField(1, "type", ds.get(2).get("type"));
		sugar.campaigns.listView.verifyField(1, "name", ds.get(2).get("name"));

		// Clear the advanced search window
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl clearAdvancedSearch = new VoodooControl("a", "css", "#search_form_clear_advanced");
		clearAdvancedSearch.click();

		// Perform advanced search based on End date
		new VoodooDate("input", "id", "range_end_date_advanced").set(ds.get(1).get("date_end"));
		advancedSearchBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Assert the search result
		sugar.campaigns.listView.verifyField(1, "type", ds.get(1).get("type"));
		sugar.campaigns.listView.verifyField(1, "date_end", ds.get(1).get("date_end"));
		sugar.campaigns.listView.verifyField(1, "name", ds.get(1).get("name"));

		// Clear the advanced search window
		VoodooUtils.focusFrame("bwc-frame");
		clearAdvancedSearch.click();

		// Perform advanced search having no matching records
		new VoodooControl("input", "id", "name_advanced").set(testName);
		advancedSearchBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Assert the search result
		int rowCount = sugar.campaigns.listView.countRows();
		Assert.assertTrue("Records with un-matching search is present in the list view", rowCount == 0);

		// Clear the advanced search window
		clearAdvancedSearch.click();
		advancedSearchBtnCtrl.click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}