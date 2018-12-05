package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_27922 extends SugarTest {
	FieldSet dashletData = new FieldSet();

	public void setup() throws Exception {
		dashletData = testData.get(testName).get(0);

		// Login as and Admin user
		sugar().login();

		// Create a Process definition and Save (Not Save & Design). Make sure to enter some description for the process definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.create();
		sugar().processDefinitions.createDrawer.showMore();
		sugar().processDefinitions.createDrawer.getEditField("name").set(testName);
		sugar().processDefinitions.createDrawer.getEditField("status").set(dashletData.get("enable"));
		sugar().processDefinitions.createDrawer.getEditField("description").set(sugar().processDefinitions.getDefaultData().get("description"));
		sugar().processDefinitions.createDrawer.openPrimaryButtonDropdown();
		sugar().processDefinitions.createDrawer.save();

		// Add Process definitions dashlet in a dashboard. Process definition is displayed in the dashlet
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.edit();
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4, 1);

		// TODO: VOOD-960, VOOD-1645
		// Add a dashlet -> select "Active Tasks"
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(dashletData.get("dashletName"));
		VoodooUtils.waitForReady(); // Wait needed here
		new VoodooControl("a", "css", ".list-view .fld_title a").click();

		// Save
		new VoodooControl("a", "css", ".layout_pmse_Project.drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify process definitions actions in dashlet are functional
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_27922_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Control of the first record in Process Definition dashlet
		// TODO: VOOD-960
		VoodooControl firstRecordOfProcessDefinitionDashletCntl = new VoodooControl("li", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1) .tab-content li");

		// For the process definition, click Design button (pencil icon)
		new VoodooControl("i", "css", firstRecordOfProcessDefinitionDashletCntl.getHookString() + " .fa-pencil").click();
		VoodooUtils.waitForReady();

		// Verify that the Designer is open with empty canvas
		// TODO: VOOD-1539
		new VoodooControl("div", "id", "ProjectTitle").assertEquals(testName, true);
		new VoodooControl("div", "id", "jcore_designer").assertAttribute("class", "custom_shape", false);

		// TODO: VOOD-960
		// Back to dashlet, click Disabled button and click confirm on the popup displayed
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		new VoodooControl("i", "css", firstRecordOfProcessDefinitionDashletCntl.getHookString() + " .fa-eye-slash").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady(30000); // Extra wait needed

		// Verify that the process definition is no longer exist in Enabled Tab
		firstRecordOfProcessDefinitionDashletCntl.assertExists(false);

		// Go to Disabled Tab
		new VoodooControl("span", "css", ".row-fluid.sortable:nth-of-type(4) .dashlet-container:nth-of-type(1) .dashlet-tabs .dashlet-tab:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Verify the process definition is moved to Disabled tab
		// TODO: VOOD-960
		new VoodooControl("a", "css", firstRecordOfProcessDefinitionDashletCntl.getHookString() + " p a").assertEquals(testName, true);

		// Back to dashlet > Disabled tab, click i button for description
		new VoodooControl("i", "css", firstRecordOfProcessDefinitionDashletCntl.getHookString() + " .fa-info-circle").click();

		// Verify the Message with description will display
		sugar().alerts.getInfo().assertContains(dashletData.get("descriptionText"), true);
		sugar().alerts.getInfo().assertContains(sugar().processDefinitions.getDefaultData().get("description"), true);
		sugar().alerts.getInfo().closeAlert();

		// Back to dashlet > Disabled tab, click Delete and confirm
		new VoodooControl("i", "css", firstRecordOfProcessDefinitionDashletCntl.getHookString() + " .fa-times").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify that the Process definition is deleted and no longer displayed in dashlet
		firstRecordOfProcessDefinitionDashletCntl.assertExists(false);

		// Also verify that the Process definition is no longer displayed in the Process definition list view
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}