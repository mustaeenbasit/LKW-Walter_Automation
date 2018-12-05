package com.sugarcrm.test.calls;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_20371 extends SugarTest {
	ContactRecord con1;

	public void setup() throws Exception {
		con1 = (ContactRecord)sugar.contacts.api.create();
		sugar.accounts.api.create();
		sugar.login();
	}

	/**
	 * Relationship with other module_Verify that the Contact and Related to columns in Call list view are the selected ones when creating
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_20371_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		// Create a call
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(testName);
		//  Adding contacts as invitees
		sugar.calls.createDrawer.clickAddInvitee();
		sugar.calls.createDrawer.selectInvitee(con1);
		// Setting the call related to a record in "Accounts" module.
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.accounts.moduleNameSingular);
		FieldSet fs = sugar.accounts.getDefaultData();
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(fs.get("name"));
		// Save
		sugar.calls.createDrawer.save();

		// Click "Calls" link in navigation shortcuts.
		sugar.navbar.navToModule(sugar.calls.moduleNamePlural);

		// Verify Related to column is the selected one in Call list view 
		sugar.calls.listView.getDetailField(1, "relatedToParentName").assertEquals(fs.get("name"), true);

		// Verify Contact is the selected one in Detail view.
		sugar.calls.listView.clickRecord(1);
		fs.clear();
		fs.put("fullName", sugar.contacts.getDefaultData().get("fullName"));
		sugar.calls.recordView.verifyInvitee(2, fs);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}