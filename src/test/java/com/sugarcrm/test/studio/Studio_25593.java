package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25593 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that multi select type of custom field is displayed in reports list view.
	 * @throws Exception
	 */
	@Test
	public void Studio_25593_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Studio -> Contacts module
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1504
		VoodooControl contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsSubPanelCtrl.click();

		// Add custom field in Contacts module.
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1507
		// Layout
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Add custom field to List view
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",customData.get("module_field_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady(30000);
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506
		// Add custom field to Record view
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Reports
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");

		// TODO: VOOD-822
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
	 
		// Create Custom Report in Contacts module, select the created custom field
		VoodooUtils.focusFrame("bwc-frame");		
		new VoodooControl("img", "css", "img[name='summationWithDetailsImg']").click();
		new VoodooControl("table", "id", "Contacts").click();
		nextBtnCtrl.click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Contacts_count").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "Contacts_"+ customData.get("module_field_name") +"").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton").click();
		new VoodooControl("input", "id", "save_report_as").set(testName);
		new VoodooControl("input", "css", "#report_details_div table tbody tr td #saveAndRunButton").click();
		VoodooUtils.waitForReady();

		// Verify multi-select custom field is displayed in Report detail view.
		new VoodooControl("td", "css", "#reportDetailsTable tbody tr:nth-child(3) td").assertContains(customData.get("module_field_name").replaceAll("_", " "), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}