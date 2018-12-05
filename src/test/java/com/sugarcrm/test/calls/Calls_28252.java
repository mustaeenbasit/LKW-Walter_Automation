package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28252 extends SugarTest {
	FieldSet qauser = new FieldSet();

	public void setup() throws Exception {
		// Creating Lead Record
		sugar().leads.api.create();

		// Login as regular user
		qauser = sugar().users.getQAUser();
		sugar().login(qauser);
	}

	/**
	 * Verify that "Related To" Lead is removed in the Guest field for all children and parent record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_28252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Calls List View
		sugar().calls.navToListView();	

		// Create a Recurring Call
		sugar().calls.listView.create();
		FieldSet callData = testData.get(testName).get(0);
		sugar().calls.createDrawer.getEditField("name").set(testName);

		// Setting Repeat Type as "Daily"
		sugar().calls.createDrawer.getEditField("repeatType").set(callData.get("repeatType"));
		sugar().calls.createDrawer.getEditField("repeatOccurType").set(callData.get("repeatOccurType"));

		// Setting "Related To" field
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().leads.moduleNameSingular);
		FieldSet leadData = sugar().leads.getDefaultData();
		String leadName = leadData.get("firstName") + " " + leadData.get("lastName");
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(leadName);

		// Click on "-" for the Lead in the Guest field.
		sugar().calls.createDrawer.getControl("removeInvitee02").click();
		VoodooUtils.waitForReady();

		// Verify that Lead is removed from the Guest field. 
		sugar().calls.createDrawer.getControl("invitees").assertContains(leadName, false);

		// Save
		sugar().calls.createDrawer.save();

		// Preview Call Record
		sugar().calls.listView.previewRecord(1);

		// Verify in GuestField qaUser can see only qaUser
		sugar().previewPane.getPreviewPaneField("assignedTo").assertEquals(qauser.get("userName"), true);

		// Verify in the "Related to" field, still see the Lead
		sugar().previewPane.getPreviewPaneField("relatedToParentName").assertEquals(leadData.get("salutation") + " " + leadName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}