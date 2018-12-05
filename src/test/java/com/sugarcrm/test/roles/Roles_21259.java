package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_21259 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		FieldSet roleRecord = testData.get("env_role_setup").get(0);

		// Login as a valid user
		sugar().login();

		// Create a new role in Sugar and select a module -> Set Access Type to be All
		AdminModule.createRole(roleRecord);
		VoodooUtils.focusFrame("bwc-frame");

		// Now on the Access matrix, Set the Contacts Edit cell to All
		// TODO: VOOD-580
		new VoodooControl("div", "css", "td#ACLEditView_Access_Contacts_edit div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_Contacts_edit div select").set(roleRecord.get("roleAll"));
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Select a user for this role
		AdminModule.assignUserToRole(roleRecord);

		// Log out from Admin user and log in as QAUser 
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify the report should be run while the Edit role is set to All or Not Set
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_21259_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to the reports module 
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// Define Controls
		// TODO: VOOD-822
		VoodooControl rolesAndColumnsReport = new VoodooControl("td", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl contactsFullNameCtrl = new VoodooControl("tr", "id", "Contacts_full_name");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "input[name='Save and Run']");

		// Create report and select Rows and columns report
		rolesAndColumnsReport.click();
		VoodooUtils.waitForReady();

		// Choose the selected module of the setup 
		new VoodooControl("table", "id", "Contacts").click();
		VoodooUtils.waitForReady();

		// Follow the report wizard to run a report
		// Define Filters : Without filters
		nextBtnCtrl.click();

		// Choose Display Columns: Name
		contactsFullNameCtrl.scrollIntoView();
		contactsFullNameCtrl.click();
		nextBtnCtrl.click();

		// Select Name file to show on the detail list. Save and Run Report.
		reportNameCtrl.set(testName);
		saveAndRunCtrl.click();
		VoodooUtils.waitForReady(30000); // Needed extra wait

		// Verify that the report should be run without an error message
		// TODO: VOOD-822
		new VoodooControl("h2", "css", ".moduleTitle h2").assertContains(testName, true);
		new VoodooControl("a", "css", ".listViewBody .oddListRowS1 a").assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}