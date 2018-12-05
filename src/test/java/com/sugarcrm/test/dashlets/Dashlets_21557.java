package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21557 extends SugarTest {
	DataSource ds = new DataSource();
	FieldSet demoRecord = new FieldSet();
	UserRecord demoUser;

	public void setup() throws Exception {
		ds = testData.get(testName);
		demoRecord = testData.get(testName + "_1" ).get(0);
		sugar.accounts.api.create(ds);
		sugar.login();
		// Create User
		demoUser = (UserRecord)sugar.users.create();
		sugar.logout();

		// Login as a valid user 
		demoUser.login();

		// Assigned created account record to demo user
		sugar.accounts.navToListView();
		FieldSet fs = new FieldSet();
		fs.put(demoRecord.get("key"), demoRecord.get("value"));
		sugar.accounts.listView.checkRecord(1);
		sugar.accounts.listView.checkRecord(2);
		sugar.accounts.massUpdate.performMassUpdate(fs);
		sugar.alerts.getSuccess().closeAlert();

		// Create summation report of account records.
		sugar.navbar.selectMenuItem(sugar.reports, "createReport");

		// TODO: VOOD-822 -need lib support of reports module
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "css", "#report_type_div [name='summationImg']").click();
		new VoodooControl("table", "id", "Accounts").click();
		VoodooControl nxtButtonCtrl = new VoodooControl("input", "id", "nextBtn");
		nxtButtonCtrl.click();
		new VoodooControl("tr", "id", "Accounts_industry").click();
		nxtButtonCtrl.click();
		nxtButtonCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div > table input:nth-child(2)").click();
		new VoodooControl("input", "id", "save_report_as").set(demoRecord.get("reportName"));
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.focusDefault();
	}

	/**
	 * Customizing dashboard_Verify that summation report detail view page is displayed when clicking 
	 * "Edit" link on top right corner of customization chart.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21557_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Click "Home" navigation tab.
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		// Click Create | Add a Row | Add a Dashlet | Saved Reports Chart Dashlet
		sugar.home.dashboard.clickCreate();
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(1, 1);
		// TODO: VOOD-591- Dashlets Support
		new VoodooControl("a", "css", ".table-striped tr:nth-child(16) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".detail.fld_save_button [name='save_button']").click();
		sugar.home.dashboard.getControl("title").set(demoRecord.get("dashboardName"));
		// Save the dashlet
		sugar.home.dashboard.save();
		VoodooUtils.waitForReady();
		VoodooControl chartCtrl = new VoodooControl("g", "css", ".nv-chartWrap");
		// Verify that the summation report detail view page is displayed
		chartCtrl.assertContains(ds.get(0).get("type"), true);
		chartCtrl.assertContains(ds.get(1).get("type"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}