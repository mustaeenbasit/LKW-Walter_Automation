package com.sugarcrm.test.tags;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28663 extends SugarTest {
	public void setup() throws Exception {
		// Login with QAUser
		sugar.login(sugar.users.getQAUser());
	}

	/**
	 * Verify Assigned To user is set correctly on a tag when it a new tag is added to a new record
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28663_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Accounts module -> Create a new record and add a new tag -> Save
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.showMore();
		sugar.accounts.createDrawer.getEditField("name").set(sugar.accounts.getDefaultData().get("name"));
		sugar.accounts.createDrawer.getEditField("tags").set(testName);
		sugar.accounts.createDrawer.save();

		// Navigate to the Tags module list view
		sugar.tags.navToListView();

		// Verify that the 'Assigned To' user is set correctly on the tag (and it is displayed correctly on Tags module List View)
		sugar.tags.listView.getDetailField(1, "name").assertEquals(testName, true);
		sugar.tags.listView.getDetailField(1, "relAssignedTo").assertEquals(sugar.users.getQAUser().get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}