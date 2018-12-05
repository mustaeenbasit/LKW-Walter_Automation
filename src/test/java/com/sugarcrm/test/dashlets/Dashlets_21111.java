package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21111 extends SugarTest {
	public void setup() throws Exception {
		sugar().calls.api.create();
		sugar().login();

		// Create a call and add qauser as invitee
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().users.getQAUser().get("userName"));
		sugar().meetings.createDrawer.save();

		// Logout from admin and login as qauser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

	}

	/**
	 * Verify the Accept, Tentative and Decline link of a call are shown in dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21111_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashboardData = testData.get(testName).get(0);

		// Define Controls for Dashlets
		// TODO: VOOD-960 - Dashlet selection 
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");
		VoodooControl saveDashboard = new VoodooControl("a", "css", ".fld_save_button a");

		// Go to Home -> My Dashboard -> Edit
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();

		// Add a Dashlet
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select "Saved Reports Chart Dashlet" tab in toggle drawer
		dashletSearchCtrl.set(dashboardData.get("dashletName"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();

		// Save Dashlet
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		saveDashboard.click();
		VoodooUtils.waitForReady();

		// Verify Held, Accept, Tentative and Decline links are show on "Planned Activities" dashlet of home page
		// TODO: VOOD-976 - need lib support of RHS on record view
		new VoodooControl("a", "css", ".dashlet-tabs.tab2 div:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".dashlet-unordered-list li a:nth-child(2)").assertContains(sugar().calls.getDefaultData().get("name"), true);
		new VoodooControl("a", "css", "[data-original-title='Held']").assertVisible(true);
		new VoodooControl("a", "css", "[data-original-title='Accepted']").assertVisible(true);
		new VoodooControl("a", "css", "[data-original-title='Tentative']").assertVisible(true);
		new VoodooControl("a", "css", "[data-original-title='Declined']").assertVisible(true);
		
		// Delete created Dashlet
		// TODO: VOOD-960
		new VoodooControl("i", "css", ".row-fluid.sortable:nth-of-type(4) .btn-group button[data-original-title='Configure'] i").click();
		new VoodooControl("a", "css", ".row-fluid.sortable:nth-of-type(4) [data-dashletaction='removeClicked']").click();
		sugar.alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}