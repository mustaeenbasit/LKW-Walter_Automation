package com.sugarcrm.test.calendar;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calendar_20320 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Display calendar_Verify that Shared Calendar panel is displayed when clicking "Shared" button
	 * @throws Exception
	 */
	@Test
	public void Calendar_20320_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to Calendar view
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// check not see User List button before click Share button
		// TODO: VOOD-863 need support for Calendar module
		VoodooControl userListBtn = new VoodooControl("button", "id", "userListButtonId");
		userListBtn.assertExists(false);

		// Click Share button and verify Shared calendar page via verifying button User List exists
		// Also click on User List button to check users listed in the shared page
		new VoodooControl("input", "id", "shared-tab").click();
		sugar().alerts.waitForLoadingExpiration();	
		userListBtn.assertExists(true);

		// TODO: VOOD-1027, need support to select more than one user and verify the selection
		userListBtn.click();
		new VoodooControl("option", "xpath", "//select[@id='shared_ids']/option[contains(.,'Administrator')]").click();
		new VoodooControl("button", "css", ".ft [title='Select']").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("h5", "css", ".calSharedUser").assertContains("Administrator", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
