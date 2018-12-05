package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29748 extends SugarTest {

	public void setup() throws Exception {
		// Creating Bug records
		DataSource bugRecord = testData.get(testName + "_bugs");
		sugar().bugs.api.create(bugRecord);

		sugar().login();
	}

	/**
	 * Verify that  "Database Failure" error message should not be appear while creating a Report.
	 * @throws Exception
	 */
	@Test
	public void Reports_29748_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Creating Rows and Columns Report
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-822
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		new VoodooControl("table", "id", "Bugs").click();

		// Click "Next
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();

		// Click on "Subject" field
		new VoodooControl("tr", "id", "Bugs_name").click();

		// Click on sort by = "Ascending"
		new VoodooControl("input", "css", "[name='order_by_radio']").click();

		// Click "Next
		nextBtnCtrl.click();

		FieldSet fs = testData.get(testName).get(0);
		new VoodooControl("input", "id", "save_report_as").set(fs.get("reportName"));

		// Click on "Preview"
		new VoodooControl("input", "css", "#report_details_div #previewButton").click();

		VoodooControl reportWizardCtrl = new VoodooControl("div", "id", "content");

		// Verify User should not get any error message "Database Failure"
		reportWizardCtrl.assertContains(fs.get("errorMessage"), false);

		// Save and Run
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify User should not get any error message "Database Failure"
		reportWizardCtrl.assertContains(fs.get("errorMessage"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}