package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28833 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that blank contract type is not created
	 *
	 * @throws Exception
	 */
	@Test
	public void Admin_28833_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("contractManagement").click();

		// TODO: VOOD-1592: Need lib support for Admin > Contract Types
		new VoodooControl("span", "css", "[data-module='ContractTypes'] .fa.fa-caret-down").click();
		VoodooControl contractTypeMenu = new VoodooControl("span", "css", "[data-module='ContractTypes'] .dropdown-menu ul li:nth-child(2)");
		contractTypeMenu.waitForVisible();
		contractTypeMenu.click();
		VoodooUtils.waitForReady();

		// Modify current URl ListView to DetailView
		String currentURL = VoodooUtils.getUrl();
		String modifiedURL = currentURL.replace(customFS.get("urlListView"), customFS.get("urlDetailView"));

		// Go to modified DetailView URL
		VoodooUtils.go(modifiedURL);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that Blank Detail view page should not be displayed for Contract type (i.e After modification in the URL, user is redirected to the list view of Contract type)
		new VoodooControl("form", "css", "[name='DetailView']").assertExists(false);
		new VoodooControl("div", "css", "#contentTable .listViewBody").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}