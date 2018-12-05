package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27289 extends SugarTest {
		
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that meeting is related to the Contact record from quick create.
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27289_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		// Click on 'Schedule Meeting' by Quick Create menu.
		sugar().navbar.quickCreateAction(sugar().meetings.moduleNamePlural);
		
		// Fill in all required field and save.
		sugar().meetings.createDrawer.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		VoodooControl relParentType = sugar().meetings.createDrawer.getEditField("relatedToParentType");
		relParentType.set(sugar().contacts.moduleNameSingular);
		
		// Verify, Contact record appears in Related to field.
		relParentType.assertContains(sugar().contacts.moduleNameSingular, true);
		
		VoodooUtils.waitForReady(); // Wait for Ajax response onchange of above relatedToParentType dropdown
		sugar().meetings.createDrawer.getEditField("relatedToParentName").set(sugar().contacts.getDefaultData().get("firstName"));
		sugar().meetings.createDrawer.save();
		
		// Go to Meetings ListView
		sugar().meetings.navToListView();
		
		// Verify in listview, Meeting record is saved with the related Contact.
		sugar().meetings.listView.verifyField(1, "relatedToParentName", sugar().contacts.getDefaultData().get("lastName"));
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}