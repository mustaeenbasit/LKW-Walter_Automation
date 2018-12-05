package com.sugarcrm.test.admin;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20172 extends SugarTest {
	FieldSet proxyData;
	VoodooControl proxyOn, proxyHost, proxyPort, proxyAuth, proxyUsername, proxyPassword;

	public void setup() throws Exception {
		proxyData = testData.get(testName).get(0);
		proxyOn = sugar().admin.systemSettings.getControl("proxyOn");
		proxyHost = sugar().admin.systemSettings.getControl("proxyHost");
		proxyPort = sugar().admin.systemSettings.getControl("proxyPort");
		proxyAuth = sugar().admin.systemSettings.getControl("proxyAuth");
		proxyUsername = sugar().admin.systemSettings.getControl("proxyUsername");
		proxyPassword = sugar().admin.systemSettings.getControl("proxyPassword");
		sugar().login();
	}

	/**
	 * Verify that updated proxy setting is displayed when saving system settings.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20172_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet sysSettings = new FieldSet();
		sysSettings.put("proxyOn", "true");
		sugar().admin.setSystemSettings(sysSettings);

		// Go back to System Settings page and  verify that the saved settings still there
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("Use proxy server field is not chcked", proxyOn.isChecked());
		VoodooUtils.focusDefault();

		// Enter valid data in "Proxy host" and "Port" field
		sysSettings.clear();
		sysSettings.put("proxyHost", proxyData.get("proxyHost"));
		sysSettings.put("proxyPort", proxyData.get("proxyPort"));
		sysSettings.put("proxyAuth", "true");
		sysSettings.put("proxyUsername", proxyData.get("proxyUsername"));
		sysSettings.put("proxyPassword", proxyData.get("proxyPassword"));
		sugar().admin.setSystemSettings(sysSettings);

		// Go back to System Settings page and  verify that the Updated proxy setting is displayed on the page
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertTrue("User proxy server field is not chcked", proxyOn.isChecked());
		proxyHost.assertContains(proxyData.get("proxyHost"), true);
		proxyPort.assertContains(proxyData.get("proxyPort"), true);
		Assert.assertTrue("Use Proxy Authentication field is not chcked", proxyAuth.isChecked());
		proxyUsername.assertContains(proxyData.get("proxyUsername"), true);
		proxyPassword.assertContains(proxyData.get("proxyPassword"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
