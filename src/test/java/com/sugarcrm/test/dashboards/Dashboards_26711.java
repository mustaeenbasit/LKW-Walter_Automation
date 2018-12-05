package com.sugarcrm.test.dashboards;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashboards_26711 extends SugarTest {
		
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * 26711 Verify that Help Dashboard could not be deleted
	 * @throws Exception
	 */
	@Test
	public void Dashboards_26711_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.leads.navToListView();
		
		//  Switch to Help Dashboard if needed
		
		// TODO VOOD-963
		VoodooControl dashboardTitle =  new VoodooControl("a", "css", "span.fld_name.detail a");
		
		// Wait for Dashboard title to appear
		sugar.dashboard.getControl("firstDashlet").waitForVisible();
		waitUntill(dashboardTitle.getText().isEmpty());
		
		// Select Help Dashboard unless it is already selected
		if(!dashboardTitle.getText().contains("Help Dashboard"))
			sugar.dashboard.chooseDashboard("Help Dashboard");
		
		// Delete is not available for Help Dashboard
		sugar.dashboard.openActionMenu();
		sugar.dashboard.getControl("delete").assertVisible(false);

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