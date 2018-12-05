package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_26152 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the Module selection list is updated when change FTS configure 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_26152_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl adminSearch = sugar().admin.adminTools.getControl("globalSearch");
		adminSearch.click();
		VoodooUtils.waitForReady();

		// Move product catalog from Disabled to Enabled section
		// Used xpath to avoid using location identifier, so that script will not fail even if the location of element is changed in future
		// TODO: VOOD-1860
		VoodooControl productCatalog = new VoodooControl("div", "xpath", "//*[contains(@class,'yui-dt yui-dt-scrollable')]//div[.='Product Catalog']");
		VoodooControl enabledSection = new VoodooControl("tr", "css", "#enabled_div tbody.yui-dt-data .yui-dt-rec.yui-dt-first");
		productCatalog.dragNDrop(enabledSection);
		VoodooControl save = sugar().admin.configureTabs.getControl("save");
		save.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigating to Search module list of Global Search 
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooControl globalSearchDropDown = sugar().navbar.search.getControl("searchModuleDropdown");
		globalSearchDropDown.click();
		VoodooControl searchModuleList = sugar().navbar.search.getControl("searchModuleList");
		FieldSet fs = testData.get(testName).get(0);

		// Verify the Product Catalog is visible under Global Search Module List
		searchModuleList.assertContains(fs.get("globalSearchModuleList"), true);

		// Go to Admin -> search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		adminSearch.click();
		VoodooUtils.waitForReady();

		// Move product catalog from Enabled to Disabled section
		VoodooControl disabledSection = new VoodooControl("tr", "css", "#disabled_div tbody.yui-dt-data .yui-dt-rec.yui-dt-first");
		productCatalog.dragNDrop(disabledSection);
		save.click();		
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigating to Search module list of Global Search 
		globalSearchCtrl.click();
		globalSearchDropDown.click();

		// Verify the Product Catalog is not visible under Global Search Module List
		searchModuleList.assertContains(fs.get("globalSearchModuleList"), false);
		globalSearchDropDown.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}