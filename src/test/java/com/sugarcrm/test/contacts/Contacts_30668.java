package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contacts_30668 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * verify that HeaderPanel fields should be filled after show more link is clicked and action buttons should not disappear 
	 * @throws Exception
	 */
	@Test
	public void Contacts_30668_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl lastNameField = sugar().contacts.createDrawer.getEditField("lastName");
		String lastNameValue = sugar().contacts.getDefaultData().get("lastName");

		// Navigate to Contacts list view and click Create button
		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.create();

		// In the Contact's create drawer, click the "show more" link
		sugar().contacts.createDrawer.showMore();

		// Put focus on "Last name" and try to type something
		lastNameField.set(lastNameValue);

		// Assert that "Last name" field should contain typed value
		lastNameField.assertEquals(lastNameValue, true);

		// Assert that Action buttons (Cancel | Save) stay intact
		sugar().contacts.createDrawer.getControl("cancelButton").assertVisible(true);
		sugar().contacts.createDrawer.getControl("saveButton").assertVisible(true);

		// Cancel the create form
		sugar().contacts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}