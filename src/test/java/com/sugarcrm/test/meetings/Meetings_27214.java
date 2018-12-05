package com.sugarcrm.test.meetings;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27214 extends SugarTest {
	UserRecord myUser;

	public void setup() throws Exception {
		myUser = (UserRecord) sugar().users.api.create();
		sugar().contacts.api.create();
		sugar().leads.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that meeting invitee is able to be added in the meeting
	 * @throws Exception
	 */
	@Test
	public void Meetings_27214_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String leadName = sugar().leads.getDefaultData().get("lastName");
		String contactName = sugar().contacts.getDefaultData().get("lastName");
		sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));

		// Selecting contact
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(contactName);		

		// Selecting Lead
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(leadName);

		// Add user
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myUser.get("lastName"));

		// save meeting record
		sugar().meetings.createDrawer.save();

		// Open the newly created meeting record
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.showMore();

		// Verify 4 meeting participants, including qauser, in the meeting record view
		FieldSet guestFS = new FieldSet();
		guestFS.put("leadName", leadName);
		sugar().meetings.recordView.verifyInvitee(1, guestFS);
		guestFS.clear();
		guestFS.put("userName", myUser.get("lastName"));
		sugar().meetings.recordView.verifyInvitee(2, guestFS);
		guestFS.clear();
		guestFS.put("userName", sugar().users.getQAUser().get("userName"));
		sugar().meetings.recordView.verifyInvitee(3, guestFS);
		guestFS.clear();
		guestFS.put("contactName", contactName);
		sugar().meetings.recordView.verifyInvitee(4, guestFS);

		// Verify qauser as "Assigned To" and "Created By"
		sugar().meetings.recordView.getDetailField("assignedTo").assertContains(sugar().users.getQAUser().get("userName"), true);
		// TODO: VOOD-1223: Need library support to get records in Guests list in Meetings record view
		new VoodooControl("span", "css", ".fld_created_by_name.detail").assertContains(sugar().users.getQAUser().get("userName"), true);

		// Avatar or Module badge is at the left side of the invitee's name
		assertTrue("None of the 1st User Avatar or Module badge is visible", (new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][1]/div[@class='cell profile']/div[@class='container']/div[@class='avatar']/img").queryVisible() || 
				new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][1]/div[@class='cell profile']/div[@class='container']/div[@class='avatar']/span").queryVisible()));
		assertTrue("None of the 2nd User Avatar or Module badge is visible", (new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][2]/div[@class='cell profile']/div[@class='container']/div[@class='avatar']/img").queryVisible() || 
				new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][2]/div[@class='cell profile']/div[@class='container']/div[@class='avatar']/span").queryVisible()));
		assertTrue("None of the Lead Avatar or Module badge is visible", (new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] div.avatar > img").queryVisible() || 
				new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] div.avatar > span").queryVisible()));
		assertTrue("None of the Contact Avatar or Module badge is visible", (new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] div.avatar > img").queryVisible() || 
				new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] div.avatar > span").queryVisible()));

		// Status appears at the right side of the invitee's name
		new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][1]/div[@class='cell profile']/div[@class='container']/div[@class='status']").assertVisible(true);
		new VoodooControl("div", "xpath", "//div[@class='participants-schedule']/div[@data-module='Users'][2]/div[@class='cell profile']/div[@class='container']/div[@class='status']").assertVisible(true);
		new VoodooControl("div", "css", ".participants-schedule [data-module='Leads'] div.cell.profile div.container div.status").assertVisible(true);
		new VoodooControl("div", "css", ".participants-schedule [data-module='Contacts'] div.cell.profile div.container div.status").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}