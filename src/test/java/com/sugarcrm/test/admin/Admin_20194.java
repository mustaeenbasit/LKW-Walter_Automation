package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20194 extends SugarTest {
	FieldSet customFS;
	
	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		sugar().login();
		
		// TODO: VOOD-1493
		// Unchecked Display Downloads Tab in admin > system settings
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("img", "xpath", "//td[contains(.,' Display Downloads Tab')]/img").click();
		
		// Verify "Display Downloads Tab" help toop-tip
		new VoodooControl("div", "css", "div[role='dialog']:not([style*='display: none'])").assertContains(customFS.get("toolTipMsg"), true);
		new VoodooControl("button", "css", "div[role='dialog']:not([style*='display: none']) button.ui-dialog-titlebar-close").click();
		
		// Click to uncheck Display Downloads Tab check-box
		sugar().admin.systemSettings.getControl("showDownloadTab").click();
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Enable the Download tab in User Settings through System Settings
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20194_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Downloads tab exist on detail view
		VoodooControl detailViewDownLoadTab = sugar().users.userPref.getControl("tab4");
		detailViewDownLoadTab.assertExists(false);
		VoodooUtils.focusDefault();
		
		// Click on edit button
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-563
		// Verify that Downloads tab exist on edit view
		VoodooControl editViewDownLoadTab = new VoodooControl("a", "css", "#EditView_tabs ul li:nth-child(5) a");
		editViewDownLoadTab.assertExists(false);
		VoodooUtils.focusDefault();
		
		// Go to system settings and check Display Downloads Tab checkbox 
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.systemSettings.getControl("showDownloadTab").click();
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
		
		sugar().navbar.navToProfile();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Downloads tab exist on detail view
		detailViewDownLoadTab.assertExists(true);
		VoodooUtils.focusDefault();
		
		// Click on edit button
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-563
		// Verify that Downloads tab exist on edit view
		editViewDownLoadTab.assertContains(customFS.get("downloadTab"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}