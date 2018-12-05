package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21066 extends SugarTest {
	public void setup() throws Exception {
		sugar.calls.api.create();
		sugar.contacts.api.create();
		sugar.login();
	}

	/**
	 * View call_Verify that call information is displayed correctly on "Call Detail View" page.
	 * @throws Exception
	 */
	@Test
	public void Calls_21066_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		
		
		// Go to the call's and Edit the call by relating the contact to this call.
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(sugar.contacts.getDefaultData().get("lastName"));
		sugar.calls.recordView.save();
		
		// Verify that calls information is displayed properly 
		sugar.calls.recordView.getDetailField("name").assertContains(sugar.calls.getDefaultData().get("name"), true);
		sugar.calls.recordView.getDetailField("relatedToParentType").assertEquals(sugar.contacts.moduleNameSingular, true);
		sugar.calls.recordView.getDetailField("relatedToParentName").assertEquals(sugar.contacts.getDefaultData().get("fullName"), true);
		
		// TODO: VOOD-872 -Lib support in view log window
		// Check out the log in Admin->System Settings
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("systemSettings").click();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//a[contains(.,'View Log')]").scrollIntoView();
		new VoodooControl("a", "xpath", "//a[contains(.,'View Log')]").click(); // Click on 'View Log' link
		// TODO: CB-255
		VoodooUtils.pause(1000);
		VoodooUtils.focusWindow(1);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that No fatal error is displayed in log file.
		new VoodooControl("div", "id", "content").assertContains("FATAL", false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}