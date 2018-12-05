package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20023 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().navbar.navToAdminTools();
	}

	/**
	 * System-generated password email template will use site url for instance URL.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20023_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("passwordManagement").click();
		// TODO: VOOD-993
		new VoodooControl("input", "css", "#edit_generatepasswordtmpl").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("iframe", "css", "#body_text_ifr").waitForVisible();
		VoodooUtils.focusFrame(0);
		new VoodooControl("p", "css", "#tinymce tr:nth-child(1) p:nth-child(5)").waitForVisible();
		new VoodooControl("p", "css", "#tinymce tr:nth-child(1) p:nth-child(5)").assertContains("$config_site_url", true);
		VoodooUtils.focusDefault();
		VoodooUtils.closeWindow();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}