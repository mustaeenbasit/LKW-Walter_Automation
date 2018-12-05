package com.sugarcrm.test.roles;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_29724 extends SugarTest {
	FieldSet rolesData = new FieldSet();
	AccountRecord myAccounts;

	public void setup() throws Exception {
		myAccounts = (AccountRecord) sugar().accounts.api.create();
		sugar().contacts.api.create();
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		rolesData = testData.get(testName).get(0);

		// Login
		sugar().login();

		// Create a Role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Set Contacts/Opportunities/RLI modules with View access as 'Owner'
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_view div select").set(rolesData.get("accessOwner"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_Opportunities_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Opportunities_view div select").set(rolesData.get("accessOwner"));
		new VoodooControl("div", "css", "td#ACLEditView_Access_RevenueLineItems_view div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_RevenueLineItems_view div select").set(rolesData.get("accessOwner"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// User must have few Contacts/Opportunities/RLIs (Assigned to Admin) already saved
		// TODO: VOOD-444
		// Link the Contact record with the Account record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccounts.getRecordIdentifier());
		sugar().alerts.getWarning().confirmAlert();
		sugar().contacts.recordView.save();
		sugar().opportunities.create();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
	}

	/**
	 * Verify that directly related fields of a module are click able when 'View' access is 'Owner'. 
	 * 
	 * @throws Exception
	 */
	@Ignore("SC-5549 - Directly related fields of a module are not clickable when 'View' access is 'Owner'.")
	@Test
	public void Roles_29724_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Logout from Admin user and Login as the User (assigned with the Role)
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to Contacts module and observe records which are not assigned to him
		sugar().contacts.navToListView();

		// Verify that the User must find related field i.e. 'Accounts' as click able on the list view
		VoodooControl accountInAccountlistViewCtrl = sugar().contacts.listView.getDetailField(1, "relAccountName");
		String accountName = myAccounts.getRecordIdentifier();
		VoodooControl accountNameCtrl = sugar().accounts.recordView.getDetailField("name");
		accountInAccountlistViewCtrl.assertEquals(accountName, true);
		accountInAccountlistViewCtrl.click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(accountName, true);

		// Now go to Opportunities module and observe records which are not assigned to him
		sugar().opportunities.navToListView();

		// Verify that the User must find related field i.e. 'Accounts' as click able on the list view
		VoodooControl accountInOpprtunityListViewCtrl = sugar().opportunities.listView.getDetailField(1, "relAccountName");
		accountInOpprtunityListViewCtrl.assertEquals(accountName, true);
		accountInOpprtunityListViewCtrl.click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(accountName, true);

		// Now go to RLI module and observe records which are not assigned to him
		sugar().revLineItems.navToListView();

		// For Non-Owned Opportunity 
		// Verify that the User must find related fields i.e. 'Accounts' as click able on the list view and 'Opportunities(non-owned)' NOT CLICK ABLE.
		VoodooControl accountInRliListViewCtrl = sugar().revLineItems.listView.getDetailField(1, "relAccountName");
		VoodooControl opportunityInRliListViewCtrl = sugar().revLineItems.listView.getDetailField(1, "relOpportunityName");
		// Verify that the Opportunity is not click able for NON OWNED Opportunity Record 
		// TODO: VOOD-1843, VOOD-1349 and VOOD-1882
		new VoodooControl("div", "css", ".fld_opportunity_name.list div").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);
		opportunityInRliListViewCtrl.assertExists(false);
		accountInRliListViewCtrl.assertEquals(accountName, true);
		accountInRliListViewCtrl.click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(accountName, true);

		// Create an Opportunity (Owner: QAuser)
		// TODO: VOOD-444
		FieldSet newRecordsData = new FieldSet();
		newRecordsData.put("name", rolesData.get("name"));
		newRecordsData.put("rli_name", testName);
		sugar().opportunities.create(newRecordsData);
		newRecordsData.clear();

		// Now go to RLI module and observe records which are not assigned to him (For OWNED record)
		sugar().revLineItems.navToListView();

		// For Owned Opportunity 
		// Verify that the User must find related fields i.e. 'Opportunities(owner)' as click able on the list view
		opportunityInRliListViewCtrl.assertEquals(rolesData.get("name"), true);
		opportunityInRliListViewCtrl.click();
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.getDetailField("name").assertEquals(rolesData.get("name"), true);

		// Verify that the User must find related fields i.e. 'Account' as click able on the list view
		// Now go to RLI module again
		sugar().revLineItems.navToListView();
		accountInRliListViewCtrl.assertEquals(accountName, true);
		accountInRliListViewCtrl.click();
		VoodooUtils.waitForReady();
		accountNameCtrl.assertEquals(accountName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}