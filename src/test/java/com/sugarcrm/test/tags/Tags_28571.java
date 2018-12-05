package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
public class Tags_28571 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify the Tags Module dropdown in the Mega Menu 
	 * @throws Exception
	 */
	@Test
	public void Tags_28571_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName).get(0);

		// Navigate to tags module and click tags mega menu
		sugar.navbar.navToModule(sugar.tags.moduleNamePlural);
		sugar.navbar.clickModuleDropdown(sugar.tags);

		// Assert the mega menu items for tags module
		sugar.tags.menu.getControl("createTag").assertEquals(fs.get("menuItem1"), true);
		sugar.tags.menu.getControl("viewTags").assertEquals(fs.get("menuItem2"), true);
		sugar.tags.menu.getControl("importTags").assertEquals(fs.get("menuItem3"), true);

		// Closing mega menu dropdown
		sugar.navbar.clickModuleDropdown(sugar.tags);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}