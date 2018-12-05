package com.sugarcrm.test.activitystream;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;

public class ActivityStream_27184 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Opportunities -> Fields -> name -> Check the "Audit" field checkbox.
		// TODO: VOOD-542 and VOOD-1504
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "name").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='audited']").set("true");
		new VoodooControl("input", "css", "input[name='fsavebtn']").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "My Activity Stream" Dashlet is saved in Listview Dashboard
	 * 
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_27184_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunity module list and create an Opportunity record
		OpportunityRecord myOpportunity = (OpportunityRecord) sugar().opportunities.create();
		FieldSet activityStreamData = testData.get(testName).get(0);

		// In RHS, create a Dashboard.
		VoodooControl dashboardTitle = sugar().leads.dashboard.getControl("dashboardTitle");
		if ( !dashboardTitle.queryContains(activityStreamData.get("dashboardTitle"), true) ) {
			sugar().dashboard.chooseDashboard(activityStreamData.get("dashboardTitle"));
		}
		sugar().opportunities.dashboard.clickCreate();
		sugar().opportunities.dashboard.getControl("title").set(activityStreamData.get("dashboardName"));
		sugar().opportunities.dashboard.addRow();
		sugar().opportunities.dashboard.addDashlet(1, 1);

		// Add a Dashlet by selecting "My Activity Stream" Dashlet
		// TODO: VOOD-960 - Dashlet Selection
		new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(activityStreamData.get("myActivityStream"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify that "My Activity Stream" Dashlet is added
		sugar().opportunities.dashboard.getControl("firstDashlet").assertVisible(true);

		// TODO: VOOD-963 - Some dashboard controls are needed 
		// Assert dashlet title 
		new VoodooControl("h4", "css", ".dashlet-title").assertEquals(activityStreamData.get("myActivityStream"), true);

		// TODO: VOOD-964 - Some dashboard controls are needed
		// Verify the Activity message about a new Opportunity is created appearing in the Dashlet.
		VoodooControl createdActivity = new VoodooControl("span", "css", ".dashlet-row .activitystream-list li div .tagged");
		createdActivity.assertContains(activityStreamData.get("assertMessageCreated") + " " + myOpportunity.getRecordIdentifier() + " " + sugar().opportunities.moduleNameSingular, true);

		// Save it
		sugar().opportunities.dashboard.save();

		// Edit the same Opportunity record by changing name.
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("name").set(testName);
		sugar().opportunities.recordView.save();

		// Go to Opportunities list view
		sugar().opportunities.navToListView();

		// TODO: VOOD-963 - Some dashboard controls are needed 
		// Verify Activity message about the updated Opportunity is appearing in the Dashlet.
		createdActivity.assertContains(activityStreamData.get("assertMessageUpdated") + " " + activityStreamData.get("opportunityName") + " " + activityStreamData.get("assertMessageOn") + " " + testName, true);
		new VoodooControl("span", "css", ".dashlet-row .activitystream-list li:nth-child(2) div .tagged").assertContains(activityStreamData.get("assertMessageCreated") + " " + myOpportunity.getRecordIdentifier() + " " + sugar().opportunities.moduleNameSingular, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}