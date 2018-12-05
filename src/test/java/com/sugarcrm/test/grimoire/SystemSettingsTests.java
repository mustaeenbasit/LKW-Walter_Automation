package com.sugarcrm.test.grimoire;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class SystemSettingsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyElementsHookValue() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElementsHookValue()...");

		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Verify action buttons
		sugar().admin.systemSettings.getControl("save").assertExists(true);
		sugar().admin.systemSettings.getControl("cancel").assertExists(true);

		// Verify User Interface controls
		sugar().admin.systemSettings.getControl("maxEntriesPerPage").assertExists(true);
		sugar().admin.systemSettings.getControl("maxEntriesPerSubPanel").assertExists(true);
		sugar().admin.systemSettings.getControl("displayServerResponseTime").assertExists(true);
		sugar().admin.systemSettings.getControl("defaultModuleFavicon").assertExists(true);
		sugar().admin.systemSettings.getControl("systemName").assertExists(true);
		sugar().admin.systemSettings.getControl("showFullNames").assertExists(true);
		sugar().admin.systemSettings.getControl("showDownloadTab").assertExists(true);
		sugar().admin.systemSettings.getControl("enableActionMenu").assertExists(true);
		sugar().admin.systemSettings.getControl("lockSubpanels").assertExists(true);

		// Verify Proxy Settings controls
		sugar().admin.systemSettings.getControl("proxyOn").click();
		sugar().admin.systemSettings.getControl("proxyHost").assertExists(true);
		sugar().admin.systemSettings.getControl("proxyPort").assertExists(true);
		sugar().admin.systemSettings.getControl("proxyAuth").click();
		sugar().admin.systemSettings.getControl("proxyUsername").assertExists(true);
		sugar().admin.systemSettings.getControl("proxyPassword").assertExists(true);

		// Verify SkypeOut controls
		sugar().admin.systemSettings.getControl("skypeOut").assertExists(true);

		// Verify Tweet to case controls
		sugar().admin.systemSettings.getControl("tweetTocase").assertExists(true);

		// Verify Advanced controls
		sugar().admin.systemSettings.getControl("clientIp").assertExists(true);
		sugar().admin.systemSettings.getControl("logMemoryUsage").assertExists(true);
		sugar().admin.systemSettings.getControl("dumpSlowQueries").assertExists(true);
		sugar().admin.systemSettings.getControl("slowQueryTime").assertExists(true);
		sugar().admin.systemSettings.getControl("uploadMaxsize").assertExists(true);
		sugar().admin.systemSettings.getControl("stackTraceErrors").assertExists(true);
		sugar().admin.systemSettings.getControl("systemSessionTimeout").assertExists(true);
		sugar().admin.systemSettings.getControl("developerMode").assertExists(true);
		sugar().admin.systemSettings.getControl("vcalTime").assertExists(true);
		sugar().admin.systemSettings.getControl("maxRecordsLimit").assertExists(true);
		sugar().admin.systemSettings.getControl("noPrivateTeamUpdate").assertExists(true);

		// Verify Logger Settings controls
		sugar().admin.systemSettings.getControl("loggerFileName").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerFileExt").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerFileSuffix").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerFileMaxSize").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerFileDateFormat").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerLevel").assertExists(true);
		sugar().admin.systemSettings.getControl("loggerFileMaxLogs").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info("verifyElementsHookValue() complete.");
	}

	@Test
	public void verifySystemSettings() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySystemSettings()...");

		// Set System settings
		FieldSet systemSettingsData = new FieldSet();
		systemSettingsData.put("maxEntriesPerPage", "10");
		systemSettingsData.put("maxEntriesPerSubPanel", "10");
		systemSettingsData.put("displayServerResponseTime", "true");
		systemSettingsData.put("defaultModuleFavicon", "false");
		systemSettingsData.put("systemName", "Sugar");
		systemSettingsData.put("showFullNames", "true");
		systemSettingsData.put("showDownloadTab", "true");
		systemSettingsData.put("enableActionMenu", "true");
		systemSettingsData.put("lockSubpanels", "false");

		// change system settings
		sugar().admin.setSystemSettings(systemSettingsData);

		// Navigate to system settings and verify updated elements values
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.systemSettings.getControl("maxEntriesPerPage").assertEquals(systemSettingsData.get("maxEntriesPerPage"), true);
		sugar().admin.systemSettings.getControl("maxEntriesPerSubPanel").assertEquals(systemSettingsData.get("maxEntriesPerSubPanel"), true);
		sugar().admin.systemSettings.getControl("systemName").assertEquals(systemSettingsData.get("systemName"), true);	
		Assert.assertTrue("Server Response time checkbox is not checked", sugar().admin.systemSettings.getControl("displayServerResponseTime").isChecked());
		Assert.assertTrue("Show Full name checkbox is not checked", sugar().admin.systemSettings.getControl("showFullNames").isChecked());
		Assert.assertTrue("Display Download tab checkbox is not checked", sugar().admin.systemSettings.getControl("showDownloadTab").isChecked());
		Assert.assertTrue("Display action with menu checkbox is not checked", sugar().admin.systemSettings.getControl("enableActionMenu").isChecked());
		Assert.assertTrue("Display module icon as Favicon checkbox is checked", !sugar().admin.systemSettings.getControl("defaultModuleFavicon").isChecked());
		Assert.assertTrue("Collapse all subpanel and disable sticky feature checkbox is checked", !sugar().admin.systemSettings.getControl("lockSubpanels").isChecked());

		VoodooUtils.voodoo.log.info("verifySystemSettings() complete.");
	}

	public void cleanup() throws Exception {}
}