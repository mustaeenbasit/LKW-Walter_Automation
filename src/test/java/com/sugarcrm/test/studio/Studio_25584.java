package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25584 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that the module in "Display" column is dragged and dropped to the "Hide Tabs" column.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25584_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Drag and drop a module record to the "Hidden Tabs" column.
		sugar().admin.disableModuleDisplay(sugar().accounts);
		
		// Go to Admin > Display Modules and subpanels
		sugar().admin.navToConfigureTabs();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1373
		// Verify that the module in "Displayed Tabs" column is dragged and dropped to the "Hidden Tabs" column. 
		new VoodooControl("div", "css", "#disabled_div .yui-dt-bd .yui-dt-data tr td div").assertEquals(sugar().accounts.moduleNamePlural, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}