package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Calendar_20321  extends SugarTest {
	public void setup() throws Exception {	
		// Create a call record in calendar for qauser user
		FieldSet startDate = new FieldSet();
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		startDate.put("date_start_date",todaysDate);
		sugar.calls.api.create(startDate);
		sugar.login();
		sugar.calls.navToListView();
		sugar.calls.listView.editRecord(1);
		sugar.calls.listView.getEditField(1, "assignedTo").set(sugar.users.getQAUser().get("userName"));
		sugar.calls.listView.saveRecord(1);
	}

	/**
	 * Display calendar_Verify that selected users' calendar is displayed in Shared Calendar panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calendar_20321_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click Shared tab
		// TODO: VOOD-863 need support for Calendar module
		new VoodooControl("input", "id", "shared-tab").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Click "User List" button in "Shared Calendar" panel.
		new VoodooControl("button", "id", "userListButtonId").click();
		
		// Select a team
		new VoodooControl("a", "css", "#shared_team_id").click();
		new VoodooControl("option", "css", "#shared_team_id option:nth-child(7)").click();
		
		// Click one name in users multiselect box
		new VoodooControl("option", "css", "#shared_ids option:nth-child(1)").click();
		
		// Click "Select" button in "Shared Calendar" panel.
		new VoodooControl("button", "css", ".ft [title='Select']").click();
		
		// Verify that the calendar sheet for the respective user is displayed. 
		new VoodooControl("h5", "css", ".calSharedUser").assertContains(sugar.users.getQAUser()
				.get("userName"), true);

		// Verify the record of the selected user in calendar sheet
		new VoodooControl("div", "id", "cal-grid").assertContains(sugar.calls.getDefaultData()
				.get("name"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
