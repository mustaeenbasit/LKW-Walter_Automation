package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20120 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady(); // Wait for the page to be ready 
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
		new VoodooControl("input", "id", "address_state").set(ds.get(0).get("state"));
		new VoodooControl("input", "id", "address_postalcode").set(ds.get(0).get("zipcode"));
		new VoodooControl("input", "id", "address_country").set(ds.get(0).get("country"));
		new VoodooControl("input", "id", "messenger_id").set(ds.get(0).get("IMName"));		
		new VoodooControl("input", "id", "SAVE_HEADER").click();		
	}

	/**
	 * Verify that employee can be edited.
	 * @throws Exception
	 */
	@Test
	public void Employees_20120_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		ds = testData.get(testName);
		// TODO VOOD-1041
		for(int i=1;i<ds.size();i++) {
			new VoodooControl("input", "id", "edit_button").click();
			// update employee
			new VoodooControl("input", "id", "last_name").set(ds.get(i).get("lastName"));
			new VoodooControl("input", "id", "first_name").set(ds.get(i).get("firstName"));
			new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(i).get("email"));
			new VoodooControl("input", "id", "title").set(ds.get(i).get("title"));
			new VoodooControl("input", "id", "department").set(ds.get(i).get("department"));
			new VoodooControl("input", "id", "phone_work").set(ds.get(i).get("phone"));
			new VoodooControl("textarea", "id", "description").set(ds.get(i).get("description"));
			new VoodooControl("textarea", "id", "address_street").set(ds.get(i).get("street"));
			new VoodooControl("input", "id", "address_city").set(ds.get(i).get("city"));
			new VoodooControl("input", "id", "address_state").set(ds.get(i).get("state"));
			new VoodooControl("input", "id", "address_postalcode").set(ds.get(i).get("zipcode"));
			new VoodooControl("input", "id", "address_country").set(ds.get(i).get("country"));
			new VoodooControl("input", "id", "messenger_id").set(ds.get(i).get("IMName"));
			new VoodooControl("select", "id", "employee_status").set(ds.get(i).get("status"));
			new VoodooControl("input", "id", "SAVE_HEADER").click();		
			// check updated value on detail view
			new VoodooControl("span", "id", "first_name").assertEquals(ds.get(i).get("firstName")+" "+ds.get(i).get("lastName"),true);
			new VoodooControl("span", "id", "email_span").assertElementContains(ds.get(i).get("email"),true);
			new VoodooControl("span", "id", "title").assertEquals(ds.get(i).get("title"),true);
			new VoodooControl("span", "id", "department").assertEquals(ds.get(i).get("department"),true);
			new VoodooControl("td", "css", ".phone").assertEquals(ds.get(i).get("phone"),true);
			new VoodooControl("span", "id", "description").assertEquals(ds.get(i).get("description"),true);
			new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(i).get("street"),true);
			new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(i).get("city"),true);
			new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(i).get("state"),true);
			new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(i).get("zipcode"),true);
			new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(i).get("country"),true);
			new VoodooControl("span", "id", "messenger_id").assertEquals(ds.get(i).get("IMName"),true);
			new VoodooControl("input", "id", "employee_status").assertEquals(ds.get(i).get("status"),true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}