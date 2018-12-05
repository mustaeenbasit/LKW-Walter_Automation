package com.sugarcrm.test.bugs;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Bugs_18583 extends SugarTest {

	public void setup() throws Exception {
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
	}
	
	/** 
	 * Verify that "View Bug Reports" link item is displayed in the navigation shortcuts
	 * @throws Exception
	 */
	@Test
	public void Bugs_18583_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.selectMenuItem(sugar.bugs, "viewBugReports");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: JIRA VOOD-703 add support to verify view reports is displayed.
		new VoodooControl("h2", "css", "div.moduleTitle h2").assertContains("Search", true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
