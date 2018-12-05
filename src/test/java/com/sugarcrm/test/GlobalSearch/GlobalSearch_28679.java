package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28679 extends SugarTest {
	VoodooControl accountsCtrl, saveBtn;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Enable/Disable functionality of modules in Global search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28679_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Admin > Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		
		// Using XPath so as to access Accounts w.r.t its position in list
		// TODO: VOOD-1860: Need Lib support to enable/disable modules for Global search
		accountsCtrl = new VoodooControl("div", "xpath", "//*[contains(@class,'yui-dt-data')]"
				+ "//div[.='Accounts']");
		saveBtn = new VoodooControl("input", "css", "[title='Save']");
		VoodooControl productCatalogueCtrl = new VoodooControl("div", "css", "#disabled_div "
				+ ".yui-dt-rec.yui-dt-first");
		
		// Drag and drop accounts to Disabled Search Modules
		accountsCtrl.dragNDrop(productCatalogueCtrl);
		saveBtn.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify no Accounts module appears on search dropdown
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		sugar().navbar.search.getControl("searchAccounts").assertVisible(false);
				
		// Searching for an existing account entity
		globalSearchCtrl.set(sugar().accounts.defaultData.get("name"));
		VoodooUtils.waitForReady();
		
		// Verify account related searches return no results
		sugar().navbar.search.getControl("searchResults").assertContains
			(customData.get("noResults"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}