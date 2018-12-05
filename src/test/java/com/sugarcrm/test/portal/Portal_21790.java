package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_21790 extends PortalTest {
	ContactRecord myContact;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {		
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		FieldSet portalSetupData = testData.get("env_portal_contact_setup").get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(portalSetupData);
		sugar().login();

		// Enable bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable portal in admin, portal contact set up
		sugar().admin.portalSetup.enablePortal();

		// TODO: VOOD-1108 - Provide a Portal Setup method
		// Link Contact with Account
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural).linkExistingRecord(myContact);
	}

	/**
	 * Add custom multiselect type field on portal layout.
	 * @throws Exception
	 */
	@Test
	public void Portal_21790_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1569 - Need Lib support for Administration page
		new VoodooControl("a", "id", "studiolink_Bugs").click();
		VoodooUtils.waitForReady(); 

		// TODO: VOOD-1504 - Support Studio Module Fields View
		// Create custom field in Bugs module.
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		new VoodooControl("option", "css", "select#type option[value='multienum']").click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("input", "id", "field_name_id").set(customData.get("field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(); 
		VoodooUtils.focusDefault();

		// Go to Admin -> Sugar Portal -> Layouts -> Bugs
		sugar().admin.navToAdminPanelLink("portalSettings");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1119 - Need Lib support for controls in Admin -> Sugar Portal
		new VoodooControl("td", "id", "Layouts").click();
		VoodooUtils.waitForReady(); 
		new VoodooControl("a", "css", "#Buttons table tbody tr td tr:nth-child(2) td a").click();
		VoodooUtils.waitForReady(); 

		// Add custom field to Portal Record view
		new VoodooControl("td", "id", "viewBtnRecordView").click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(); 

		// Layouts bread crumb link
		new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)").click();
		VoodooUtils.waitForReady();

		// Add custom field to Portal List view
		new VoodooControl("td", "id", "viewBtnListView").click();
		VoodooControl defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]", customData.get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();

		// Login to Sugar Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", myContact.get("portalName"));
		portalUser.put("password", myContact.get("password"));
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to Bugs
		portal().navbar.navToModule(portal().bugs.moduleNamePlural);
		portal().bugs.listView.create();

		// TODO: VOOD-1116 - Need Lib support for Bug and Case modules controls and functions in Portal
		// Verify the custom multiselect field is shown on edit view correctly.
		String newField = String.format("[data-name='%s']", customData.get("display_name"));
		new VoodooControl("div", "css", newField).assertExists(true);

		// Create a new bug with custom field filled.
		new VoodooSelect("span", "css", ".fld_" + customData.get("display_name") + ".edit").set(customData.get("input_value"));
		portal().bugs.recordView.getEditField("name").set(customData.get("bug_subject"));
		portal().bugs.portalCreateDrawer.save();

		// Verify custom field is shown in list view with the input data.
		String newFieldValue = String.format(".fld_" + customData.get("display_name") + " div");
		new VoodooControl("div", "css", newFieldValue).assertEquals(customData.get("input_value"), true);

		// TODO: VOOD-1096 - Portal Module Listview support
		new VoodooControl("a", "css", ".list-view table tbody td:nth-child(2) span div a").click();
		portal().alerts.waitForLoadingExpiration();

		// Verify custom field is shown in detail view with the input data.
		new VoodooControl("div", "css", newFieldValue).assertEquals(customData.get("input_value"), true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}