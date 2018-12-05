package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class GlobalSearch_28688 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the help text in Admin -> System settings -> List View Items per page
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28688_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		// Nav to System Settings Page
		sugar().navbar.navToAdminTools();
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Click on "List view Items per page" info tip
		// TODO: VOOD-1493 Need lib support for Admin > system settings > Inline help tip
		new VoodooControl("img", "xpath", "//td[contains(.,'Listview items per page')]/img").click();
		
		// Verify "List view Items per page" help toop-tip
		// TODO: VOOD-1493 Need lib support for Admin > system settings > Inline help tip
		new VoodooControl("div", "css", "div[role='dialog']:not([style*='display: none'])").assertContains(customFS.get("toolTipMsg"), true);
		new VoodooControl("button", "css", "div[role='dialog']:not([style*='display: none']) button.ui-dialog-titlebar-close").click();
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}