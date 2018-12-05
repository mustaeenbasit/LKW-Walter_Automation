package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProductCategoriesModuleTest extends SugarTest {
	public void setup() throws Exception {
		sugar().productCategories.api.create();
		sugar().login();
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		FieldSet fs = new FieldSet();
		fs.put("name", "Demo Widgets");
		// create one more product
		sugar().productCategories.api.create(fs);

		sugar().productCategories.navToListView();
		sugar().productCategories.listView.editRecord(2);
		sugar().productCategories.listView.getEditField(2, "name").set("Test");
		sugar().productCategories.listView.getEditField(2, "parentCategory").set(fs.get("name"));
		sugar().productCategories.listView.getEditField(2, "order").set("4");

		sugar().productCategories.listView.saveRecord(2);

		sugar().productCategories.listView.getDetailField(2, "name").assertEquals("Test", true);
		sugar().productCategories.listView.getDetailField(2, "order").assertEquals("4", true);
		sugar().productCategories.listView.getDetailField(2, "parentCategory").assertEquals(fs.get("name"), true);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		FieldSet fs = new FieldSet();
		fs.put("name", "Parrett Widgets");
		sugar().productCategories.api.create(fs);

		sugar().productCategories.navToListView();
		sugar().productCategories.listView.clickRecord(1);

		sugar().productCategories.recordView.edit();
		sugar().productCategories.recordView.getEditField("parentCategory").set(sugar().productCategories.getDefaultData().get("name"));
		sugar().productCategories.recordView.getEditField("order").set("3");
		sugar().productCategories.recordView.getEditField("description").set("changed description");
		sugar().productCategories.recordView.save();

		sugar().productCategories.recordView.getDetailField("name").assertEquals("Parrett Widgets", true);
		sugar().productCategories.recordView.getDetailField("parentCategory").assertContains(sugar().productCategories.getDefaultData().get("name"), true);
		sugar().productCategories.recordView.getDetailField("order").assertEquals("3", true);
		sugar().productCategories.recordView.getDetailField("description").assertEquals("changed description", true);
		sugar().productCategories.recordView.getDetailField("date_entered_date").assertVisible(true);
		sugar().productCategories.recordView.getDetailField("date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		sugar().productCategories.navToListView();
		// preview record
		sugar().productCategories.listView.previewRecord(1);

		sugar().previewPane.getPreviewPaneField("name").assertEquals(sugar().productCategories.getDefaultData().get("name"), true);
		sugar().previewPane.getPreviewPaneField("order").assertEquals(sugar().productCategories.getDefaultData().get("order"), true);
		sugar().previewPane.getPreviewPaneField("description").assertEquals(sugar().productCategories.getDefaultData().get("description"), true);
		sugar().previewPane.getPreviewPaneField("date_entered_date").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("date_modified_date").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyListHeadersWithSortIcon() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeadersWithSortIcon()...");

		sugar().productCategories.navToListView();
		// Verify all sort headers in listview
		for(String header : sugar().productCategories.listView.getHeaders()){
			sugar().productCategories.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertVisible(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeadersWithSortIcon() test complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().productCategories.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().productCategories);
		sugar().productCategories.menu.getControl("createProductCatalog").assertVisible(true);
		sugar().productCategories.menu.getControl("viewProductCatalog").assertVisible(true);
		sugar().productCategories.menu.getControl("viewManufacturers").assertVisible(true);
		sugar().productCategories.menu.getControl("viewProductCategories").assertVisible(true);
		sugar().productCategories.menu.getControl("viewProductTypes").assertVisible(true);
		sugar().productCategories.menu.getControl("importProductCategories").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().productCategories); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	public void cleanup() throws Exception {}
}