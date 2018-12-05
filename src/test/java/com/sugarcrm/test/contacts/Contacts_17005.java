package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_17005 extends SugarTest {
	FieldSet contactData;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		contactData = testData.get(testName).get(0);
		sugar().login();
	}

	/** Verify auto duplicate check on both first name and last name while creating a contact
	 * @throws Exception
	 */
	@Test
	public void Contacts_17005_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts list view
		sugar().contacts.navToListView();

		// Click create button
		sugar().contacts.listView.create();

		// Fill in start letter of first name and last name of default data
		sugar().contacts.createDrawer.getEditField("firstName").set(contactData.get("firstName"));
		sugar().contacts.createDrawer.getEditField("lastName").set(contactData.get("lastName"));

		// Click save button
		sugar().contacts.createDrawer.save();

		// Verify the duplicate
		sugar().contacts.createDrawer.getControl("duplicateCount").assertContains(contactData.get("duplicateCount"), true);
		sugar().contacts.createDrawer.getControl("duplicateHeaderRow").assertExists(true);

		// TODO: VOOD-566 to support field access in duplicate check
		new VoodooControl("div", "css", "div[data-voodoo-name='dupecheck-list-edit'] span[data-voodoo-name='full_name'] div").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);
		sugar().contacts.createDrawer.ignoreDuplicateAndSave();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
