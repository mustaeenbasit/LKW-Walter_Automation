package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21051 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();

		// TODO: VOOD-592 - Add dashlet support to home screen model
		// TODO: VOOD-960 - Dashlet selection 
		VoodooControl configure = new VoodooControl("div", "css", "li.row-fluid.sortable:nth-of-type(3) .btn-group .btn");
		VoodooControl dropdownAction = new VoodooControl("li", "css", "li.row-fluid.sortable:nth-of-type(3) .btn-group .dropdown-menu li");
		VoodooSelect container = new VoodooSelect("div", "css", "[data-name='module'] .select2-container");
		VoodooControl save = new VoodooControl("a", "css", "#drawers .fld_save_button a");
		
		// Set My Meetings Dashboard on HomePage of Admin User
		configure.click();
		dropdownAction.click();
		VoodooUtils.waitForReady();
		container.set(sugar().meetings.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Saving the My Meetings Dashboard
		save.click();
		VoodooUtils.waitForReady();

		// Logout from the admin user and login with the qauser.
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that meetings are displayed in "My Meetings" of the special user's Home page,
	 * when the meetings are assigned to that special user.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21051_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource meetingsData = testData.get(testName);
		FieldSet massUpdateData = testData.get(testName + "_massUpdate").get(0);

		// Existing meetings records are needed
		// TODO: VOOD-444 - Support creating relationships via API
		sugar().meetings.create(meetingsData);

		// Mass-updating single record for the Assigned To User
		sugar().meetings.navToListView();
		sugar().meetings.listView.checkRecord(2);
		sugar().meetings.listView.openActionDropdown();
		sugar().meetings.listView.massUpdate();
		sugar().meetings.massUpdate.getControl("massUpdateField02").set(massUpdateData.get("assignedTo"));
		sugar().meetings.massUpdate.getControl("massUpdateValue02").set(meetingsData.get(0).get("assignedTo"));
		sugar().meetings.massUpdate.update();

		// Logout from the qauser and login into the Administrator.
		sugar().logout();
		sugar().login();

		// Sorted the Meetings in Dashlet to pick appropriate position.
		// TODO: VOOD-592 - Add dashlet support to home screen model
		new VoodooControl("th","css", ".orderByname").click();
		VoodooUtils.waitForReady();

		// Verify displaying only My Meetings (i.e 2 records) on home dashboard page under My Meetingsdashlet
		new VoodooControl("tr", "css", ".layout_Home li:nth-child(3) .dashlet-content tbody .single").assertContains(meetingsData.get(0).get("name"), true);
		new VoodooControl("tr", "css", ".layout_Home li:nth-child(3) .dashlet-content tbody .single:nth-child(2)").assertContains(meetingsData.get(1).get("name"), true);
		new VoodooControl("tr", "css", ".layout_Home li:nth-child(3) .dashlet-content tbody .single:nth-child(3)").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}