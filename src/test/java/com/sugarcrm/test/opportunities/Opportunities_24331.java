package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24331 extends SugarTest {

	public void setup() throws Exception {
		// Opportunity record exist.
		sugar().opportunities.api.create();
		sugar().login();

		// Enable Projects for sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().projects);
	}

	/**
	 * Verify that project without project task can be created in full form mode for an opportunity in "Projects" sub-panel.
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24331_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity recordView
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// TODO: VOOD-1713
		VoodooControl projectSubpanelCtrl = new VoodooControl("a", "css", ".filtered.layout_Project .fld_create_button.small a");

		// Click "Create" button in "Projects" sub-panel.
		projectSubpanelCtrl.scrollIntoViewIfNeeded(false);
		projectSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().projects.editView.getEditField("name").set(testName);
		sugar().projects.editView.getEditField("date_estimated_start").set(sugar().projects.getDefaultData().get("date_estimated_start"));
		sugar().projects.editView.getEditField("date_estimated_end").set(sugar().projects.getDefaultData().get("date_estimated_end"));
		VoodooUtils.focusDefault();
		sugar().projects.editView.save();

		// TODO: VOOD-1713
		// Expend SubPanel
		projectSubpanelCtrl.scrollIntoViewIfNeeded(false);
		new VoodooControl("div", "css", ".filtered.layout_Project .subpanel-header").click();
		VoodooUtils.waitForReady();

		// Verify that the project created for the opportunity is displayed in "project" sub-panel.
		new VoodooControl("span", "css", ".filtered.layout_Project .flex-list-view .dataTable tbody tr .list.fld_name").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}