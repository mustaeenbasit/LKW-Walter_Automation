package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20133 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar.login();
	}

	/**
	 * Click the name link from users list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Employees_20133_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// create employees
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
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();
		// open the user from list view
		sugar.users.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.listView.getControl("nameBasic").set(ds.get(0).get("firstName"));
		sugar.users.listView.getControl("searchButton").click();
		sugar.users.listView.getControl("firstRecordListView").click();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.users.detailView.getDetailField("fullName").assertEquals(ds.get(0).get("fullName"), true);
		sugar.users.detailView.getDetailField("emailAddress").assertEquals(ds.get(0).get("email"), true);		

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}