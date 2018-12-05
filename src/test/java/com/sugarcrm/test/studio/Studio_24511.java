package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24511 extends SugarTest {
	FieldSet customData = new FieldSet();
	String lastName = "";

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().login();

		// TODO: VOOD-542 - need lib support for studio
		// Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl contactsModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactsModuleCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504 - Support Studio Module Fields View
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// layout -> Record view
		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsModuleCtrl.click();
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel .le_row"); 
		VoodooControl moveToNewFiller =	new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFiller);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);

		// List view
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='" + customData.get("module_field_name") + "_c").dragNDrop(moveHere);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Contacts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		lastName = sugar().contacts.recordView.getEditField("lastName").getText();
		sugar().contacts.recordView.save();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		// Verifying newly created field 
		new VoodooControl("div", "css" ,".fld_"+customData.get("module_field_name")+"_c.detail div").assertEquals(lastName, true);
	}

	/**
	 * Show calculated fields on dashlet 
	 * @throws Exception
	 */
	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Dashboard
		new VoodooControl("button", "css" ,".logo").click(); // Need to execute before below code line
		sugar().home.dashboard.clickCreate();
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);

		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(customData.get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Adding Quotes module
		new VoodooSelect("div", "css", ".edit.fld_module div").set(sugar().contacts.moduleNamePlural);
		VoodooUtils.waitForReady();

		// Save
		new VoodooControl("a", "css", "#drawers .fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verifying newly created field on dashlet
		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		new VoodooControl("div", "css", ".fld_"+customData.get("module_field_name")+"_c.list div").assertEquals(lastName, true);
		sugar().home.dashboard.cancel();
		sugar().alerts.confirmAllAlerts();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}