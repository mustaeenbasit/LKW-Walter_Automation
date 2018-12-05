package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21128 extends SugarTest {
	FieldSet meetingData;
	VoodooControl dropdowneditorCtrl,meetingstatusCtrl,saveBtnCtrl;

	public void setup() throws Exception {
		sugar().login();
		meetingData = testData.get(testName).get(0);

		// Go to Admin > Dropdown Editor > meeting_status_dom.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-781
		dropdowneditorCtrl= new VoodooControl("a", "css" ,"#dropdowneditor");
		dropdowneditorCtrl.click();
		VoodooUtils.waitForReady();
		meetingstatusCtrl=new VoodooControl("a", "xpath" ,"//a[@class='mbLBLL' and text()='meeting_status_dom']");
		meetingstatusCtrl.click();
		VoodooUtils.waitForReady();

		// Add "Rescheduled" to Item Name and Display Label
		new VoodooControl("input", "css" ,"#drop_name").set(meetingData.get("status"));
		new VoodooControl("input", "css" ,"#drop_value").set(meetingData.get("status"));
		new VoodooControl("input", "css" ,"#dropdownaddbtn").click();
		VoodooUtils.pause(2000); // pause required for Ajax call
		saveBtnCtrl=new VoodooControl("input", "id", "saveBtn");
		saveBtnCtrl.click();
		sugar().admin.studio.waitForAJAX(30000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}
	/**
	 * meeting_status_dom list in edit view can works properly.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_21128_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a meeting with status = 'Rescheduled'
		sugar().meetings.create(meetingData);
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.getDetailField("status").assertEquals(meetingData.get("status"), true);

		// Edit newly created meeting
		sugar().meetings.recordView.edit();

		// Verify the Meeting status is still set to Rescheduled as previously saved.
		sugar().meetings.recordView.getEditField("status").assertEquals(meetingData.get("status"), true);
		sugar().meetings.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}