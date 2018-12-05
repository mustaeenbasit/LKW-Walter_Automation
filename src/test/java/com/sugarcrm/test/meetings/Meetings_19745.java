package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19745 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().meetings.api.create(ds);
		sugar().login();
	}

	/**
	 * Search meeting_Verify that all the meeting records are displayed in "Meeting List" view when clicking "Clear" button.
	 * @throws Exception
	 */
	@Test
	public void Meetings_19745_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.setSearchString(ds.get(0).get("name"));

		// Verify meeting record with search condition
		sugar().meetings.listView.verifyField(1, "name", ds.get(0).get("name"));
		sugar().meetings.listView.clearSearch();

		// Verify all meeting records back with clear search
		sugar().meetings.listView.verifyField(1, "name", ds.get(2).get("name"));
		sugar().meetings.listView.verifyField(2, "name", ds.get(1).get("name"));
		sugar().meetings.listView.verifyField(3, "name", ds.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}