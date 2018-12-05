package com.sugarcrm.test.GlobalSearch;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17518 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * Show all modules that enabled for global search after click down arrow in global search box
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17518_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin -> Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("globalSearch").click();
		VoodooUtils.waitForReady();
		
		ArrayList<String> enabledModuleList= new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			// TODO: VOOD-1582 :getText() is needed here to get the list of all enabled module
			String enabledModule = new VoodooControl("div", "css", "#enabled_div .yui-dt-data tr:nth-child("+(i+1)+") td:nth-child(1)").getText();
			enabledModuleList.add(enabledModule);
		}
		
		// TODO: VOOD-1582 :getText() is needed here to get disabled module
		String disabledModule = new VoodooControl("div", "css", "#disabled_div .yui-dt-data tr:nth-child(1) td:nth-child(1)").getText();
		VoodooUtils.focusDefault();
		
		// Define Control
		VoodooControl searchModuleList = sugar().navbar.search.getControl("searchModuleList");

		// Click down arrow in global search box
		sugar().navbar.getControl("globalSearch").click();
		sugar().navbar.search.getControl("searchModuleDropdown").click();
		
		// Verify all modules that enabled for global search
		for (int j = 0; j < 26; j++) {
			// TR-10692 ('Tags' module is not showing in drop down list of global search)
			if (!enabledModuleList.get(j).equals(sugar().tags.moduleNamePlural))
				searchModuleList.assertContains(enabledModuleList.get(j), true);
		}
		
		// Verify that disabled module is not showing in search
		searchModuleList.assertContains(disabledModule, false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}