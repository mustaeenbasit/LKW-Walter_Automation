package com.sugarcrm.test.meetings;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27356 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that Recurrence fields appear correctly in Meetings
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27356_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Define controls for Meetings Repeat Type
		VoodooControl repeatInterval = sugar().meetings.recordView.getEditField("repeatInterval");
		VoodooControl repeatUntil = sugar().meetings.recordView.getEditField("repeatUntil");
		VoodooControl repeatCount = sugar().meetings.recordView.getEditField("repeatOccur");
		VoodooControl repeatTypeDetail = sugar().meetings.recordView.getDetailField("repeatType");
		VoodooControl repeatIntervalDetail = sugar().meetings.recordView.getDetailField("repeatInterval");
		VoodooControl repeatCountDetail = sugar().meetings.recordView.getDetailField("repeatOccur");

		// Click on Create button
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(testName);

		// Verify that following fields are not visible : Repeat Interval, Repeat Until and Repeat Occurrences
		repeatInterval.assertVisible(false);
		repeatUntil.assertVisible(false);
		repeatCount.assertVisible(false);

		// Select Repeat Type = Daily and default Occurrences = 10
		sugar().meetings.recordView.getEditField("repeatType").set(customData.get("repeat_type"));

		// Verify that In create view: If "Repeat Type" is enabled then assert following fields are visible: Repeat Interval, Repeat Until and Repeat Occurrences
		repeatInterval.assertVisible(true);
		repeatUntil.assertVisible(true);
		repeatCount.assertVisible(true);

		sugar().meetings.createDrawer.save();
		sugar().meetings.listView.clickRecord(1);

		// Verify that in recordView:If "Repeat Type" is enabled then assert following fields are visible: Repeat Interval and Repeat Occurrences
		repeatTypeDetail.assertContains(customData.get("repeat_type"), true);
		repeatIntervalDetail.assertContains(customData.get("repeat_interval"), true);
		repeatCountDetail.assertContains(customData.get("repeat_count"), true);

		// Edit created meeting record
		sugar().meetings.recordView.edit();

		// Verify that in editView: assert shows the following fields: Repeat Interval and Repeat Occurrences
		repeatTypeDetail.assertContains(customData.get("repeat_type"), true);
		repeatIntervalDetail.assertContains(customData.get("repeat_interval"), true);
		repeatCountDetail.assertContains(customData.get("repeat_count"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}