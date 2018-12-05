package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21569 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify home page displays dashlet with new web page embedded within it.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21569_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletWebPageUrl = new VoodooControl("input", "css", ".edit.fld_url input");
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl saveBtnCtrl = new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a");

		// Go to Home -> My Dashboard -> Edit
		FieldSet customFS = testData.get(testName).get(0);
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);

		// Add a Dashlet -> Select " Web Page" tab in toggle drawer
		dashletSearchCtrl.set(customFS.get("selectWebPageDashlet"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady(); // Extra wait needed 

		// Enter in constantcontact.com in the web line URL
		dashletWebPageUrl.set(customFS.get("webPageUrl"));

		// Save Dashlet
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Save the home page Dashboard
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify that Home page should display dashlet with new web page embedded within it.
		new VoodooControl("iframe", "css", "[data-voodoo-name='webpage']").assertExists(true);
		new VoodooControl("iframe", "css", ".webpage.fld_url iframe").assertAttribute("src", customFS.get("webPageUrl"), true);

		// Verify that entire home page is not redirected to URL in dashlet.
		sugar.home.dashboard.getControl("dashboardTitle").assertContains(customFS.get("dashboardTitle"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}