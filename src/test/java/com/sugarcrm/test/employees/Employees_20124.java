package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20124 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl dropdownCreateCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// TODO: VOOD-1041 - need lib support of employees module
		// Navigate Profile Dropdown -> Employee -> Create Employee record
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar().alerts.waitForLoadingExpiration();
		dropdownCreateCtrl = new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down");
		dropdownCreateCtrl.click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "last_name").set(customData.get("last_name"));
		new VoodooControl("input", "id", "first_name").set(customData.get("first_name"));
		new VoodooControl("input", "id", "department").set(customData.get("department"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(customData.get("email_address"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Sort Employees_Verify that Employees can be sorted by column in "Employees" list view.
	 * @throws Exception
	 */
	@Test
	public void Employees_20124_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		dropdownCreateCtrl.click();

		// TODO: VOOD-1041 - need lib support of employees module
		// List Employee
		VoodooControl viewEmployeesCtrl = new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_EMPLOYEE_LIST']");
		viewEmployeesCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl resultCtrl = new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a");
		String fullNameStr = String.format("%s %s", customData.get("first_name"), customData.get("last_name"));
		resultCtrl.assertEquals(fullNameStr, true);

		// Name Sort in ascending order 
		VoodooControl nameSortCtrl = new VoodooControl("img", "css", "table.list.view tr th img");
		nameSortCtrl.click();
		resultCtrl.assertEquals(customData.get("admin2"), true);

		// Name Sort in descending order 
		nameSortCtrl.click();
		resultCtrl.assertEquals(sugar().users.getQAUser().get("userName"), true);

		// Department Sort in ascending order
		VoodooControl departmentSortCtrl = new VoodooControl("img", "css", "table.list.view tr th:nth-of-type(2) img");
		departmentSortCtrl.click();
		resultCtrl.assertEquals(customData.get("admin"), true);

		// Department Sort in descending order
		departmentSortCtrl.click();
		resultCtrl.assertEquals(fullNameStr, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}