package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_26693 extends SugarTest {
	DataSource ds;
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);

		// Create Contact record with default data
		myContact = (ContactRecord)sugar().contacts.create();
	}

	/**
	 * Verify a Contact record for which the email address field
	 * contains an apostrophe can be saved without error
	 */
	@Test
	public void Contacts_26693_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open contact record from the listview
		myContact.navToRecord();
		sugar().alerts.waitForLoadingExpiration();

		// Edit the contact record and set the email address value
		sugar().contacts.recordView.edit();
		new VoodooControl("input", "css", ".fld_email.edit input").set(ds.get(0).get("email"));
		sugar().contacts.recordView.save();

		// Verify the email address value is displayed as expected upon save
		new VoodooControl("a", "css", ".fld_email.detail a").assertContains(ds.get(0).get("email"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
