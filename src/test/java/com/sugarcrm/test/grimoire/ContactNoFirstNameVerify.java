package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class ContactNoFirstNameVerify extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyNoContactFirstName() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyNoContactFirstName()...");

		FieldSet contactDataNoFirstName = new FieldSet();
		contactDataNoFirstName.put("lastName", "LastNameOnly");

		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.setFields(contactDataNoFirstName);
		sugar().contacts.createDrawer.save();

		ContactRecord myContact = new ContactRecord(contactDataNoFirstName);
		myContact.verify();

		VoodooUtils.voodoo.log.info("verifyNoContactFirstName() complete.");
	}

	public void cleanup() throws Exception {}
}