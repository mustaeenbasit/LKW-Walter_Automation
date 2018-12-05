package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_24249 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		//Create contact record
		sugar().contacts.api.create();
	}

	/**
	 * TC24249: Verify that "check for duplicates" functionality works correctly for Contacts
	 *
	 * @author jenniferxia
	 */
	@Test
	public void Contacts_24249_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts list view
		sugar().contacts.navToListView();

		// Click create button
		sugar().contacts.listView.create();

		DataSource ds = testData.get(testName);

		// Fill in the first name and the last name of contact record
		sugar().contacts.createDrawer.getEditField("firstName").set(ds.get(0).get("first_name"));
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(0).get("last_name"));

		// Click save button
		sugar().contacts.createDrawer.save();

		// Verify successful message is shown and no duplicate message is displayed
		// TODO VOOD-683
		new VoodooControl("div", "css", "div.alert.alert-success.alert-block").assertExists(true);
		new VoodooControl("div", "css", "div#alerts").assertElementContains(ds.get(0).get("save_msg"), true);
		new VoodooControl("div", "css", "div#alerts").assertElementContains(ds.get(0).get("dup_msg"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
