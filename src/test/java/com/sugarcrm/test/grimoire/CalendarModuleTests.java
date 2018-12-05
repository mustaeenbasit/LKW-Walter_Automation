package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class CalendarModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyCreateViaMenu() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCreateViaMenu()...");

		// Verify menu items are present under Calendar menu
		sugar().navbar.navToModule("Calendar");
		sugar().navbar.clickModuleDropdown(sugar().calendar);
		sugar().calendar.menu.getControl("scheduleMeeting").assertVisible(true);
		sugar().calendar.menu.getControl("scheduleCall").assertVisible(true);
		sugar().calendar.menu.getControl("createTask").assertVisible(true);
		sugar().calendar.menu.getControl("today").assertVisible(true);

		// Verify "Schedule Meeting" menu item is functional
		sugar().calendar.menu.getControl("scheduleMeeting").click();
		sugar().meetings.createDrawer.getEditField("name").assertVisible(true);
		sugar().meetings.createDrawer.cancel();

		// Verify "Schedule Call" menu item is functional
		sugar().navbar.clickModuleDropdown(sugar().calendar);
		sugar().calendar.menu.getControl("scheduleCall").click();
		sugar().calls.createDrawer.getEditField("name").assertVisible(true);
		sugar().calls.createDrawer.cancel();

		// Verify "Create Task" menu item is functional
		sugar().navbar.clickModuleDropdown(sugar().calendar);
		sugar().calendar.menu.getControl("createTask").click();
		sugar().tasks.createDrawer.getEditField("subject").assertVisible(true);
		sugar().tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("verifyCreateViaMenu() complete.");
	}

	public void cleanup() throws Exception {}
}