package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28366 extends SugarTest {
	DataSource leadData,contactData;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		contactData = testData.get(testName+"_conData");
		sugar.leads.api.create(leadData);
		sugar.contacts.api.create(contactData);

		sugar.login();
		// Create call & add 7 invitees
		sugar.calls.navToListView();		
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(sugar.calls.moduleNameSingular);
		// 3 Lead Invitees
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.leads.moduleNameSingular);
		for (int i=0; i<3; i++){
			sugar.calls.createDrawer.getEditField("relatedToParentName").set(leadData.get(i).get("lastName"));
		}
		// 3 Contact Invitees
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		for (int i=0; i<3; i++){
			sugar.calls.createDrawer.getEditField("relatedToParentName").set(contactData.get(i).get("lastName"));
		}
		// 1 user invitee
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(sugar.users.qaUser.get("userName"));
		sugar.calls.createDrawer.save();
	}

	/**
	 * Verify that Copy function should copy all invitees to the new meeting/calls
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28366_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in Meetings/Calls sidecar module recordView
		VoodooControl moreGuestsCtrl = new VoodooControl("button", "css", "[data-action='show-more']");
		moreGuestsCtrl.click();

		// Select "Copy"
		sugar.calls.recordView.copy();
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();
		moreGuestsCtrl.click();

		// Verify 7 guests in the copied call in record view
		for (int i = 0; i < 3; i++){
			sugar.calls.recordView.getControl("invitees").assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			sugar.calls.recordView.getControl("invitees").assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		sugar.calls.recordView.getControl("invitees").assertContains(sugar.users.qaUser.get("userName"), true);

		// Look at the copied meeting/call in preview in Listview
		sugar.calls.navToListView();
		sugar.calls.listView.previewRecord(1);

		// Verify 7 guests in the copied call in preview in List View
		// Click on "More Guests" in preview
		moreGuestsCtrl.click();
		// TODO: VOOD-1428
		VoodooControl guestFieldCtrl = new VoodooControl("div", "css", "div[name='invitees'] .participants");
		for (int i = 0; i < 3; i++){
			guestFieldCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			guestFieldCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		guestFieldCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}