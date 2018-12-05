package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_create extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void Contacts_create_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ContactRecord myContact= (ContactRecord)sugar().contacts.create();

		myContact.verify();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}