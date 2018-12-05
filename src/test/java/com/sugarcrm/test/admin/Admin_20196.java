package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20196 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Activities tab does not appear in enable/disable modules.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20196_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar().admin.navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.configureTabs.getControl("displayedModules").assertElementContains(ds.get(0).get("label"), false);
		sugar().admin.configureTabs.getControl("displayedSubpanels").assertElementContains(ds.get(0).get("label"), false);
		sugar().admin.configureTabs.getControl("hiddenModules").assertElementContains(ds.get(0).get("label"), false);
		sugar().admin.configureTabs.getControl("hiddenSubpanels").assertElementContains(ds.get(0).get("label"), false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}