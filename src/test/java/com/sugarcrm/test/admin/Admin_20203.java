package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20203 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Check "Rename Modules" UI on Admin page
	 * @throws Exception
	 */
	@Test
	public void Admin_20203_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1159
		// Verify Rename Modules label and its description
		new VoodooControl("a", "id", "rename_tabs").assertEquals(testData.get(testName).get(0).get("rename_module_lbl_txt"), true);
		new VoodooControl("td", "css", "div.dashletPanelMenu.wizard  table:nth-child(19) tr:nth-of-type(1) td:nth-of-type(4)").assertEquals(testData.get(testName).get(0).get("rename_module_description"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}