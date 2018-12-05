package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28945 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Backup module is validated properly
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_28945_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Admin > BackupManagement
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("backupManagement").click();
		
		// TODO: VOOD-1579 -Need lib support for Admin > Backup module
		VoodooControl backupDirCtrl = new VoodooControl("input", "css", "[name='backup_dir']");
		VoodooControl backupZipCtrl = new VoodooControl("input", "css", "[name='backup_zip']");
		VoodooControl confirmBtnCtrl = new VoodooControl("input", "css", "[value='Confirm Settings']");
		backupDirCtrl.set("");
		backupZipCtrl.set("");
		confirmBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that backup directory name and filename is required
		new VoodooControl("form", "css", "[name='Backups'] table tbody tr:nth-child(1) td:nth-child(2) div").assertContains(customFS.get("error_msg1"), true);
		new VoodooControl("form", "css", "[name='Backups'] table tbody tr:nth-child(2) td:nth-child(2) div").assertContains(customFS.get("error_msg2"), true);
		
		// Set backup directory name and file name
		backupDirCtrl.set(testName);
		backupZipCtrl.set(testName+".zip");
		confirmBtnCtrl.click();
		VoodooUtils.waitForReady();
		
		// Verify that backup directory and filename saved successfully
		new VoodooControl("form", "css", "[name='Backups']").assertContains(customFS.get("confirm_msg"), true);
		
		// Verify that Text fields should be read only fields and user should not be able to click on Confirm settings.
		new VoodooControl("input", "css", "[name='backup_dir']").assertAttribute("readonly", "", true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}