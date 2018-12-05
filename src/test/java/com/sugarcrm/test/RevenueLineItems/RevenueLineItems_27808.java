package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27808 extends SugarTest {
	FieldSet customData;
	VoodooControl rliModuleCtrl, manufacturerCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// New FiledSet with only required data for productCatalog api call
		FieldSet fs = new FieldSet();
		fs.putAll(customData);
		fs.remove("module_field_name");
		sugar().productCatalog.api.create(fs);
		sugar().login();

		// Create Manufacturer
		sugar().manufacturers.api.create();

		// link Manufacturer to product
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.clickRecord(1);
		sugar().productCatalog.recordView.edit();
		sugar().productCatalog.recordView.getEditField("manufacturerName").set(sugar().manufacturers.getDefaultData().get("name"));
		sugar().productCatalog.recordView.getEditField("unitPrice").set(customData.get("unitPrice"));
		sugar().productCatalog.recordView.save();
	}

	/**
	 * Verify that selecting Product auto-populated Manufacturer field on RLI create view
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27808_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		rliModuleCtrl = new VoodooControl("a", "id", "studiolink_RevenueLineItems");
		VoodooUtils.waitForReady();
		rliModuleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);
		sugar().admin.studio.clickStudio();

		// Record view
		rliModuleCtrl.click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",customData.get("module_field_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Creating RLI record
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("product").set(customData.get("name"));
		VoodooUtils.waitForReady();

		// Verify that manufacturer name is auto-populated from selected product
		new VoodooControl("div", "css", ".fld_manufacturer_name.edit .select2-chosen div").assertContains(sugar().manufacturers.getDefaultData().get("name"), true);

		// Cancel RLI creation
		sugar().revLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}