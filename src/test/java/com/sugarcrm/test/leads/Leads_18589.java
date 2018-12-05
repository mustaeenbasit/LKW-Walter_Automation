package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_18589 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Detail View_Verified that a task is displayed in Active tasks dashlet of Leads
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_18589_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Toggle to My Dashboard
		sugar().leads.dashboard.chooseDashboard(customData.get("dashboard_title"));

		// Edit My DashBoard to add the "Active Tasks" Dashlet
		sugar().leads.dashboard.edit();
		sugar().leads.dashboard.addRow();
		sugar().leads.dashboard.addDashlet(3, 1);

		// TODO: VOOD-960 - Dashlet selection
		// Select Active task
		new VoodooControl("a", "css", ".list-view .list.fld_title a").click();
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Drag and drop the "Active Tasks" dashlet to the top
		new VoodooControl("i", "css", ".dashlet-row li:nth-child(3) .fa-arrows-v").dragNDrop(new VoodooControl("i", "css", ".dashlet-row li .fa-arrows-v"));
		VoodooUtils.waitForReady();

		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("a", "css", "div.dashboard.edit a[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Go to Active tasks dashlet and click + icon to create task
		new VoodooControl("i", "css", ".dashlet-row li .dashlet-header .fa.fa-plus").click();
		new VoodooControl("a", "css", ".dashlet-row li .dashlet-header .dropdown-menu li").click();
		VoodooUtils.waitForReady();

		// Entering the subject in the task subject field
		sugar().tasks.createDrawer.getEditField("subject").set(testName);

		// Assert that the value of the fields Status = Not Started, related to = Lead and Related lead Name 
		sugar().tasks.createDrawer.getEditField("status").assertEquals(customData.get("relatedLeadStatus"), true);
		sugar().tasks.createDrawer.getEditField("relRelatedToParentType").assertEquals(customData.get("relatedToModule"), true);
		sugar().tasks.createDrawer.getEditField("relRelatedToParent").assertEquals(sugar().leads.getDefaultData().get("fullName"), true);
		sugar().tasks.createDrawer.save();

		// TODO: VOOD-963 - Some dashboard controls are needed
		// Assert the text of the "To Do" tab
		VoodooControl toDo = new VoodooControl("span", "css", ".dashlet-row li .dashlet-tabs-row div:nth-child(3) a");
		toDo.assertContains(customData.get("dashletTabName"), true);
		toDo.click();

		// Assert that ToDo tab displays "1" as count
		new VoodooControl("span", "css", ".dashlet-row li .dashlet-tabs-row div:nth-child(3) .count").assertContains(customData.get("toDoCount"), true);		

		// The task record created via dashlet is displayed in "Active tasks" dashlet's "To do" section
		new VoodooControl("a", "css", ".dashlet-row li .active p a:nth-child(2)").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}