package com.sugarcrm.test.dashboards;

import java.lang.reflect.Field;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

public class Dashboards_26653 extends SugarTest {
	DataSource dashboardData;
	
	public void setup() throws Exception {
		sugar.login();

		// some modules are skipped in csv because of the custom behavior
		dashboardData = testData.get("Dashboards_26653");
		
		for(int i=0; i<dashboardData.size(); i++)
			getModule(dashboardData.get(i).get("module_name")).api.create();
		
	}

	/**
	 * 26653 Verify that content of help dashlet in help Dashboard is relevant to the module and view  
	 * @throws Exception
	 */
	@Ignore("Blocking due to VOOD-591 and 592")
	@Test
	public void Dashboards_26653_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String module_name, help_text;
		
		for(int i=0; i<dashboardData.size(); i++){
			module_name = dashboardData.get(i).get("module_name");
			help_text = dashboardData.get(i).get("help_text");
			
			// TODO VOOD-963
			VoodooControl dashboardTitle =  new VoodooControl("a", "css", "span.fld_name.detail a");			
			
			// verify Help Dashboard on the list view
			getModule(module_name).navToListView();
			
			// Wait for Dashboard title to appear
			sugar.dashboard.getControl("dashboard").waitForVisible();
			waitUntill(dashboardTitle.getText().isEmpty());
			
			// Select Help Dashboard unless it is already selected
			if(!dashboardTitle.getText().contains("Help Dashboard"))
				sugar.dashboard.chooseDashboard("Help Dashboard");
			
			sugar.dashboard.getControl("firstDashlet").assertElementContains(help_text, true);
			
			// verify Help Dashboard on the record view
			getModule(module_name).listView.clickRecord(1);
			VoodooUtils.waitForAlertExpiration();
			
			// Wait for Dashboard title to appear

			// Failing due to VOOD-591 and VOOD-592 - chooseDashboard() method in Dashboard.java
			// has different CSS for record view causing it to fail
			sugar.dashboard.getControl("dashboard").waitForVisible();
			waitUntill(dashboardTitle.getText().isEmpty());
			
			// Select Help Dashboard unless it is already selected
			if(!dashboardTitle.getText().contains("Help Dashboard"))
				sugar.dashboard.chooseDashboard("Help Dashboard");
			
			sugar.dashboard.getControl("firstDashlet").assertElementContains(help_text, true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
	
	/**
	 * Get a StandardModule by its name, e.g. getModule("leads") returns the same as sugar.leads
	 * @param moduleName - module name 
	 * @return StandardModule object
	 */
	StandardModule getModule(String moduleName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field f = sugar.getClass().getDeclaredField(moduleName);
		StandardModule m = (StandardModule)f.get(sugar);
		
		return m;
	}
	
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