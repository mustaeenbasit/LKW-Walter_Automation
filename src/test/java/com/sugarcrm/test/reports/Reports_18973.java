package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.test.SugarTest;

public class Reports_18973 extends SugarTest {
	VoodooControl reportModuleCtrl;
	VoodooControl createReportCtrl;
	VoodooControl createSummationReportCtrl;
	VoodooControl casesModuleCtrl;
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
	
	VoodooControl casesButtonCtrl;
	VoodooControl layoutsButtonCtrl;
	VoodooControl recordViewButtonCtrl;
	VoodooControl listViewButtonCtrl;
	VoodooControl fieldCtrl;
	VoodooControl addNewFieldCtrl;
	VoodooControl moduleFieldNameCtrl;
	VoodooControl reportableCtrl;
	VoodooControl moveToLayoutPanelCtrl;
	VoodooControl moveToNewFilter;
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
	
	CaseRecord myCase1, myCase2;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();

		ds = testData.get(testName);
		
		// TODO: VOOD-822
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		createReportCtrl = new VoodooControl("li", "css", ".dropdown.active .dropdown-menu.scroll li:nth-of-type(1)");
		createSummationReportCtrl = new VoodooControl("img", "name", "summationImg");
		casesModuleCtrl = new VoodooControl("table", "id", "Cases");
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
		casesButtonCtrl = new VoodooControl("a", "id", "studiolink_Cases");
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
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
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

		// Navigate to Admin > Studio > Cases > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		
		casesButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// Add field and save
		addNewFieldCtrl.click();
		VoodooUtils.waitForReady();
		// Select Data Type - Checkbox
		new VoodooControl("option", "css", "select#type option[value='bool']").click();
		VoodooUtils.waitForReady();
		
		moduleFieldNameCtrl.set(ds.get(0).get("fieldName"));
		reportableCtrl.set("true");

		// Save button
		saveFieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Cases in breadcrumb
		// TODO: Investigate why below Calls breadcrumb control in Add fields page is not 
		// working in Jenkins.
		// new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		// VoodooUtils.waitForReady();

		studioInFooterCtrl.click();
		VoodooUtils.waitForReady();
		casesButtonCtrl.click();
		VoodooUtils.waitForReady();

		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Add new field to Cases Record view
		recordViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",ds.get(0).get("fieldName")); 

		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		
		// Create accounts rec as it is a mandatory field in Cases module
		myAccount = (AccountRecord)sugar().accounts.api.create();
		
		// Create two Case recs. First with myfield as false and second one with myfield as true
		myCase1 = (CaseRecord)sugar().cases.api.create(); // Create First Case
		FieldSet fs = new FieldSet();
		fs.put("name", "Yet Another Case");
		myCase2 = (CaseRecord) sugar().cases.api.create(fs); //Create Second Case
		
		myCase1.navToRecord();
		sugar().cases.recordView.edit();
		sugar().cases.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		new VoodooControl("input", "css", "span.fld_myfield_c.edit input").set("false");
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		myCase2.navToRecord();
		sugar().cases.recordView.edit();
		sugar().cases.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		new VoodooControl("input", "css", "span.fld_myfield_c.edit input").set("true");
		sugar().cases.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Run summation report with custom checkbox field as group by
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_18973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule("Reports");
		VoodooUtils.focusDefault();
		
		reportModuleCtrl.click();
		createReportCtrl.click();		

		VoodooUtils.focusFrame("bwc-frame");
		
		createSummationReportCtrl.click();
		casesModuleCtrl.click();
		// Filter
		nextBtnCtrl.click(); 
	
		// Group by
		String customFieldName = String.format("Cases_%s_c",ds.get(0).get("fieldName"));
		new VoodooControl("tr", "id", customFieldName).click();
		nextBtnCtrl.click();
		
		// Display Summaries
		// TC Step#12: Click Accounts
		new VoodooControl("a", "xpath", "//*[@id='module_tree']/div/div/div/div/div[1]/table/tbody/tr/td[contains(.,'Account')]/a").click();
		// TC Step#12: The selected field is still here
		new VoodooControl("tr", "id", "display_summaries_row_group_by_row_1").assertContains(ds.get(0).get("fieldName"), true);
		nextBtnCtrl.click();
		
		// Select chart type
		chartbtnCtrl.click();
		nextoBtnCtrl.click();
		
		// Save Report
		reportNameCtrl.set(ds.get(0).get("reportName"));
		saveAndRunCtrl.click();
		
		// As two Case recs exist - one with myfield checked and another unchecked, hence
		// there should be two lines in the reports - one with 'No' and another with 'Yes'
		new VoodooControl("td", "css", "div#report_results tr:nth-of-type(2) td").assertContains("No", true);
		new VoodooControl("td", "css", "div#report_results tr:nth-of-type(3) td").assertContains("Yes", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}