package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21637 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Display dashlet action icons by default
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21637_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource dashletData = testData.get(testName);
		int newDashletStartRow = Integer.parseInt(dashletData.get(0).get("newDashletStartRow"));

		// Edit My dashboard in home page
		sugar().home.dashboard.edit();

		// Adding chart and web dashlets (Forecast Bar chart, Forecast Pipeline chart, Saved Report chart dashlet, Web Page dashlet)
		// TODO: 1004 - Library support need to create dashlet
		for (int i = 0, j = newDashletStartRow; i < dashletData.size(); i++, j++) {
			sugar().home.dashboard.addRow();
			sugar().home.dashboard.addDashlet(j, 1);
			new VoodooControl("input", "css", ".inline-drawer-header input").set(dashletData.get(i).get("dashletName"));
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".fld_title a").click();
			VoodooUtils.waitForReady();
			new VoodooControl("a", "css", ".active .fld_save_button a").click();
			VoodooUtils.waitForReady();
		}

		// Save the home page dashboard
		// TODO: VOOD-1645
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verifying Refesh, Edit and Remove actions are appearing in all created dashlets.
		// TODO: VOOD-670 - More Dashlet Support
		for (int i = 0; i < dashletData.size(); i++, newDashletStartRow++) {
			// Click on gear icon
			new VoodooControl("i", "css", ".dashlets li .row-fluid.sortable:nth-of-type(" + newDashletStartRow + ") [data-action='loading']").click();
			VoodooUtils.waitForReady();
			new VoodooControl("i", "css", ".dashlets li .row-fluid.sortable:nth-of-type(" + newDashletStartRow + ") [data-dashletaction='editClicked']").assertEquals(dashletData.get(0).get("dashletActions"), true);
			new VoodooControl("i", "css", ".dashlets li .row-fluid.sortable:nth-of-type(" + newDashletStartRow + ") [data-dashletaction='refreshClicked']").assertEquals(dashletData.get(1).get("dashletActions"), true);
			new VoodooControl("i", "css", ".dashlets li .row-fluid.sortable:nth-of-type(" + newDashletStartRow + ") [data-dashletaction='removeClicked']").assertEquals(dashletData.get(2).get("dashletActions"), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}