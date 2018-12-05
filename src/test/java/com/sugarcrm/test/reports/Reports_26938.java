package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_26938 extends SugarTest {
	VoodooControl reportModuleCtrl;
	VoodooControl createReportCtrl;
	VoodooControl createRowsColsReportCtrl;
	VoodooControl leadsModuleCtrl;
	VoodooControl nextBtnCtrl;
	VoodooControl fieldNameCtrl;
	VoodooControl mktonextBtnCtrl;
	VoodooControl chartbtnCtrl;
	VoodooControl nextoBtnCtrl;
	VoodooControl reportNameCtrl;
	VoodooControl searchTeamCtrl;
	VoodooControl assignedToCtrl;
	VoodooControl removeTeamCtrl;
	VoodooControl setPrimaryCtrl;
	VoodooControl selectTeamCtrl;
	VoodooControl saveAndRunCtrl;
	
	VoodooControl leadsButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl listViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl addNewFieldCtrl;
	VoodooControl moduleFieldNameCtrl;
	VoodooControl reportableCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter1;
	VoodooControl moveToNewFilter2;
	VoodooControl defaultPanelListViewCtrl;
	VoodooControl hiddenPanelListViewCtrl;

	VoodooControl formulaResultCtrl;
	VoodooControl saveBtnCtrl;
	VoodooControl trashCtrl;
	VoodooControl saveFieldBtnCtrl;
	VoodooControl studioInFooterCtrl;
	VoodooControl resetModuleCtrl;
	VoodooControl resetRelationshipsCtrl;
	VoodooControl resetFieldsCtrl;
	VoodooControl resetLayoutsCtrl;
	VoodooControl resetLabelsCtrl;
	VoodooControl resetExtensionsCtrl;
	VoodooControl executeRepairCtrl;
	VoodooControl saveAndDeployCtrl;
	VoodooControl restoreDefaultCtrl;
	
	DataSource ds;
	
	CaseRecord myCase;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);
		
		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)");
		createRowsColsReportCtrl = new VoodooControl("img", "name", "rowsColsImg");
		leadsModuleCtrl = new VoodooControl("table", "id", "Leads");
		nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		chartbtnCtrl = new VoodooControl("option", "css", "select[name='chart_type'] option:nth-of-type(2)");
		nextoBtnCtrl = new VoodooControl("input", "css", "div#chart_options_div table:nth-of-type(1) input#nextButton");
		reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		searchTeamCtrl = new VoodooControl("input", "css", "#ReportsWizardForm_team_name_table tbody tr:nth-child(2) td:nth-child(1) span input.sqsEnabled.yui-ac-input");
		assignedToCtrl = new VoodooControl("input", "id", "assigned_user_name");
		removeTeamCtrl = new VoodooControl("input", "id", "remove_team_name_collection_0");
		setPrimaryCtrl = new VoodooControl("input", "id", "primary_team_name_collection_0");
		selectTeamCtrl = new VoodooControl("li", "css", "div#ReportsWizardForm_ReportsWizardForm_team_name_collection_0_results ul li:nth-of-type(1)");
		saveAndRunCtrl = new VoodooControl("input", "css", "div#report_details_div table:nth-child(1) #saveAndRunButton");

		// TODO: VOOD-938
		leadsButtonCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		addNewFieldCtrl = new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]");
		moduleFieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		reportableCtrl = new VoodooControl("input", "name", "reportableCheckbox");
		layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewButtonCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		listViewButtonCtrl = new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a");
		defaultPanelListViewCtrl = new VoodooControl("ul", "css", "td#Default ul");
		hiddenPanelListViewCtrl = new VoodooControl("ul", "css", "td#Hidden ul");
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveToNewFilter1 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		moveToNewFilter2 =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)"); 
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		trashCtrl = new VoodooControl("div", "id", "delete");
		saveFieldBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		studioInFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		resetModuleCtrl = new VoodooControl("input", "id" ,"exportBtn");
		resetRelationshipsCtrl = new VoodooControl("input", "name" ,"relationships");
		resetFieldsCtrl = new VoodooControl("input", "name" ,"fields");
		resetLayoutsCtrl = new VoodooControl("input", "name" ,"layouts");
		resetLabelsCtrl = new VoodooControl("input", "name" ,"labels");
		resetExtensionsCtrl = new VoodooControl("input", "name" ,"extensions");
		executeRepairCtrl = new VoodooControl("button", "id" ,"execute_repair");
		saveAndDeployCtrl = new VoodooControl("input", "id", "publishBtn");
		restoreDefaultCtrl = new VoodooControl("input", "id", "historyDefault");

		// Navigate to Admin > Studio > Leads > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Add decimal field and save
		addNewFieldCtrl.click();
		VoodooUtils.waitForReady();
		// Select Data Type - Decimal
		new VoodooControl("option", "css", "select#type option[value='decimal']").click();
		VoodooUtils.waitForReady();
		moduleFieldNameCtrl.set(ds.get(0).get("decimalName"));

		// Save button
		saveFieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Add float field and save
		// TODO: Investigate as to why second continuous invocation of addNewFieldCtrl is not working. Kludge: below 6 lines
		studioInFooterCtrl.click();
		VoodooUtils.waitForReady();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		addNewFieldCtrl.click();
		VoodooUtils.waitForReady();
		// Select Data Type - Float
		new VoodooControl("option", "css", "select#type option[value='float']").click();
		VoodooUtils.waitForReady();
		moduleFieldNameCtrl.set(ds.get(0).get("floatName"));

		// Save button
		saveFieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: Investigate why Leads in breadcrumb is not working
		// new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		// VoodooUtils.waitForReady();
		studioInFooterCtrl.click();
		VoodooUtils.waitForReady();
		leadsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add new decimal field to Leads Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();

		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(0).get("decimalName")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter1);

		dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(0).get("floatName")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter2);

		saveAndDeployCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		
		// Create test Lead rec
		sugar().leads.api.create();
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.edit();
		String customFieldName1 = String.format("span.fld_%s_c.edit input",ds.get(0).get("decimalName"));
		String customFieldName2 = String.format("span.fld_%s_c.edit input",ds.get(0).get("floatName"));
		new VoodooControl("input", "css", customFieldName1).set(ds.get(0).get("decimalValue"));
		new VoodooControl("input", "css", customFieldName2).set(ds.get(0).get("floatValue"));
		sugar().leads.recordView.save();
		sugar().alerts.waitForLoadingExpiration(30000);
	}

	/**
	 * Verify that custom decimal and float fields are formatted properly in Reports
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_26938_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule("Reports");
		VoodooUtils.focusDefault();
		
		reportModuleCtrl.click();
		createReportCtrl.click();		

		VoodooUtils.focusFrame("bwc-frame");
		
		createRowsColsReportCtrl.click();
		leadsModuleCtrl.click();
		// Filter
		nextBtnCtrl.click(); 
	
		// Select columns
		String customFieldName = String.format("Leads_%s_c",ds.get(0).get("decimalName"));
		new VoodooControl("tr", "id", customFieldName).click();
		customFieldName = String.format("Leads_%s_c",ds.get(0).get("floatName"));
		new VoodooControl("tr", "id", customFieldName).click();
		nextBtnCtrl.click();
		
		// Save Report
		reportNameCtrl.set(ds.get(0).get("reportName"));
		saveAndRunCtrl.click();
		
		// Check Expected Result 1
		new VoodooControl("td", "css", "table.list.view tr:nth-of-type(3) td:nth-child(1)").assertContains(ds.get(0).get("decimalExpectedValue1"), true);
		new VoodooControl("td", "css", "table.list.view tr:nth-of-type(3) td:nth-child(2)").assertContains(ds.get(0).get("floatExpectedValue1"), true);

		VoodooUtils.focusDefault();

		// Change thousands separator and decimal separator
		FieldSet fs =  new FieldSet();
		fs.put("advanced_grouping_seperator", ".");
		fs.put("advanced_decimal_separator", ",");
		sugar().users.setPrefs(fs);

		// Run Report again
		sugar().navbar.navToModule("Reports");
		// TODO: VOOD-1062
		new VoodooControl("button", "css", "li[data-module='Reports'] .btn.btn-invisible.dropdown-toggle").click();
		VoodooUtils.pause(2000); // Wait added to allow the custom report to appear in the dropdown menu
		new VoodooControl("li", "css", "li[data-module='Reports'] ul[role='menu'] li:nth-of-type(6)").click();
		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "showHideReportDetails").waitForVisible(60000);
		
		// Check Expected Result 2
		new VoodooControl("td", "css", "table.list.view tr:nth-of-type(3) td:nth-child(1)").assertContains(ds.get(0).get("decimalExpectedValue2"), true);
		new VoodooControl("td", "css", "table.list.view tr:nth-of-type(3) td:nth-child(2)").assertContains(ds.get(0).get("floatExpectedValue2"), true);

		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}