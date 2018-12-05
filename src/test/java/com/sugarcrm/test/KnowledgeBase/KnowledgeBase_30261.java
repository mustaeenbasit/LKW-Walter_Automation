package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30261 extends SugarTest {
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName);
		sugar().knowledgeBase.api.create(kbData);
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Test Case 30261: Verify that report has been saved and displayed correctly for KB 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30261_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		// Creating row column report for kb module
		new VoodooControl("img", "css", "[name='rowsColsImg']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#buttons_table tr:nth-child(3) #buttons_td:nth-of-type(2) td").click();
		VoodooUtils.waitForReady();

		// Defining Language filter 
		new VoodooControl("tr", "id", "KBContents_language").click();
		VoodooUtils.waitForReady();

		// Clicking on next button
		VoodooControl nextButtonCtrl = new VoodooControl("input", "id", "nextBtn");
		nextButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Selecting display columns
		new VoodooControl("tr", "id", "KBContents_id").click();
		new VoodooControl("tr", "id", "KBContents_language").click();
		new VoodooControl("tr", "id", "KBContents_name").click();
		new VoodooControl("tr", "id", "KBContents_approved").click();
		new VoodooControl("tr", "id", "KBContents_exp_date").click();
		new VoodooControl("tr", "id", "KBContents_active_date").click();

		// Clicking on next button
		nextButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Entering "Report Name", Check box "Show Query"
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "id", "show_query").click();
		VoodooUtils.waitForReady();

		// Save and run report
		new VoodooControl("input", "id", "saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verifying report's name
		new VoodooControl("table", "id", "reportDetailsTable").assertContains(testName, true);

		// Verifying all KB records are displaying in report
		VoodooControl kbReportDataPanel = new VoodooControl("div", "css", ".listViewBody.nosearch");
		for (int i = 0; i < kbData.size(); i++) {
			kbReportDataPanel.assertContains(kbData.get(i).get("name"), true);
		}

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}