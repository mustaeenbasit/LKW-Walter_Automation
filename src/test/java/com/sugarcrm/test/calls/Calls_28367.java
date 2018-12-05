package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28367 extends SugarTest {
	DataSource leadData,contactData;
	FieldSet callData;

	public void setup() throws Exception {
		leadData = testData.get(testName);
		contactData = testData.get(testName+"_conData");
		callData = testData.get(testName+"_callData").get(0);
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
	 * Verify that Copy function should copy all invitees to the new repeat meeting/calls
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28367_execute() throws Exception {
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
		// Repeat Meeting
		sugar.calls.createDrawer.getEditField("repeatType").set(callData.get("repeatType"));
		sugar.calls.createDrawer.save();

		sugar.calls.navToListView();
		// TODO : VOOD-1416 countRows() method not working fine, Once VOOD-1416 is resolved Uncomment Line#70, #71
		// Verify 10 such copied meetings are created. 
		// int count = sugar.calls.listView.countRows();
		// Assert.assertTrue(count == 11);

		sugar.calls.listView.clickRecord(1);
		moreGuestsCtrl.click();
		VoodooControl inviteeCtrl = sugar.calls.recordView.getControl("invitees");
		// Verify 7 guests in the copied call in record view
		for (int i = 0; i < 3; i++){
			inviteeCtrl.assertContains(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+contactData.get(i).get("lastName"), true);
			inviteeCtrl.assertContains(sugar.leads.getDefaultData().get("salutation")+" "+sugar.leads.getDefaultData().get("firstName")+" "+leadData.get(i).get("lastName"), true);
		}
		sugar.calls.recordView.getControl("invitees").assertContains(sugar.users.qaUser.get("userName"), true);

		// Look at the copied call in preview in Listview
		sugar.calls.navToListView();
		sugar.calls.listView.previewRecord(1);

		// Verify 7 guests in the copied call in preview in List View
		// Click on "More Guests" in preview
		// TODO: VOOD-1428
		new VoodooControl("button", "css", "[name='invitees'] [data-action='show-more']").click();
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