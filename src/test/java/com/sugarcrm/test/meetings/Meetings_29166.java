package com.sugarcrm.test.meetings;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29166 extends SugarTest {

	public void setup() throws Exception {
		// Log-in as chris user and create a dashboard with 2 "Planned Activites" dashlets
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Meetings : Verify that number of records in "Planned Activity" Dashlet matching with defined display row
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_29166_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Dashboard and add 2 dashlets (Planned Activities), one with 20 rows and other with 10 rows
		sugar().dashboard.clickCreate();
		sugar().dashboard.getControl("title").set(testName);
		DataSource dashletRows = testData.get(testName);
		for (int i = 0; i < dashletRows.size() - 1; i++) {
			sugar().dashboard.addRow();
			sugar().dashboard.addDashlet(i + 1, 1);

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
		String currentDate = date.plusMinutes(10).toString("MM/dd/yyyy");
		String repeatUntilDate = date.plusWeeks(2).toString("MM/dd/yyyy");

		// TODO: VOOD-670 - More Dashlet Support
		VoodooControl dashletAction = new VoodooControl("a", "css", ".row-fluid.sortable .dashlet-header a .fa.fa-plus");
		VoodooControl scheduleMeetingOption = new VoodooControl("a", "css", ".dropdown-menu li [data-dashletaction='createRecord']");
		VoodooControl startDate = sugar().meetings.createDrawer.getEditField("date_start_date");
		VoodooControl repeatType = sugar().meetings.createDrawer.getEditField("repeatType");
		VoodooControl meetingName = sugar().meetings.createDrawer.getEditField("name");
		VoodooControl firstDashletFutureTab = new VoodooControl("button", "css", ".row-fluid.sortable div .btn-group.dashlet-group [value='future']");
		VoodooControl secondDashletFutureTab = new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) div .btn-group.dashlet-group [value='future']");
		VoodooControl firstDashletTodayTab = new VoodooControl("button", "css", ".row-fluid.sortable div .btn-group.dashlet-group [value='today']");
		VoodooControl secondDashletTodayTab = new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) div .btn-group.dashlet-group [value='today']");
		VoodooControl configureButton = new VoodooControl("i", "css", ".row-fluid.sortable:nth-child(2) .dashlet-header button .fa.fa-cog");
		VoodooControl refreshOption = new VoodooControl("a", "css", ".row-fluid.sortable:nth-child(2) .dropdown-menu a[data-dashletaction='refreshClicked']");
		VoodooControl firstDashletMeetingCount = new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab.active .count");
		VoodooControl secondDashletMeetingCount = new VoodooControl("div", "css", ".row-fluid.sortable:nth-child(2) .dashlet-tab.active .count");
		VoodooControl firstDashletModuleName = new VoodooControl("div", "css", ".row-fluid.sortable .dashlet-tab.active");
		VoodooControl secondDashletModuleName = new VoodooControl("div", "css", ".row-fluid.sortable:nth-child(2) .dashlet-tab.active");
		String moduleMeetingSingular = sugar().meetings.moduleNameSingular;
		String moduleMeetingPlural = sugar().meetings.moduleNamePlural;

		// Schedule a meeting (from dashlet) that has recurrences for 2 weeks from today
		dashletAction.click();
		VoodooUtils.waitForReady();
		scheduleMeetingOption.click();
		VoodooUtils.waitForReady();

		// Enter the values in the fields to create meeting
		startDate.set(currentDate);
		repeatType.set(dashletRows.get(0).get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatUntil").set(repeatUntilDate);
		meetingName.set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.createDrawer.save();

		// Click future tab in first dashlet and assert the meeting count = 14
		firstDashletFutureTab.click();
		VoodooUtils.waitForReady();
		firstDashletMeetingCount.assertEquals(dashletRows.get(0).get("count"), true);
		firstDashletModuleName.assertContains(moduleMeetingPlural, true);

		// Click future tab in second dashlet and assert meeting count = 10+
		// Refresh the dashlet to view the meeting count in the second dashlet
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletFutureTab.click();
		VoodooUtils.waitForReady();
		secondDashletMeetingCount.assertEquals(dashletRows.get(1).get("count"), true);
		secondDashletModuleName.assertContains(moduleMeetingPlural, true);

		// Click today tab in the first dashlet and assert meeting count = 1
		firstDashletTodayTab.click();
		VoodooUtils.waitForReady();
		firstDashletMeetingCount.assertEquals(dashletRows.get(0).get("countToday"), true);
		firstDashletModuleName.assertContains(moduleMeetingSingular, true);

		// Click today tab in the second dashlet and assert meeting count = 1
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletTodayTab.click();
		VoodooUtils.waitForReady();
		secondDashletMeetingCount.assertEquals(dashletRows.get(0).get("countToday"), true);
		secondDashletModuleName.assertContains(moduleMeetingSingular, true);

		// Create meeting with 20 occurrences
		dashletAction.click();
		VoodooUtils.waitForReady();
		scheduleMeetingOption.click();
		VoodooUtils.waitForReady();
		startDate.set(currentDate);
		repeatType.set(dashletRows.get(0).get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatOccurType").set(dashletRows.get(0).get("repeatOccurrence"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(dashletRows.get(0).get("rowCount"));
		meetingName.set(testName);
		sugar().meetings.createDrawer.save();

		// Click future tab in first dashlet and assert meeting count = 20+
		firstDashletFutureTab.click();
		VoodooUtils.waitForReady();
		firstDashletMeetingCount.assertEquals(dashletRows.get(2).get("count"), true);
		firstDashletModuleName.assertContains(moduleMeetingPlural, true);

		// Click future tab in second dashlet and assert meeting count = 10+
		configureButton.click();
		refreshOption.click();
		VoodooUtils.waitForReady();
		secondDashletFutureTab.click();
		VoodooUtils.waitForReady();
		secondDashletMeetingCount.assertEquals(dashletRows.get(1).get("count"), true);
		secondDashletModuleName.assertContains(moduleMeetingPlural, true);

		// Click "show more" in first dashlet and assert meeting count = 33
		new VoodooControl("button", "css", ".row-fluid.sortable [data-voodoo-name='list-bottom'] button[data-action='show-more']").click();
		VoodooUtils.waitForReady();
		firstDashletMeetingCount.assertEquals(dashletRows.get(2).get("rowCount"), true);
		firstDashletModuleName.assertContains(moduleMeetingPlural, true);

		// Click "show more" in second dashlet and assert meeting count = 20+
		new VoodooControl("button", "css", ".row-fluid.sortable:nth-child(2) [data-voodoo-name='list-bottom'] button[data-action='show-more']").click();
		VoodooUtils.waitForReady();
		secondDashletMeetingCount.assertEquals(dashletRows.get(2).get("count"), true);
		secondDashletModuleName.assertContains(moduleMeetingPlural, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}