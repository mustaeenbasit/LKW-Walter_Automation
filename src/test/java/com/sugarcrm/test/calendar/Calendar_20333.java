package com.sugarcrm.test.calendar;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Calendar_20333 extends SugarTest {
	ContactRecord myContactRecord;

	public void setup() throws Exception {
		myContactRecord = (ContactRecord) sugar.contacts.api.create();
		sugar.login();

		// Click "Calendar" navigation tab.
		sugar.navbar.navToModule(sugar.calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Access 08:00 time slot and click Schedule Call from warning
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar.alerts.getWarning().clickLink(1);

		// Call Subject data and save.
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();
	}

	/**
	 * Verify that invitees can be added to a call
	 * @throws Exception
	 */
	@Test
	public void Calendar_20333_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click any call subject in Calendar view.
		// TODO: VOOD-863
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "css", ".week div[time='08:00am'] div.head div:nth-child(2)").click();
		VoodooUtils.focusDefault();
		sugar.alerts.waitForLoadingExpiration();

		// Add a Users or a Contacts in Guests field.
		// TODO: VOOD-1345
		sugar.calls.recordView.edit();
		VoodooControl addInviteeCtrl = new VoodooControl("button", "css", ".fld_invitees.edit [data-action='addRow']");
		VoodooSelect inviteeSearchCtrl = new VoodooSelect("input", "css", "#select2-drop div input");

		// Add invitee Contacts
		addInviteeCtrl.click();
		inviteeSearchCtrl.set(myContactRecord.getRecordIdentifier());

		// Add invitee Users
		addInviteeCtrl.click();
		inviteeSearchCtrl.set(sugar.users.qaUser.get("userName"));

		// Click "Save" button on Call Edit view page.
		sugar.calls.recordView.save();

		// Navigate to List View of Calls module.
		sugar.calls.navToListView();

		// Click Activity Stream.
		sugar.calls.listView.getControl("activityStream").click();

		// Verify that the added users or contacts are displayed in the Activity Stream.
		sugar.calls.listView.activityStream.assertCommentContains("Linked" + " " + sugar.contacts.getDefaultData().get("fullName") + " " + "to" + " " + testName, 1, true);
		sugar.calls.listView.activityStream.assertCommentContains("Linked" + " " + sugar.users.qaUser.get("userName") + " " + "to" + " " + testName, 2, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}