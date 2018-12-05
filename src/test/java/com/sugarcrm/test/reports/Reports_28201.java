package com.sugarcrm.test.reports;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_28201 extends SugarTest {
	VoodooControl qliCtrl,studioCtrl;

	public void setup() throws Exception {
		// Creating Manufacturer and Product type record
		sugar().manufacturers.api.create();
		sugar().productTypes.api.create();
		sugar().login();

		// Enable Manufacturer Name and Product Type field
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		qliCtrl = new VoodooControl("a", "id", "studiolink_Products");
		qliCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooControl manufacturerNameCtrl = new VoodooControl("div", "css", ".le_field[data-name='manufacturer_name']");
		VoodooControl typeNameCtrl = new VoodooControl("div", "css", "[data-name='type_name']");
		manufacturerNameCtrl.scrollIntoViewIfNeeded(false);
		manufacturerNameCtrl.dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		typeNameCtrl.scrollIntoViewIfNeeded(false);
		typeNameCtrl.dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(2)"));
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Product Manufacturer should be Available in Report Filter / Field.
	 *
	 * @throws Exception
	 */
	@Test
	public void Reports_28201_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Enable QLI
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);

		// Create QLI with Manufacturer and Product Type created above
		FieldSet qliData = testData.get(testName).get(0);
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.create();
		sugar().quotedLineItems.createDrawer.getEditField("name").set(qliData.get("name"));

		// TODO: VOOD-1036
		new VoodooControl("span", "css", ".fld_manufacturer_name.edit .select2-arrow").click();
		new VoodooSelect("input","css", "#select2-drop div input").set(sugar().manufacturers.getDefaultData().get("name"));
		new VoodooControl("span", "css", ".fld_type_name.edit .select2-arrow").click();
		new VoodooSelect("input","css", "#select2-drop div input").set(sugar().productTypes.getDefaultData().get("name"));
		sugar().quotedLineItems.createDrawer.save();

		// Go to Reports -> Create reports -> Rows and Columns Report -> QLI
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-822
		new VoodooControl("td", "css", "img[name='rowsColsImg']").click();
		new VoodooControl("table", "css", "table[id='Quoted Line Items']").click();

		// Verify Manufacturers shows as a related module to QLI
		// TODO: VOOD-468
		new VoodooControl("a", "css", "#module_tree div:nth-of-type(10) td.ygtvcell.ygtvcontent a").assertContains(qliData.get("module1"), true);

		// Verify Product Types shows as a related module to Products QLI
		new VoodooControl("a", "css", "#module_tree div:nth-of-type(16) td.ygtvcell.ygtvcontent a").assertContains(qliData.get("module2"), true);

		// Click on cancel
		new VoodooControl("input", "id", "cancelBtn").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}