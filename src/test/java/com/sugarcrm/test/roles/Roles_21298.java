package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21298 extends SugarTest {

	public void setup() throws Exception {
		FieldSet roleRecordData = testData.get("env_role_setup").get(0);
		DataSource customDS = testData.get(testName);
		DataSource privateUserDS = testData.get(testName+"_userdata");
		sugar().accounts.api.create();

		// Create PrivateUser via UI
		sugar().users.api.create(privateUserDS);
		sugar().login();

		// Navigate to User Management -> Edit
		sugar().admin.navToAdminPanelLink("userManagement");
		sugar().users.listView.basicSearch(privateUserDS.get(0).get("userName"));
		sugar().users.listView.clickRecord(1);
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on Advanced Tab
		sugar().users.editView.getControl("advancedTab").click();
		VoodooUtils.waitForReady();

		// Remove Global Team from Private User and add only Private Team
		// TODO: VOOD-563
		new VoodooControl("button", "css", "[name='remove_team_name_collection_0']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='teamset_div'] input").set(privateUserDS.get(0).get("userName").substring(0,4));
		new VoodooControl("li", "css", "#EditView_team_name_table li").click();
		new VoodooControl("input", "css", "[name='primary_team_name_collection']").click();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		// Create two roles
		for (int i = 0; i < privateUserDS.size(); i++) {
			roleRecordData.put("roleName", testName + "_" + i);
			AdminModule.createRole(roleRecordData);
			VoodooUtils.focusFrame("bwc-frame");

			// Select "Account" module"
			// TODO: VOOD-856
			new VoodooControl("a", "css", "#contentTable tr:nth-child(2) td a").click();
			VoodooUtils.waitForReady();
			new VoodooControl("div", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) div.aclNot.Set").click();
			new VoodooControl("select", "css", "#ACLEditView table tbody tr:nth-child(2) td:nth-child(2) select").set(customDS.get(i).get("access_type"));
			VoodooUtils.waitForReady();

			// Save role
			new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
			VoodooUtils.waitForReady();
			VoodooUtils.focusDefault();

			// Assign a non-admin user (QAUser) into this Role
			AdminModule.assignUserToRole(roleRecordData);

			// Assing non Admin user (chris) into this Role
			FieldSet userFs = new FieldSet();
			userFs.put("userName", privateUserDS.get(1).get("userName"));
			AdminModule.assignUserToRole(userFs);
		}

		// Assign Accounts record to Private User
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relTeam").set(customDS.get(0).get("privateTeamName"));
		sugar().accounts.recordView.getEditField("name").set(customDS.get(0).get("accountName"));
		sugar().accounts.recordView.getEditField("relAssignedTo").set(privateUserDS.get(0).get("userName"));
		sugar().accounts.recordView.getEditField("billingAddressStreet").set(customDS.get(0).get("billingAddressStreet"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Multiple roles module level Acc Type: Admin & Normal
	 * @throws Exception
	 */
	@Test
	public void Roles_21298_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();

		// Verify that QAuser cannot view the "PrivateAccount" on the listview.
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}