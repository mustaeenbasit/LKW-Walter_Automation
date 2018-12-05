package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20126 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "last_name").set(ds.get(0).get("lastName"));
		new VoodooControl("input", "id", "first_name").set(ds.get(0).get("firstName"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(0).get("email"));
		new VoodooControl("input", "id", "title").set(ds.get(0).get("title"));
		new VoodooControl("input", "id", "department").set(ds.get(0).get("department"));
		new VoodooControl("input", "id", "phone_work").set(ds.get(0).get("phone"));
		new VoodooControl("textarea", "id", "description").set(ds.get(0).get("description"));
		new VoodooControl("textarea", "id", "address_street").set(ds.get(0).get("street"));
		new VoodooControl("input", "id", "address_city").set(ds.get(0).get("city"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		new VoodooControl("div", "id", "Employees_detailview_tabs").waitForVisible();
	}

	/**
	 * Cancel duplicate employee
	 * @throws Exception
	 */
	@Test
	public void Employees_20126_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		ds = testData.get(testName);
		// TODO VOOD-1041
		new VoodooControl("span", "css", "li.sugar_action_button span.ab").click();		
		new VoodooControl("a", "id", "duplicate_button").click();	
		new VoodooControl("input", "id", "last_name").waitForVisible();
		new VoodooControl("a", "id", "CANCEL_HEADER").click();	
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "id", "Employees_detailview_tabs").waitForVisible();
		new VoodooControl("span", "id", "first_name").assertEquals(ds.get(0).get("firstName")+" "+ds.get(0).get("lastName"),true);
		new VoodooControl("span", "id", "email_span").assertElementContains(ds.get(0).get("email"),true);
		new VoodooControl("span", "id", "title").assertEquals(ds.get(0).get("title"),true);
		new VoodooControl("span", "id", "department").assertEquals(ds.get(0).get("department"),true);
		new VoodooControl("td", "css", ".phone").assertEquals(ds.get(0).get("phone"),true);
		new VoodooControl("span", "id", "description").assertEquals(ds.get(0).get("description"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(0).get("street"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(0).get("city"),true);
		// check no record is create after cancel duplicate
		VoodooUtils.focusDefault();
		new VoodooControl("li", "css", "li[data-module='Employees']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "search_name_basic").set(ds.get(0).get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.evenListRowS1").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}