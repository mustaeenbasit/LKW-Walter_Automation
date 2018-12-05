package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28548 extends SugarTest {
	DataSource ds = new DataSource();
	VoodooControl searchCtrl;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().login();
	}

	/**
	 * Verify that "Current Quarter Forecast" and "Detailed Forecast" reports are disabled in the Opps+RLIs mode
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_28548_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Reports module
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);

		// TODO: VOOD-822 :- need lib support of reports module 
		VoodooControl nameCtrl = new VoodooControl("input", "css", "#Reportsadvanced_searchSearchForm [name='name']");
		searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		VoodooControl listViewCtrl = new VoodooControl("div", "css", ".list.view");

		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl.set(ds.get(0).get("reportName"));
		// TODO: VOOD-975
		searchCtrl.click();
		
		// Assert that "Current Quarter Forecast" reports is not present in the list of OOTB reports
		listViewCtrl.assertContains(ds.get(0).get("reportName"), false);
		nameCtrl.set(ds.get(1).get("reportName"));
		searchCtrl.click();
		// Assert that "Detailed Forecast"  reports is not present in the list of OOTB reports
		listViewCtrl.assertContains(ds.get(1).get("reportName"), false);
		VoodooUtils.focusDefault();

		// Switch to Opportunities only mode
		sugar().admin.api.switchToOpportunitiesView();

		// Go to reports module  
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		nameCtrl.set(ds.get(0).get("reportName"));
		
		// TODO: VOOD-975
		searchCtrl.click();
		// Assert that "Current Quarter Forecast" reports is available in the list of OOTB reports  
		listViewCtrl.assertContains(ds.get(0).get("reportName"), true);
		nameCtrl.set(ds.get(1).get("reportName"));
		searchCtrl.click();
		// Assert that "Detailed Forecast" reports is available in the list of OOTB reports  
		listViewCtrl.assertContains(ds.get(1).get("reportName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}