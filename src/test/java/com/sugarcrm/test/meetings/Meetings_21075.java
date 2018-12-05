package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21075 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that a meeting is not scheduled when entering invalid value in "Start Date" text field.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21075_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar().meetings.navToListView();
		for(int i=0;i<ds.size();i++) {
			sugar().meetings.listView.create();
			sugar().meetings.createDrawer.getEditField("name").set(ds.get(i).get("name"));
			sugar().meetings.createDrawer.getEditField("date_start_date").set(ds.get(i).get("date_start_date"));
			sugar().meetings.createDrawer.getControl("saveButton").click();

			// Verify error message on invalid start date format
			sugar().meetings.createDrawer.getEditField("date_start_date").assertAttribute("class", "required", true);
			sugar().alerts.getError().assertContains(ds.get(i).get("error_msg"), true);
			sugar().alerts.getError().closeAlert();
			sugar().meetings.createDrawer.cancel();

			// Verify meeting record is not saved
			sugar().meetings.listView.assertIsEmpty();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}