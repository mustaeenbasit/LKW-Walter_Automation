package com.sugarcrm.test.targetlists;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.TargetRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class TargetLists_26621 extends SugarTest {
	StandardSubpanel accountsSubpanel, contactsSubpanel, leadsSubpanel, targetsSubpanel, usersSubpanel;
	AccountRecord myAccount;
	ContactRecord myContact;
	LeadRecord myLead;
	TargetRecord myTarget;
	UserRecord qaUser;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar.accounts.api.create();
		myContact = (ContactRecord)sugar.contacts.api.create();
		myLead = (LeadRecord)sugar.leads.api.create();
		myTarget = (TargetRecord)sugar.targets.api.create();
		sugar.targetlists.api.create();
		sugar.login();
		qaUser = new UserRecord(sugar.users.getQAUser());

		// Add accounts, contacts, leads, target and user in the created target list
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.clickRecord(1);
		accountsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.accounts.moduleNamePlural);
		accountsSubpanel.linkExistingRecord(myAccount);
		contactsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
		leadsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);
		targetsSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.targets.moduleNamePlural);
		targetsSubpanel.linkExistingRecord(myTarget);
		
		// TODO: VOOD-662 [Jenkins required, scroll up to make User Subpanel full visible]
		sugar.targetlists.recordView.subpanels.get(sugar.accounts.moduleNamePlural).hover();
		
		usersSubpanel = sugar.targetlists.recordView.subpanels.get(sugar.users.moduleNamePlural);
		usersSubpanel.linkExistingRecord(qaUser);   
	}

	/**
	 * Copy target list not only fields but also recipients
	 * @throws Exception
	 */
	@Test
	public void TargetLists_26621_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// In action list, select Copy action
		sugar.targetlists.recordView.copy();

		// In the edit view, change the name of the target list record become a unique name.Save it.
		sugar.targetlists.createDrawer.getEditField("targetlistName").set(testName);
		sugar.targetlists.createDrawer.save();
		sugar.alerts.waitForLoadingExpiration();

		// Verify the new target list record is created with the new name.
		sugar.targetlists.recordView.getDetailField("targetlistName").assertEquals(testName, true);

		// Verify all the records in the original target list record are appearing in the new target list record's sub panel.
		// Accounts
		FieldSet verificationFS = new FieldSet();
		verificationFS.put("name", myAccount.getRecordIdentifier());
		verificationFS.put("workPhone", sugar.accounts.getDefaultData().get("workPhone"));
		accountsSubpanel.scrollIntoViewIfNeeded(false);
		accountsSubpanel.expandSubpanel();
		accountsSubpanel.scrollIntoViewIfNeeded(false);
		accountsSubpanel.verify(1, verificationFS, true);
		verificationFS.clear();

		// Contacts
		verificationFS.put("fullName", myContact.getRecordIdentifier());
		verificationFS.put("phoneWork", sugar.contacts.getDefaultData().get("phoneWork"));
		contactsSubpanel.scrollIntoViewIfNeeded(false);
		contactsSubpanel.expandSubpanel();
		contactsSubpanel.scrollIntoViewIfNeeded(false);
		contactsSubpanel.verify(1, verificationFS, true);
		verificationFS.clear();

		// Leads
		verificationFS.put("fullName", myLead.getRecordIdentifier());
		verificationFS.put("leadSource", sugar.leads.getDefaultData().get("leadSource"));
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.expandSubpanel();
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.verify(1, verificationFS, true);
		verificationFS.clear();

		// Target
		verificationFS.put("firstName", myTarget.getRecordIdentifier());
		verificationFS.put("title", sugar.targets.getDefaultData().get("title"));
		targetsSubpanel.scrollIntoViewIfNeeded(false);
		targetsSubpanel.expandSubpanel();
		targetsSubpanel.scrollIntoViewIfNeeded(false);
		targetsSubpanel.verify(1, verificationFS, true);
		verificationFS.clear();

		// User
		verificationFS.put("userName",qaUser.get("userName") );
		usersSubpanel.scrollIntoViewIfNeeded(false);
		usersSubpanel.expandSubpanel();
		usersSubpanel.scrollIntoViewIfNeeded(false);
		usersSubpanel.verify(1, verificationFS , true);
		verificationFS.clear();

		// Verify all the fields in the original target list record are appearing in the new target list record. 
		sugar.targetlists.recordView.getDetailField("targetlistName").assertEquals(testName, true);
		sugar.targetlists.recordView.getDetailField("description").assertEquals(sugar.targetlists.getDefaultData().get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}