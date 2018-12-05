package com.sugarcrm.test.employees;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Employees_20125 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Employee's "Reports To" field auto fill search
	 * @throws Exception
	 */
	@Test
	public void Employees_20125_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		DataSource ds = testData.get(testName);
		// TODO VOOD-1041
		// open employee create window
		sugar().navbar.toggleUserActionsMenu();
		new VoodooControl("a", "css", "li.profileactions-employees a").click();	
		VoodooUtils.waitForReady();
		new VoodooControl("i", "css", "li[data-module='Employees'] i.fa-caret-down").click();
		new VoodooControl("a", "css", "li[data-module='Employees'] a[data-navbar-menu-item='LNK_NEW_EMPLOYEE']").click();
		VoodooUtils.focusFrame("bwc-frame");
		// input data in report to field
		new VoodooControl("input", "id", "reports_to_name").set(ds.get(0).get("input"));
		new VoodooControl("div", "id", "EditView_reports_to_name_results").waitForVisible();
		new VoodooControl("li", "css", "#EditView_reports_to_name_results ul li").assertEquals(ds.get(0).get("matched"), true);
		new VoodooControl("input", "id", "CANCEL_HEADER").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 