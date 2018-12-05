package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_17530 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify that 'list view' is Initial state of the on activity stream/list view toggle button
	 *  on each module with the list view.
	 */
	@Test
	public void Contacts_17530_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Contacts listview
		sugar().contacts.navToListView();

		// Check the unique identifier on the contacts 'list view' contains the "name" of default state for "activities stream/list view" toggle button
		new VoodooControl("th", "css", "th[data-fieldname='full_name']").assertContains("Name", true);

		// Go to Accounts listview
		sugar().accounts.navToListView();

		// Check the correct module is shown next to Module tab to ensure the 'list view' is the default state for "activities stream/list view" toggle button
		new VoodooControl("th", "css", "th[data-fieldname='name']").assertContains("Name", true);

		// Go to Opportunities listview
		sugar().opportunities.navToListView();

		// Check the correct module is shown next to Module tab to ensurethe 'list view' is the default state for "activities stream/list view" toggle button
		new VoodooControl("th", "css", "th[data-fieldname='name']").assertContains("Name", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
