package com.sugarcrm.test.teams;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_24741 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().teams.api.create(customData);
		sugar().login();
	}

	/**
	 * Team_List View_Delete multiple teams 
	 * @throws Exception
	 */
	@Test
	public void Teams_24741_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// View teams 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// Searching newly created Teams
		sugar().teams.listView.basicSearch(customData.get(0).get("name").substring(0,2));
		sugar().teams.listView.checkRecord(1);
		sugar().teams.listView.checkRecord(2);

		// Delete Teams
		sugar().teams.listView.delete();

		// Verify that the pop up window is displayed correctly
		Assert.assertTrue(VoodooUtils.isDialogVisible());

		// TODO: VOOD-1045 - Library support needed for asserting the message appeared in javascript dialog box.
		// Once resolved, we need to add code for Dialog message - "Are you sure you want to delete the 2 selected record(s)?"
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();

		// Verify that the selected teams are deleted successfully.
		sugar().teams.listView.basicSearch(customData.get(0).get("name").substring(0,2));
		Assert.assertTrue("Selected teams are not deleted", sugar().teams.listView.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}