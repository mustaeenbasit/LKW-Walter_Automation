package com.sugarcrm.test.cases;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */

public class Cases_18096 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Create report with cases module
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		new VoodooControl("img", "css", "img[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Cases").click();
		VoodooControl nextBtn = new VoodooControl("input", "id", "nextBtn");
		nextBtn.click();
		new VoodooControl("tr", "id", "Cases_name").click();
		nextBtn.click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify user able to view reports for Cases from Cases module drop down list
	 * @throws Exception
	 */

	@Test
	public void Cases_18096_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.selectMenuItem(sugar().cases, "viewCaseReports");
		VoodooUtils.waitForReady();
		// TODO: VOOD-822
		// Verify reports module is in bwc-mode
		new VoodooControl("iframe", "id", "bwc-frame").assertExists(true);

		// Verify all case reports be displayed in listview (4 default + 1 newly created in set up)
		int row = sugar().reports.listView.countRows();
		Assert.assertTrue("Number of rows did not equal FIVE.", row == 5);

		// Verify report according to search
		new VoodooControl("input", "css", "#Reportsadvanced_searchSearchForm [name='name']").set(testName);
		new VoodooControl("input", "id", "search_form_submit_advanced").click();
		VoodooUtils.focusDefault();

		// Verify report is populated according to search and count of rows is 1
		row = sugar().reports.listView.countRows();
		Assert.assertTrue("Number of rows did not equal ONE.", row == 1);
		new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-of-type(4) span a").assertContains(testName, true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}