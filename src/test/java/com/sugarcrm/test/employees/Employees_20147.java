package com.sugarcrm.test.employees;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20147 extends SugarTest {
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
		new VoodooControl("input", "id", "address_state").set(ds.get(0).get("state"));
		new VoodooControl("input", "id", "address_postalcode").set(ds.get(0).get("zipcode"));
		new VoodooControl("input", "id", "address_country").set(ds.get(0).get("country"));
		new VoodooControl("input", "id", "messenger_id").set(ds.get(0).get("IMName"));		
		new VoodooControl("input", "id", "SAVE_HEADER").click();		

		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that adding employee record to Sugar causing a new user without user name to be automatically created
	 * @throws Exception
	 */
	@Test
	public void Employees_20147_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		VoodooUtils.focusFrame("bwc-frame");

		// Already on Employees detail view
		// Verify newly entered field values
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(1) td:nth-of-type(2)").assertContains("Active", true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(2) td:nth-of-type(2)").assertContains(ds.get(0).get("firstName")+" "+ds.get(0).get("lastName"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(3) td:nth-of-type(2)").assertContains(ds.get(0).get("title"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(4) td:nth-of-type(2)").assertContains(ds.get(0).get("department"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(9) td:nth-of-type(2)").assertContains(ds.get(0).get("IMName"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(10) td:nth-of-type(2)").assertContains(ds.get(0).get("street"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(10) td:nth-of-type(2)").assertContains(ds.get(0).get("city"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(10) td:nth-of-type(2)").assertContains(ds.get(0).get("state"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(10) td:nth-of-type(2)").assertContains(ds.get(0).get("zipcode"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(10) td:nth-of-type(2)").assertContains(ds.get(0).get("country"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(11) td:nth-of-type(2)").assertContains(ds.get(0).get("description"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(12) td:nth-of-type(2)").assertContains(ds.get(0).get("email"), true);
		new VoodooControl("td", "css", "div#Employees_detailview_tabs table.panelContainer tr:nth-of-type(3) td:nth-of-type(4)").assertContains(ds.get(0).get("phone"), true);

		VoodooUtils.focusDefault();

		// Goto Admin users page
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("userManagement").click();

		VoodooUtils.focusFrame("bwc-frame");

		// Verify username is empty
		assertTrue("Username is not Empty!", new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'"+ds.get(0).get("firstName")+" "+ds.get(0).get("lastName")+"')]/td[4]/a").getText().trim().isEmpty());

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}