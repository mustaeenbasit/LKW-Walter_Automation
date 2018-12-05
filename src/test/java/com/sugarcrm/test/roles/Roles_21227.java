package com.sugarcrm.test.roles;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Roles_21227 extends SugarTest {
	FieldSet roleRecordData;
	ArrayList<Record> myContacts;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName).get(0);
		myContacts = new ArrayList<Record>();
		sugar().accounts.api.create();
		myContacts.add(sugar().contacts.api.create()); // Create a contact and assigned it to QAUser
		FieldSet contactsName = new FieldSet();
		contactsName.put("lastName", roleRecordData.get("lastName"));
		myContacts.add(sugar().contacts.api.create(contactsName));
		contactsName.clear();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// For Contacts module, Set "Primary Address Street" to "Owner Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().contacts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();

		// Set "Primary Address Street" to "Owner Read/Owner Write"
		new VoodooControl("div", "id", "primary_addresslink").click();
		new VoodooControl("select", "id", "flc_guidprimary_address").set(roleRecordData.get("access"));
		VoodooUtils.waitForReady();

		// Save Role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set "Primary Address Street" to "Owner Read/Owner Write" on Contacts - it can't view the address of non-owned contact by "Copy Address" button in account's detailview
	 * @throws Exception
	 */
	@Test
	public void Roles_21227_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Assigned the Contact record to QAUser(Owned record for QAUser)
		// TODO: VOOD-1475 (Create record from API and edit record)
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(2);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().contacts.recordView.save();

		// Goto "Accounts" module
		sugar().accounts.navToListView();

		// Go to account record and select one owned contact and one non-owned contact in Contacts subpanel.
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contactSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecords(myContacts);

		// In the Account's record view, click the one of "Copy Address" buttons,which are next to the address fields
		// TDOD: VOOD-555
		VoodooControl copyAddressCtrl = new VoodooControl("input", "css", ".fld_copy input");
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		copyAddressCtrl.set(Boolean.toString(true));
		sugar().accounts.recordView.save();

		// Click on the Non-Owned contact from subpanel to view detail view
		sugar().accounts.recordView.subpanels.get(sugar().opportunities.moduleNamePlural).scrollIntoViewIfNeeded(false); // Need to scroll
		contactSubpanel.clickRecord(2);

		// Verify that it can't view the primary address of non-owned contact.
		// TODO: VOOD-1445
		VoodooControl primaryAddressNoAccessCtrl = new VoodooControl("sapn", "css", ".fld_primary_address span");
		sugar().contacts.recordView.getDetailField("primaryAddressStreet").assertExists(false);
		primaryAddressNoAccessCtrl.assertContains(roleRecordData.get("noAccess"), true);

		// Verify that the user can't able click "Copy Address" button alternate address in Contact's record view
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		copyAddressCtrl.isDisabled();
		sugar().contacts.recordView.cancel();

		// Verify that it can view the primary address of owned Contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.getDetailField("primaryAddressStreet").assertContains(sugar().contacts.getDefaultData().get("primaryAddressStreet"), true);

		// Verify that the user is able to click "Copy Address" button alternate address in Contact's record view
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		copyAddressCtrl.assertChecked(false);
		copyAddressCtrl.set(Boolean.toString(true));
		copyAddressCtrl.assertChecked(true);
		sugar().contacts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}