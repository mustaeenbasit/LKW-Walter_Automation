package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_26561 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify no error occurred when click on Emails subpanel for Account module under studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_26561_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();

		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls
		new VoodooControl("a", "id", "studio").click();

		new VoodooControl("a", "id", "studiolink_Accounts").click();

		// Click on Subpanels button under Studio -> Accounts
		new VoodooControl("a", "css", "td#subpanelsBtn a").click();
		VoodooUtils.waitForReady();

		// Click on Emails button under Studio -> Accounts -> Subpanels
		new VoodooControl("a", "css", "Div#Buttons tr td:nth-of-type(6) a").click();

		// Verify there has no error occurred by click on Save & Deploy button
		new VoodooControl("a", "id", "savebtn").click();

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
