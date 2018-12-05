package com.sugarcrm.test.calls;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.test.SugarTest;

public class Calls_30544 extends SugarTest {
	ArrayList<Record> records = new ArrayList<Record>();

	public void setup() throws Exception {
		DataSource customDS = testData.get(testName+"_data");

		// Create four Calls and four Meetings records
		records.addAll(sugar().calls.api.create(customDS));
		FieldSet fs = new FieldSet();
		fs.put("location", customDS.get(1).get("name"));
		customDS.add(fs);
		records.addAll(sugar().meetings.api.create(customDS));
		sugar().login();
	}

	/**
	 * Verify that correct results appear when global searching in Calls
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_30544_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// In top right searching window, type "project"
		sugar().navbar.getControl("globalSearch").set(customFS.get("searchKey"));
		VoodooUtils.waitForReady();

		// Verify that The searched result list drop down appear. The string of "project" is highlighted in 3 fields-Subject, Location, Description.
		sugar().navbar.search.getControl("searchResults").assertExists(true);
		VoodooControl searchResDropDown = sugar().navbar.search.getControl("searchResults");
		searchResDropDown.assertContains(customFS.get("descField"), true);
		searchResDropDown.assertContains(customFS.get("searchKey"), true);
		// TODO: VOOD-1951
		// Also verifying that the searched string "Project" is bold
		new VoodooControl("strong", "css", "ul.dropdown-menu.search-results li .primary strong").assertEquals(customFS.get("searchKey"), true);

		// Click on "View all results" link.
		sugar().navbar.search.getControl("viewAllResults").click();
		VoodooUtils.waitForReady();

		// All the results are listing. You will see the string of "project" is highlighted in 3 fields - Subject, Location, Description.  Beside of above 3 field, you can see Start Date and End Date, Status and etc. 
		for (FieldSet fs : records) {
			// TODO: VOOD-1951
			// Also verifying that the searched string "Project" is bold
			new VoodooControl("strong", "css", "ul.nav.search-results li .primary a strong").assertEquals(customFS.get("searchKey"), true);
			sugar().globalSearch.assertContains(customFS.get("locationField"), true);
			sugar().globalSearch.assertContains(customFS.get("descField"), true);
			sugar().globalSearch.assertContains(customFS.get("sDateField"), true);
			sugar().globalSearch.assertContains(customFS.get("eDateField"), true);
			sugar().globalSearch.assertContains(fs.get("name"), true);
		}

		// At RHS "Filter", you also see same numbers of Meeting records at Modules session, as well as "Created by Me" and "Modified by Me"
		// TODO: VOOD-1848
		int totalRecord = records.size();
		new VoodooControl("span", "css", "[data-facet-criteria='assigned_user_id'] span").assertContains(""+totalRecord+"", true);
		new VoodooControl("span", "css", "[data-facet-criteria='Meetings'] span").assertContains(customFS.get("totalMeetingCount"), true);
		new VoodooControl("span", "css", "[data-facet-criteria='Calls'] span").assertContains(customFS.get("totalCallsCount"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}