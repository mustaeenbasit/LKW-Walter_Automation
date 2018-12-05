package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28368 extends SugarTest {
	DataSource leadData,contactData,customData;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		contactData = testData.get(testName+"_conData");
		customData = testData.get(testName+"_subpanelLimit");
		sugar.leads.api.create(leadData);
		sugar.contacts.api.create(contactData);

		sugar.login();
		// Navigating to Admin > System Settings to change the "Subpanel items per page" back to 5 i.e default.
		sugar.admin.setSystemSettings(customData.get(0)); 

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
	 * Verify that Copy function should copy all invitees to the new call when Subpanel per page is changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28368_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		// Click on "More Guests" link
		// TODO: VOOD-1354 Need Lib Support for "More Guest..." link in calls/meetings sidecar module recordView
		VoodooControl moreGuestsCtrl = new VoodooControl("button", "css", "[data-voodoo-name='invitees'] [data-action='show-more']");
		moreGuestsCtrl.click();
		VoodooUtils.waitForReady(); // For More Guests to load
		moreGuestsCtrl.click(); // TR-8076 Need to list all the guests otherwise copy function doesn't work properly

		// Select "Copy"
		sugar.calls.recordView.copy();
		sugar.calls.createDrawer.getEditField("name").set(testName);
		sugar.calls.createDrawer.save();

		// Verify total 7 guests(+1 Administrator) in the copied call in record view
		VoodooControl inviteeCtrl = sugar.calls.recordView.getControl("invitees");
		// Verify by default 2invitees(+1 Administrator) appear in the Guest Field
		for (int i = 0; i < 2; i++){
			inviteeCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
		}
		inviteeCtrl.assertContains("Administrator", true);

		// Click on "More Guests" link
		moreGuestsCtrl.click();

		// Verify totally 6 invitees appear
		inviteeCtrl.assertContains("Administrator", true);
		for (int i = 0; i < 2; i++){
			inviteeCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			inviteeCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		inviteeCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(2).get("lastName"), true);

		// Click on "More Guests" link
		moreGuestsCtrl.click();

		// Verify total 8 invitees appear
		inviteeCtrl.assertContains("Administrator", true);
		for (int i = 0; i < 2; i++){
			inviteeCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			inviteeCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		inviteeCtrl.assertContains(sugar.users.qaUser.get("userName"), true);

		// Verify No more "More Guests" link appear
		moreGuestsCtrl.assertVisible(false);

		// Look at the copied call in preview in Listview
		sugar.calls.navToListView();
		sugar.calls.listView.previewRecord(1);

		// Verify 7 guests(+1 Administrator) in the copied call in preview in List View
		// TODO: VOOD-1428
		VoodooControl moreGuestPreviewCtrl = new VoodooControl("button", "css", "[name='invitees'] [data-action='show-more']");
		VoodooControl guestFieldCtrl = new VoodooControl("div", "css", "div[name='invitees'] .participants");

		// Verify by default 3 invitees appear
		guestFieldCtrl.assertContains("Administrator", true);
		for (int i = 0; i < 2; i++){
			guestFieldCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
		}

		// Click on "More Guests" link
		moreGuestPreviewCtrl.click();
		VoodooUtils.waitForReady();

		// Verify total 6 invitees appear
		guestFieldCtrl.assertContains("Administrator", true);
		for (int i = 0; i < 2; i++){
			guestFieldCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			guestFieldCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		guestFieldCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(2).get("lastName"), true);

		// Click on "More Guests" link
		moreGuestPreviewCtrl.click();

		// Verify total 8 invitees appear
		guestFieldCtrl.assertContains("Administrator", true);
		for (int i = 0; i < 3; i++){
			guestFieldCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			guestFieldCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		guestFieldCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		// Verify No more "More Guests" link appear
		moreGuestPreviewCtrl.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}