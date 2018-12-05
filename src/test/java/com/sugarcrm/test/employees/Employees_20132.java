package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20132 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("i", "css","li[data-module='Employees'] i.fa-caret-down").click();
		new VoodooControl("a", "css","li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "last_name").set(ds.get(0).get("lastName"));
		new VoodooControl("input", "id", "first_name").set(ds.get(0).get("firstName"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(0).get("email"));
		new VoodooControl("select", "id", "employee_status").set(ds.get(0).get("status"));
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
		new VoodooControl("button", "id", "btn_reports_to_name").click();
		// sometime can't find the popup window without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#last_name_advanced").set(ds.get(0).get("report"));
		new VoodooControl("input", "css", "#search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame"); 
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		new VoodooControl("div", "id", "Employees_detailview_tabs").waitForVisible();
	}

	/**
	 * duplicate employee
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20132_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-1041
		new VoodooControl("span", "css", "li.sugar_action_button span.ab").click();
		new VoodooControl("a", "id", "duplicate_button").click();
		// check data is copied over
		new VoodooControl("input", "id", "last_name").assertEquals(ds.get(0).get("lastName"),true);
		new VoodooControl("input", "id", "first_name").assertEquals(ds.get(0).get("firstName"),true);
		new VoodooControl("input", "id", "Users0emailAddress0").assertEquals(ds.get(0).get("email"),true);
		new VoodooControl("option", "css", "#employee_status option[value='"+ds.get(0).get("status")+"'][selected='selected']").assertVisible(true);
		new VoodooControl("input", "id", "title").assertEquals(ds.get(0).get("title"),true);
		new VoodooControl("input", "id", "department").assertEquals(ds.get(0).get("department"),true);
		new VoodooControl("input", "id", "phone_work").assertEquals(ds.get(0).get("phone"),true);
		new VoodooControl("textarea", "id", "description").assertEquals(ds.get(0).get("description"),true);
		new VoodooControl("textarea", "id", "address_street").assertEquals(ds.get(0).get("street"),true);
		new VoodooControl("input", "id", "address_city").assertEquals(ds.get(0).get("city"),true);
		new VoodooControl("input", "id", "address_state").assertEquals(ds.get(0).get("state"),true);
		new VoodooControl("input", "id", "address_postalcode").assertEquals(ds.get(0).get("zipcode"),true);
		new VoodooControl("input", "id", "address_country").assertEquals(ds.get(0).get("country"),true);
		new VoodooControl("input", "id", "messenger_id").assertEquals(ds.get(0).get("IMName"),true);
		new VoodooControl("input", "id", "reports_to_name").assertContains(ds.get(0).get("report"),true);

		// input new data and save
		new VoodooControl("input", "id", "last_name").set(ds.get(1).get("lastName"));
		new VoodooControl("input", "id", "first_name").set(ds.get(1).get("firstName"));
		new VoodooControl("input", "id", "Users0emailAddress0").set(ds.get(1).get("email"));
		new VoodooControl("select", "id", "employee_status").set(ds.get(1).get("status"));
		new VoodooControl("input", "id", "title").set(ds.get(1).get("title"));
		new VoodooControl("input", "id", "department").set(ds.get(1).get("department"));
		new VoodooControl("input", "id", "phone_work").set(ds.get(1).get("phone"));
		new VoodooControl("textarea", "id", "description").set(ds.get(1).get("description"));
		new VoodooControl("textarea", "id", "address_street").set(ds.get(1).get("street"));
		new VoodooControl("input", "id", "address_city").set(ds.get(1).get("city"));
		new VoodooControl("input", "id", "address_state").set(ds.get(1).get("state"));
		new VoodooControl("input", "id", "address_postalcode").set(ds.get(1).get("zipcode"));
		new VoodooControl("input", "id", "address_country").set(ds.get(1).get("country"));
		new VoodooControl("input", "id", "messenger_id").set(ds.get(1).get("IMName"));		
		new VoodooControl("button", "id", "btn_reports_to_name").click();
		// sometime can't find the popup window without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#last_name_advanced").set(ds.get(1).get("report"));
		new VoodooControl("input", "css", "#search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame"); 		
		new VoodooControl("input", "id", "SAVE_HEADER").click();

		// check data on detail view
		new VoodooControl("span", "id", "first_name").assertEquals(ds.get(1).get("firstName")+" "+ds.get(1).get("lastName"),true);
		new VoodooControl("span", "id", "email_span").assertElementContains(ds.get(1).get("email"),true);
		new VoodooControl("span", "id", "title").assertEquals(ds.get(1).get("title"),true);
		new VoodooControl("span", "id", "department").assertEquals(ds.get(1).get("department"),true);
		new VoodooControl("td", "css", ".phone").assertEquals(ds.get(1).get("phone"),true);
		new VoodooControl("span", "id", "description").assertEquals(ds.get(1).get("description"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(1).get("street"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(1).get("city"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(1).get("state"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(1).get("zipcode"),true);
		new VoodooControl("span", "id", "address_country").assertElementContains(ds.get(1).get("country"),true);
		new VoodooControl("span", "id", "messenger_id").assertEquals(ds.get(1).get("IMName"),true);
		new VoodooControl("input", "id", "employee_status").assertEquals(ds.get(1).get("status"),true);
		new VoodooControl("span", "id", "reports_to_name").assertContains(ds.get(1).get("report"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}