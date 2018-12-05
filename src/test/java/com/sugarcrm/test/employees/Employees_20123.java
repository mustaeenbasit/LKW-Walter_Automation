package com.sugarcrm.test.employees;

import com.sugarcrm.candybean.datasource.FieldSet;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20123 extends SugarTest {
	DataSource ds1 = new DataSource();
	DataSource ds2 = new DataSource();

	public void setup() throws Exception {
		ds1 = testData.get(testName);
		ds2 = testData.get(testName + "_1");
		sugar.login();

		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerPage", ds2.get(0).get("num1"));
		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);

		// create employees
		// TODO VOOD-1041
		sugar.navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();
		VoodooUtils.waitForReady();
		for (int i = 0; i < ds1.size(); i++) {
			new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
			new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
			VoodooUtils.focusFrame("bwc-frame");
			new VoodooControl("input", "id", "last_name").set(ds1.get(i).get("lastName"));
			new VoodooControl("input", "id", "first_name").set(ds1.get(i).get("firstName"));
			new VoodooControl("input", "id", "Users0emailAddress0").set(ds1.get(i).get("email"));
			new VoodooControl("select", "id", "employee_status").set(ds1.get(i).get("status"));
			new VoodooControl("input", "id", "SAVE_HEADER").click();
			VoodooUtils.focusDefault();
		}
		new VoodooControl("li", "css", "li[data-module='Employees']").click();
		VoodooUtils.focusFrame("bwc-frame");
	}

	/**
	 * Employees mass update function works fine
	 *
	 * @throws Exception
	 */
	@Test
	public void Employees_20123_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO VOOD-1041
		new VoodooControl("input", "id", "search_name_basic").set(ds1.get(0).get("firstName"));
		new VoodooControl("input", "id", "search_form_submit").click();
		// select record in different page and do mass update
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 input.checkbox").click();
		new VoodooControl("button", "id", "listViewEndButton_top").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 input.checkbox").click();
		new VoodooControl("a", "id", "massupdate_listview_top").click();
		new VoodooControl("select", "id", "mass_show_on_employees").set(ds2.get(0).get("display"));
		new VoodooControl("select", "id", "mass_employee_status").set(ds2.get(0).get("status"));
		new VoodooControl("select", "id", "mass_preferred_language").set(ds2.get(0).get("language"));
		new VoodooControl("input", "css", "div#mass_update_div input[value='Select']").click();
		// sometime can't find the popup window without the wait
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#last_name_advanced").set(ds2.get(0).get("reportto"));
		new VoodooControl("input", "css", "#search_form_submit").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();
		new VoodooControl("input", "id", "search_name_basic").waitForVisible();
		// check mass updated result
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(6)").assertContains(ds2.get(0).get("reportto"), true);
		new VoodooControl("a", "css"    , "table.list.view tr.oddListRowS1 td:nth-child(9)").assertContains(ds2.get(0).get("status"), true);
		new VoodooControl("a", "css", "table.list.view tr.evenListRowS1 td:nth-child(6)").assertContains(ds2.get(0).get("reportto"), false);
		new VoodooControl("a", "css", "table.list.view tr.evenListRowS1 td:nth-child(9)").assertContains(ds2.get(0).get("status"), false);
		new VoodooControl("button", "id", "listViewEndButton_top").click();
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(6)").assertContains(ds2.get(0).get("reportto"), true);
		new VoodooControl("a", "css", "table.list.view tr.oddListRowS1 td:nth-child(9)").assertContains(ds2.get(0).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}