package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class TargetLists_19654 extends SugarTest {
	FieldSet customData, fs;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		
		// Creating TargetList records
		sugar().targetlists.api.create();
		sugar().login();
		
		// TODO: VOOD-822
		VoodooControl rowColsImg = new VoodooControl("img", "css", "[name='rowsColsImg']");
		VoodooControl targetsLink = new VoodooControl("table", "id", "Targets");
		VoodooControl targetsFullName = new VoodooControl("tr", "id", "Prospects_full_name");
		VoodooControl condition = new VoodooControl("select", "css", "#filter_designer_div [name='qualify']");
		VoodooControl FilterInputBox = new VoodooControl("input", "css", "#filter_designer_div [name='text_input']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl targetAccName = new VoodooControl("tr", "css", "[id='Prospects_account_name']");
		VoodooControl reportNameField = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunButton = new VoodooControl("input", "id", "saveAndRunButton");
		
		
		// Creating 2 reports based on Targets module
		for (int i = 0; i <= 1; i++) {
	
			// Create Rows and Columns report, select Targets module
			sugar().navbar.selectMenuItem(sugar().reports, "createReport");
			VoodooUtils.focusFrame("bwc-frame");
	
			// Select Row and Column Report Type > Targets Module
			rowColsImg.click();
			targetsLink.click();
			
			// Set filter for Targets name
			targetsFullName.click();
			condition.set(customData.get("condition"));
			FilterInputBox.set(testName);
			nextBtnCtrl.click();
			
			// Configure display fields for report
			targetAccName.click();
			nextBtnCtrl.click();
			
			// Save and Run report
			reportNameField.set(testName+"_Report_"+(i+1));
			saveAndRunButton.click();
			VoodooUtils.focusDefault();
		}
		
		// Changing 'Listview Items per page' in Admin >> System Settings to '1'
		fs = new FieldSet();
		fs.put("maxEntriesPerPage", customData.get("maxEntriesPerPageTest"));
		sugar().admin.setSystemSettings(fs);
	}

	/**
	 * Target Lists: Pagination function in the popup "Select From Report" windows in the "Targets" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_19654_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		// TODO: VOOD-1420
		VoodooControl moreReportsLink = new VoodooControl("button", "css", "[data-action='show-more']");
		VoodooControl reportListPopUpTitle = new VoodooControl("span", "css", ".record-cell [data-fieldname='title']");
		
		sugar().targetlists.navToListView();		
		sugar().targetlists.listView.clickRecord(1);
	
		// Navigating to Targets subpanel and selecting the 'Select from Reports' option
		sugar().targetlists.recordView.subpanels.get(sugar().targets.moduleNamePlural).clickOnSelectFromReport();
	
		// Verifying that Report List pop-up menu is displayed
		reportListPopUpTitle.assertContains(customData.get("reportListPopUpTitle"), true);
		
		// Verifying that only one report is displayed
		Assert.assertTrue("Reports in listview are not equal to ONE when it should", sugar().reports.searchSelect.countRows() == 1);
		moreReportsLink.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verifying that only two report is displayed post clicking the show more link
		Assert.assertTrue("Reports in listview are not equal to TWO when it should", sugar().reports.searchSelect.countRows() == 2);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}