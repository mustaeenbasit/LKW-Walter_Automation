package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_28791 extends SugarTest {
	VoodooControl employeesLink, qauser1, qauser2, actionButton, searchName, submitButton;
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify Employee record can be saved when copying an employee
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_28791_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// TODO: VOOD-1041 -need lib support of employees module
		sugar.navbar.toggleUserActionsMenu();
		employeesLink = new VoodooControl("a", "css", "li.profileactions-employees a");
		employeesLink.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1041
		searchName = new VoodooControl("input", "id", "search_name_basic");
		submitButton = new VoodooControl("input", "id", "search_form_submit");
		qauser1 = new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-child(3) td:nth-child(3) b a");
		qauser2 = new VoodooControl("a", "css", "#MassUpdate table tbody tr:nth-child(4) td:nth-child(3) b a");
		
		// Open an existing employee like qauser
		searchName.set(sugar.users.getQAUser().get("userName"));
		submitButton.click();
		qauser1.click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// From Edit action drop down, select Copy
		actionButton  = new VoodooControl("span", "css", ".sugar_action_button .ab");
		actionButton.click();
		new VoodooControl("a", "id", "duplicate_button").click();
		
		// Click save button
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// TODO: VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		employeesLink.click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		searchName.set(sugar.users.getQAUser().get("userName"));
		submitButton.click();
		
		// Verify that record is saved. ie. You have 2 qauser records with same info
		// TODO: VOOD-1526 -Need a method to return count of the control on the page
		qauser1.assertContains(sugar.users.getQAUser().get("userName"), true);
		qauser2.assertContains(sugar.users.getQAUser().get("userName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}