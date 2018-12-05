package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21077 extends SugarTest {
	DataSource meetingsDS;

	public void setup() throws Exception {
		meetingsDS = testData.get(testName);
		sugar().meetings.api.create(meetingsDS);
		sugar().login();
	}
	/**
	 * Search meeting_Verify that searching with large number of meetings works correctly.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21077_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();

		// Asserting record count in List View vs Data Source
		int count = sugar().meetings.listView.countRows();
		Assert.assertTrue("The record count is diffrent in List View and Data Source ", count == meetingsDS.size()); 

		// Searching a single record in List View 
		String searchString = meetingsDS.get(5).get("name").substring(0, 3);
		sugar().meetings.listView.setSearchString(searchString);
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(5).get("name"));
		sugar().meetings.listView.clearSearch();

		// Searching multiple records in List View 
		searchString = meetingsDS.get(8).get("name").substring(0, 3);
		sugar().meetings.listView.setSearchString(searchString);

		// Sorting the records in list view in ascending order to get the correct sort order based on subject
		sugar().meetings.listView.sortBy("headerName", true);
		sugar().meetings.listView.verifyField(1, "name", meetingsDS.get(8).get("name"));
		sugar().meetings.listView.verifyField(2, "name", meetingsDS.get(9).get("name"));
		sugar().meetings.listView.clearSearch();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}