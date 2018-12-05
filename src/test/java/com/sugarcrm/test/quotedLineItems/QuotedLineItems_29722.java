package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_29722 extends SugarTest {
	ContactRecord myContact;
	VoodooControl qliNameCtrl, qliContactsNameCtrl;

	public void setup() throws Exception {
		sugar().quotedLineItems.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();

		// Login as an Admin user
		sugar().login();

		// Enabling module QLI
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		// One QLI must have a 'Contact Name' field filled up with a contact
		// TODO: VOOD-444
		qliNameCtrl = sugar().quotedLineItems.createDrawer.getEditField("name");
		qliContactsNameCtrl = sugar().quotedLineItems.createDrawer.getEditField("relContactName");
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.create();
		qliNameCtrl.set(testName);
		qliContactsNameCtrl.set(myContact.getRecordIdentifier());
		sugar().quotedLineItems.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that proper Contact name should be displayed while merging or duplicate checks for QLI records. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_29722_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click Create to create a new one
		sugar().quotedLineItems.listView.create();

		// Enter QLI name such that it shows the QLI with contact name (in pre-requisite) as a duplicate on click of Save button.
		qliNameCtrl.set(testName);
		sugar().quotedLineItems.createDrawer.getControl("saveButton").click();

		// Click 'Select' link of the QLI shown as a duplicate
		sugar().quotedLineItems.createDrawer.selectAndEditDuplicate(1);

		// Verify that the User should see Contact name being displayed for contact field
		qliContactsNameCtrl.assertEquals(myContact.get("fullName"), true);

		// Cancel the create drawer
		sugar().quotedLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}