package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27292 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// TODO: VOOD-1282: No primary email inserted through REST call in create method inside RecordsModule.java 
		// Create Contact with valid email
		FieldSet contactEmail = new FieldSet();
		contactEmail.put("emailAddress", customData.get("contactEmail"));
		sugar().contacts.api.create(contactEmail);

		// Create Lead record with valid email
		FieldSet leadEmail = new FieldSet();
		leadEmail.put("emailAddress", customData.get("leadEmail"));
		sugar().leads.api.create(leadEmail);

		sugar().login();

		// Email Setting is setup in admin
		FieldSet emailSetupData = testData.get(testName + "_emailSetup").get(0);
		sugar().admin.setEmailServer(emailSetupData);
	}

	/**
	 * Verify that the user is not asked if he/she wants to send invitee emails when the user clicks
	 * save button while create a new meeting
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27292_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		String leadName = sugar().leads.getDefaultData().get("lastName");
		String contactName = sugar().contacts.getDefaultData().get("lastName");
		String qaUserName = sugar().users.getQAUser().get("userName");

		// In Guests fields, add Contact as invitees.
		VoodooControl parentType = sugar().meetings.createDrawer.getEditField("relatedToParentType");
		parentType.set(sugar().contacts.moduleNameSingular);
		VoodooControl parentName = sugar().meetings.createDrawer.getEditField("relatedToParentName");
		parentName.set(contactName);

		// In Guests fields, add Lead as invitees.
		parentType.set(sugar().leads.moduleNameSingular);
		parentName.set(leadName);

		// In Guests fields, add user as invitees.
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(qaUserName);
		sugar().meetings.createDrawer.save();

		// Verify that no pop up appears asking user "Do you want to save & send Invites?" when clicked on "Save" button.
		sugar().alerts.getAlert().assertContains(customData.get("assert"), false);
		sugar().meetings.listView.clickRecord(1);

		// Verify that new Meeting is saved with correct meeting info
		FieldSet guestFS = new FieldSet();
		guestFS.put("adminName", customData.get("admin"));
		sugar().meetings.recordView.verifyInvitee(1, guestFS);
		guestFS.clear();
		guestFS.put("leadName", leadName);
		sugar().meetings.recordView.verifyInvitee(2, guestFS);
		guestFS.clear();
		guestFS.put("userName", qaUserName);
		sugar().meetings.recordView.verifyInvitee(3, guestFS);
		guestFS.clear();
		guestFS.put("contactName", contactName);
		sugar().meetings.recordView.verifyInvitee(4, guestFS);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}