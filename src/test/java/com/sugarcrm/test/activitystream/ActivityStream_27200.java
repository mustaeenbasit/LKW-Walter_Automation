package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_27200 extends SugarTest {

	public void setup() throws Exception {
		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "My Activity Stream" Dashlet is saved in recordview Activity Stream
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_27200_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Lead record
		sugar().leads.create();
		FieldSet activityStreamData = testData.get(testName).get(0);

		// Edit the record by changing STATUS
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getEditField("status").set(activityStreamData.get("newStatus"));
		sugar().leads.recordView.save();

		// In RHS, click on Create button and try to create a Dashboard
		VoodooControl dashboardTitle = sugar().leads.dashboard.getControl("dashboardTitle");
		if ( !dashboardTitle.queryContains(activityStreamData.get("dashboardTitle"), true) ) {
			sugar().dashboard.chooseDashboard(activityStreamData.get("dashboardTitle"));
		}
		sugar().leads.dashboard.clickCreate();
		sugar().leads.dashboard.getControl("title").set(activityStreamData.get("dashboardName"));
		sugar().leads.dashboard.addRow();
		sugar().leads.dashboard.addDashlet(1, 1);

		// Add a Dashlet by selecting "My Activity Stream" Dashlet
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(activityStreamData.get("myActivityStream"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save it
		sugar().opportunities.dashboard.save();

		// Verify that "My Activity Stream" Dashlet is added
		sugar().leads.dashboard.getControl("firstDashlet").assertVisible(true);

		// Assert dashlet title 
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("h4", "css", ".dashlet-title").assertEquals(activityStreamData.get("myActivityStream"), true);

		// Verify the  Message about modifying STATUS is displayed.
		new VoodooControl("span", "css", ".dashlet-row .activitystream-list li:nth-child(2) div .tagged").assertContains(activityStreamData.get("assertMessageCreated") + " " + sugar().leads.getDefaultData().get("fullName") + " " + sugar().leads.moduleNameSingular, true);
		new VoodooControl("span", "css", ".dashlet-row .activitystream-list li div .tagged").assertContains(activityStreamData.get("assertMessageUpdated") + " " + activityStreamData.get("status") + " " + activityStreamData.get("assertMessageOn") + " " + sugar().leads.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}