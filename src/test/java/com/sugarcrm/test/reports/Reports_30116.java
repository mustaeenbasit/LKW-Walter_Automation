package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_30116 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify usefulness label should not be displayed in Reports
	 * @throws Exception
	 */
	@Test
	public void Reports_30116_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-822 Need library support for Reports module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='summationImg']");

		// Create a Summation Report based on Opportunity
		sugar().reports.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		createSummationReportCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("table", "id", sugar().opportunities.moduleNamePlural).click();
		VoodooUtils.waitForReady();

		// In Related Module section, Expand "Assigned to User"/"Users" section by clicking on "+" icon available before it.
		new VoodooControl("td", "id", "ygtvt3").click();
		VoodooUtils.waitForReady();

		// Verify 'Usefulness" label should not be displayed
		new VoodooControl("div", "id", "ygtvc3").assertContains(customData.get("verificationText"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}