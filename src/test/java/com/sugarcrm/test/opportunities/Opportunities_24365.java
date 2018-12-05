package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24365 extends SugarTest {
	public void setup() throws Exception {
		// Create Opportunity record
		sugar().opportunities.api.create();

		// Login
		sugar().login();

		// Enable Projects module
		sugar().admin.enableModuleDisplayViaJs(sugar().projects);

		// Enable Projects sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().projects);
	}

	/**
	 * In-Line Create Projects_Verify that project can be in-line created from Projects sub-panel for an opportunity
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24365_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Opportunity record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Click "Create" button in Projects sub-panel
		// TODO: VOOD-1032
		VoodooControl projectsSubpanelCtrl = new VoodooControl("div", "css", ".filtered.layout_Project");
		projectsSubpanelCtrl.scrollIntoViewIfNeeded(false);
		new VoodooControl("a", "css", projectsSubpanelCtrl.getHookString() + " a[name='create_button']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Enter data in all the fields in in-line create page
		sugar().projects.editView.getEditField("name").set(testName);
		sugar().projects.editView.getEditField("date_estimated_start").set(sugar().projects.getDefaultData().get("date_estimated_start"));
		sugar().projects.editView.getEditField("date_estimated_end").set(sugar().projects.getDefaultData().get("date_estimated_end"));
		VoodooUtils.focusDefault();

		// Click "Save" button
		sugar().projects.editView.save();

		// Verify that the project is displayed in Projects sub-panel with information as entered for creating
		// TODO: VOOD-1032
		projectsSubpanelCtrl.scrollIntoViewIfNeeded(true); // Scroll into view and click to expend the subpanel
		new VoodooControl("a", "css", projectsSubpanelCtrl.getHookString() + " .list.fld_name a").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}