package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21208 extends SugarTest {
	DataSource roleRecordData;
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		roleRecordData = testData.get(testName);
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		sugar().login();

		// Create a role myRole
		AdminModule.createRole(roleRecordData.get(0));
		VoodooUtils.focusFrame("bwc-frame");

		// For Accounts module, Set all field permissions to "Read/Owner Write"
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "xpath", "//*[@id='contentTable']/tbody/tr/td/table[2]/tbody/tr/td[1]/table/tbody/tr/td/a[contains(.,'"+sugar().accounts.moduleNamePlural+"')]").click();
		VoodooUtils.waitForReady();

		// Set 'Assigned To' permissions to "Read/Owner Write"
		new VoodooControl("div", "id", "assigned_user_namelink").click();
		if (!new VoodooControl("select", "id", "flc_guidassigned_user_name").queryVisible())
			new VoodooControl("div", "id", "assigned_user_namelink").click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set(roleRecordData.get(0).get("access"));

		// Set 'Team' permissions to "Read/Owner Write"
		new VoodooControl("div", "id", "team_namelink").click();
		if (!new VoodooControl("select", "id", "flc_guidteam_name").queryVisible())
			new VoodooControl("div", "id", "team_namelink").click();
		new VoodooControl("select", "id", "flc_guidteam_name").set(roleRecordData.get(0).get("access"));

		// Set 'Type' permissions to "Read/Owner Write"
		new VoodooControl("div", "id", "account_typelink").click();
		if (!new VoodooControl("select", "id", "flc_guidaccount_type").queryVisible())
			new VoodooControl("div", "id", "account_typelink").click();
		new VoodooControl("select", "id", "flc_guidaccount_type").set(roleRecordData.get(0).get("access"));

		// Set 'Industry' permissions to "Read/Owner Write"
		new VoodooControl("div", "id", "industrylink").click();
		if (!new VoodooControl("select", "id", "flc_guidindustry").queryVisible())
			new VoodooControl("div", "id", "industrylink").click();
		new VoodooControl("select", "id", "flc_guidindustry").set(roleRecordData.get(0).get("access"));

		// Set 'Campaign' permissions to "Read/Owner Write"
		new VoodooControl("div", "id", "campaign_namelink").click();
		if (!new VoodooControl("select", "id", "flc_guidcampaign_name").queryVisible())
			new VoodooControl("div", "id", "campaign_namelink").click();
		new VoodooControl("select", "id", "flc_guidcampaign_name").set(roleRecordData.get(0).get("access"));

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
	 * Field permissions set to Read Owner Write on Accounts - Mass update on owned records
	 * @throws Exception
	 */
	@Test
	public void Roles_21208_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Create an Account(Owned record for QAUser)
		sugar().accounts.create();

		// Go to Accounts module, click check box next to an owned record for QAUser and select "Mass Update"
		sugar().accounts.listView.checkRecord(1);
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();

		// Verify that Assigned User, Industry, Campaign, Type and Teams fields should be editable and update all fields
		for(int i = 0; i <= roleRecordData.size(); i++) {
			VoodooControl massUpdateValue = sugar().accounts.massUpdate.getControl("massUpdateValue0"+(i+2));
			if(i < roleRecordData.size()) {
				// Set the mass update field
				sugar().accounts.massUpdate.getControl("massUpdateField0"+(i+2)).set(roleRecordData.get(i).get("massUpdateField"));

				// Verify that the available fields are editable field
				massUpdateValue.assertAttribute("class", "select2", true);

				// Update the values for the available fields
				massUpdateValue.set(roleRecordData.get(i).get("massUpdateValue"));
				sugar().accounts.massUpdate.addRow(i+2);
			} else {
				// Set the mass update field
				sugar().accounts.massUpdate.getControl("massUpdateField0"+(i+2)).set(sugar().campaigns.moduleNameSingular);
				// Verify that the available fields are editable field
				massUpdateValue.assertAttribute("class", "select2", true);

				// Update the values for the available fields
				massUpdateValue.set(myCampaign.getRecordIdentifier());
			}
		}

		// Click Update
		sugar().accounts.massUpdate.update();

		// Verify that all fields should be edited/updated properly
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertContains(roleRecordData.get(0).get("massUpdateValue"), true);
		sugar().accounts.recordView.getDetailField("relTeam").assertContains(roleRecordData.get(1).get("massUpdateValue"), true);
		sugar().accounts.recordView.getDetailField("type").assertContains(roleRecordData.get(2).get("massUpdateValue"), true);
		sugar().accounts.recordView.getDetailField("industry").assertContains(roleRecordData.get(3).get("massUpdateValue"), true);

		// TODO: VOOD-555
		new VoodooControl("a", "css", ".fld_campaign_name.detail").assertContains(myCampaign.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}