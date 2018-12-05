package com.sugarcrm.test.dashboards;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashboards_26712 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * 26712 Verify that Help Dashboard can be activated/deactivated by selecting from the dashboard dropdown 
	 * @throws Exception
	 */

	@Ignore("Blocking due to VOOD-591 and 592")
	@Test
	public void Dashboards_26712_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();
		
		// There is "Notice that Help Dashboard is displayed in RHS panel by default" in expected results
		// At the moment Help Dashboard is not always displayed by default
		
		// TODO VOOD-963
		VoodooControl dashboardTitle = new VoodooControl("a", "css", "span.fld_name.detail a");	
		
		// Wait for Dashboard title to appear
		
		// Failing due to VOOD-591 and VOOD-592 - chooseDashboard() method in Dashboard.java
		// has different CSS for record view causing it to fail
		sugar.dashboard.getControl("dashboard").waitForVisible();
		waitUntill(dashboardTitle.getText().isEmpty());

		// Select Help Dashboard unless it is already selected
		if(!dashboardTitle.getText().contains("Help Dashboard"))
			sugar.dashboard.chooseDashboard("Help Dashboard");
		
		dashboardTitle.assertContains("Help Dashboard", true);
		
		// Select My Dashboard from dashboard dropdown
		sugar.dashboard.chooseDashboard("My Dashboard");
		dashboardTitle.assertContains("My Dashboard", true);
		
		// Select Help Dashboard again
		sugar.dashboard.chooseDashboard("Help Dashboard");
		dashboardTitle.assertContains("Help Dashboard", true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
	
	/**
	 * Wait for condition to became false
	 * @throws Exception
	 */
	void waitUntill(Boolean condition) throws Exception{
		int n = 0;
		while(condition){
			VoodooUtils.pause(500);
			if(n++ == 10) break;
		}
	}
}