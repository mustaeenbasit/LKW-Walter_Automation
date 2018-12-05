package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_27714 extends SugarTest {
	CallRecord myCall;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		// Initialize test records
		sugar().leads.api.create();
		myCall = (CallRecord)sugar().calls.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that when a Contact/Lead is selected Related to, the Contact/Lead is appearing in Guests field
	 * @throws Exception
	 */
	@Test
	public void Calls_27714_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Relate call to contact
		sugar().navbar.navToModule(sugar().calls.moduleNamePlural);
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		VoodooControl callRelType = sugar().calls.recordView.getEditField("relatedToParentType");
		VoodooControl callRelName = sugar().calls.recordView.getEditField("relatedToParentName");
		callRelType.set(sugar().contacts.moduleNameSingular);
		callRelName.set(myContact.get("lastName"));
		sugar().calls.recordView.save();

		// Verify that the Contact is automatically appearing in Guests field, as a call invitee. 
		FieldSet fs = new FieldSet();
		String contactName = myContact.getRecordIdentifier();
		String leadName = sugar().leads.getDefaultData().get("lastName");
		fs.put("name", contactName);
		sugar().calls.recordView.verifyInvitee(2, fs);
		
		// Verify that preview the calls from listview, the Contact is in Guests field.
		sugar().calls.navToListView();
		sugar().calls.listView.previewRecord(1);
		
		// Verify that preview should have Contact in the Assigned/Guests field
		VoodooControl callPreviewRelName = sugar().previewPane.getPreviewPaneField("relatedToParentName");
		callPreviewRelName.assertContains(contactName, true);
		// TODO: VOOD-1880 - Need Lib Support to verify guest in Call/Meetings CreateDrawer/Preview Pane/Edit modes
		new VoodooControl("span", "css", ".detail.fld_invitees .participants .row:nth-child(3) .cell.name").assertContains(myContact.getRecordIdentifier(), true);

		// Edit the call by removing the Contact, select a Lead in "Related To".
		// Remove contact from guest list
		myCall.removeInvitee(myContact);
		
		// Relate lead to call
		sugar().calls.recordView.edit();
		callRelType.set(sugar().leads.moduleNameSingular);
		callRelName.set(leadName);
		sugar().calls.recordView.save();
		
		// Verify that Lead is Invited
		fs.put("name", leadName);
		sugar().calls.recordView.verifyInvitee(2, fs);
		
		// Navigate to Preview in list view
		sugar().calls.navToListView();
		sugar().calls.listView.previewRecord(1);
		sugar().previewPane.showMore();
		
		// Verify that preview should not have Contact in the Assigned/Guests field, but should have Lead as Assigned/one invitee.
		callPreviewRelName.assertContains(contactName, false);
		callPreviewRelName.assertContains(leadName, true);
		// TODO: VOOD-1880 - Need Lib Support to verify guest in Call/Meetings CreateDrawer/Preview Pane/Edit modes
		new VoodooControl("span", "css", ".detail.fld_invitees").assertContains(contactName, false);
		new VoodooControl("span", "css", ".detail.fld_invitees .participants .row:nth-child(3) .cell.name").assertContains(leadName, true);
			
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}