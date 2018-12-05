package com.sugarcrm.test.roles;

import java.util.HashMap;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21251 extends SugarTest {
	FieldSet customFs = new FieldSet();
	UserRecord chrisUser;
	HashMap<String, String> myTaskRecords = new HashMap<>();

	public void setup() throws Exception {
		customFs = testData.get(testName).get(0);

		// Create two Account records with different name
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().login();

		// Create user Chris via UI due to API not set user status to Active
		chrisUser = (UserRecord) sugar().users.create();

		// Create a role
		AdminModule.createRole(customFs);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set only five fields to "Read only"
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#contentTable tbody tr td table:nth-child(9) tbody tr td:nth-child(1) table tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();

		// Storing css attribute as key=>value in HashMap 
		myTaskRecords.put("account_typelink", "flc_guidaccount_type");
		myTaskRecords.put("campaign_namelink", "flc_guidcampaign_name");
		myTaskRecords.put("emaillink", "flc_guidemail");
		myTaskRecords.put("employeeslink", "flc_guidemployees");
		myTaskRecords.put("industrylink", "flc_guidindustry");
		myTaskRecords.put("namelink", "flc_guidname");
		myTaskRecords.put("phone_officelink", "flc_guidphone_office");
		myTaskRecords.put("annual_revenuelink", "flc_guidannual_revenue");
		myTaskRecords.put("created_by_namelink", "flc_guidcreated_by_name");
		myTaskRecords.put("email1link", "flc_guidemail1");
		myTaskRecords.put("facebooklink", "flc_guidfacebook");
		myTaskRecords.put("invalid_emaillink", "flc_guidinvalid_email");
		myTaskRecords.put("ownershiplink", "flc_guidownership");
		myTaskRecords.put("ratinglink", "flc_guidrating");
		myTaskRecords.put("assigned_user_namelink", "flc_guidassigned_user_name");
		myTaskRecords.put("descriptionlink", "flc_guiddescription");
		myTaskRecords.put("email2link", "flc_guidemail2");
		myTaskRecords.put("modified_by_namelink", "flc_guidmodified_by_name");
		myTaskRecords.put("parent_namelink", "flc_guidparent_name");
		myTaskRecords.put("duns_numlink", "flc_guidduns_num");
		myTaskRecords.put("email_opt_outlink", "flc_guidemail_opt_out");
		myTaskRecords.put("googlepluslink", "flc_guidgoogleplus");
		myTaskRecords.put("my_favoritelink", "flc_guidmy_favorite");
		myTaskRecords.put("phone_faxlink", "flc_guidphone_fax");
		myTaskRecords.put("sic_codelink", "flc_guidsic_code");
		
		// Set all field permissions to "Read Only"
		// TODO: VOOD-856
		for(String key : myTaskRecords.keySet()) {
			VoodooControl notSetTextCtrl = new VoodooControl("div", "id", key);
			notSetTextCtrl.click();
			VoodooControl selectPermission = new VoodooControl("select", "id", myTaskRecords.get(key));
			if(!selectPermission.queryVisible())
				notSetTextCtrl.click();
			selectPermission.set(customFs.get("read_only"));
			VoodooUtils.waitForReady();
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(customFs);
		AdminModule.assignUserToRole(chrisUser);

		// Assign Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Field permissions set to Read Only on Accounts - List and detail view of owned records.
	 * @throws Exception
	 */
	@Test
	public void Roles_21251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();

		// Verify that all accounts are displayed on the list view of Accounts.
		sugar().accounts.listView.verifyField(1, "name", testName);
		sugar().accounts.listView.verifyField(2, "name", sugar().accounts.getDefaultData().get("name"));

		// Go to recordView
		// After clicking on Edit button user can see all fields as "Read Only" (he is not available to change them all). Save, Cancel etc. buttons are also presented on the page
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		// Verify that the records should not be editable on detailView inline edit.
		for(String key : myTaskRecords.keySet()) {
			int charLength = key.length()-4;
			String fieldName = key.substring(0, charLength);
			new VoodooControl ("i", "css", "span[data-name='"+fieldName+"'] .fa.fa-pencil").assertVisible(false);
		}
		
		sugar().accounts.recordView.edit();
		
		// Also verify Cancel, Save buttons
		sugar().accounts.recordView.getControl("saveButton").assertExists(true);
		sugar().accounts.recordView.getControl("cancelButton").assertExists(true);
		
		// After clicking on Edit button user can see all fields as "Read Only" (he is not available to change them all)
		sugar().accounts.recordView.getEditField("name").assertExists(false);
		sugar().accounts.recordView.getEditField("workPhone").assertExists(false);
		sugar().accounts.recordView.getEditField("emailAddress").assertExists(false);
		sugar().accounts.recordView.getEditField("industry").assertExists(false);
		sugar().accounts.recordView.getEditField("type").assertExists(false);
		sugar().accounts.recordView.getEditField("description").assertExists(false);
		sugar().accounts.recordView.getEditField("employees").assertExists(false);
		sugar().accounts.recordView.getEditField("ownership").assertExists(false);
		sugar().accounts.recordView.getEditField("workPhone").assertExists(false);
		sugar().accounts.recordView.getEditField("sicCode").assertExists(false);
		sugar().accounts.recordView.getEditField("rating").assertExists(false);
		sugar().accounts.recordView.getEditField("annualRevenue").assertExists(false);
		sugar().accounts.recordView.getEditField("fax").assertExists(false);
		
		// Go to next rocord
		sugar().accounts.recordView.cancel();
		sugar().accounts.recordView.gotoNextRecord();
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		
		// Verify that Save, Cancel buttons are also presented on the page
		sugar().accounts.recordView.getControl("saveButton").assertExists(true);
		sugar().accounts.recordView.getControl("cancelButton").assertExists(true);
		
		// Verify that after clicking on Edit button user can see all fields as "Read Only" (he is not available to change them all)
		sugar().accounts.recordView.getEditField("name").assertExists(false);
		sugar().accounts.recordView.getEditField("workPhone").assertExists(false);
		sugar().accounts.recordView.getEditField("emailAddress").assertExists(false);
		sugar().accounts.recordView.getEditField("industry").assertExists(false);
		sugar().accounts.recordView.getEditField("type").assertExists(false);
		sugar().accounts.recordView.getEditField("description").assertExists(false);
		sugar().accounts.recordView.getEditField("employees").assertExists(false);
		sugar().accounts.recordView.getEditField("ownership").assertExists(false);
		sugar().accounts.recordView.getEditField("workPhone").assertExists(false);
		sugar().accounts.recordView.getEditField("sicCode").assertExists(false);
		sugar().accounts.recordView.getEditField("rating").assertExists(false);
		sugar().accounts.recordView.getEditField("annualRevenue").assertExists(false);
		sugar().accounts.recordView.getEditField("fax").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}