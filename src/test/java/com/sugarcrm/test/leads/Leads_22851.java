package com.sugarcrm.test.leads;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Leads_22851 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that when converting a lead to an account, custom fields are converted too
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_22851_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running" + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// TODO: VOOD-1506 - Support Studio Module RecordView Layouts View
		VoodooControl accountsStudioCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl leadsModuleCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		VoodooControl fieldsBtn = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "input[value='Add Field']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		// Go to Studio > Accounts > Fields and add a custom text field 'custom_textfield'
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		accountsStudioCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1504 - Support Studio Module Fields View
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set("custom_testfield");
		// save the field
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add field to Account's module record view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		accountsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();

		// Add one row
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel");
		VoodooControl rowCtrl = new VoodooControl("div", "css", "#toolbox .le_row.special");
		rowCtrl.dragNDrop(moveToLayoutPanelCtrl);

		// Add Custom text field to Record View
		String dataNameDraggableField = String.format("div[data-name=%s_c]", fs.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special"));
		VoodooControl publishButton = new VoodooControl("input", "id", "publishBtn");
		publishButton.click();
		VoodooUtils.waitForReady();

		// Add text field with the same name to 'Leads' module.
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		leadsModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtn.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set("custom_testfield");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Add field to Leads's module record view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		leadsModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();
		rowCtrl.dragNDrop(moveToLayoutPanelCtrl);
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special"));
		publishButton.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Create a new Lead, populate all required fields as well as 'custom_textfield'
		sugar().leads.navToListView();
		sugar().leads.listView.create();
		sugar().leads.createDrawer.getEditField("lastName").set(sugar().leads.getDefaultData().get("lastName"));
		VoodooControl customField = new VoodooControl("input", "css", "[name=custom_testfield_c]");
		// verify that custom field is visible on leads create drawer
		customField.assertVisible(true);
		customField.set(fs.get("name"));
		sugar().leads.createDrawer.save();
		sugar().leads.listView.clickRecord(1);

		// Convert Lead (recently created) and associate to Account.
		sugar().leads.recordView.openPrimaryButtonDropdown();
		// Convert a Lead.
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		VoodooControl convertCtrl = new VoodooControl("a", "css", ".fld_lead_convert_button.detail a");
		convertCtrl.click();
		// Associate Account
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(fs.get("accountName"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();
		// Save and Convert the Lead.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Observe the created account detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		// verify that custom field is available on Account's module record view
		new VoodooControl("span", "css", "[data-name=custom_testfield_c]").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}