package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_29669 extends SugarTest {
	FieldSet customFS = new FieldSet();

	public void setup() throws Exception {
		sugar().contacts.api.create();
		customFS = testData.get(testName).get(0);
		sugar().login();

		// Go to contact listView and Update email filed
		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "emailAddress").set(customFS.get("email"));
		sugar().contacts.listView.saveRecord(1);
	}

	/**
	 * Verify that user is able to save email while email field is in error state. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contacts_29669_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to listView
		sugar().contacts.navToListView();
		
		// Click on Preview button.
		sugar().contacts.listView.previewRecord(1);
		
		// Click on Actions > Edit button
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "emailAddress").set(customFS.get("wrongEmail"));
		sugar().contacts.listView.saveRecord(1);

		// TODO: VOOD-1292
		new VoodooControl("i", "css", ".fld_email.edit .error-tooltip.add-on").hover();
		new VoodooControl("span", "css", ".fld_email.edit .error-tooltip.add-on").assertAttribute("data-original-title", "Error. Invalid Email Address: "+customFS.get("wrongEmail")+" ", true);
		sugar().contacts.listView.getEditField(1, "firstName").click();
		sugar().contacts.listView.getEditField(1, "emailAddress").set(customFS.get("email"));

		// Save record
		sugar().contacts.listView.saveRecord(1);

		// Verify that Email Address should be saved on clicking Save button.
		sugar().contacts.listView.getDetailField(1, "emailAddress").assertContains(customFS.get("email"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}