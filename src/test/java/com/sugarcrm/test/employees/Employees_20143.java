package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20143 extends SugarTest {

	DataSource ds;
	VoodooControl searchCtrl;
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		// Create two employees 
		for(int i=0;i<ds.size();i++) {
			new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
			new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("select", "id", "employee_status").set(ds.get(i).get("status"));
			new VoodooControl("input", "id", "last_name").set(ds.get(i).get("lastName"));
			new VoodooControl("input", "id", "first_name").set(ds.get(i).get("firstName"));
			new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(i).get("email"));
			new VoodooControl("input", "id", "title").set(ds.get(i).get("title"));
			new VoodooControl("input", "id", "department").set(ds.get(i).get("department"));
			new VoodooControl("input", "id", "phone_work").set(ds.get(i).get("phone"));
			new VoodooControl("textarea", "id", "description").set(ds.get(i).get("description"));
			new VoodooControl("textarea", "id", "address_street").set(ds.get(i).get("street"));
			new VoodooControl("input", "id", "address_city").set(ds.get(i).get("city"));
			new VoodooControl("input", "id", "SAVE_HEADER").click();
			VoodooUtils.focusDefault();
		}
	}

	/**
	 *  show all employees in employees module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20143_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl activeCtrl = new VoodooControl("input", "id", "open_only_active_users_basic");
		// Check Active Employees checkbox on the basic search panel
		activeCtrl.click();

		searchCtrl = new VoodooControl("input", "id", "search_form_submit");
		searchCtrl.click();
		sugar.alerts.waitForLoadingExpiration();

		// Assert that employee with active status is shown in list view
		new VoodooControl("td", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(9)").assertEquals(ds.get(1).get("status"), true);
		new VoodooControl("td", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(9)").assertEquals(ds.get(1).get("status"), true);

		activeCtrl.click();
		searchCtrl.click();

		//  Assert that employee records with all status is shown in List View.
		new VoodooControl("td", "css", "#MassUpdate > table > tbody > tr.oddListRowS1 > td:nth-child(9)").assertEquals(ds.get(1).get("status"), true);
		new VoodooControl("td", "css", "#MassUpdate > table > tbody > tr.evenListRowS1 > td:nth-child(9)").assertEquals(ds.get(0).get("status"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}