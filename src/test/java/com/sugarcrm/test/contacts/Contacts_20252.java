package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_20252 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Navigation and Cancel from menu Quick Create Contacts
	 *
	 * @throws Exception
	 */
	@Test
	public void Contacts_20252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Quick create menu is implemented
		sugar().navbar.quickCreateAction(sugar().contacts.moduleNamePlural);
		sugar().contacts.createDrawer.getEditField("lastName").assertVisible(true);
		sugar().contacts.createDrawer.cancel();

		// Verify Contact is not created
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertIsEmpty();
	}

	public void cleanup() throws Exception {}
}
