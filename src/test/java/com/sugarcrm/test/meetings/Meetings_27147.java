package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27147 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		sugar().meetings.api.create();
		customFS = testData.get(testName).get(0);
		sugar().login();

		// TODO: VOOD-563
		// In login(Admin) user's profile, in External Account, create Webex and save successfully.
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab5").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "eapm_assigned_user_create_button").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("select", "id", "application").click();
		VoodooUtils.waitForReady();
		new VoodooControl("", "css", "#application option:nth-child(3)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "name").set(customFS.get("name"));
		new VoodooControl("input", "id", "password").set(customFS.get("password"));
		new VoodooControl("input", "id", "url").set(customFS.get("url"));
		new VoodooControl("input", "id", "EditViewSave").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
	}

	/**
	 * Verify that non-User "Join Webex" receive error message
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_27147_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit the Meeting and set type is "Webex".
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("type").set(customFS.get("meetingType"));

		// Verify that the Assigned User is the login(Admin) user.
		sugar().meetings.recordView.getEditField("assignedTo").assertEquals(customFS.get("loginUser"), true);

		// Save Meeting
		sugar().meetings.recordView.save();

		// Logout as Admin and Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// TODO: VOOD-563
		// Go to Meetings module.Select the same Webex meeting.In action drop down, click on "Join Webex".
		sugar().meetings.navToListView();
		sugar().meetings.listView.openRowActionDropdown(1);
		
		// TODO: VOOD-1222
		new VoodooControl("a", "css", "div[data-voodoo-name='recordlist'] tbody tr:nth-of-type(1) span[data-voodoo-name='join_button'] a").click();

		// An error message pop up.Verify the message is "Error You are not able to join this Meeting because you are not an Invitee."
		sugar().alerts.getError().assertContains(customFS.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}