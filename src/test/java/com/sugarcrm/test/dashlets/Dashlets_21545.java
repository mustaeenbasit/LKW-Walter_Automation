package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.CreateDrawer;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21545 extends SugarTest {

	public void setup() throws Exception {
		FieldSet opportunityDefaultData = sugar().opportunities.defaultData;

		// Create an Account record
		sugar().accounts.api.create();

		// Login as an Admin
		sugar().login();

		CreateDrawer opportunityCreateDrawer = sugar().opportunities.createDrawer;

		// Navigate to the Opportunities module and create an Opportunity record
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.create();
		opportunityCreateDrawer.getEditField("name").set(opportunityDefaultData.get("name"));
		opportunityCreateDrawer.getEditField("relAccountName").set(opportunityDefaultData.get("relAccountName"));
		opportunityCreateDrawer.getEditField("rli_name").set(opportunityDefaultData.get("rli_name"));
		opportunityCreateDrawer.getEditField("rli_expected_closed_date").set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		opportunityCreateDrawer.getEditField("rli_likely").set(opportunityDefaultData.get("rli_likely"));
		opportunityCreateDrawer.save();
	}

	/**
	 * Edit basic charts: Verify that "Pipeline" basic chart is not changed and the toggle drawer is closed when clicking "Cancel" button in the drawer.
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21545_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet pipelineDashletData = testData.get(testName).get(0);;

		// Navigate to the Home page
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);

		// Initialize String variables
		String dashletName = pipelineDashletData.get("pipelineDashletName");
		String dashletValue = String.format("%s\n%s", pipelineDashletData.get("pipelineDashletValue1"), pipelineDashletData.get("pipelineDashletValue2"));

		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		VoodooControl pipelineDashletTitle = new VoodooControl("h4", "css", ".dashlets .layout_Home:nth-child(2) .dashlet-row li.sortable .dashlet-title");
		VoodooControl pipelineDashletGraphValue = new VoodooControl("g", "css", ".dashlets .layout_Home:nth-child(2) .dashlet-row li.sortable .nv-label-value");

		// Assert the Pipeline Dashlet title and value before clicking the edit dashlet option
		pipelineDashletTitle.assertEquals(dashletName, true);
		pipelineDashletGraphValue.assertEquals(dashletValue, true);

		// TODO: VOOD-1376 - Need library support for Pipeline Dashlet on Home Dashboard
		//  Click on configure dropdown
		new VoodooControl("a", "css", ".dashlets .layout_Home:nth-child(2) .dashlet-row li.sortable .dropdown-toggle").click();

		// Click the Edit option
		new VoodooControl("a", "css", ".dashlets .layout_Home:nth-child(2) .dashlet-row li.sortable .dropdown-menu a[data-dashletaction='editClicked']").click();
		VoodooControl toggleDrawer = new VoodooControl("div", "css", ".drawer.active");

		// Assert that the Toggle drawer opens.
		toggleDrawer.assertVisible(true);

		// Click on Cancel button
		new VoodooControl("a", "css", ".detail.fld_cancel_button :not(.hide)").click();
		VoodooUtils.waitForReady();

		// Assert that Pipeline chart is not changed(i.e Title and Value are still the same) and the toggle drawer is closed.
		toggleDrawer.assertVisible(false);
		pipelineDashletTitle.assertEquals(dashletName, true);
		pipelineDashletGraphValue.assertEquals(dashletValue, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}