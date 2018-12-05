package com.sugarcrm.test.dashlets;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TaskRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29822 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		// Create 10 task records
		customDS = testData.get(testName);
		sugar().tasks.api.create(customDS);

		// Create 11th task assigned to other user
		TaskRecord otherTask = (TaskRecord) sugar().tasks.api.create();

		// Login as admin
		sugar().login();

		// Edit owner of 11th task to other user
		FieldSet editTaskFS = new FieldSet();
		editTaskFS.put("relAssignedTo", sugar().users.getQAUser().get("userName"));
		otherTask.edit(editTaskFS);
		editTaskFS.clear();
	}

	/**
	 * Verify that active task dashlet should work as per the settings
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29822_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Home Dashboard
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().dashboard.edit();

		// Add a new dashlet
		sugar().dashboard.addRow();
		sugar().dashboard.addDashlet(4, 1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// Add active tasks dashlet
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("input", "css", ".span4.search").set( dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".span8 div div span.detail.fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify dashlet shows "10" in upcoming tab header
		// TODO: VOOD-960 - Dashlet selection 
		int intNumberOfRecords = customDS.size();
		VoodooControl upcomingTabHeaderCtrl = new VoodooControl("span", "css", ".layout_Home.span4 div.dashlet-tabs.tab3 div div:nth-child(2) a span");
		upcomingTabHeaderCtrl.assertContains(String.format("%s", intNumberOfRecords), true);

		// Click on upcoming tab in dashlet
		upcomingTabHeaderCtrl.click();

		// Verify count of list items in dashlet
		VoodooControl tasksListCtrl = new VoodooControl("a", "css", ".tab-content li");
		Assert.assertTrue("Number of tasks in Dashlet is not 10", tasksListCtrl.count() == intNumberOfRecords);

		// Verify "More Tasks.." button footer does not exist
		new VoodooControl("a", "css", ".layout_Home.span4 li:nth-child(4) .block-footer").assertExists(false);

		// Verify Other Users Task is not listed
		tasksListCtrl.assertContains(sugar().tasks.getDefaultData().get("subject"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}