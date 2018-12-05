package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_30850 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Required field validation is appearing while creating RSS dashlet in dashboard
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_30850_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960
		// Go to Home -> My Dashboard -> Edit
		sugar().navbar.navToModule(sugar().home.moduleNamePlural);
		sugar().home.dashboard.clickCreate();

		// Add a Dashlet
		sugar().home.dashboard.addRow();
		sugar().home.dashboard.addDashlet(1, 1);
		FieldSet customFS = testData.get(testName).get(0);

		// Select RSS feed from sugar dashlet "select and search" drawer.
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("dashletName"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();

		// Save Dashlet
		new VoodooControl("a", "css", ".layout_Home.drawer.active .fld_save_button a").click();

		// Verify that the Required field validation is appearing
		new VoodooControl("span", "css", ".edit.fld_feed_url").assertAttribute("class", "error");
		new VoodooControl("i", "css", ".error-tooltip.add-on .fa.fa-exclamation-circle").hover();
		new VoodooControl("span", "css", ".error-tooltip.add-on[data-original-title='Error. This field is required.']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}