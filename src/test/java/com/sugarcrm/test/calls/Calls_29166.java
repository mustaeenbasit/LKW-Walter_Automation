package com.sugarcrm.test.calls;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29166 extends SugarTest {

	public void setup() throws Exception {
		// Log-in as QAuser(Sally) user and create a dashboard with 2 "Planned Activites" dashlets
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Calls : Verify that number of records in "Planned Activity" Dashlet matching with defined display row
	 * @throws Exception
	 */
	@Test
	public void Calls_29166_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Dashboard and add 2 dashlets (Planned Activities), one with 20 rows and other with 10 rows
		sugar().dashboard.clickCreate();
		sugar().dashboard.getControl("title").set(testName);
		DataSource dashletRows = testData.get(testName);
		for (int i = 0; i < dashletRows.size()-1; i++) {
			sugar().dashboard.addRow();
			sugar().dashboard.addDashlet(i+1, 1);

			// TODO: VOOD-960 - Dashlet Selection
			new VoodooControl("input", "css", ".layout_Home .inline-drawer-header .search").set(dashletRows.get(0).get("plannedActivity"));
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".list-view .fld_title a").click();
			VoodooUtils.waitForReady();
			new VoodooSelect("div", "css", ".fld_limit div").set(dashletRows.get(i).get("rowCount"));
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".active .fld_save_button a").click();
			VoodooUtils.waitForReady();
		}
		sugar().dashboard.save();

		// Calculating the values for Current date & Repeat Until date
		DateTime date = DateTime.now();
		String currentDate = date.toString("MM/dd/yyyy");
		String repeatUntilDate = date.plusWeeks(2).toString("MM/dd/yyyy");

		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl dashletAction = new VoodooControl("a", "css", ".row-fluid.sortable .dashlet-header a .fa.fa-plus");
		VoodooControl scheduleCallOption = new VoodooControl("a", "css", ".dropdown-menu li:nth-child(2) [data-dashletaction='createRecord']");
		VoodooControl startDate = sugar().calls.createDrawer.getEditField("date_start_date");
		VoodooControl repeatType = sugar().calls.createDrawer.getEditField("repeatType");
		VoodooControl callName = sugar().calls.createDrawer.getEditField("name");
		VoodooControl firstDashletFutureTab = new VoodooControl("button", "css", ".row-fluid.sortable div .btn-group.dashlet-group [value='future']");
		VoodooControl secondDashletFutureTab = new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) div .btn-group.dashlet-group [value='future']");
		VoodooControl firstDashletTodayTab = new VoodooControl("button", "css", ".row-fluid.sortable div .btn-group.dashlet-group [value='today']");
		VoodooControl secondDashletTodayTab = new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) div .btn-group.dashlet-group [value='today']");
		VoodooControl configureButton = new VoodooControl("i", "css", ".row-fluid.sortable:nth-child(2) .dashlet-header button .fa.fa-cog");
		VoodooControl refreshOption = new VoodooControl("a", "css", ".row-fluid.sortable:nth-child(2) .dropdown-menu a[data-dashletaction='refreshClicked']");
		VoodooControl firstDashletCallCount = new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab:nth-child(2) .count");
		VoodooControl secondDashletCallCount = new VoodooControl("div", "css", ".row-fluid.sortable:nth-child(2) .dashlet-tab:nth-child(2) .count");
		VoodooControl firstDashletModuleName = new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab:nth-child(2)");
		VoodooControl secondDashletModuleName = new VoodooControl("div", "css", ".row-fluid.sortable:nth-child(2) .dashlet-tab:nth-child(2)");
		String moduleCallSingular = sugar().calls.moduleNameSingular;
		String moduleCallPlural = sugar().calls.moduleNamePlural;

		// Schedule a call (from dashlet) that has recurrences for 2 weeks from today
		dashletAction.click();
		VoodooUtils.waitForReady();
		scheduleCallOption.click();
		VoodooUtils.waitForReady();

		// Enter the values in the fields to create call
		startDate.set(currentDate);
		repeatType.set(dashletRows.get(0).get("repeatType"));
		sugar().calls.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		callName.set(sugar().calls.getDefaultData().get("name"));
		sugar().calls.createDrawer.save();

		// Click future tab in first dashlet and assert the call count = 14
		firstDashletFutureTab.click();
		VoodooUtils.waitForReady();
		firstDashletCallCount.assertEquals(dashletRows.get(0).get("count"), true);
		firstDashletModuleName.assertContains(moduleCallPlural, true);

		// Click future tab in second dashlet and assert call count = 10+
		// Refresh the dashlet to view the call count in the second dashlet
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletFutureTab.click();
		VoodooUtils.waitForReady();
		secondDashletCallCount.assertEquals(dashletRows.get(1).get("count"), true);
		secondDashletModuleName.assertContains(moduleCallPlural, true);

		// Click today tab in the first dashlet and assert call count = 1
		firstDashletTodayTab.click();
		VoodooUtils.waitForReady();
		firstDashletCallCount.assertEquals(dashletRows.get(0).get("countToday"), true);
		firstDashletModuleName.assertContains(moduleCallSingular, true);

		// Click today tab in the second dashlet and assert call count = 1
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletTodayTab.click();
		VoodooUtils.waitForReady();
		secondDashletCallCount.assertEquals(dashletRows.get(0).get("countToday"), true);
		secondDashletModuleName.assertContains(moduleCallSingular, true);

		// Create call with 20 occurrences
		dashletAction.click();
		VoodooUtils.waitForReady();
		scheduleCallOption.click();
		VoodooUtils.waitForReady();
		startDate.set(currentDate);
		repeatType.set(dashletRows.get(0).get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(dashletRows.get(0).get("repeatOccurrence"));
		sugar().calls.createDrawer.getEditField("repeatOccur").set(dashletRows.get(0).get("rowCount"));
		callName.set(testName);
		sugar().calls.createDrawer.save();

		// Click future tab in first dashlet and assert call count = 20+
		firstDashletFutureTab.click();
		VoodooUtils.waitForReady();
		firstDashletCallCount.assertEquals(dashletRows.get(2).get("count"), true);
		firstDashletModuleName.assertContains(moduleCallPlural, true);

		// Click future tab in second dashlet and assert call count = 10+
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletFutureTab.click();
		VoodooUtils.waitForReady();
		secondDashletCallCount.assertEquals(dashletRows.get(1).get("count"), true);
		secondDashletModuleName.assertContains(moduleCallPlural, true);

		// Click "show more" in first dashlet and assert call count = 33
		firstDashletModuleName.click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", ".row-fluid.sortable [data-voodoo-name='list-bottom'] button[data-action='show-more']").click();
		VoodooUtils.waitForReady();
		firstDashletCallCount.assertEquals(dashletRows.get(2).get("rowCount"), true);
		firstDashletModuleName.assertContains(moduleCallPlural, true);

		// Click "show more" in second dashlet and assert call count = 20+
		secondDashletModuleName.click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) [data-voodoo-name='list-bottom'] button[data-action='show-more']").click();
		VoodooUtils.waitForReady();
		secondDashletCallCount.assertEquals(dashletRows.get(2).get("count"), true);
		secondDashletModuleName.assertContains(moduleCallPlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}