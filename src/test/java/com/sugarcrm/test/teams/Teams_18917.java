package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_18917 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Delete private team from detail view with the corresponding user still exists
	 * @throws Exception
	 */
	@Test
	public void Teams_18917_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamData = testData.get(testName).get(0);

		// Go to Admin -> Team Management
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("teamsManagement").click();

		// Search the team
		sugar().teams.listView.basicSearch(sugar().users.getQAUser().get("lastName"));
		sugar().teams.listView.clickRecord(1);

		// Click the delete button on team's detail view page
		sugar().teams.detailView.delete();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Observe that a message appears in red text above the record that says "This private team [username] cannot be deleted until the user [username] is deleted."
		// TODO: VOOD-518
		new VoodooControl("p", "css", "#contentTable td p.error:nth-of-type(2)").assertEquals(teamData.get("assert"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}