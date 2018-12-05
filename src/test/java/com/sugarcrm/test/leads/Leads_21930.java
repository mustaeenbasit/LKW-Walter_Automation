package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21930 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Lead Detail View_Verify that detail information related to a lead can be viewed by clicking Subject links 
	 * in the dashlet(s) of "Lead" detail view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_21930_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Toggle to My Dashboard
		FieldSet customData = testData.get(testName).get(0);
		sugar().leads.dashboard.chooseDashboard(customData.get("myDashboard"));
		VoodooUtils.waitForReady();

		// Edit My DashBoard to add the "Active Tasks" Dashlet
		sugar().leads.dashboard.edit();
		sugar().leads.dashboard.addRow();
		sugar().leads.dashboard.addDashlet(3, 1);

		// TODO: VOOD-960 - Dashlet Selection
		// Select Active task
		new VoodooControl("a", "css", ".list-view .list.fld_title a").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-963 - Some dashboard controls are needed
		// Drag and drop the "Active Tasks" dashlet to the top
		new VoodooControl("i", "css", ".dashlet-row li:nth-child(3) .fa-arrows-v").dragNDrop(new VoodooControl("i", "css", ".dashlet-row li:nth-child(1) .fa-arrows-v"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-963 - CSS selector for save button not available for the dashboard edit view
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-963 - Some dashboard controls are needed
		// TODO: VOOD-960 - Dashlet Selection
		// Go to Active tasks dashlet and click + icon to create task
		new VoodooControl("i", "css", ".dashlet-row li .dashlet-header .fa.fa-plus").click();
		new VoodooControl("a", "css", ".dashlet-row li .dashlet-header .dropdown-menu li").click();
		VoodooUtils.waitForReady();

		// Entering the subject in the task subject field
		sugar().tasks.createDrawer.getEditField("subject").set(testName);
		sugar().tasks.createDrawer.save();

		// TODO: VOOD-963 - Some dashboard controls are needed
		// Click the "To Do" tab
		new VoodooControl("span", "css", ".dashlet-row li .dashlet-tabs-row div:nth-child(3) a").click();

		// Click Task record displayed in Active Tasks dashlet
		new VoodooControl("a", "css", ".dashlet-row li .active p a:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Verify that user is navigated to the clicked task's Record View Page
		sugar().tasks.recordView.getDetailField("subject").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}