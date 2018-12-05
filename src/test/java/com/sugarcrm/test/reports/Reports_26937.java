package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_26937 extends SugarTest {
	FieldSet userPref = new FieldSet();
	VoodooControl iconCtrl,studioCtrl,layoutsBtn,studioLinkLeads,viewRecorView;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that custom integer field is formatted properly in Reports.
	 * @throws Exception
	 */
	@Test
	public void Reports_26937_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to admin panel 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on studio link  
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Click on leads in studio panel 
		// TODO: VOOD-1504
		studioLinkLeads = new VoodooControl("a", "id", "studiolink_Leads");
		studioLinkLeads.click();
		VoodooUtils.waitForReady();

		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option:nth-of-type(13)").click();
		VoodooUtils.waitForReady();
		FieldSet customData = testData.get(testName).get(0);
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));

		// Click Save button
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		//  Add field to Record View layouts in leads module
		// TODO: VOOD-1506
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(4)").click();
		layoutsBtn = new VoodooControl("td", "id", "layoutsBtn");
		layoutsBtn.click();
		VoodooUtils.waitForReady();
		
		viewRecorView = new VoodooControl("td", "id", "viewBtnrecordview");
		viewRecorView.click();
		VoodooUtils.waitForReady();

		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		VoodooControl newRowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		newRowCtrl.dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);   

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Create new lead with test_int set to 1492 and Save.
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("firstName").set(customData.get("firstName"));
		sugar().leads.createDrawer.getEditField("lastName").set(customData.get("lastName"));	
		new VoodooControl("input", "css", "[name='test_int_c']").set(customData.get("custome_field_value"));

		// save record
		sugar().leads.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		sugar().alerts.getSuccess().closeAlert();

		// Create rows and columns report based on Leads module with no filters and display columns Name and test_int
		// TODO: VOOD-822
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#report_type_div table tbody tr:nth-child(2) td:nth-child(1) table tbody tr:nth-child(1) td").click();
		new VoodooControl("table", "id", "Leads").click();
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Leads_full_name").click();
		new VoodooControl("tr", "id", "Leads_test_int_c").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "save_report_as").set(customData.get("report_name"));
		new VoodooControl("input", "id", "saveAndRunButton").click();

		// assert test_int field is formatted based on user preferences in the report
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#report_results td.oddListRowS1.number_align").assertContains(customData.get("custom_field_group_value"), true);
		VoodooUtils.focusDefault();

		// Go to user profile and change 1000s and decimal separators
		userPref = testData.get(testName + "_1").get(0);
		sugar().users.setPrefs(userPref);

		// Running the report with updated integer format (decimal separators)
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		iconCtrl = new VoodooControl("i", "css", ".dropdown.active .fa.fa-caret-down");
		iconCtrl.click();
		VoodooControl customReportLinkCtrl = new VoodooControl("a", "css", "div.module-list li:nth-child(7) ul li:nth-child(6) a");
		customReportLinkCtrl.waitForVisible();
		customReportLinkCtrl.click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "runReportButton").click();

		// test_int field is formatted in the report based on updated user preferences
		new VoodooControl("td", "css", "#report_results td.oddListRowS1.number_align").assertContains(customData.get("custom_field_decimal_value"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}