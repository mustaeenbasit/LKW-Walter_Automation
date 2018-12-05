package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_24507  extends SugarTest {
	VoodooControl accountsButtonCtrl, reportModuleCtrl;
	DataSource customFieldData;
	FieldSet customData;

	public void setup() throws Exception {
		customFieldData = testData.get(testName);
		customData = testData.get(testName + "_1").get(0);
		sugar().login();

		// TODO: VOOD-938 
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl moduleFieldName = new VoodooControl("input", "id", "field_name_id");
		VoodooControl fieldSaveBtn = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		VoodooControl addFieldBtn = new VoodooControl("input", "css", "#studiofields > input:nth-child(1)");
		VoodooControl fieldDataType = new VoodooControl("select", "css", "#type");
		VoodooControl calculateBtnCtrl = new VoodooControl("input", "id", "calculated");
		VoodooControl editFormulaBtnControl = new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']");
		VoodooControl formulaInputControl = new VoodooControl("textarea", "css", "#formulaInput");
		VoodooControl formulaSaveControl = new VoodooControl("input", "id", "fomulaSaveButton");

		// Navigate to Admin > Studio > Accounts > Fields > Name		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		accountsButtonCtrl.click(); 
		VoodooUtils.waitForReady();

		// create custom  calculated fields in admin module 
		for(int i = 0 ; i < customFieldData.size() ; i++){
			fieldCtrl.click();
			addFieldBtn.click();
			fieldDataType.set(customFieldData.get(i).get("data_type"));
			VoodooUtils.waitForReady();
			moduleFieldName.set(customFieldData.get(i).get("m_field_name"));
			calculateBtnCtrl.click();
			editFormulaBtnControl.click();
			// Set formula
			formulaInputControl.set(customData.get("formula"));
			formulaSaveControl.click();
			VoodooUtils.waitForReady();
			fieldSaveBtn.click();
			VoodooUtils.waitForReady();
			// multiple fields not working on JENKINS, that's why below code add
			sugar().admin.studio.clickStudio();
			accountsButtonCtrl.click();
			VoodooUtils.waitForReady();
		}

		// Layout
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		for (int i = 0; i < customFieldData.size(); i++) {		
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customFieldData.get(i).get("m_field_name")+"_c"); 
			new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);			
		}
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Show calculated fields in report
	 * @throws Exception
	 */
	@Test
	public void Reports_24507_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(customData.get("account_name"));
		sugar().accounts.createDrawer.save();

		// TODO: VOOD-822
		// Create report
		sugar().navbar.navToModule(customData.get("report_module_plural_name"));
		reportModuleCtrl = new VoodooControl("li", "css", ".dropdown.active .fa-caret-down");
		VoodooControl createReportCtrl = new VoodooControl("a", "css", "[data-navbar-menu-item='LBL_CREATE_REPORT']");
		VoodooControl createSummationReportCtrl = new VoodooControl("img", "css", "img[name='summationWithDetailsImg']");
		VoodooControl myFormulaNameCtrl = new VoodooControl("tr", "id", "Accounts_"+customFieldData.get(2).get("m_field_name")+"_c");
		VoodooControl myFloatNameCtrl = new VoodooControl("tr", "id", "Accounts_"+customFieldData.get(1).get("m_field_name")+"_c");
		VoodooControl myCurrencyNameCtrl = new VoodooControl("tr", "id", "Accounts_"+customFieldData.get(0).get("m_field_name")+"_c");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl nxtbtnCtrl = new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");

		// Create Custom Report in Accounts module, select the created custom field
		reportModuleCtrl.click();
		createReportCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Accounts").click();
		myFormulaNameCtrl.assertContains(customFieldData.get(2).get("m_field_name"), true);
		myFloatNameCtrl.assertContains(customFieldData.get(1).get("m_field_name"), true);
		myCurrencyNameCtrl.assertContains(customFieldData.get(0).get("m_field_name"), true);
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Accounts_count").click();
		nextBtnCtrl.click();
		myFormulaNameCtrl.waitForVisible();
		myFormulaNameCtrl.click();
		myFloatNameCtrl.click();
		myCurrencyNameCtrl.click();
		nextBtnCtrl.click();
		nxtbtnCtrl.click();
		reportNameCtrl.set(customData.get("name"));
		// save report
		saveAndRunCtrl.click();

		// Verify calculated fields in report 
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(1) > td:nth-child(3)").assertContains(customFieldData.get(0).get("m_field_name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(1) > td:nth-child(2)").assertContains(customFieldData.get(1).get("m_field_name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(1) > td:nth-child(1)").assertContains(customFieldData.get(2).get("m_field_name"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(2) > td:nth-child(1)").assertContains(customData.get("myformula_val"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(2) > td:nth-child(2)").assertContains(customData.get("myfloat_val"),true);
		new VoodooControl("td", "css", ".reportlistView tr:nth-child(2) > td:nth-child(3)").assertContains(customData.get("mycurrency_val"),true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}