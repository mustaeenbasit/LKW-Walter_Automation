package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21272 extends SugarTest {
	FieldSet roleRecordData, customFS;
	UserRecord chrisUser;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		customFS = testData.get(testName).get(0);
		
		// Create two Account records with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create();
		sugar().accounts.api.create(fs);
		sugar().login();

		// Create user Chris via UI due to API not set user status to Active
		chrisUser = (UserRecord) sugar().users.create();

		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("td", "css", "#contentTable tbody tr td table:nth-child(9) tbody tr td:nth-child(1) table tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();

		// Set Name as Read Only
		new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(customFS.get("read_only"));
		
		// Set Office Phone as Read Only
		new VoodooControl("div", "id", "phone_officelink").click();
		new VoodooControl("select", "id", "flc_guidphone_office").set(customFS.get("read_only"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into this Role
		AdminModule.assignUserToRole(roleRecordData);

		// Assing non Admin user (chris) into this Role
		FieldSet userFs = new FieldSet();
		userFs.put("userName", chrisUser.getRecordIdentifier());
		AdminModule.assignUserToRole(userFs);

		// Assign Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.getQAUser().get("userName"));
		sugar().accounts.recordView.save();

		// Logout as Admin and Login as Chris
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Field permissions set to Read/Write & Not Set on Accounts - Verify Listview, Detailview Read
	 * @throws Exception
	 */
	@Test
	public void Roles_21272_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().accounts.navToListView();
		
		// Verify Name, City, Billing Country, Phone, User, Email Address, and Date Created, are displayed properly on the list view for owned and non-owned records.
		sugar().accounts.listView.getDetailField(1, "name").assertExists(true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCity").assertExists(true);
		sugar().accounts.listView.getDetailField(1, "billingAddressCountry").assertExists(true);
		sugar().accounts.listView.getDetailField(1, "workPhone").assertExists(true);
		sugar().accounts.listView.getDetailField(1, "emailAddress").assertExists(true);
		
		// Click on owned Account record for QAUser to display the detailed view.
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();
		
		// Verify that all fields are displayed (Name, Website,Billing Address, Email Address, Description, Office Phone, Fac, Shipping Address, Type, Annual Revenue, SIC Code, Member of, Campaign, Industry, Employees, Ticker Symbol, Ownership, Rating, Assigned to, Teams, Date Modified, and Date Created)
		VoodooControl nameFieldCtrl = sugar().accounts.recordView.getDetailField("name");
		VoodooControl billingAddCityCtrl = sugar().accounts.recordView.getDetailField("billingAddressCity");
		VoodooControl billingAddCountyCtrl = sugar().accounts.recordView.getDetailField("billingAddressCountry");
		VoodooControl workPhoneCtrl = sugar().accounts.recordView.getDetailField("workPhone");
		VoodooControl emailAddressCtrl = sugar().accounts.recordView.getDetailField("emailAddress");
		VoodooControl descriptionCtrl = sugar().accounts.recordView.getDetailField("description");
		VoodooControl typeCtrl = sugar().accounts.recordView.getDetailField("type");
		VoodooControl memberOfCtrl = sugar().accounts.recordView.getDetailField("memberOf");
		VoodooControl sisCodeCtrl = sugar().accounts.recordView.getDetailField("sicCode");
		VoodooControl industryCtrl = sugar().accounts.recordView.getDetailField("industry");
		VoodooControl tickerSymbolCtrl = sugar().accounts.recordView.getDetailField("tickerSymbol");
		VoodooControl employeesCtrl = sugar().accounts.recordView.getDetailField("employees");
		VoodooControl ratingCtrl = sugar().accounts.recordView.getDetailField("rating");
		VoodooControl ownerShipCtrl = sugar().accounts.recordView.getDetailField("ownership");
		VoodooControl relAssignedToCtrl = sugar().accounts.recordView.getDetailField("relAssignedTo");
		VoodooControl relTeamCtrl = sugar().accounts.recordView.getDetailField("relTeam");
		
		nameFieldCtrl.assertExists(true);
		billingAddCityCtrl.assertExists(true);
		billingAddCountyCtrl.assertExists(true);
		workPhoneCtrl.assertExists(true);
		emailAddressCtrl.assertExists(true);
		descriptionCtrl.assertExists(true);
		typeCtrl.assertExists(true);
		memberOfCtrl.assertExists(true);
		sisCodeCtrl.assertExists(true);
		industryCtrl.assertExists(true);
		tickerSymbolCtrl.assertExists(true);
		employeesCtrl.assertExists(true);
		ratingCtrl.assertExists(true);
		ownerShipCtrl.assertExists(true);
		relAssignedToCtrl.assertExists(true);
		relTeamCtrl.assertExists(true);
		
		// Go to non-owned Account record
		sugar().accounts.recordView.gotoNextRecord();
		
		// Verify that all fields are displayed (Name, Website,Billing Address, Email Address, Description, Office Phone, Fac, Shipping Address, Type, Annual Revenue, SIC Code, Member of, Campaign, Industry, Employees, Ticker Symbol, Ownership, Rating, Assigned to, Teams, Date Modified, and Date Created)
		nameFieldCtrl.assertExists(true);
		billingAddCityCtrl.assertExists(true);
		billingAddCountyCtrl.assertExists(true);
		workPhoneCtrl.assertExists(true);
		emailAddressCtrl.assertExists(true);
		descriptionCtrl.assertExists(true);
		typeCtrl.assertExists(true);
		memberOfCtrl.assertExists(true);
		sisCodeCtrl.assertExists(true);
		industryCtrl.assertExists(true);
		tickerSymbolCtrl.assertExists(true);
		employeesCtrl.assertExists(true);
		ratingCtrl.assertExists(true);
		ownerShipCtrl.assertExists(true);
		relAssignedToCtrl.assertExists(true);
		relTeamCtrl.assertExists(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}