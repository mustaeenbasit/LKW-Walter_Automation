package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20339 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Schedule meeting_Verify that meeting is scheduled at a special time
	 * @throws Exception
	 */
	@Test
	public void Calendar_20339_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click Day button then select a time slot to schedule a meeting
		// TODO: VOOD-863
		new VoodooControl("div", "id", "day-tab").click();
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();

		// Schedule a meeting, enter the "Subject" and all mandatory fields
		sugar.meetings.createDrawer.getEditField("name").set(testName);

		// Click on "Save" button.
		sugar.meetings.createDrawer.save();

		// Verify that the meeting is shown on the Day calendar and click on info icon	
		// TODO: VOOD-863
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").assertContains(testName, true);
		new VoodooControl("div", "css", ".week div[time='08:00am'] .head .adicon").click();
		VoodooUtils.waitForReady();

		// Verify the meeting name at the Additional Detail page 
		new VoodooControl("div", "css", ".ui-dialog-content").assertContains(testName, true);

		// Click on the view icon on the Additional Detail page 
		new VoodooControl("a", "css", ".ui-dialog-title a[title='View']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that the Meeting detail information is displayed on "Meeting Detail View" page.
		sugar.meetings.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}