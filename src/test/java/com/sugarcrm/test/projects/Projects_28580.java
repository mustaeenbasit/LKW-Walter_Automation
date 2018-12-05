package com.sugarcrm.test.projects;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Projects_28580 extends SugarTest {
	public void setup() throws Exception {
		sugar().projects.api.create();

		// Login
		sugar().login();

		// Enable Projects module
		sugar().admin.enableModuleDisplayViaJs(sugar().projects);
	}

	/**
	 * Verify that project record can be deleted successfully from the record view and list view  
	 * 
	 * @throws Exception
	 */
	@Test
	public void Projects_28580_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define controls
		// TODO: VOOD-1987, VOOD-1363
		VoodooControl exportToMsProjectBtnCtrl = new VoodooControl("a", "id", "export_to_ms_project_button");
		VoodooControl listViewMessageCtrl = new VoodooControl("p", "css", ".listViewEmpty .msg");

		// Navigate to Projects module record view and under the action drop down next to Edit button, click on Export to MS project
		sugar().projects.navToListView();
		sugar().projects.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().projects.detailView.openPrimaryButtonDropdown();
		exportToMsProjectBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Under the action drop down next to Edit button, click on Delete button -> Pop up message appear -> Click on Ok button
		sugar().projects.detailView.delete();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Verify that the Project is successfully deleted from record view
		VoodooControl firstRecordCheckbocCtrl = sugar().projects.listView.getControl("checkbox01");
		firstRecordCheckbocCtrl.assertExists(false);
		listViewMessageCtrl.assertVisible(true);
		VoodooUtils.focusDefault();

		// Create another project 
		sugar().projects.api.create();

		// Navigate to Projects module record view and under the action drop down next to Edit button, click on Export to MS project.
		sugar().projects.navToListView();
		sugar().projects.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().projects.detailView.openPrimaryButtonDropdown();
		exportToMsProjectBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Go back to list view 
		sugar().projects.navToListView();

		// Check the check box next to created project 
		sugar().projects.listView.toggleSelectAll();
		VoodooUtils.focusDefault();

		// Select Delete 
		sugar().projects.listView.delete();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady();

		// Verify that the Project is successfully deleted from list view
		firstRecordCheckbocCtrl.assertExists(false);
		listViewMessageCtrl.assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}