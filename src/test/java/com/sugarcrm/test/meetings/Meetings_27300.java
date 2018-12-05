package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27300 extends SugarTest {
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Edit All Recurrences" should be not visible if the meeting is not recurring meeting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27300_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.openRowActionDropdown(1);
		
		// TODO: VOOD-1222 -Need library support to verify fields in ListView and RecordView of Calls/Meetings module
		// Verify That "Edit All Recurrences" action is not available in the listView action drop down.
		VoodooControl actionDropdown = new VoodooControl("li", "xpath", "//*[@class='dropdown-menu']/li[contains(.,'"+customData.get("rowActionDropdown")+"')]");
		actionDropdown.assertExists(false);
		
		// Go to recordView
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		
		// Verify That "Edit All Recurrences" action is not available in the recordView action drop down.
		actionDropdown.assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}