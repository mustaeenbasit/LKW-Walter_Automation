package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_26504 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl qliCtrl;

	public void setup() throws Exception {
		customData =testData.get(testName).get(0);
		// Creating Product Category
		FieldSet fs1 = new FieldSet();
		fs1.put("name", customData.get("productCategory"));
		sugar().productCategories.api.create(fs1);
		fs1.clear();
		sugar().productCatalog.api.create(customData);
		sugar().login();

		// TODO: VOOD-542
		qliCtrl = new VoodooControl("a", "id", "studiolink_Products");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to QLI > Layout > Record View
		// TODO: VOOD-1506
		qliCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Drag and drop "CategoryName" & "type" field in the Layout
		// Adding New Row & New Filter in Record view layout of Qli
		// TODO: VOOD-1506
		VoodooControl insertNewRow = new VoodooControl("div", "css", "#toolbox .le_row");
		VoodooControl toPanel = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel");
		insertNewRow.dragNDrop(toPanel);
		VoodooControl categoryName = new VoodooControl("div", "css", "#toolbox div[data-name='category_name']");
		VoodooControl type = new VoodooControl("div", "css", "#toolbox div[data-name='type_name']");
		VoodooControl filler1 = toPanel.getChildElement("div", "css", ".le_row div:nth-child(1).le_field.special");
		VoodooControl filler2 = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel .le_row div:nth-child(2).le_field.special");
		categoryName.scrollIntoViewIfNeeded(false);
		categoryName.dragNDrop(filler1);
		type.scrollIntoViewIfNeeded(false);
		type.dragNDrop(filler2);
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Creating Product type
		fs1.put("name", customData.get("type"));
		sugar().productTypes.create(fs1);
		fs1.clear();

		// link product type and product category to Product Catalog
		sugar().productCatalog.navToListView();
		sugar().productCatalog.listView.clickRecord(1);
		sugar().productCatalog.recordView.edit();
		sugar().productCatalog.recordView.getEditField("productCategory").set(customData.get("productCategory"));
		sugar().productCatalog.recordView.getEditField("type").set(customData.get("type"));
		sugar().productCatalog.recordView.save();

		// Enabling QuotedLineItems Module through Enable & Disable Modules
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that Product Type is pulled in when selecting a Product Template when creating Quoted Line Item Record
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_26504_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to QuotedLineItems and opening Create Drawer View
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.create();
		sugar().quotedLineItems.createDrawer.getEditField("name").set(testName);

		// Asserting ProductType is auto-filled when ProductTemplate is chosen
		sugar().quotedLineItems.createDrawer.getEditField("productTemplate").set(customData.get("name"));
		// TODO:VOOD-1036
		new VoodooSelect("div", "css", ".fld_type_name.edit div").assertContains(customData.get("type"), true);
		sugar().quotedLineItems.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}