package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20342 extends SugarTest {
	DataSource meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName);
		sugar.login();

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click Schedule Meetings from warning
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();

		// Meeting Subject data and save.
		sugar.meetings.createDrawer.getEditField("name").set(meetingData.get(0).get("name"));
		sugar.meetings.createDrawer.getEditField("status").set(meetingData.get(0).get("status"));
		sugar.meetings.createDrawer.getEditField("description").set(meetingData.get(0).get("description"));
		sugar.meetings.createDrawer.save();
	}

	/**
	 * Verify that editing a meeting can be canceled in Edit Activity window in Calendar module.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20342_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click any Meeting subject in Calendar view.
		// TODO: VOOD-863
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		sugar.meetings.recordView.edit();
		sugar.meetings.recordView.getEditField("name").set(meetingData.get(1).get("name"));
		sugar.meetings.recordView.getEditField("status").set(meetingData.get(1).get("status"));
		sugar.meetings.recordView.getEditField("description").set(meetingData.get(1).get("description"));
		sugar.meetings.recordView.getEditField("date_start_time").set(meetingData.get(1).get("date_start_time"));

		// Click Cancel button
		sugar.meetings.recordView.cancel();

		// Verify that the Meeting information is not updated in Calendar view.
		// TODO: VOOD-863
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").assertContains(meetingData.get(0).get("name"), true);
		new VoodooControl("div", "css", ".week div[time='09:00am']").assertContains(meetingData.get(1).get("name"), false);
		new VoodooControl("div", "css", ".week div[time='08:00am'] .head .adicon").click();
		VoodooUtils.waitForReady();

		// Verify the Meeting subject at the Additional Detail page is not updated
		// TODO: VOOD-863
		VoodooControl additionalDetailPageValues = new VoodooControl("div", "css", ".ui-dialog-content");
		additionalDetailPageValues.assertContains(meetingData.get(0).get("name"), true);
		additionalDetailPageValues.assertContains(meetingData.get(0).get("status"), true);
		additionalDetailPageValues.assertContains(meetingData.get(0).get("description"), true);
		additionalDetailPageValues.assertContains(meetingData.get(0).get("date_start_time"), true);

		// Click on the view icon on the Additional Detail page
		// TODO: VOOD-863
		new VoodooControl("a", "css", ".ui-dialog-title a[title='View']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();
		// Verify that the Meeting detail information is is not updated on "Meetings Detail View" page.
		sugar.meetings.recordView.getDetailField("name").assertEquals(meetingData.get(0).get("name"), true);
		sugar.meetings.recordView.getDetailField("status").assertEquals(meetingData.get(0).get("status"), true);
		sugar.meetings.recordView.getDetailField("description").assertEquals(meetingData.get(0).get("description"), true);
		sugar.meetings.recordView.getDetailField("date_start_time").assertContains(meetingData.get(0).get("date_start_time"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}