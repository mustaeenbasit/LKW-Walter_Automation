package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20059 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Custom modules can be enabled for global search.
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20059_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		// create custom person module with enable import
		// TODO: VOOD-788
		VoodooControl modulebuilder = new VoodooControl("a", "css" ,"#moduleBuilder");
		modulebuilder.click();
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		// TODO: VOOD-933
		// create custom module 
		DataSource ds = testData.get(testName);
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(ds.get(0).get("package_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(ds.get(0).get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();		
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"table#new_module a").click();	
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(ds.get(0).get("package_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(ds.get(0).get("package_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(ds.get(0).get("package_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='importable']").click();
		new VoodooControl("table", "css" ,"tr#factory_modules table#type_person").click();		
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		// deploy custom module
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		// TODO: VOOD-1010
		VoodooUtils.pause(40000); //Pause needed because package deployment taking too much time;
		VoodooUtils.focusDefault();

		// navigate to custom module to create a record
		sugar().navbar.navToModule("cust_customer");
		VoodooControl createBtn = new VoodooControl("a", "css", "span.fld_create_button.list-headerpane > a");
		createBtn.click();
		VoodooUtils.waitForReady();
		VoodooControl lastName = new VoodooControl("input", "css", ".fld_last_name.edit [name='last_name']");
		String lastName1 = ds.get(0).get("last_name");
		lastName.set(lastName1);
		VoodooControl title = new VoodooControl("input", "css", ".fld_title.edit [name='title']");
		title.set(ds.get(0).get("title"));
		VoodooControl department = new VoodooControl("input", "css", ".fld_department.edit [name='department']");
		department.set(ds.get(0).get("department"));
		VoodooControl saveBtn = new VoodooControl("a", "css", ".create.fld_save_button a");
		saveBtn.click();
		VoodooUtils.waitForReady();

		// create second record
		createBtn.click();
		VoodooUtils.waitForReady();
		String lastName2 = ds.get(1).get("last_name");
		lastName.set(lastName2);
		title.set(ds.get(1).get("title"));
		department.set(ds.get(1).get("department"));
		saveBtn.click();
		VoodooUtils.waitForReady();

		// search out the already created record in custom module using global search
		sugar().navbar.setGlobalSearch(lastName1);
		VoodooControl searchResultCtrl = sugar().navbar.search.getControl("searchResults");
		VoodooUtils.waitForReady();
		searchResultCtrl.assertElementContains(lastName1, true);
		searchResultCtrl.assertElementContains(lastName2, false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}