package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_28056 extends SugarTest {	
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.contacts.api.create();
		sugar.login();
	}

	/**
	 *  Verify that "Related To" field is saving the value
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_28056_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to create a new call record
		sugar.calls.navToListView();
		sugar.calls.listView.create();
		sugar.calls.createDrawer.getEditField("name").set(testName);

		// Choose "Related to" field as: Accounts, choose an existing Account record
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.accounts.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(sugar.accounts.getDefaultData().get("name"));

		// Save Call.
		sugar.calls.createDrawer.save();

		// Observe the detail view of this call
		sugar.calls.listView.clickRecord(1);

		// Verify that 'Related to' field should be displayed as: Account chosen
		sugar.calls.recordView.getDetailField("relatedToParentType").assertEquals(sugar.accounts.moduleNameSingular, true);
		sugar.calls.recordView.getDetailField("relatedToParentName").assertEquals(sugar.accounts.getDefaultData().get("name"), true);

		// Edit call
		sugar.calls.recordView.edit();

		// Choose "Related to" as: Contacts, choose an existing Contact record
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(sugar.contacts.getDefaultData().get("firstName"));

		// Save Call
		sugar.calls.recordView.save();

		// Verify that 'Related to' field should be displayed as: Contact chosen 
		sugar.calls.recordView.getDetailField("relatedToParentType").assertEquals(sugar.contacts.moduleNameSingular, true);
		sugar.calls.recordView.getDetailField("relatedToParentName").assertEquals(sugar.contacts.getDefaultData().get("salutation")+" "+sugar.contacts.getDefaultData().get("firstName")+" "+sugar.contacts.getDefaultData().get("lastName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}