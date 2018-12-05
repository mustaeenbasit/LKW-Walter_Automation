package com.sugarcrm.test.notes;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Notes_27586 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Verify Notes module should be enabled by default
	 * 
	 * @throws Exception
	 */
	@Test
	public void Notes_27586_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1154
		new VoodooControl("a", "id", "enable_wireless_modules").click();
		sugar().alerts.waitForLoadingExpiration();
		
		// Assert that Notes module is enabled for mobile 
		new VoodooControl("div", "id", "enabled_div").assertContains(sugar().notes.moduleNamePlural, true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}