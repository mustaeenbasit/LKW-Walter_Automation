package com.sugarcrm.test.meetings;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29922 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Start Date color is not removed when "Scheduled" meetings are previewed
	 * @throws Exception
	 */
	@Test
	public void Meetings_29922_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio > Meetings > Layouts > list View  
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-542, VOOD-1507
		new VoodooControl("a", "id", "studiolink_Meetings").click();
		new VoodooControl("td", "id", "layoutsBtn a").click();
		new VoodooControl("td", "id", "viewBtnlistview").click();

		// Move "status" from Default column to Hidden column
		new VoodooControl("li", "css", "[data-name='status']").dragNDrop(new VoodooControl("ul", "css", "td#Hidden ul"));

		// Click on "Save and Deploy"
		new VoodooControl("input", "id" ,"savebtn").click();		
		VoodooUtils.focusDefault();

		// Set previous date
		String yesterdayDate = DateTime.now().minusDays(1).toString("MM/dd/yyyy");

		// Create a meeting where start and End date is set as previous date and status as "Scheduled".
		sugar().navbar.selectMenuItem(sugar().meetings, "createMeeting");
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.getEditField("date_start_date").set(yesterdayDate);
		sugar().meetings.createDrawer.getEditField("date_end_date").set(yesterdayDate);
		sugar().meetings.createDrawer.save();

		// TODO: VOOD-1882
		VoodooControl listViewStartDateTime = new VoodooControl("span", "css", ".list.fld_date_start");
		FieldSet meetingDataDS = testData.get(testName).get(0);

		// Click on preview and verify the background color doesn't change for start date
		sugar().meetings.listView.previewRecord(1);
		listViewStartDateTime.assertCssAttribute("background-color", meetingDataDS.get("bgColor"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
