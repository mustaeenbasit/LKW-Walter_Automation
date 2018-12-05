package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24647 extends SugarTest {
	VoodooControl layoutSubPanelCtrl, recordViewSubPanelCtrl, fieldsBtn,saveBtnCtrl, addFieldCtrl, fieldNameCtrl, fieldSaveBtnCtrl, publishBtnCtrl;
	VoodooControl moveToLayoutPanelCtrl, moveFromLayoutPanelCtrl, defaultSubPanelCtrl, moveToNewFilter, layoutSubBreadCrumbCtrl, listViewLayoutCtrl;
	FieldSet moduleData = new FieldSet(); 
	DataSource fieldsData = new DataSource();

	public void setup() throws Exception {
		moduleData = testData.get(testName).get(0);
		fieldsData = testData.get(testName +"_Fields");

		// Login as a valid user
		sugar().login();

		// create package
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-933 - Library support needed for controls on Admin Module Builder and Module Loader
		new VoodooControl("a", "id" ,"newPackageLink").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForAlertExpiration();

		// create custom module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(moduleData.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "id" ,"type_basic").click();	
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForAlertExpiration();

		VoodooControl breadCrumbCtrl = new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)");
		breadCrumbCtrl.click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("input", "css" ,"input[name='deploybtn']").click();

		// TODO: VOOD-1010 - Module deploy needs too much time to finish
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusDefault();

		// Create a new field
		// TODO: VOOD-542 - need lib support for studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1504 - Support Studio Module Fields View
		VoodooControl customModule = new VoodooControl("a", "id", "studiolink_test1_Test");
		customModule.click();
		VoodooUtils.waitForReady();
		fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		addFieldCtrl = new VoodooControl("input", "css", "input[value='Add Field']");
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		fieldNameCtrl.set(fieldsData.get(0).get("module_field_name"));
		fieldSaveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		fieldSaveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Add custom fields to List view
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		customModule.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		listViewLayoutCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		listViewLayoutCtrl.click();
		VoodooUtils.waitForReady();
		defaultSubPanelCtrl = new VoodooControl("td", "id", "Default");
		defaultSubPanelCtrl.waitForVisible();
		String dataNameDraggableLi = String.format("li[data-name=%s]",fieldsData.get(0).get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooUtils.waitForReady();
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Layouts breadcrumb link
		layoutSubBreadCrumbCtrl = new VoodooControl("a", "css", "div.bodywrapper div a:nth-of-type(5)");
		layoutSubBreadCrumbCtrl.waitForVisible();
		layoutSubBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();

		// Add custom fields to Record view
		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		moveFromLayoutPanelCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		moveFromLayoutPanelCtrl.dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",fieldsData.get(0).get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		VoodooUtils.waitForReady();
		publishBtnCtrl = new VoodooControl("input", "id", "publishBtn");
		publishBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		breadCrumbCtrl.click();

		// Create one to one relationship between the custom module and contacts module
		// TODO: VOOD-1505 - Support Studio Module Relationships View
		new VoodooControl("td", "id", "relationshipsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='addrelbtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#relationship_type_field option[value='one-to-one']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "select#rhs_mod_field option[value='Contacts']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name=saverelbtn]").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Using Related Field tab to create formula
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24647_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Studio -> Contacts
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-542 - need lib support for studio
		VoodooControl contactModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		contactModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn.click();
		VoodooUtils.waitForReady();

		// Create a custom field in contacts module
		// TODO: VOOD-1504 - Support Studio Module Fields View
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(fieldsData.get(1).get("module_field_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("a", "css", ".rel_field.button a").click();
		new VoodooControl("option", "css", "select#selrf_rmodule option[value='test1_test_contacts_1']").click();
		new VoodooControl("option", "css", "select#selrf_rfield option[value='"+fieldsData.get(0).get("display_name")+"']").click();
		new VoodooControl("button", "css", "[name='selrf_insertbtn']").click();
		VoodooUtils.waitForReady();

		// Verify the related field is added in the formula builder window
		// TODO: VOOD-1504 - Support Studio Module Fields View
		new VoodooControl("textarea", "id", "formulaInput").assertAttribute("value", fieldsData.get(0).get("expected_formula"), true);
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		// Verify the formula is saved correctly
		new VoodooControl("input", "id", "formula").assertContains(fieldsData.get(0).get("expected_formula"), true);

		// Save custom field
		fieldSaveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Navigate to studio
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactModuleCtrl.click();
		VoodooUtils.waitForReady();

		// Add the created custom field into layout
		// TODO: VOOD-1507 - Support Studio Module ListView Layouts View
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		// Add custom fields to List view
		listViewLayoutCtrl.click();
		VoodooUtils.waitForReady();
		String dataNameDraggableLi = String.format("li[data-name=%s]",fieldsData.get(1).get("display_name")); 
		new VoodooControl("li", "css", dataNameDraggableLi).dragNDrop(defaultSubPanelCtrl);
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		layoutSubBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();

		// Add custom fields to Record view
		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		moveFromLayoutPanelCtrl.dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",fieldsData.get(1).get("display_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		publishBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Create a record in custom module, filled the custom field 
		sugar().navbar.navToModule(moduleData.get("data_module"));
		// TODO: VOOD-939 - Need lib support for newly created module
		new VoodooControl("a", "css", ".btn.btn-primary[name='create_button']").click();
		VoodooUtils.waitForReady();
		// Create record in custom module
		new VoodooControl("input", "css", "[name='name']").set(fieldsData.get(2).get("record_name"));
		new VoodooControl("input", "css", "[aria-label='"+fieldsData.get(0).get("module_field_name")+"']").set(fieldsData.get(2).get("myfield"));
		new VoodooControl("a", "css", ".layout_test1_Test .create.fld_save_button .btn.btn-primary[name='save_button']").click();
		VoodooUtils.waitForReady();

		// Create or edit a contact record
		sugar().contacts.api.create();
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();

		new VoodooSelect("span", "css", "[data-voodoo-name='test1_test_contacts_1_name'] span").set(fieldsData.get(2).get("record_name"));
		sugar().contacts.recordView.save();

		// Verify the data in calculated field show on detail view
		// TODO: VOOD-939 - Need lib support for newly created module
		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		VoodooControl customFieldCtrl = new VoodooControl("div", "css", ".fld_newfield_c div");
		customFieldCtrl.assertEquals(fieldsData.get(2).get("myfield"), true);

		// Verify the data in calculated field show on list view
		sugar().contacts.navToListView();
		customFieldCtrl.assertEquals(fieldsData.get(2).get("myfield"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}