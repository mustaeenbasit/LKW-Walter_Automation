package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_23251 extends SugarTest {
	FieldSet moduleData = new FieldSet();

	public void setup() throws Exception {
		moduleData = testData.get(testName).get(0);
		sugar().login();

		// create package
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-933: Library support needed for controls on Admin Module Builder and Module Loader
		new VoodooControl("a", "id" ,"newPackageLink").click();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("package_name"));
		new VoodooControl("input", "css" ,"input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();	
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();

		// create module
		new VoodooControl("a", "id" ,"new_module").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"input[name='name']").set(moduleData.get("module_name"));
		new VoodooControl("input", "css" ,"input[name='label']").set(moduleData.get("plural_label"));
		new VoodooControl("input", "css" ,"input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "id" ,"type_basic").click();		
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();

		// Deploy the package
		new VoodooControl("input", "css" ,"input[name='deploybtn']").click();		
		new VoodooControl("img", "css" ,"div.bodywrapper > img").waitForInvisible(60000);
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify favorites feature is available in custom modules.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23251_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(moduleData.get("data_module"));

		// TODO: VOOD-939: Need lib support for newly created module
		VoodooControl createBtn = new VoodooControl("a", "css", ".btn.btn-primary[name='create_button']");
		VoodooControl nameInput = new VoodooControl("input", "css", "input[name=name]");
		VoodooControl saveBtn = new VoodooControl("a", "css", ".create.fld_save_button a");
		createBtn.click();
		nameInput.set(moduleData.get("module_record_name1"));
		saveBtn.click(); 
		sugar().alerts.waitForLoadingExpiration();

		createBtn.click();
		nameInput.set(moduleData.get("module_record_name2"));
		saveBtn.click(); 
		sugar().alerts.waitForLoadingExpiration();

		// Verify Favorite star icon
		VoodooControl fav1 = new VoodooControl("button", "css", ".layout_" + moduleData.get("data_module") + " tr.single .fld_my_favorite button");
		fav1.assertExists(true);
		VoodooControl fav2 = new VoodooControl("button", "css", ".layout_" + moduleData.get("data_module") + " tr.single:nth-of-type(2) .fld_my_favorite button");
		fav2.assertExists(true);

		// Mark as favorite
		fav1.click();
		VoodooUtils.waitForReady();
		fav1.assertAttribute("class", "active", true);

		// Verify record with My Favorites filter
		VoodooControl openFilterDropdown = new VoodooControl("a", "css", "span[data-voodoo-name='filter-filter-dropdown'] .select2-choice-type");
		openFilterDropdown.click();
		VoodooUtils.waitForReady();
		VoodooControl myFavoriteFilter = new VoodooControl("a", "css", ".search-filter-dropdown [data-id='favorites']");
		myFavoriteFilter.click();
		VoodooUtils.waitForReady();
		fav1.assertVisible(true);
		fav2.assertVisible(false);

		// Reset favorite
		openFilterDropdown.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".search-filter-dropdown [data-id='all_records']").click();
		VoodooUtils.waitForReady();
		fav1.click();

		// Verify all records without Favorite
		fav1.assertAttribute("class", "active", false);
		fav2.assertAttribute("class", "active", false);

		// Verify nil records with My Favorites filter
		openFilterDropdown.click();
		VoodooUtils.waitForReady();
		myFavoriteFilter.click();
		VoodooUtils.waitForReady();
		fav1.assertVisible(false);
		fav2.assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}