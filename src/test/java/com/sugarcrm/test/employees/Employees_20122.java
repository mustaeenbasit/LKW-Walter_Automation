package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20122 extends SugarTest {
	FieldSet customData;
	VoodooControl dropdownCreateCtrl, empProfileCtrl, resultCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Employee_AdvancedSearch
	 * @throws Exception
	 */
	@Test
	public void Employees_20122_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// TODO VOOD-1041
		// Navigate Employee -> Create Employee
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
		dropdownCreateCtrl.click();

		// List Employee
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_EMPLOYEE_LIST']").click();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl advanceSearchCtrl = new VoodooControl("a", "id", "advanced_search_link");
		advanceSearchCtrl.click();
		VoodooUtils.waitForReady();

		// Advance Search 
		// 1. last name as qauser 
		VoodooControl lastNameCtrl = new VoodooControl("input", "id", "last_name_advanced");
		VoodooControl submitCtrl = new VoodooControl("a", "id", "search_form_submit_advanced");
		resultCtrl = new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a");
		lastNameCtrl.set(sugar.users.getQAUser().get("userName"));
		submitCtrl.click();

		// Verify result 
		resultCtrl.assertContains(sugar.users.getQAUser().get("userName"), true);

		// 2. first and last name 
		new VoodooControl("input", "id", "first_name_advanced").set(customData.get("first_name"));
		lastNameCtrl.set(customData.get("last_name"));
		submitCtrl.click();

		String fullNameStr = String.format("%s %s", customData.get("first_name"),customData.get("last_name"));
		// Verifying result 
		resultCtrl.assertContains(fullNameStr, true);

		// 3. status as Terminated
		new VoodooControl("option", "css", "#employee_status_advanced option[value='Terminated']").click();
		submitCtrl.click();

		// Verifying result 
		resultCtrl.assertContains(fullNameStr, true);

		// 4. empty search 
		lastNameCtrl.set(customData.get("invalid_name"));
		submitCtrl.click();

		// Verifying result
		resultCtrl.assertVisible(false);
		new VoodooControl("p", "css", ".listViewEmpty p").assertContains(customData.get("no_result_found"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}