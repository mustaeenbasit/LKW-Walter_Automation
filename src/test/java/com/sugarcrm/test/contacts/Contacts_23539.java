package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23539 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().notes.api.create();
		sugar().login();
	}

	/**
	 * Delete note or attachment_Verify that a related note or attachment can be deleted from contact detail view.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23539_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet dashletData = testData.get(testName).get(0);

		// Navigate to Contacts Record View
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		VoodooControl dashboardTitle = sugar().opportunities.dashboard.getControl("dashboard");;
		if(!dashboardTitle.queryContains(dashletData.get("myDashboard"), true)) {
			sugar().opportunities.dashboard.chooseDashboard(dashletData.get("myDashboard"));
		}

		sugar().contacts.dashboard.edit();
		sugar().contacts.dashboard.addRow();
		sugar().contacts.dashboard.addDashlet(3, 1);

		// TODO: VOOD-670 - More Dashlet Support
		// Select "Notes & Attachments" in dashlets list
		new VoodooControl("input", "css", ".inline-drawer-header input").set(dashletData.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_title a").click();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save Dashboard
		// TODO: VOOD-1417 - sugar.contacts.dashboard.save(); not working
		new VoodooControl("a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .btn-toolbar .fld_save_button a").click();

		// TODO: VOOD-670 - More Dashlet Support
		new VoodooControl("a", "css", ".dashboard-pane li:nth-child(3) .dashlet-header a").click();
		new VoodooControl("li", "css", ".dashboard-pane li:nth-child(3) li:nth-child(2)").click();
		new VoodooControl("input", "css", "[name='Notes_select']").click();
		VoodooUtils.waitForReady();
		VoodooControl attachmentsCtrl = new VoodooControl("div", "css", "[data-voodoo-name='attachments']");

		// Verify that a note record is listed.
		String notesSubject = sugar().notes.getDefaultData().get("subject");
		attachmentsCtrl.assertContains(notesSubject, true);

		// Click on Unlink of that Note record.
		new VoodooControl("a", "css", ".rowaction.btn-mini").click();

		// Verify that a warning message appears
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().confirmAlert();

		// Verify that the Note record is disappearing from the "Notes and Attachments" dashlet.
		attachmentsCtrl.assertContains(notesSubject, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}