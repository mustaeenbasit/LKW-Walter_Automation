package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ManufacturersModuleTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().manufacturers.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().manufacturers);

		// Verify menu items
		sugar().manufacturers.menu.getControl("createProduct").assertVisible(true);
		sugar().manufacturers.menu.getControl("viewProductCatalog").assertVisible(true);
		sugar().manufacturers.menu.getControl("viewManufacturers").assertVisible(true);
		sugar().manufacturers.menu.getControl("viewProductCategories").assertVisible(true);
		sugar().manufacturers.menu.getControl("viewProductTypes").assertVisible(true);
		sugar().manufacturers.menu.getControl("importManufacturers").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().manufacturers); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	@Test
	public void verifyModuleTitle() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

		sugar().manufacturers.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().manufacturers.listView.verifyModuleTitle(sugar().manufacturers.moduleNamePlural);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyModuleTitle() complete.");
	}

	public void cleanup() throws Exception {}
}