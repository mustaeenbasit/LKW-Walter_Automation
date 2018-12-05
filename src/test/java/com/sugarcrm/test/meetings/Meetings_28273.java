package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_28273 extends SugarTest {
	DataSource leadData;
	FieldSet meetingData;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		meetingData = testData.get(testName + "_meetingData").get(0);
		sugar().leads.api.create(leadData);

		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that subpanel limit is respected in Guests field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_28273_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		for (int i=0; i<6; i++){
			sugar().meetings.createDrawer.getEditField("relatedToParentName").set(leadData.get(i).get("lastName"));
		}
		sugar().meetings.createDrawer.save();

		sugar().meetings.listView.clickRecord(1);
		// Verify 5 guests in the guest field
		for (int i=0; i<5; i++){
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}	

		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more[data-action='show-more']").click();
		sugar().alerts.waitForLoadingExpiration();
		// Verify 2 more guests in the guest field
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(5).get("lastName"), true);
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().users.getQAUser().get("userName"), true);

		sugar().meetings.navToListView();
		// qaUser create a repeat meeting and has 7 guests
		sugar().meetings.listView.create();
		sugar().meetings.createDrawer.getEditField("name").set(meetingData.get("name"));
		sugar().meetings.createDrawer.getEditField("repeatType").set(meetingData.get("repeatType"));
		sugar().meetings.createDrawer.getEditField("repeatEndType").set(meetingData.get("repeatEndType"));
		sugar().meetings.createDrawer.getEditField("repeatOccur").set(meetingData.get("repeatOccur"));
		sugar().meetings.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		for (int i=0; i<6; i++){
			sugar().meetings.createDrawer.getEditField("relatedToParentName").set(leadData.get(i).get("lastName"));
		}
		sugar().meetings.createDrawer.save();

		sugar().meetings.listView.clickRecord(1);
		// Verify 5 guests in the guest field
		for (int i=0; i<5; i++){
			sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}	

		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		new VoodooControl("button", "css", ".btn-link.btn-invisible.more[data-action='show-more']").click();
		sugar().alerts.waitForLoadingExpiration();
		// Verify 2 more guests in the guest field
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().leads.getDefaultData().get("salutation")+" "+sugar().leads.getDefaultData().get("firstName")+" "+leadData.get(5).get("lastName"), true);
		sugar().meetings.recordView.getControl("invitees").assertContains(sugar().users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}