package com.sugarcrm.test.roles;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Roles_21253 extends SugarTest {
	FieldSet roleRecordData;
	UserRecord chrisUser;

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		FieldSet customFs = testData.get(testName).get(0);
		
		// Create two Account records with different name
		sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().accounts.api.create(fs);
		sugar().login();

		// Create user Chris via UI due to API not set user status to Active
		chrisUser = (UserRecord) sugar().users.create();
					
		// Create a role
		AdminModule.createRole(roleRecordData);
		VoodooUtils.focusFrame("bwc-frame");

		// Select "Account" module and set only five fields to "Read only"
		// TODO: VOOD-856
		new VoodooControl("td", "css", "#contentTable tbody tr td table:nth-child(9) tbody tr td:nth-child(1) table tbody tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady();
		
		// Set Type as Read Only
		new VoodooControl("div", "id", "account_typelink").scrollIntoView();
		new VoodooControl("div", "id", "account_typelink").click();
		VoodooControl guidTypeCtrl = new VoodooControl("select", "id", "flc_guidaccount_type");
		if (!guidTypeCtrl.queryVisible())
			new VoodooControl("div", "id", "account_typelink").click();
		guidTypeCtrl.set(customFs.get("read_only"));
		
		// Set Employees as Read Only
		new VoodooControl("div", "id", "employeeslink").scrollIntoView();
		new VoodooControl("div", "id", "employeeslink").click();
		VoodooControl guidEmployeeCtrl = new VoodooControl("select", "id", "flc_guidemployees");
		if (!guidEmployeeCtrl.queryVisible())
			new VoodooControl("div", "id", "employeeslink").click();
		guidEmployeeCtrl.set(customFs.get("read_only"));
		
		// Set Industry as Read Only
		new VoodooControl("div", "id", "industrylink").scrollIntoView();
		new VoodooControl("div", "id", "industrylink").click();
		if (!new VoodooControl("select", "id", "flc_guidindustry").queryVisible())
			new VoodooControl("div", "id", "industrylink").click();
		new VoodooControl("select", "id", "flc_guidindustry").set(customFs.get("read_only"));
		
		// Set Name as Read Only
		new VoodooControl("div", "id", "namelink").scrollIntoView();
		new VoodooControl("div", "id", "namelink").click();
		if (!new VoodooControl("select", "id", "flc_guidname").queryVisible())
			new VoodooControl("div", "id", "namelink").click();
		new VoodooControl("select", "id", "flc_guidname").set(customFs.get("read_only"));
		
		// Set OwnerShip as Read Only
		VoodooControl ownerShipLinkCtrl = new VoodooControl("div", "id", "ownershiplink");
		ownerShipLinkCtrl.scrollIntoView();
		ownerShipLinkCtrl.click();
		if (!new VoodooControl("select", "id", "flc_guidownership").queryVisible())
			ownerShipLinkCtrl.click();
		new VoodooControl("select", "id", "flc_guidownership").set(customFs.get("read_only"));
		
		// Set "Assigned User" as Read Only
		VoodooControl assignedUserLinkCtrl = new VoodooControl("div", "id", "assigned_user_namelink");
		assignedUserLinkCtrl.scrollIntoView();
		assignedUserLinkCtrl.click();
		if (!new VoodooControl("select", "id", "flc_guidassigned_user_name").queryVisible())
			assignedUserLinkCtrl.click();
		new VoodooControl("select", "id", "flc_guidassigned_user_name").set(customFs.get("read_only"));
		
		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign a non-admin user (QAUser) into the Role
		AdminModule.assignUserToRole(roleRecordData);
		AdminModule.assignUserToRole(chrisUser);
		
		// Assign Accounts record to QAUser
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		for(int i = 0; i < 2; i++) {
			sugar().accounts.recordView.edit();
			sugar().accounts.recordView.showMore();
			if(i == 0)
				sugar().accounts.recordView.getEditField("relAssignedTo").set(sugar().users.qaUser.get("userName"));
			else
				sugar().accounts.recordView.getEditField("relAssignedTo").set(chrisUser.getRecordIdentifier());
			sugar().accounts.recordView.save();
			sugar().accounts.recordView.gotoNextRecord();
		}

		// Logout as Admin and Login as QAuser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Field permissions set to Read Only on Accounts - Reports of owned records
	 * @throws Exception
	 */
	@Ignore("TY-732 - Modules are not visible in Report Wizard of reports module")
	@Test
	public void Roles_21253_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts record listView
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		
		// Click Next
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		
		// Select Account > Name and click next
		new VoodooControl("tr", "id", "Accounts_name").click();
		new VoodooControl("tr", "id", "Accounts_ownership").click();
		new VoodooControl("tr", "id", "Accounts_account_type").click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		new VoodooControl("tr", "id", "Accounts_employees").click();
		
		// Click on Related Modules "Accounts" > Assigned to User 
		new VoodooControl("a", "css", "#module_tree > div > div > div > div > div:nth-child(1) > table > tbody > tr > td.ygtvcell.ygtvcontent > a").click();
		new VoodooControl("tr", "id", "Users_full_name").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click(); // Save and Run
		VoodooUtils.waitForReady();
		
		// Verify that reports owned by QAuser and Chris are displayed on the report.
		assertTrue("odd Row does not contain any of the user", new VoodooControl("a", "css", "#report_results div table tbody tr.oddListRowS1 td:nth-child(6) a").queryContains(chrisUser.get("fullName"), true) || 
		new VoodooControl("a", "css", "#report_results div table tbody tr.oddListRowS1 td:nth-child(6) a").queryContains(sugar().users.qaUser.get("userName"), true));

		assertTrue("Even Row does not contain any of the user", new VoodooControl("a", "css", "#report_results div table tbody tr.evenListRowS1 td:nth-child(6) a").queryContains(chrisUser.get("fullName"), true) || 
		new VoodooControl("a", "css", "#report_results div table tbody tr.evenListRowS1 td:nth-child(6) a").queryContains(sugar().users.qaUser.get("userName"), true));
		
		// Click on first record shown on report list view
		new VoodooControl("a", "css", "#report_results div table tbody tr:nth-child(3) td:nth-child(1) a").click();
		VoodooUtils.focusWindow(1);
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.showMore();
		
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		// Verify that the records should not be editable on recordView.
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='assigned_user_name'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='industry'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='employees'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='ownership'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='account_type'] .fa.fa-pencil").assertExists(false);
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on second record shown on report list view
		new VoodooControl("a", "css", "#report_results div table tbody tr:nth-child(4) td:nth-child(1) a").click();
		VoodooUtils.waitForReady();
		sugar().accounts.recordView.showMore();
		
		// TODO: VOOD-854 - Need lib support for inline edit on Record view
		// Verify that the records should not be editable on recordView.
		new VoodooControl ("i", "css", "span[data-name='name'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='assigned_user_name'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='industry'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='employees'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='ownership'] .fa.fa-pencil").assertExists(false);
		new VoodooControl ("i", "css", "span[data-name='account_type'] .fa.fa-pencil").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}