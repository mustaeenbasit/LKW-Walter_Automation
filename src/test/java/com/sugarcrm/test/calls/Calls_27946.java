package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27946 extends SugarTest {
	CallRecord myCallRecord;
	MeetingRecord myMeetingRecord;
	TargetListRecord myTargetListRecord;
	TaskRecord myTaskRecord;
	
	public void setup() throws Exception {
		myCallRecord = (CallRecord)sugar.calls.api.create();
		myMeetingRecord = (MeetingRecord)sugar.meetings.api.create();
		myTargetListRecord = (TargetListRecord)sugar.targetlists.api.create();
		myTaskRecord = (TaskRecord)sugar.tasks.api.create();
		sugar.login();
	}
	/**
	 * "Find Duplicate" isn't available in record view action list
	 * @throws Exception
	 */
	@Test
	public void Calls_27946_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Verify "Find Duplicate" on Calls/Meetings/TargetList/Task module
		for(int i = 0; i < 4; i++) {
			if(i == 0) {
				myCallRecord.navToRecord();
				sugar.calls.recordView.openPrimaryButtonDropdown();
			} else if(i == 1) {
				myMeetingRecord.navToRecord();
				sugar.meetings.recordView.openPrimaryButtonDropdown();
			} else if(i == 2) {
				myTargetListRecord.navToRecord();
				sugar.targetlists.recordView.openPrimaryButtonDropdown();
			} else {
				myTaskRecord.navToRecord();
				sugar.tasks.recordView.openPrimaryButtonDropdown();
			}
			
			// TODO: VOOD-691
			// Verify that "Find Duplicate" link isn't available.
			FieldSet customData = testData.get(testName).get(0);
			new VoodooControl("li", "xpath", "//*[@class='dropdown-menu']/li[contains(.,'"+customData.get("action_drop_down")+"')]").assertExists(false);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}