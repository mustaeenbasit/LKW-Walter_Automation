package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_21184 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify dashlet to show a list view of any module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_21184_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource customData = testData.get(testName);
				
		// Go to Homepage > edit (under create button)
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);
		sugar.home.dashboard.edit();

		// Add a Dashlet
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(4, 1);
		
		// Define Controls for Dashlets
		// TODO: VOOD-960
		VoodooControl dashletSearchCtrl = new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input");
		VoodooControl dashletSelectCtrl = new VoodooControl("a", "css", ".list-view .fld_title a");
		VoodooControl moduleDropDown = new VoodooSelect("div", "css", ".edit.fld_module div.select2");	
		VoodooControl selectedModule = new VoodooControl("span", "css", ".edit.fld_module span.select2-chosen");
		
		// Add a Dashlet -> Select "List View"
		dashletSearchCtrl.set(customData.get(0).get("listview"));
		VoodooUtils.waitForReady();
		dashletSelectCtrl.click();
		VoodooUtils.waitForReady();
			
		// Click Module field drop down and verify that user is able to select any module from the Module drop down list
		for (int i = 0; i < customData.size(); i++) {
			moduleDropDown.set(customData.get(i).get("module"));
			VoodooUtils.waitForReady();
			selectedModule.assertContains(customData.get(i).get("module"), true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}