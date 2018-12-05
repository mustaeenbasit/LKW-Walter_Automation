package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20215 extends SugarTest {
	FieldSet customData;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify module name in studio sync with updating module name
	 * @throws Exception
	 */
	@Test
	public void Admin_20215_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.renameModule(sugar().quotes, customData.get("singularName"), customData.get("pluralName"));

		// Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Verify that All "Quotes" should be "Qus" in Studio modules
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Quotes").assertContains(customData.get("pluralName"), true);
		new VoodooControl("div", "id", "mbTree").assertContains(customData.get("pluralName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}