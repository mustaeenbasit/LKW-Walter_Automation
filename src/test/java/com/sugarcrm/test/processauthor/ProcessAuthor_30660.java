package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30660 extends SugarTest {
	UserRecord chrisUser;
	FieldSet processInfo = new FieldSet();

	public void setup() throws Exception {
		processInfo = testData.get(testName).get(0);

		// Log-In as admin
		sugar().login();

		// Create User
		chrisUser = (UserRecord)sugar().users.create();

		// Navigate to East team's record view
		sugar().teams.navToListView();
		sugar().teams.listView.basicSearch(processInfo.get("eastTeam"));
		sugar().teams.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-518
		// Assing team to chris user
		new VoodooControl("a", "css", "#team_memberships_select_button").click();
		VoodooUtils.focusWindow(1);

		// Select Chris user to team East
		new VoodooControl("input", "css", ".list.view tr:nth-child(3) td:nth-child(1) input").click();
		new VoodooControl("input", "css", "#MassUpdate_select_button").click();
		VoodooUtils.focusWindow(0);
	}

	/**
	 * Verify claim process form should open while claiming the process from self service
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30660_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + processInfo.get("version") + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create an account to trigger the process
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();

		// Log-Out from Admin
		sugar().logout();

		// Log-In as Chris
		sugar().login(chrisUser);

		// Create a Dashboard and add Processes Dashlet
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.getControl("title").set(testName);
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("a", "css", "[data-original-title='Processes'] a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();

		// Save the dashboard
		sugar().home.dashboard.save();

		// Click the Self Service Processes tab in the dashlet
		new VoodooControl("a", "css", ".dashlet [data-voodoo-name='dashlet-inbox'] .dashlet-tab:nth-of-type(2) a").click();
		// Click the process name
		new VoodooControl("a", "css", "[data-voodoo-name='dashlet-inbox'] .active.tab-pane p a").click();

		// TODO: VOOD-1933 - Need Lib support for "Claim" process record view
		// Assert that the claim process page is displayed
		new VoodooControl("a", "css", ".detail.fld_reject_button a").assertEquals(processInfo.get("claimButton"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}