package com.sugarcrm.test.dashboards;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashboards_20052 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify the default dashlets are present on My Dashboard
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashboards_20052_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Need to pause otherwise the first assertVisible will fail
		VoodooUtils.pause(2000);

		// Assert Learning Resources dashlet
		new VoodooControl("div", "css", 
				".dashboard .dashlet-row .row-fluid:nth-of-type(1) .dashlet-header .dashlet-title")
				.assertVisible(true);

		// Assert Twitter dashlet
		new VoodooControl("div", "css", 
				".dashlet-row .row-fluid:nth-of-type(2) .dashlet-header .dashlet-title")
				.assertVisible(true);

		// Assert Contacts listview dashlet
		new VoodooControl("div", "css", 
				".dashboard .dashlet-row .row-fluid:nth-of-type(3) .dashlet-header .dashlet-title")
				.assertContains("My Contacts", true);

		// Assert Pipeline dashlet
		new VoodooControl("div", "css",
				".dashboard .layout_Home:nth-of-type(2) .dashlet-row .dashlet-title")
				.assertVisible(true);
		
		// Assert Top 10 Sales dashlet
		new VoodooControl("div", "css", 
				".dashboard li.layout_Home:nth-of-type(2) li.row-fluid:nth-of-type(2) .dashlet-title")
				.assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}