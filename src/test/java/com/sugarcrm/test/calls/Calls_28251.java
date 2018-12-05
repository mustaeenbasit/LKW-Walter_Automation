package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28251 extends SugarTest {
	VoodooControl guestFieldCtrl;

	public void setup() throws Exception {
		sugar().contacts.api.create();

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that "Related To" Contact is removed in the Guest field for all children and parent record
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_28251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet CallData = testData.get(testName).get(0);

		// Create a Daily recurrence call by selecting "Related To" has a contact record.
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("repeatType").set(CallData.get("repeat_type"));
		sugar().calls.createDrawer.getEditField("repeatEndType").set(CallData.get("repeat_end_type"));
		sugar().calls.createDrawer.getEditField("repeatOccur").set(CallData.get("repeat_count"));
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().contacts.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("firstName"));

		// Save the Call
		sugar().calls.createDrawer.save();

		for(int i=1;i<= Integer.parseInt(CallData.get("repeat_count"));i++){
			// In List view, preview of of the above repeat meetings.
			sugar().calls.listView.previewRecord(i);

			// Verify that in the "Related to" field, see the Contact. 
			sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(sugar().contacts.getDefaultData().get("salutation")+" "+sugar().contacts.getDefaultData().get("firstName")+" "+sugar().contacts.getDefaultData().get("lastName"), true);

			// TODO: VOOD-1243
			//  Verify that in the Guest field, QAUser can see the Contact and QAUser.
			guestFieldCtrl = new VoodooControl("div", "css", "div[name='invitees'] .participants");
			guestFieldCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
			guestFieldCtrl.assertContains(sugar().contacts.getDefaultData().get("salutation")+" "+sugar().contacts.getDefaultData().get("firstName")+" "+sugar().contacts.getDefaultData().get("lastName"), true);
		}

		// TODO: VOOD-757
		// "Edit All Recurrences" for the above call.
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", "[name='edit_recurrence_button']").click();
		sugar().alerts.waitForLoadingExpiration();

		// TODO: VOOD-1221
		// Click on - for the Contact in the Guest field.
		new VoodooControl("button", "css", ".fld_invitees.edit div div:nth-child(5) div.cell.buttons button").click();

		// TODO: VOOD-1243
		// Verify that the Contact is removed from the Guest field.
		new VoodooControl("div", "css", "div[data-module='Contacts']").assertVisible(false);

		// Save Call
		sugar().calls.recordView.save();
		sugar().calls.navToListView();

		for(int i=1;i<= Integer.parseInt(CallData.get("repeat_count"));i++){
			// In List view, preview of of the above repeat call.
			sugar().calls.listView.previewRecord(i);

			// Verify that in the "Related to" field, still see the Contact. 
			sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(sugar().contacts.getDefaultData().get("salutation")+" "+sugar().contacts.getDefaultData().get("firstName")+" "+sugar().contacts.getDefaultData().get("lastName"), true);

			// Verify that in the Guest field, QAUser can see only QAUser.  
			guestFieldCtrl.assertContains(sugar().users.getQAUser().get("userName"), true);
			guestFieldCtrl.assertContains(sugar().contacts.getDefaultData().get("salutation")+" "+sugar().contacts.getDefaultData().get("firstName")+" "+sugar().contacts.getDefaultData().get("lastName"), false);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}