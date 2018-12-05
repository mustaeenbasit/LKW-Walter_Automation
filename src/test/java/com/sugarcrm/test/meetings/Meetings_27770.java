package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27770 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Navigate to Meetings
		sugar().meetings.navToListView();

		// Create a repeat meeting
		sugar().meetings.listView.create();
		FieldSet customData = testData.get(testName).get(0);

		// Setting Name, Repeat Type & Repeat Occur Type
		sugar().meetings.createDrawer.setFields(customData);
		sugar().meetings.createDrawer.save();
	}

	/**
	 * Verify that inline edit a repeat meeting works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27770_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open repeat Meeting
		sugar().meetings.listView.clickRecord(1);

		// Click on Repeat Type
		VoodooControl repeatTypeDetailCtrl = sugar().meetings.recordView.getDetailField("repeatType");
		repeatTypeDetailCtrl.click();

		// Hover on Repeat Type Ctrl
		repeatTypeDetailCtrl.hover();

		// Verify pencil icon isn't appearing. 
		// TODO: VOOD-854
		new VoodooControl("i", "css", "span[data-name='repeat_type'] .fa.fa-pencil").assertVisible(false);

		// Click on Repeat Type Ctrl
		repeatTypeDetailCtrl.click();

		// Verify  Repeat Type is not editable
		repeatTypeDetailCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}