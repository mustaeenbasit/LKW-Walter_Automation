package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18961  extends SugarTest {
	VoodooControl advanceReportingCtrl, navigationCtrl, viewAdvanceReportCtrl;
	DataSource ds;

	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);

		// VOOD-643
		// navigate to report module 
		sugar().navbar.navToModule(ds.get(0).get("module_plural_name"));
		navigationCtrl =new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		navigationCtrl.click();

		// VOOD-1057 
		// Click "Manage Advanced Reports" link from navigation shortcuts.
		advanceReportingCtrl = new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_ADVANCED_REPORTING']");
		advanceReportingCtrl.click();
		navigationCtrl.click();

		// Click Create Custom Query link
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "[name = 'name']").set(ds.get(0).get("query_name"));

		// Enter valid query in query edit view
		new VoodooControl("textarea", "id", "custom_query").set(ds.get(0).get("query"));

		// Click "Save" button.
		new VoodooControl("input", "css", "#EditView > input.button").click();
		VoodooUtils.focusDefault();

		// Click to Advance Report Module
		sugar().navbar.navToModule(ds.get(0).get("advance_report_name"));
		navigationCtrl.click();

		// Click Create Advance Report link
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_REPORTMAKER']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "report_name").set(ds.get(0).get("report_name"));

		// Click "Save" button.
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();

		navigationCtrl.click();
		// Click Create Data Format link
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_DATASET']").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "name").set(ds.get(0).get("data_format_name"));
		new VoodooControl("button", "id", "btn_query_name").click();
		VoodooUtils.focusWindow(1);

		// select custom query
		new VoodooControl("a", "css", "table.list.view tr:nth-child(3) td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Click "Save" button.
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		VoodooUtils.focusDefault();
	}

	/**
	 *  Verify that Enterprise Data format is in editable mode in Reports
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_18961_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Advance Reports in navbar 
		sugar().navbar.navToModule(ds.get(0).get("advance_report_name"));
 		navigationCtrl.click();
 		
 		// Click on View Advance Report link
 		viewAdvanceReportCtrl = new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_LIST_REPORTMAKER']");
 		viewAdvanceReportCtrl.click();
 		VoodooUtils.focusFrame("bwc-frame");
 		
 		// click on list item
 		new VoodooControl("a", "css", "tr.oddListRowS1 > td:nth-child(3) a").click();
 		VoodooUtils.focusDefault();
 		VoodooUtils.focusFrame("bwc-frame");
 		
 		// Click to Select to add data format in report
 		new VoodooControl("input", "css", "#form [title='Select']").click();
 		VoodooUtils.focusWindow(1);
 		
 		// select data format in list
 		new VoodooControl("a", "css", "tr.oddListRowS1 td:nth-child(1) a").click();
 		VoodooUtils.focusWindow(0);
 		VoodooUtils.focusFrame("bwc-frame");
 		
 		// Click "Edit" of a "Data Format"
 		new VoodooControl("a", "css", "#contentTable tr.oddListRowS1 > td:nth-child(6) > slot > a:nth-child(3)").click();
 		VoodooUtils.focusDefault();
 		VoodooUtils.focusFrame("bwc-frame");
 		new VoodooControl("input", "id", "name").assertEquals(ds.get(0).get("data_format_name"), true);
 		new VoodooControl("input", "id", "query_name").assertEquals(ds.get(0).get("query_name"), true);
 		new VoodooControl("a", "css", "#Default_DataSets_Subpanel tr:nth-child(1) td:nth-child(4) a").assertEquals(ds.get(0).get("report_name"), true);
 		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}