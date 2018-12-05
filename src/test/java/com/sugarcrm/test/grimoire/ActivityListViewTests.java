package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class ActivityListViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void isEmpty() throws Exception {
		VoodooUtils.voodoo.log.info("Running isEmpty()...");

		sugar().calls.navToListView();
		sugar().calls.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("isEmpty() complete.");
	}

	@Test
	public void selectMyScheduleFilterForCalls() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectMyScheduleFilterForCalls()...");

		// 2 API records with different status
		sugar().calls.api.create();
		FieldSet customCall = new FieldSet();
		customCall.put("name", testName);
		customCall.put("status", "Held");
		sugar().calls.api.create(customCall);

		// Verify only scheduled calls
		sugar().calls.navToListView();
		sugar().calls.listView.selectFilterMySchedule();
		sugar().calls.listView.verifyField(1, "name", sugar().calls.getDefaultData().get("name"));
		sugar().calls.listView.getControl("checkbox02").assertVisible(false);

		VoodooUtils.voodoo.log.info("selectMyScheduleFilterForCalls() complete.");
	}

	@Test
	public void selectMyScheduleFilterForMeetings() throws Exception {
		VoodooUtils.voodoo.log.info("Running selectMyScheduleFilterForMeetings()...");

		// 2 API records with different status
		sugar().meetings.api.create();
		FieldSet customCall = new FieldSet();
		customCall.put("name", testName);
		customCall.put("status", "Held");
		sugar().meetings.api.create(customCall);

		// Verify only scheduled meetings
		sugar().meetings.navToListView();
		sugar().meetings.listView.selectFilterMySchedule();
		sugar().meetings.listView.verifyField(1, "name", sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.listView.getControl("checkbox02").assertVisible(false);

		VoodooUtils.voodoo.log.info("selectMyScheduleFilterForMeetings() complete.");
	}

	public void cleanup() throws Exception {}
}