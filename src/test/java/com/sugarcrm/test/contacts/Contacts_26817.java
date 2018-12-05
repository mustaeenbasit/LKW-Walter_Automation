package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_26817 extends SugarTest {
	AccountRecord myAccount;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();

		// Login to Sugar
		sugar().login();
	}

	/** Verify that account record is populated automatically when create a new case of a contact
	 * @throws Exception
	 */
	@Test
	public void Contacts_26817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a contact with Account record(As Account is needed in the contact record, therefore create Contact record from UI instead of API)
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.createDrawer.save();

		// Go to Contacts module to open the contact in record view
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Click "+" to add a new case
		sugar().contacts.recordView.subpanels.get(sugar().cases.moduleNamePlural).addRecord();

		// Verify that Account field is populated with account related to contact
		sugar().cases.createDrawer.assertVisible(true);
		sugar().cases.createDrawer.getEditField("relAccountName").assertContains(myAccount.getRecordIdentifier(), true);

		// Cancel the Cases create drawer
		sugar().cases.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
