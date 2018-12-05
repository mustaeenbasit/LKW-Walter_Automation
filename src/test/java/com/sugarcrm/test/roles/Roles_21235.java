package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21235 extends SugarTest {
	DataSource roleRecordData = new DataSource();
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();

		// Login
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Owner Read/Owner Write"
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
		VoodooUtils.waitForReady();

		// Logout from the Admin user
		sugar().logout();
	}

	/**
	 * Field permissions set to Owner Read/Owner Write on Accounts - Full form edit and viewing of owned records
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21235_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create an Account(Owned record for QAUser)
		sugar().accounts.create();

		// Go to Accounts record owned by QAUser and click Edit button on the Account.
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		String qauser = roleRecordData.get(0).get("userName");

		// Verify that all fields are editable
		/**
		 * Update all fields shows that fields are editable (Not update Assigned user' because "Owner Read/Owner Write" permission').Therefore, edit all the fields
		 */
		sugar().accounts.recordView.getEditField("annualRevenue").set(roleRecordData.get(0).get("fieldValues"));
		sugar().accounts.recordView.getEditField("ownership").set(roleRecordData.get(1).get("fieldValues"));
		sugar().accounts.recordView.getEditField("rating").set(roleRecordData.get(2).get("fieldValues"));
		sugar().accounts.recordView.getEditField("tickerSymbol").set(roleRecordData.get(3).get("fieldValues"));
		sugar().accounts.recordView.getEditField("description").set(roleRecordData.get(5).get("fieldValues"));
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
		sugar().accounts.recordView.getDetailField("description").assertEquals(roleRecordData.get(5).get("fieldValues"), true);
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

		// Update assigned user to verify No Access(which shows success modification)
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(roleRecordData.get(4).get("fieldValues"));
		sugar().accounts.recordView.save();

		// Verify that modification can be saved(As "No Access" is shown for all fields)
		// TODO: VOOD-1445
		String noAccessValue = roleRecordData.get(0).get("noAccess");
		new VoodooControl("span", "css", ".noaccess.fld_annual_revenue span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_ownership span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_rating span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_ticker_symbol span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_description span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_billing_address span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_phone_fax span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_sic_code span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_website span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_account_type span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_email span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_campaign_name span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_industry span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_name span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_phone_office span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_assigned_user_name span").assertContains(noAccessValue, true);
		new VoodooControl("span", "css", ".noaccess.fld_team_name span").assertContains(noAccessValue, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}