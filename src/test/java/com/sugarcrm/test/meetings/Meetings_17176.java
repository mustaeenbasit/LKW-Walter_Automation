package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_17176 extends SugarTest {
	DataSource meetingData = new DataSource();
	int meetingDataSourceSize = 0;

	public void setup() throws Exception {
		meetingData = testData.get(testName);
		meetingDataSourceSize = meetingData.size();

		// Login as an Admin
		sugar().login();

		// Create 4 meetings 
		for(int i = 0; i < meetingDataSourceSize; i++) {
			sugar().meetings.api.create(meetingData.get(i));
		}		

		// Navigate to Meetings list view
		sugar().meetings.navToListView();
	}

	/**
	 * Recently viewed recormeetingData display format for BC module
	 * @throws Exception
	 */
	@Test
	public void Meetings_17176_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Sort by name to ensure the meetings are in descending order
		sugar().meetings.listView.sortBy("headerName", false);

		// Navigate to the record view of first meeting i.e Meeting4
		sugar().meetings.listView.clickRecord(1);

		// View the other Meeting records by clicking the chevronRight icon
		for(int i = 1; i < meetingDataSourceSize; i++) {
			sugar().meetings.recordView.gotoNextRecord();
		}	

		// Click the Meetings module drop down
		sugar().navbar.clickModuleDropdown(sugar().meetings);

		// Assert that only 3 recently viewed meetings are displayed in the module drop-down in reverse chronological order
		// TODO: VOOD-771 - Need defined control for recently viewed list on navbar
		for(int i = 0; i < meetingDataSourceSize; i++) {
			VoodooControl recentlyViewedMeeting = new VoodooControl("li", "css", "li[data-module='Meetings'] div.dropdown-menu.scroll li:nth-of-type(" + (5 + i) + ") a");
			if (i <= 2) {
				recentlyViewedMeeting.assertEquals(meetingData.get(meetingDataSourceSize - (i + 1)).get("name"), true);
			}
			else {
				recentlyViewedMeeting.assertExists(false);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}