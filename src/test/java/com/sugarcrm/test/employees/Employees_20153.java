package com.sugarcrm.test.employees;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20153 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify that action dropdown list in employees list view page works correctly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20153_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		
		VoodooUtils.pause(1000); // sometime can't find the bwc frame without the wait
		VoodooUtils.focusFrame("bwc-frame");	
		
		new VoodooControl("div", "css", ".listViewBody").waitForVisible();
		new VoodooControl("input", "id", "search_name_basic").set(sugar.users.getQAUser().get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();

		// Click Mass Update checkbox
		new VoodooControl("input", "id", "massall_top").set("true");

		// Click down arrow besides Mass Update
		new VoodooControl("span", "css", "ul#actionLinkTop > li.sugar_action_button > span").click();

		// Verify Export button exists
		new VoodooControl("a", "id", "export_listview_top").assertExists(true);;

		// Click Mass Update
		new VoodooControl("a", "id", "massupdate_listview_top").click();
		
		// Mass Update actions
		new VoodooControl("select", "id", "mass_employee_status").set("Leave of Absence");
		new VoodooControl("select", "id", "mass_show_on_employees").set("Yes");
		new VoodooControl("input", "id", "mass_reports_to_name").set("Administrator");
		VoodooUtils.pause(2000); // Wait for drop down to appear
		new VoodooControl("li", "css", "div#MassUpdate_mass_reports_to_name_results li:nth-of-type(1)").click();
		
		// Update
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify Mass Update Action result		
		new VoodooControl("td", "css", ".listViewBody form#MassUpdate table tr:nth-of-type(3) td:nth-of-type(3)").assertContains(sugar.users.getQAUser().get("firstName"), true);
		new VoodooControl("td", "css", ".listViewBody form#MassUpdate table tr:nth-of-type(3) td:nth-of-type(6)").assertContains("Administrator", true);
		new VoodooControl("td", "css", ".listViewBody form#MassUpdate table tr:nth-of-type(3) td:nth-of-type(9)").assertContains("Leave of Absence", true);
		
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}