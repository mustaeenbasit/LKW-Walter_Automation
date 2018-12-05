package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20381 extends SugarTest {

	public void setup() throws Exception {
		// Create a call record in calendar for Admin user
		FieldSet startDate = new FieldSet();
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startDate.put("date_start_date",todaysDate);
		sugar.calls.api.create(startDate);
		sugar.login();
	}

	/**
	 * Verify that displayed calendar for users' order can be changed by "Up" and "Down" arrow in "Shared Calendar" panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20381_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		// Click Shared tab
		// TODO: VOOD-863 need support for Calendar module
		new VoodooControl("input", "id", "shared-tab").click();
		// Click "User List" button in "Shared Calendar" panel.
		new VoodooControl("button", "id", "userListButtonId").click();
		// TODO: VOOD-1027, need support to select more than one user and verify the selection
		// Click one name in users multiselect box
		new VoodooControl("option", "css", "#shared_ids option:nth-child(2)").click();
		// Click "Up" arrow to change the order of the displayed user
		new VoodooControl("a", "css", "#shared_cal_edit tbody tr td a:nth-child(1)").click();
		// Click "Select" button in "Shared Calendar" panel.
		new VoodooControl("button", "css", ".ft [title='Select']").click();

		// Verify that the calendar sheet for the respective user is displayed. 
		new VoodooControl("h5", "css", ".calSharedUser").assertContains("Administrator", true);

		// Verify the record of the selected user in calendar sheet
		new VoodooControl("div", "id", "cal-grid").assertContains(sugar.calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}