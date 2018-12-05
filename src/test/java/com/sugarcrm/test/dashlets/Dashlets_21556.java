package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21556 extends SugarTest {
	DataSource accounts = new DataSource();
	
	public void setup() throws Exception {
		// Initializing Accounts data
		accounts = testData.get(testName + "_accounts");
		sugar().accounts.api.create(accounts);
		
		// Logging in as admin
		sugar().login();
		
		// Create summation report of account records.
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");

		// TODO: VOOD-822 Need lib support of reports module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "#report_type_div [name='summationImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nxtButtonCtrl = new VoodooControl("input", "id", "nextBtn");
		nxtButtonCtrl.click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		nxtButtonCtrl.click();
		nxtButtonCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div > table input:nth-child(2)").click();
		new VoodooControl("input", "id", "save_report_as").set(sugar().accounts.moduleNamePlural + " " + sugar().reports.moduleNameSingular);
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Customizing dashboard: Verify that the summation chart is displayed on "Home" page when selecting or creating a dashlet in dashboard edit view
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21556_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Click "Home" navigation tab.
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		
		// Click Edit button to go to Dashboard EditView
		sugar().home.dashboard.edit();
		
		// Add a new row and add a dashlet on Dashboard EditView
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(4,1);
		
		// TODO: VOOD-591 - Dashlet support
		new VoodooControl("a", "css", ".table-striped tr:nth-child(19) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Save the dashboard
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();
		
		// Initializing dashlet name csv
		FieldSet dashletData = testData.get(testName).get(0);

		// Verifying that 'Accounts By Type By Industry' dashlet is displayed
		new VoodooControl("h4", "css", "li:nth-child(4) .dashlet-title").assertEquals(dashletData.get("dashletName"), true);

		// Initializing Chart Control
		VoodooControl chartCtrl = new VoodooControl("g", "css", ".nv-chartWrap");
				
		// Verify that the Summation report dashlet is displayed with the Accounts data as created above
		chartCtrl.assertContains(accounts.get(0).get("type"), true);
		chartCtrl.assertContains(accounts.get(1).get("type"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}