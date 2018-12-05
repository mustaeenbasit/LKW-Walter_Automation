package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20134 extends SugarTest {
	FieldSet customData;
	VoodooControl empProfileCtrl, dropdownCreateCtrl, resultCtrl, detailViewCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();

		// TODO: VOOD-1041
		// Navigate Profile Dropdown -> Employee -> Create Employee record
		sugar.navbar.toggleUserActionsMenu();
		empProfileCtrl = new VoodooControl("a", "css", "li.profileactions-employees a");
		empProfileCtrl.click();
		VoodooUtils.waitForReady();
		dropdownCreateCtrl = new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down");
		dropdownCreateCtrl.click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "last_name").set(customData.get("last_name"));
		new VoodooControl("input", "id", "first_name").set(customData.get("first_name"));
		new VoodooControl("select", "id", "employee_status").click();
		new VoodooControl("option", "css", "#employee_status option[value='Terminated']").click();
		new VoodooControl("input", "id", "Users0emailAddress0").set(customData.get("email_address"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Employees_List view_Check the value of Status.
	 * @throws Exception
	 */
	@Test
	public void Employees_20134_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		dropdownCreateCtrl.click();
		// TODO: VOOD-1041
		// List Employee
		VoodooControl viewEmployeesCtrl = new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_EMPLOYEE_LIST']");
		viewEmployeesCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");

		// Advance Search 
		VoodooControl advanceSearchCtrl = new VoodooControl("a", "id", "advanced_search_link");
		advanceSearchCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl submitCtrl = new VoodooControl("a", "id", "search_form_submit_advanced");
		VoodooControl listViewStatusCtrl = new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(9)");
		VoodooControl detailViewStatusCtrl = new VoodooControl("input", "id", "employee_status");
		resultCtrl = new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a");

		// 1. Records whose status is Active 
		new VoodooControl("option", "css", "#employee_status_advanced option[value='Active']").click();
		submitCtrl.click();

		// Verify employee status = 'Active' on list view
		listViewStatusCtrl.assertEquals(customData.get("status_active"), true);
		resultCtrl.click();		

		// Detail View of first record
		VoodooUtils.focusFrame("bwc-frame");
		detailViewCtrl = new VoodooControl("div", "id", "Employees_detailview_tabs");
		detailViewCtrl.waitForVisible();

		// Verify 1st employee whose status is'Active' on detail view
		detailViewStatusCtrl.assertEquals(customData.get("status_active"), true);

		// Pagination if more than 1 record
		new VoodooControl("button", "css", "button[title='Next']").click();
		VoodooUtils.waitForAlertExpiration();

		// Verify 2nd employee whose status is 'Active' on detail view
		detailViewStatusCtrl.assertEquals(customData.get("status_active"), true);
		VoodooUtils.focusDefault();
		dropdownCreateCtrl.click();
		viewEmployeesCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "search_form_clear_advanced").click();

		// 2. Records whose status is 'Terminated'
		new VoodooControl("option", "css", "#employee_status_advanced option[value='Terminated']").click();
		submitCtrl.click();

		// Verify employee status = 'Terminated' on list view
		listViewStatusCtrl.assertEquals(customData.get("status_terminated"), true);
		resultCtrl.click();		
		VoodooUtils.focusFrame("bwc-frame");
		detailViewCtrl.waitForVisible();

		// Verify employee status = 'Terminated' on detail view
		detailViewStatusCtrl.assertEquals(customData.get("status_terminated"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}