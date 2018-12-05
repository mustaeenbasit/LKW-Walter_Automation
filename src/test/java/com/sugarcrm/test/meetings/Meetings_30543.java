package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_30543 extends SugarTest {
	DataSource meetingsData = new DataSource();

	public void setup() throws Exception {
		// Login as Admin
		sugar().login();

		// Creating meeting records with custom data from UI to populate data 
		// in created by and modified by field in GlobalSearch results
		meetingsData = testData.get(testName);
		sugar().meetings.create(meetingsData);
		if (sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}
	}

	/**
	 * Verify that correct results appear when global searching in Meetings
	 * @throws Exception
	 */
	@Test
	public void Meetings_30543_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enter "Discuss" in global search window
		sugar().navbar.setGlobalSearch(meetingsData.get(0).get("name").substring(0, 7));

		// Observe the result list
		// TODO: VOOD-1868 - Support Global search typeahead results
		VoodooControl searchResultsCtrl = new VoodooControl("li", "css", ".search-results li.search-result:nth-child(");
		String nameCtrl = ") div h3 strong";
		String locationCtrl = ") div:nth-child(2) span:nth-child(2) strong";
		String descriptionCtrl = ") div:nth-child(2) span:nth-child(4) strong";

		// Verify that Name,Location,Description fields gets highlighted in global search dropdown
		// TODO: VOOD-1951 - Need lib support to assert bold string.
		int count = meetingsData.size();
		for (int i = 1; i < count; i++) {
			new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + i + nameCtrl).assertEquals(meetingsData.get(i - 1).get("name"), true);
			new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + i + locationCtrl).assertEquals(meetingsData.get(i - 1).get("location"), true);
			new VoodooControl("strong", "css", searchResultsCtrl.getHookString() + i + descriptionCtrl).assertEquals(meetingsData.get(i - 1).get("description"), true);
		}

		// Click on "View all results"
		sugar().navbar.viewAllResults();

		// Verify that Name,Location,Description fields gets highlighted in global search result window
		VoodooControl allSearchResultsCtrl = new VoodooControl("li", "css", "ul.nav.search-results .search-result:nth-child(");
		for (int i = 1; i <= count; i++) {
			new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + i + nameCtrl).assertEquals(meetingsData.get(i - 1).get("name"), true);
			new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + i + locationCtrl).assertEquals(meetingsData.get(i - 1).get("location"), true);
			new VoodooControl("strong", "css", allSearchResultsCtrl.getHookString() + i + descriptionCtrl).assertEquals(meetingsData.get(i - 1).get("description"), true);
		}

		// Verify Start date and End date fields are present in global search results
		String startDate = sugar().meetings.getDefaultData().get("date_start_date");
		String endDate = sugar().meetings.getDefaultData().get("date_end_date");
		for (int i = 1; i <= count; i++) {
			sugar().globalSearch.getRow(i).assertContains(startDate, true);
			sugar().globalSearch.getRow(i).assertContains(endDate, true);
		}

		// Verify the values on RHS in Created by, Modified by and Module record count facets   
		// TODO: VOOD-1848 - Need library support to check enable/disable property of preview button and RHS pane support on Global search results page
		FieldSet facetData = testData.get(testName + "_facet").get(0);
		new VoodooControl("span", "css", ".dashlet-cell.rows li:nth-child(3) li span").assertEquals(facetData.get("created"), true);
		new VoodooControl("span", "css", ".dashlet-cell.rows li:nth-child(4) li span").assertEquals(facetData.get("modified"), true);
		new VoodooControl("span", "css", ".dashlet-cell.rows li:nth-child(5) li span").assertEquals(facetData.get("moduleCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}