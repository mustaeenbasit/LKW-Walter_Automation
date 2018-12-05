package com.sugarcrm.test.contacts;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_23500 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify that contact can be created through "Create Contact" mega menu.
	 *  @throws Exception
	 */
	@Test
	public void Contacts_23500_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// Go to Contacts module
		sugar().contacts.navToListView();

		// Click "Create Contact" link in Navigation shortcuts
		sugar().navbar.selectMenuItem(sugar().contacts,ds.get(0).get("menu"));

		//  Enter all the mandatory fields like "Last Name" & "Team" fields
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(0).get("lastname"));

		// Click 'Save' Button
		sugar().contacts.createDrawer.save();

		// Select the created record from the list view
		sugar().contacts.listView.clickRecord(1);

		// TODO VOOD-581
		// Verify that the contact detail information is displayed as entered on "Contact Record View" page.
		sugar().contacts.recordView.getDetailField("fullName").assertContains(ds.get(0).get("lastname"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
