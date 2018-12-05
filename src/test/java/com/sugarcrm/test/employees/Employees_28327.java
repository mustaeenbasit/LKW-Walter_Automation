package com.sugarcrm.test.employees;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Employees_28327 extends SugarTest {
	FieldSet employeeData;
	VoodooControl userManagement;

	public void setup() throws Exception {
		employeeData = testData.get(testName).get(0);
		userManagement = sugar.admin.adminTools.getControl("userManagement");
		sugar.login();
	}

	/**
	 * Related User record for New Employee shows status field as empty
	 * @throws Exception
	 */
	@Test
	public void Employees_28327_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// TODO: VOOD-1041
		// Create Employee record
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "first_name").set(employeeData.get("first_name"));
		new VoodooControl("input", "id", "last_name").set(employeeData.get("last_name"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(employeeData.get("email_address"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Admin -> User Management
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		userManagement.click();
		VoodooUtils.focusDefault();
		sugar.users.listView.basicSearch(employeeData.get("last_name"));
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-772
		// Verify user name and status fields are blank
		// TODO: VOOD-1582
		// This is one of the case where we need to use getText(), because on DOM page we have "&nbsp" special character on empty fields for Users listview
		Boolean userName= new VoodooControl("td", "css", "tr.oddListRowS1 td:nth-of-type(4)").getText().trim().isEmpty();
		Assert.assertTrue("Username is empty", userName);
		Boolean status = new VoodooControl("td", "css", "tr.oddListRowS1 td:nth-of-type(9)").getText().trim().isEmpty();
		Assert.assertTrue("Status is Active/Inactive or is not empty", status);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
