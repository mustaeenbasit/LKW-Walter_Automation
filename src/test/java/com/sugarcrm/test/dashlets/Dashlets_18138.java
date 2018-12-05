package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_18138 extends SugarTest {
	FieldSet qaUser = new FieldSet();

	public void setup() throws Exception {
		qaUser = sugar().users.getQAUser();
		sugar().contacts.api.create();
		sugar().login();

		// Assign contact record to QAuer
		sugar().contacts.navToListView();
		sugar().contacts.listView.editRecord(1);
		sugar().contacts.listView.getEditField(1, "relAssignedTo").set(qaUser.get("userName"));
		sugar().contacts.listView.saveRecord(1);
		sugar().logout();
	}

	/**
	 * Multi-select (enum) field in Dashlet allow user to select all field names
	 * @throws Exception
	 */
	@Test
	public void Dashlets_18138_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login through Demo user
		sugar().login(qaUser);

		// TODO: VOOD-670 - More Dashlet Support
		// verify that contact record is displayed in "My Contacts" dashlet 
		VoodooControl nameFieldCtrl = new VoodooControl("a", "css", ".list.fld_full_name a");
		String contactName = sugar().contacts.getDefaultData().get("fullName");
		nameFieldCtrl.assertEquals(contactName, true);

		// Click Configure on My Contacts Dashlet
		new VoodooControl("div", "css", ".row-fluid.sortable:nth-of-type(3) .btn-group").click();
		VoodooUtils.waitForReady();

		// Click Edit from Menu
		new VoodooControl("a", "css", ".btn-group.open .dashlet-toolbar a").click();
		VoodooUtils.waitForReady();
		VoodooControl selectFieldCtrl = new VoodooControl("ul", "css", ".select2-choices.ui-sortable .select2-search-field.ui-sortable-handle");

		// Select all field names offered. 
		for (int i = 0; i < 4; i++) {
			selectFieldCtrl.click();
			new VoodooControl("div", "css", "#select2-drop .select2-highlighted div").click();
		}

		// Save the options selected
		new VoodooControl("a", "css",".drawer.active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify that contact record is still displayed in "My Contacts" dashlet 
		nameFieldCtrl.assertEquals(contactName, true);

		// Verify that 'Full Name' field is visible in listview header
		new VoodooControl("th", "css", ".orderByfull_name").assertVisible(true);

		// Verify that 'Account Name' field is visible in listview header
		new VoodooControl("th", "css", ".orderByaccount_name").assertVisible(true);

		// Verify that 'Office Phone' field is visible in listview header
		new VoodooControl("th", "css", ".orderByphone_work").assertVisible(true);

		// Verify that 'Title' field is visible in listview header
		new VoodooControl("th", "css", ".orderBytitle").assertVisible(true);

		// Verify that 'Email' field is visible in listview header
		new VoodooControl("th", "css", ".orderByemail").assertVisible(true);

		// Verify that 'User' field is visible in listview header
		new VoodooControl("th", "css", ".orderByassigned_user_name").assertVisible(true);

		// Verify that 'Date Modified' field is visible in listview header
		new VoodooControl("th", "css", ".orderBydate_modified").assertVisible(true);

		// Verify that 'Date Created' field is visible in listview header
		new VoodooControl("th", "css", ".orderBydate_entered").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}