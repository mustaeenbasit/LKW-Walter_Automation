package com.sugarcrm.test.targetlists;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_26618 extends SugarTest{
	TargetRecord myTarget;
	ContactRecord myContact;
	LeadRecord myLead;
	AccountRecord myAccount;
	UserRecord qaUser;
	StandardSubpanel targetsSubpanel, contactsSubpanel, accountsSubpanel, leadsSubpanel, usersSubpanel;

	public void setup() throws Exception {
		myAccount = (AccountRecord) sugar().accounts.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
		myTarget = (TargetRecord) sugar().targets.api.create();
		sugar().targetlists.api.create();
		qaUser =  new UserRecord(sugar().users.getQAUser());

		// Login as QAUser
		qaUser.login();		

		// Now populate the subpanels of the targetlist by linking records
		sugar().targetlists.navToListView();
		sugar().targetlists.listView.clickRecord(1);

		// Targets
		targetsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecord(myTarget);

		// Contacts
		contactsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);

		// Leads
		leadsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);

		// Users
		usersSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		usersSubpanel.linkExistingRecord(qaUser);

		// Accounts
		accountsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural);
		accountsSubpanel.linkExistingRecord(myAccount);
	}

	/**
	 * Copy targetlist for recipients
	 * 
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26618_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Copy an existing targetlist to a new targetlist
		sugar().targetlists.recordView.copy();
		sugar().targetlists.createDrawer.getEditField("targetlistName").set(testName);

		// Save and navigate to the record
		sugar().targetlists.createDrawer.save();

		// Verify the new target list record is created with the new name.
		sugar().targetlists.recordView.getDetailField("targetlistName").assertEquals(testName, true);

		// Verify subpanels of the new targetlist contains the same records
		// Targets
		FieldSet fs = new FieldSet();       
		fs.put("firstName", myTarget.getRecordIdentifier());
		sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural).expandSubpanel();
		sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural).verify(1, fs, true);
		fs.clear();

		// Contacts
		fs.put("fullName", myContact.getRecordIdentifier());
		sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural).expandSubpanel();
		sugar().targetlists.recordView.subpanels.get(sugar().contacts.moduleNamePlural).verify(1, fs, true);
		fs.clear();

		// Leads
		fs.put("fullName", myLead.getRecordIdentifier());
		sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural).expandSubpanel();
		sugar().targetlists.recordView.subpanels.get(sugar().leads.moduleNamePlural).verify(1, fs, true);
		fs.clear();

		// User
		fs.put("userName", qaUser.get("userName"));
		sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural).expandSubpanel();
		sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural).verify(1, fs, true);
		fs.clear();

		// Accounts
		fs.put("name", myAccount.getRecordIdentifier());
		sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural).expandSubpanel();
		sugar().targetlists.recordView.subpanels.get(sugar().accounts.moduleNamePlural).verify(1, fs, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 