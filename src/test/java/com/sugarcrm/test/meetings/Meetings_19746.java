package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_19746 extends SugarTest {
	ContactRecord myCon;
	
	public void setup() throws Exception {
		myCon = (ContactRecord) sugar().contacts.api.create();
		sugar().login();

		// Create meeting and add Contact as invitee
		sugar().navbar.selectMenuItem(sugar().meetings, "create" + sugar().meetings.moduleNameSingular);
		sugar().alerts.waitForLoadingExpiration();
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(myCon.getRecordIdentifier());
		sugar().meetings.createDrawer.save();
	}

	/**
	 * Remove meeting's contact invitee. 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19746_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to meeting detail view, Click "-" icon removee invitee from guest list
		sugar().meetings.listView.clickRecord(1);
		VoodooControl inviteeCtrl = sugar().meetings.recordView.getControl("invitees");
		inviteeCtrl.assertContains(myCon.getRecordIdentifier(),true);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getControl("removeInvitee02").click();
		sugar().meetings.recordView.save();

		// Verify contact is removed from guest list
		inviteeCtrl.assertContains(sugar().contacts.getDefaultData().get("fullName"),false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}