package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21211 extends SugarTest {
	DataSource roleRecordData;
	CampaignRecord myCampaign;
	AccountRecord myAccount;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		FieldSet accountName = new FieldSet();
		accountName.put("name", roleRecordData.get(0).get("name"));
		myAccount = (AccountRecord) sugar().accounts.api.create(accountName);
		accountName.clear();
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();
		for(int i = 0; i < roleRecordData.size(); i++) {
			// Need to click again on fieldCtrl if permissionsCtrl is not visible as some time script fails to click.
			VoodooControl fieldCtrl = new VoodooControl("div", "id", roleRecordData.get(i).get("fields"));
			VoodooControl permissionsCtrl = new VoodooControl("select", "id", roleRecordData.get(i).get("permissions"));
			fieldCtrl.click();
			if(!permissionsCtrl.queryVisible())
				fieldCtrl.click();
			permissionsCtrl.set(roleRecordData.get(0).get("access"));
		}

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData.get(0));

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Read Owner Write on Accounts - Full form edit of owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21211_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create an Account(Owned record for QAUser)
		sugar().accounts.create();

		// Go to Accounts record owned by QAUser and click Edit button on the Account.
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		String qauser = sugar().users.getQAUser().get("userName");

		// Update all fields
		// Update all fields shows that fields are editable.Therefore, edit all the fields
		sugar().accounts.recordView.getEditField("annualRevenue").set(roleRecordData.get(0).get("fieldValues"));
		sugar().accounts.recordView.getEditField("ownership").set(roleRecordData.get(1).get("fieldValues"));
		sugar().accounts.recordView.getEditField("rating").set(roleRecordData.get(2).get("fieldValues"));
		sugar().accounts.recordView.getEditField("tickerSymbol").set(roleRecordData.get(3).get("fieldValues"));
		sugar().accounts.recordView.getEditField("relAssignedTo").set(roleRecordData.get(4).get("fieldValues"));
		sugar().accounts.recordView.getEditField("description").set(roleRecordData.get(5).get("fieldValues"));
		sugar().accounts.recordView.getEditField("memberOf").set(myAccount.getRecordIdentifier());
		sugar().accounts.recordView.getEditField("billingAddressStreet").set(roleRecordData.get(8).get("fieldValues"));
		sugar().accounts.recordView.getEditField("fax").set(roleRecordData.get(9).get("fieldValues"));
		sugar().accounts.recordView.getEditField("sicCode").set(roleRecordData.get(10).get("fieldValues"));
		sugar().accounts.recordView.getEditField("website").set(roleRecordData.get(11).get("fieldValues"));
		sugar().accounts.recordView.getEditField("type").set(roleRecordData.get(12).get("fieldValues"));
		sugar().accounts.recordView.getEditField("emailAddress").set(roleRecordData.get(14).get("fieldValues"));
		sugar().accounts.recordView.getEditField("employees").set(roleRecordData.get(15).get("fieldValues"));
		sugar().accounts.recordView.getEditField("industry").set(roleRecordData.get(16).get("fieldValues"));
		sugar().accounts.recordView.getEditField("name").set(roleRecordData.get(17).get("fieldValues"));
		sugar().accounts.recordView.getEditField("workPhone").set(roleRecordData.get(18).get("fieldValues"));
		sugar().accounts.recordView.getEditField("relTeam").set(qauser);

		// TODO: VOOD-555
		new VoodooSelect("span", "css", ".fld_campaign_name").set(myCampaign.getRecordIdentifier());
		new VoodooControl("input", "css", ".fld_twitter.edit input").set(roleRecordData.get(7).get("fieldValues"));

		// Click Save
		sugar().accounts.recordView.save();

		// Verify that all fields should be edited properly
		sugar().accounts.recordView.getDetailField("annualRevenue").assertEquals(roleRecordData.get(0).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("ownership").assertEquals(roleRecordData.get(1).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("rating").assertEquals(roleRecordData.get(2).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("tickerSymbol").assertEquals(roleRecordData.get(3).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertEquals(roleRecordData.get(4).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("description").assertEquals(roleRecordData.get(5).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("memberOf").assertEquals(myAccount.getRecordIdentifier(), true);
		sugar().accounts.recordView.getDetailField("billingAddressStreet").assertEquals(roleRecordData.get(8).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("fax").assertEquals(roleRecordData.get(9).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("sicCode").assertEquals(roleRecordData.get(10).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("website").assertEquals(roleRecordData.get(11).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("type").assertEquals(roleRecordData.get(12).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("emailAddress").assertContains(roleRecordData.get(14).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("employees").assertEquals(roleRecordData.get(15).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("industry").assertEquals(roleRecordData.get(16).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("name").assertEquals(roleRecordData.get(17).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("workPhone").assertEquals(roleRecordData.get(18).get("fieldValues"), true);
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(qauser, true);

		// TODO: VOOD-555
		new VoodooSelect("a", "css", ".fld_campaign_name").assertEquals(myCampaign.getRecordIdentifier(), true);
		new VoodooControl("div", "css", ".fld_twitter.detail").assertEquals(roleRecordData.get(7).get("fieldValues"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}