package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_19000 extends SugarTest {
	VoodooControl opportunityModuleCtrl,reportModuleCtrl;
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 *  Verify that 'relate' fields containing module name doesn't cause report to fail.
	 * @throws Exception
	 */
	@Test
	public void Reports_19000_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-938
		opportunityModuleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		
		// Go to Admin > Studio > fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		opportunityModuleCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		
		// Create a 'Relate' field and related to the 'Contacts' module.
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#ext2").set(customData.get("related_module"));
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		opportunityModuleCtrl.click();
		new VoodooControl("td", "id", "layoutsBtn").click();

		// Add custom field in Record View
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create a record in opportunity module by filling value in custom field.
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooSelect("a", "css", ".fld_myfield_c.edit a").set(sugar().contacts.getDefaultData().get("firstName"));
		sugar().opportunities.createDrawer.getEditField("rli_name").set(testName);
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));
		sugar().opportunities.createDrawer.save();
		
		// TODO: VOOD-822
		// Create Custom Report in Opportunities module
		VoodooControl createRowsColumnReportCtrl = new VoodooControl("td", "css", "#report_type_div tr:nth-child(2) td:nth-child(1) tr:nth-child(1) td:nth-child(1)");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		
		sugar().navbar.navToModule(sugar().reports.moduleNamePlural);
		sugar().navbar.clickModuleDropdown(sugar().reports);
		sugar().reports.menu.getControl("createReport").click();
		VoodooUtils.focusFrame("bwc-frame");
		createRowsColumnReportCtrl.click();
		new VoodooControl("table", "id", "Opportunities").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Opportunities_myfield_c").click();
		new VoodooControl("tr", "id", "Opportunities_name").click();
		nextBtnCtrl.click();
		reportNameCtrl.set(customData.get("report_name"));
		saveAndRunCtrl.click();
		VoodooUtils.waitForAlertExpiration();
		
		// Verify, Report is correctly generated.
		new VoodooControl("tr", "css", ".listViewBody tr:nth-child(3)").assertContains(sugar().contacts.getDefaultData().get("firstName"),true);
		new VoodooControl("tr", "css", ".listViewBody tr:nth-child(3)").assertContains(sugar().opportunities.getDefaultData().get("name"),true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}