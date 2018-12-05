package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19742 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * View meeting_Verify that meeting information is displayed correctly on "Meeting Detail View" page.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19742_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);
		sugar().meetings.listView.getDetailField(1, "name").click();
		// Verify Meeting information is displayed correctly on "Meeting Detail View" page
		sugar().meetings.recordView.getDetailField("name").assertContains(sugar().meetings.getDefaultData().get("name"), true);
		sugar().meetings.recordView.getDetailField("status").assertContains(sugar().meetings.getDefaultData().get("status"), true);
		sugar().meetings.recordView.getDetailField("type").assertContains(sugar().meetings.getDefaultData().get("type"), true);
		sugar().meetings.recordView.getDetailField("description").assertContains(sugar().meetings.getDefaultData().get("description"), true);
		sugar().meetings.recordView.getDetailField("date_end_date").assertContains(sugar().meetings.getDefaultData().get("date_end_date"), true);
		sugar().meetings.recordView.getDetailField("date_start_date").assertContains(sugar().meetings.getDefaultData().get("date_start_date"), true);
		// Verify Guests
		// TODO: VOOD-1223
		VoodooControl participantsCtrl = new VoodooControl("div", "css", ".participants-schedule");
		participantsCtrl.assertContains(customData.get("participant1"), true);
		participantsCtrl.assertContains(customData.get("participant2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}