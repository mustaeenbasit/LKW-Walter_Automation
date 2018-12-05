package com.sugarcrm.test.roles;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_30201 extends SugarTest {
	FieldSet roleFields = new FieldSet();	
	UserRecord chrisUser;
	public void setup() throws Exception {
		chrisUser = (UserRecord) sugar().users.api.create();
		sugar().accounts.api.create();
		sugar().login();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();

		// Assigning account record to chris user
		sugar().accounts.recordView.getEditField("relAssignedTo").set(chrisUser.getRecordIdentifier());
		sugar().accounts.recordView.save();

		// Creating opportunity record 
		sugar().opportunities.create();
	}

	/**
	 * Verify Account name(of non owned records) is not a clickable link in the list view when view is set to owner.
	 * @throws Exception
	 */
	@Ignore("MAR-3355 - Opportunity & Account name shouldn't hyperlink for converted lead when 'View' access is 'None' or 'Owner'." + 
			"SC-5549 - Directly related fields of a module are not clickable when 'View' access is 'Owner'. ")
	@Test
	public void Roles_30201_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		roleFields = testData.get(testName).get(0);

		// Creating one role
		sugar().admin.createRole(roleFields);
		VoodooUtils.focusFrame("bwc-frame");

		// View is set to owner for account module in created role.
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#ACLEditView_Access_Accounts_view .aclNot.Set").click();
		new VoodooControl("select", "css", "#ACLEditView_Access_Accounts_view select").set("Owner");
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assigning this role to qauser
		sugar().admin.assignUserToRole(roleFields);
		VoodooUtils.waitForReady();
		sugar().logout();

		// Login as qauser
		sugar().login(sugar().users.getQAUser());

		// Navigate to opportunity list view
		sugar().opportunities.navToListView();

		// Verifying account name is not hyperlinked in listview of opportunity
		sugar().opportunities.listView.getDetailField(1, "relAccountName").assertExists(false);

		// Verifying account name in listview
		// TODO: VOOD-911
		new VoodooControl("a", "css", ".list.fld_account_name .ellipsis_inline").assertEquals(sugar().accounts.defaultData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}