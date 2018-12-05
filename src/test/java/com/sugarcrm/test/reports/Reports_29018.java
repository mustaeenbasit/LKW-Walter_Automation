package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ManufacturerRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_29018 extends SugarTest {
	ManufacturerRecord myManufacturerRecord;
	
	public void setup() throws Exception {
		// Create test manufacturer record
		myManufacturerRecord = (ManufacturerRecord) sugar().manufacturers.api.create();
		
		// Login as admin
		sugar().login();

		// QLI module should be enabled in Admin > Display Modules and Subpanels
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that Fatal error should not be displayed When select Manufacturer module as a related module of QLI in Reports module
	 * @throws Exception
	 */
	@Test
	public void Reports_29018_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-822 - Need lib support of reports module
		// Create Custom(Rows and Columns) Report in QLI module
		VoodooControl createSummationReportCtrl = new VoodooControl("td", "css", "img[name='rowsColsImg']");

		// Go to Reports -> Create reports -> Rows and Columns -> QLI 
		sugar().navbar.navToModule("Reports");
		new VoodooControl("li", "css", ".dropdown.active .fa-caret-down").click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.waitForVisible();
		createSummationReportCtrl.click();
		VoodooUtils.waitForReady();
		
		// Clicking QLI module
		new VoodooControl("a", "css", "tr:nth-child(5) td a").click();
		VoodooUtils.waitForReady();

		// Select Assigned to User
		new VoodooControl("td", "css", ".ygtvchildren div:nth-child(10) td:nth-child(3)").click();
		new VoodooControl("tr", "id", "Manufacturers_name").click();
		new VoodooControl("input", "css", "[value='Select']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Verify that Fatal error should not be displayed
		new VoodooControl("body", "css", ".popupBody").assertContains(customData.get("errorMessage"), false);

		VoodooControl manufacturerName = new VoodooControl("a", "css", "tr.oddListRowS1 td a");
		
		// Verify that a list of Manufacturers should be displayed
		manufacturerName.assertContains(sugar().manufacturers.getDefaultData().get("name"), true);

		// Select a manufacturer
		manufacturerName.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that selected manufacturer displayed
		new VoodooControl("input", "css", ".bd .sqsEnabled").assertContains(myManufacturerRecord.getRecordIdentifier(), true);

		// Cancel report creation
		new VoodooControl("input", "css", "#filters_div [name='Cancel']").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}