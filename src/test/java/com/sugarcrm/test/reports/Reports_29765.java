package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_29765 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl accountsCtrl, searchBtnCtrl;

	public void setup() throws Exception {
		sugar().login();
		
		customData = testData.get(testName).get(0);
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		
		// Create and deploy a custom field for Accounts module in Record view
		// TODO: VOOD-542
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();

		// Add field and save
		// TODO: VOOD-542
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// Add field to Record view
		// TODO: VOOD-542
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that user should not get any error message "Database Failure" while sorting through Custom field
	 * in Ascending/Descending order.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_29765_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create 2 records of Accounts module with custom field value
		sugar().accounts.navToListView();
		for (int i = 0; i < 2; i++) {
			sugar().accounts.listView.create();
			sugar().accounts.createDrawer.getEditField("name").set(testName+"_"+i);
			new VoodooControl("input", "css", ".fld_customfield_c.edit input").set(customData.get("customField"+(i+1)));
			sugar().accounts.createDrawer.save();
		}
				
		// TODO: VOOD-822
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		searchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		
		// Navigate to Reports-> Create a new Report-> Rows and Columns Reports
		sugar().reports.navToListView();
		VoodooUtils.waitForReady();
		sugar().navbar.clickModuleDropdown(sugar().reports);
		VoodooUtils.waitForReady();
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "img[name='rowsColsImg']").click();
		
		// Click on Accounts module then click on next button
		new VoodooControl("table", "id", sugar().accounts.moduleNamePlural).click();
		nextBtnCtrl.click();
		
		// Click on fields name like Name and more above created Custom field
		// TODO: VOOD-822
		new VoodooControl("input", "id", "Accounts_name").click();
		new VoodooControl("input", "id", "Accounts_customfield_c").click();
		
		// Click on next button
		nextBtnCtrl.click();
		
		// Enter Report Name and click on Save and Run button
		reportNameCtrl.set(customData.get("reportName"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Sort the report in either Ascending/Descending according to Custom field
		// TODO: VOOD-1408
		new VoodooControl("a", "css", "#report_results tr:nth-child(2) th:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		
		// Verify that sorting is working rather than displaying "Database Failure"
		// TODO: VOOD-822
		new VoodooControl("div", "id", "report_results").assertContains(customData.get("databaseFailure"), false);
		new VoodooControl("td", "css", "#report_results tr.oddListRowS1 td:nth-child(2)").assertContains(customData.get("customField1"), true);
		new VoodooControl("td", "css", "#report_results tr.evenListRowS1 td:nth-child(2)").assertContains(customData.get("customField2"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}