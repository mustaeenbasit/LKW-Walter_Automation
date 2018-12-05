package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20137 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);

		sugar().login();

		// create employees
		// TODO: VOOD-1041 - need lib support of employees module
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		for (int i = 0; i < ds.size(); i++) {
			new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
			new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "id", "last_name").set(ds.get(i).get("lastName"));
			new VoodooControl("input", "id", "first_name").set(ds.get(i).get("firstName"));
			new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(i).get("email"));
			new VoodooControl("input", "id", "title").set(ds.get(i).get("title"));
			new VoodooControl("select", "id", "employee_status").set(ds.get(i).get("status"));
			new VoodooControl("input", "id", "SAVE_HEADER").click();
			VoodooUtils.focusDefault();
		}
		new VoodooControl("li", "css", "li[data-module='Employees']").click();
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Employees detail view pagination
	 *
	 * @throws Exception
	 */
	@Test
	public void Employees_20137_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-1041 - need lib support of employees module
		// open the detail view of first employee
		new VoodooControl("input", "id", "search_name_basic").set(ds.get(0).get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(3) a").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "id", "Employees_detailview_tabs").waitForVisible();

		// click next button on employee's detail view
		for (int i = ds.size(); i > 1; i--) {
			new VoodooControl("span", "id", "first_name").assertEquals(ds.get(i - 1).get("firstName") + " " + ds.get(i - 1).get("lastName"), true);
			new VoodooControl("span", "id", "email_span").assertElementContains(ds.get(i - 1).get("email"), true);
			new VoodooControl("span", "id", "title").assertEquals(ds.get(i - 1).get("title"), true);
			new VoodooControl("input", "id", "employee_status").assertEquals(ds.get(i - 1).get("status"), true);
			// click Next button
			new VoodooControl("button", "css", "td.paginationWrapper button[title='Next']").click();
			VoodooUtils.waitForReady();
		}

		// click previous button on employee's detail view
		for (int i = 0; i < ds.size(); i++) {
			new VoodooControl("span", "id", "first_name").assertEquals(ds.get(i).get("firstName") + " " + ds.get(i).get("lastName"), true);
			new VoodooControl("span", "id", "email_span").assertElementContains(ds.get(i).get("email"), true);
			new VoodooControl("span", "id", "title").assertEquals(ds.get(i).get("title"), true);
			new VoodooControl("input", "id", "employee_status").assertEquals(ds.get(i).get("status"), true);
			// click previous button
			new VoodooControl("button", "css", "td.paginationWrapper button[title='Previous']").click();
			VoodooUtils.waitForReady();
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}