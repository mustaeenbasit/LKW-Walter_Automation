package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_27161 extends SugarTest {
	FieldSet customData;
	UserRecord jim;
	VoodooControl salesAdminCtrl, editCtrl, typeCtrl, typeSelectCtrl, saveRoleCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify non-edit permission filed works well in filter creation
	 * @throws Exception
	 */
	@Test
	public void Roles_27161_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1200 - After resolved, we can use with default CSV data
		// Jim user
		FieldSet jimData = testData.get(testName+"_user").get(0);
		jim = (UserRecord)sugar().users.create(jimData);

		// In Admin > Role Management > Sales Admin, set Accounts > Type to Read-Only.
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("rolesManagement").click();

		// Click on 'Sales Administrator'
		// TODO: VOOD-580
		VoodooUtils.focusFrame("bwc-frame");
		salesAdminCtrl = new VoodooControl("a", "css", "#MassUpdate tr:nth-child(5) td:nth-child(3) a");
		salesAdminCtrl.click();

		// Set Type to 'Read Only'
		VoodooUtils.focusFrame("bwc-frame");
		editCtrl = new VoodooControl("a", "css", "#contentTable tr:nth-of-type(2) td a");
		editCtrl.click();
		VoodooUtils.waitForReady();
		typeCtrl = new VoodooControl("div", "css", ".detail.view #account_typelink");
		typeCtrl.click();
		typeSelectCtrl = new VoodooControl("select", "id", "flc_guidaccount_type");
		typeSelectCtrl.set(customData.get("read_permission"));
		saveRoleCtrl = new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON");
		saveRoleCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Log out of Sugar as Admin and log in as Jim 
		sugar().logout();
		sugar().login(jim);

		// Accounts Type filter
		sugar().accounts.navToListView();
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		sugar().accounts.listView.filterCreate.setFilterFields("type", customData.get("type"), customData.get("operator"), customData.get("value1"), 1);
		VoodooUtils.waitForReady();
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));

		// TODO: VOOD-1580
		new VoodooControl("a","css",".layout_Accounts .detail.fld_account_type li.select2-search-choice a").click();

		// Set filter Fields
		sugar().accounts.listView.filterCreate.setFilterFields("type", customData.get("type"), customData.get("operator"), customData.get("value2"), 1);
		VoodooUtils.waitForReady();
		// Verify no record
		sugar().accounts.listView.assertIsEmpty();

		// cancel filter
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}