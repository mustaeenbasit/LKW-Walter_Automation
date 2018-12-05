package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21897 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		// Lead Record
		sugar().leads.api.create();

		// Login as admin
		sugar().login();

		// Navigating to Leads list view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Add New Dashboard with name "TestDashboard"
		sugar().leads.dashboard.clickCreate();
		customData = testData.get(testName).get(0);
		sugar().leads.dashboard.getControl("title").set(customData.get("dashboardTitle"));

		// Add a new row for 'Notes & Attachments'
		sugar().leads.dashboard.addRow();
		sugar().leads.dashboard.addDashlet(1, 1);

		// TODO: VOOD-960 Dashlet selection 
		// Select "Notes & Attachments" from dashlets list
		new VoodooControl("input", "css", ".inline-drawer-header input").set(customData.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_title a").click();

		// TODO: VOOD-1004 Library support need to create dashlet
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		sugar().leads.dashboard.save();
	}

	/**
	 * Create Note_Verify that note is not created in "Notes & Attachments" dashlet of a lead when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_21897_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "+" to add related record.
		// TODO: VOOD-960 Dashlet selection 
		new VoodooControl("a", "css", ".dashlet-toolbar .btn-invisible").click();

		// Clicking the 'Create Related Record' option from dropdown 
		new VoodooControl("a", "css", "[data-dashletaction='openCreateDrawer']").click();
		VoodooUtils.waitForReady();

		// Filling note fields
		sugar().notes.createDrawer.getEditField("subject").set(testName);
		// TODO: PAT-1528 Make relate field read-only when creating from subpanels, Once this story is resolved 
		// we can remove lines where we are filling related fields i.e. "relTeam", "relAssignedTo"
		String qauserName = sugar().users.getQAUser().get("userName");
		sugar().notes.createDrawer.getEditField("relTeam").set(qauserName);
		sugar().notes.createDrawer.getEditField("description").set(customData.get("noteDescription"));
		sugar().notes.createDrawer.getEditField("relAssignedTo").set(qauserName);

		// Click "Cancel"
		sugar().notes.createDrawer.cancel();

		// Verifying no note is created in 'Notes & Attachments' dashlet
		// TODO: VOOD-670 More Dashlet Support
		new VoodooControl("div", "css", ".layout_Home .dashlet-content .block-footer").assertEquals(customData.get("noDataText"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}