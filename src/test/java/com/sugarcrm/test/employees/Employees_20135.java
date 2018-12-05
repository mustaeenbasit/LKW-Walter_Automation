package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20135 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl employeeCtrl, advanceSearchCtrl, savedSearchDropdown, deleteCtrl, layoutCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Employees_Search panel_Saved Search&Layout
	 * @throws Exception
	 */
	@Test
	public void Employees_20135_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// TODO: VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		employeeCtrl = new VoodooControl("a", "css", "li.profileactions-employees a");
		employeeCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// Advance search
		advanceSearchCtrl = new VoodooControl("a", "id", "advanced_search_link");
		advanceSearchCtrl.click();
		VoodooUtils.waitForReady();

		// Verify default employee records
		VoodooControl oddRecordCtrl = new VoodooControl("tr", "css", ".oddListRowS1");
		VoodooControl evenRecordCtrl =new VoodooControl("tr", "css", ".evenListRowS1");
		oddRecordCtrl.assertVisible(true);
		evenRecordCtrl.assertVisible(true);

		// Search criteria (lastname + email)
		new VoodooControl("input", "id", "last_name_advanced").set(sugar.users.getQAUser().get("userName"));
		new VoodooControl("input", "id", "email_advanced").set(customData.get("emailAddress"));
		new VoodooControl("input", "id", "search_form_submit_advanced").click();

		// Verify record is matching search criteria
		oddRecordCtrl.assertVisible(true);
		evenRecordCtrl.assertVisible(false);

		// Layout options
		layoutCtrl = new VoodooControl("a", "id", "tabFormAdvLink");
		layoutCtrl.click();

		// Verify columns which are sortable are displayed in "Order by column" select list
		VoodooControl orderBySelectCtrl = new VoodooControl("select", "id", "orderBySelect");
		orderBySelectCtrl.assertContains(customData.get("name_sort"), true);
		orderBySelectCtrl.assertContains(customData.get("dept_sort"), true);
		orderBySelectCtrl.assertContains(customData.get("title_sort"), true);
		orderBySelectCtrl.assertContains(customData.get("phone_sort"), true);
		orderBySelectCtrl.assertContains(customData.get("emp_status_sort"), true);
		orderBySelectCtrl.assertContains(customData.get("date_sort"), true);

		// Verify buttons "Update" and "Delete" are displayed but in disabled state
		new VoodooControl("button", "id", "ss_update").assertAttribute("disabled", "true");
		deleteCtrl = new VoodooControl("button", "id", "ss_delete");
		deleteCtrl.assertAttribute("disabled", "true");

		// Saved search
		new VoodooControl("input", "css", "input[name='saved_search_name']").set(customData.get("saved_search"));
		new VoodooControl("input", "css", "input[name='saved_search_submit']").click();
		VoodooUtils.waitForReady();

		// Verify record is matching the search criteria
		oddRecordCtrl.assertVisible(true);
		evenRecordCtrl.assertVisible(false);
		new VoodooControl("tr", "css", ".oddListRowS1 td:nth-of-type(3)").assertContains(sugar.users.getQAUser().get("userName"), true);

		// Reset search to default state (i.e None)
		savedSearchDropdown = new VoodooControl("select", "id", "saved_search_select");
		savedSearchDropdown.set(customData.get("none"));
		VoodooUtils.waitForReady();
		oddRecordCtrl.assertVisible(true);
		evenRecordCtrl.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}