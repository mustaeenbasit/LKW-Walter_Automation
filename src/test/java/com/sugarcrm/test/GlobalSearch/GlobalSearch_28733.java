package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28733 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify keyboard shortcuts for Global Search
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28733_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Global search something and hit Enter.
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();

		// TODO: VOOD-1882
		// Verify Search bar is extended horizontally
		VoodooControl expandCollapsCtrl = new VoodooControl("div", "css", ".navbar .search");
		expandCollapsCtrl.assertAttribute("class", "expanded");

		// Enter any value in the Search bar (which exists in the modules record) for eg: qauser 
		globalSearchCtrl.append(sugar().users.qaUser.get("userName"));

		// Verify that the quick search dropdown appears
		new VoodooControl("h3", "css", "ul.search-results li a h3").assertContains(sugar().users.qaUser.get("userName"), true);

		// TODO: CB-252,VOOD-1437
		// Use Esc or Ctrl + alt + I (For mac use command instead of control Ctrl).
		globalSearchCtrl.append("" + '\uE00C'); // Esc
		VoodooUtils.waitForReady();

		// Verify that the Global search bar is collapsed and Quick search dropdown should be closed (i.e Exit the Global Search field).
		expandCollapsCtrl.assertAttribute("class", "expanded", false);
		sugar().globalSearch.getRow(1).assertExists(false);

		// TODO: CB-252,VOOD-1437
		// Use s or Ctrl + alt + 0.
		globalSearchCtrl.append("" + '\u0073');

		// Verify that the Focus should be set on the Global search bar (i.e Access the Global Search field).
		expandCollapsCtrl.assertAttribute("class", "expanded");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}