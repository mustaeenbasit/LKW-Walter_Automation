package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProductTypesModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().productTypes.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().productTypes);
		// Verify menu items
		sugar().productTypes.menu.getControl("createProductType").assertVisible(true);
		sugar().productTypes.menu.getControl("viewProductTypes").assertVisible(true);
		sugar().productTypes.menu.getControl("createProduct").assertVisible(true);
		sugar().productTypes.menu.getControl("viewProductCatalog").assertVisible(true);
		sugar().productTypes.menu.getControl("viewManufacturers").assertVisible(true);
		sugar().productTypes.menu.getControl("viewProductCategories").assertVisible(true);
		sugar().productTypes.menu.getControl("importProductTypes").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().productTypes); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements()...");

		sugar().productTypes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().productTypes.listView.getControl("createButton").assertVisible(true);
		sugar().productTypes.listView.getControl("startButton").assertVisible(true);
		sugar().productTypes.listView.getControl("endButton").assertVisible(true);
		sugar().productTypes.listView.getControl("prevButton").assertVisible(true);
		sugar().productTypes.listView.getControl("nextButton").assertVisible(true);
		sugar().productTypes.listView.getControl("pageNumbers").assertVisible(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		sugar().productTypes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().productTypes.listView.verifyModuleTitle("Product Types");
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyModuleTitle() complete.");
	}

	@Test
	public void verifyListviewField() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListviewField()...");

		sugar().productTypes.api.create();
		sugar().productTypes.navToListView();
		sugar().productTypes.listView.verifyField(1, "name", sugar().productTypes.defaultData.get("name"));
		sugar().productTypes.listView.verifyField(1, "description", sugar().productTypes.defaultData.get("description"));
		sugar().productTypes.listView.verifyField(1, "order", sugar().productTypes.defaultData.get("order"));

		VoodooUtils.voodoo.log.info("verifyListviewField() test complete");
	}

	@Test
	public void countRowsTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running countRows()...");

		sugar().productTypes.api.create();
		sugar().productTypes.navToListView();
		// Verify 1 record count
		int row = sugar().productTypes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal one.", row == 1);

		// Verify 3 record count
		DataSource ds = new DataSource();
		FieldSet name1 = new FieldSet();
		name1.put("name", "Hardware");
		FieldSet name2 = new FieldSet();
		name2.put("name", "Software");
		ds.add(name1);
		ds.add(name2);
		sugar().productTypes.api.create(ds);
		VoodooUtils.refresh(); // to populate data

		row = sugar().productTypes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal three.", row == 3);
		VoodooUtils.focusDefault();

		// Verify 2 records after deleting 1 record
		sugar().productTypes.listView.deleteRecord(1);
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		row = sugar().productTypes.listView.countRows();
		Assert.assertTrue("Number of rows did not equal two.", row == 2);
		// Verify no records after deleting all records
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("countRows() test complete");
	}

	@Test
	public void verifyEditFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().productTypes.api.create();
		FieldSet defaultData = sugar().productTypes.getDefaultData();

		sugar().productTypes.navToListView();
		sugar().productTypes.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().productTypes.editView.getEditField("name").assertEquals(defaultData.get("name"), true);
		sugar().productTypes.editView.getEditField("description").assertEquals(defaultData.get("description"), true);
		sugar().productTypes.editView.getEditField("order").assertEquals(defaultData.get("order"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void saveAndCreateNewTest() throws Exception {
		VoodooUtils.voodoo.log.info("Running saveAndCreateNewTest()...");

		sugar().productTypes.navToListView();
		sugar().productTypes.listView.clickCreate();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().productTypes.editView.getEditField("name").set("Test");
		sugar().productTypes.editView.getEditField("description").set("test data");
		sugar().productTypes.editView.getEditField("order").set("5");
		VoodooUtils.focusDefault();
		sugar().productTypes.editView.saveAndCreateNew();
		sugar().productTypes.listView.verifyField(1,"name","Test");
		sugar().productTypes.listView.verifyField(1,"description","test data");
		sugar().productTypes.listView.verifyField(1,"order","5");

		VoodooUtils.voodoo.log.info("saveAndCreateNewTest() complete.");
	}

	public void cleanup() throws Exception {}
}