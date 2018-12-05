package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_24154 extends SugarTest{

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify that creating a contact can be cancelled
	 *
	 *  @author jenniferxia
	 */
	@Test
	public void Contacts_24154_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		sugar().contacts.navToListView();

		// Create a new contact
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("firstName").set(ds.get(0).get("first_name"));
		sugar().contacts.createDrawer.getEditField("lastName").set(ds.get(0).get("last_name"));

		// Cancel creating contact
		sugar().contacts.createDrawer.cancel();

		// Verify return to list view and no contact is created
		sugar().contacts.listView.getControl("createButton").assertVisible(true);

		new VoodooControl("div", "css", "div.flex-list-view-content").assertContains(ds.get(0).get("first_name"), false);
		new VoodooControl("div", "css", "div.flex-list-view-content").assertContains(ds.get(0).get("last_name"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
