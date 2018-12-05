package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_28858 extends SugarTest {
	public void setup() throws Exception {
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify module dropdown is alphabetically ordered when selecting module for List View Dashlet [Only for English]
	 * @throws Exception
	 */
	@Test
	public void Dashlets_28858_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		DataSource customData = testData.get(testName);

		// Add a 'List View' Dashlet on My Dashboard
		sugar().home.dashboard.edit();

		// Clicking 'Add row' on Second column
		// TODO: VOOD-960 - Dashlet selection 
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("a", "css", ".dashlets li:nth-child(2) [data-value='1']").click();
		sugar().home.dashboard.addDashlet(3,1);

		// Select "list view" from the list
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooControl("input", "css", ".layout_Home input[placeholder='Search by Title, Description...']").set(customData.get(0).get("listView"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		//  Clicking on Modules drop down
		// TODO: VOOD-1004 - Library support need to create dashlet
		new VoodooSelect("div", "css", ".edit.fld_module div").click();

		// Verifying that module names are displayed ordered alphabetically
		for (int i = 1; i <= customData.size(); i++) {
			// TODO: VOOD-1004 - Library support need to create dashlet
			new VoodooControl("div", "css", ".select2-drop-active li:nth-child(" + i + ") div").assertEquals(customData.get(i - 1).get("moduleName"), true);
		}

		// Cancel the Dashlet and Dashboard edit mode
		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("div", "css", ".select2-result-label").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_cancel_button a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}