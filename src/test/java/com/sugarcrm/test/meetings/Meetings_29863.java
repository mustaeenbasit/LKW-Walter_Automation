package com.sugarcrm.test.meetings;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_29863 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that record created through single search & select drawer are selected by default
	 * @throws Exception
	 */
	@Test
	public void Meetings_29863_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Meetings create drawer
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Clicking Add Invitee and navigating on Contact search and select drawer
		sugar().meetings.recordView.clickAddInvitee();
		VoodooSelect searchInvitee = (VoodooSelect) sugar().meetings.recordView.getControl("searchInvitee");
		searchInvitee.clickSearchForMore();

		// Creating a Contacts record through contact search & select drawer
		sugar().contacts.searchSelect.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);

		// TODO: VOOD-632 - Selectors fails if one drawer is created on top of other
		new VoodooControl("a", "css", ".drawer.active .fld_save_button  a").click();
		VoodooUtils.waitForReady();

		// Asserting the added contact on Meetings guest list 
		sugar().meetings.createDrawer.getControl("invitees").assertContains(testName, true);
		sugar().meetings.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}