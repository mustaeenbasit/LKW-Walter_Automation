package com.sugarcrm.test.contacts;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_17663 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify Primary Address is in a single line for street, city, zip, state and country
	 * @throws Exception
	 */
	@Test
	public void Contacts_17663_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.showMore();

		// Verify primary address data - all are having individual controls
		sugar().contacts.recordView.getDetailField("primaryAddressStreet").assertEquals(myContact.get("primaryAddressStreet"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressCity").assertEquals(myContact.get("primaryAddressCity"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressState").assertEquals(myContact.get("primaryAddressState"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressPostalCode").assertEquals(myContact.get("primaryAddressPostalCode"), true);
		sugar().contacts.recordView.getDetailField("primaryAddressCountry").assertEquals(myContact.get("primaryAddressCountry"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
