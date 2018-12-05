package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27283 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that Start time and End time with specific color in meeting scheduler
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27283_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to meetings record view
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// Verify that green line and red line are at the correct time that match with Start and End time.
		new VoodooControl("div", "css", ".participants-schedule div:nth-child(3) .start_end_overlay.right_border").assertCssAttribute("border-left", customData.get("left_border"));
		new VoodooControl("div", "css", ".participants-schedule div:nth-child(5) .start_end_overlay.right_border").assertCssAttribute("border-right", customData.get("right_border"));
		
		// Change Start time and End time
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("date_start_time").set(customData.get("start_time"));
		sugar().meetings.recordView.getEditField("date_end_time").set(customData.get("end_time"));
		sugar().meetings.recordView.save();
		
		// Navigate to meetings record view
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		
		// Verify that green line and red line are at the correct time that match with Start and End time.
		new VoodooControl("div", "css", ".participants-schedule div:nth-child(3) .start_end_overlay.right_border").assertCssAttribute("border-left", customData.get("left_border"));
		new VoodooControl("div", "css", ".participants-schedule div:nth-child(5) .start_end_overlay.right_border").assertCssAttribute("border-right", customData.get("right_border"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}