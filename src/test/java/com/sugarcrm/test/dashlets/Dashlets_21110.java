package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21110 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().calls.api.create();
		sugar().login();

		// Create a call from admin and add "qauser" as invitee
		FieldSet qaUser = sugar().users.getQAUser();
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.getEditField("teams").set(customFS.get("teamName"));
		sugar().calls.recordView.clickAddInvitee();
		sugar().calls.recordView.selectInvitee(qaUser.get("userName"));
		sugar().calls.recordView.save();

		// Logout from Admin and Login as qauser
		sugar().logout();
		sugar().login(qaUser);
	}

	/**
	 * Verify normal user cannot access a call which belong to admin team 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Calls ListView
		sugar().calls.navToListView();

		// Verify ListView is Empty i.e No Call is availaible in the list
		sugar().calls.listView.assertIsEmpty();

		// Navigate to Home Page and Select Planned Activities Dashlet.
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.clickCreate();
		sugar().dashboard.getControl("title").set(testName);
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 - Dashlet Selection
		// Select planned activities
		new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(customFS.get("myPlannedActivities"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		sugar().home.dashboard.save();

		// Navigate to Calls Tab
		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab:nth-child(2)").click();
		VoodooUtils.waitForReady();
		VoodooControl callCount = new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab:nth-child(2) .count");
		VoodooControl dashletContent = new VoodooControl("div", "css", ".tab-pane.active .block-footer");

		// Verify Calls Tab is Empty for Today
		callCount.assertEquals(customFS.get("count"), true);
		dashletContent.assertEquals(customFS.get("assertText"), true);

		// Switch to Future Tab
		new VoodooControl("button", "css", ".row-fluid.sortable div .btn-group.dashlet-group [value='future']").click();
		VoodooUtils.waitForReady();

		// Verify Calls Tab is Empty for Future
		callCount.assertEquals(customFS.get("count"), true);
		dashletContent.assertEquals(customFS.get("assertText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}