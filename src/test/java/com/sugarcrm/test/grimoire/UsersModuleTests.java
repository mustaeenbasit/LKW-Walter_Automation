package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class UsersModuleTests extends SugarTest {
	UserRecord newUser;
	FieldSet userData = new FieldSet();

	public void setup() throws Exception {
		userData = new FieldSet();
		userData.put("firstName", "");
		userData.put("lastName", "AJ");
		userData.put("userName", "AJ");
		userData.put("fullName", "AJ");
		userData.put("newPassword", "Aj123!");
		userData.put("confirmPassword", "Aj123!");
		userData.put("password", "Aj123!");
		userData.put("emailAddress", "aj@12.com");
		sugar().login();
	}

	@Test
	public void create() throws Exception {
		VoodooUtils.voodoo.log.info("Running create()...");

		newUser = (UserRecord)sugar().users.create(userData);
		newUser.verify(newUser);

		VoodooUtils.voodoo.log.info("create() complete.");
	}

	@Test
	public void apiCreate() throws Exception {
		VoodooUtils.voodoo.log.info("Running apiCreate()...");

		userData.put("status", "Inactive");
		newUser = (UserRecord)sugar().users.api.create(userData);
		newUser.verify(newUser);

		VoodooUtils.voodoo.log.info("apiCreate() complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		// Go to users listView
		sugar().users.navToListView();

		// Click to open list of menu items
		sugar().navbar.clickModuleDropdown(sugar().users);

		// Verify that the Users Menu Items are visible
		sugar().users.menu.getControl("createNewUser").assertVisible(true);
		sugar().users.menu.getControl("createGroupUser").assertVisible(true);
		sugar().users.menu.getControl("createPortalApiUser").assertVisible(true);
		sugar().users.menu.getControl("reassignRecords").assertVisible(true);
		sugar().users.menu.getControl("importUsers").assertVisible(true);

		// Using "clickModuleDropdown" method to close open list of menu items
		sugar().navbar.clickModuleDropdown(sugar().users);

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	public void cleanup() throws Exception {}
}