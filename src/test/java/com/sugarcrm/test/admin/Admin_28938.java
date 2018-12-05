package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28938 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that error messages should not display for "Directory doesn't exist" and "Directory exists" at the same time
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_28938_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		
		// Go to Admin > BackupManagement
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("backupManagement").click();
		
		// TODO: VOOD-1579 -Need lib support for Admin > Backup module
		new VoodooControl("input", "css", "[name='backup_dir']").set(testName);
		new VoodooControl("input", "css", "[name='backup_zip']").set(testName+".zip");
		new VoodooControl("input", "css", "[value='Confirm Settings']").click();
		VoodooUtils.waitForReady();
		
		VoodooControl backupFormSettingCtrl = new VoodooControl("form", "css", "[name='Backups']");
		
		// Verify that Backup "Directory does not exist" and could not be created & "Directory exists" message should not be displayed at the same time.
		backupFormSettingCtrl.assertContains(customFS.get("not_exist_message"), false);
		
		// Verify that only appearing this message "Settings confirmed. Press backup to perform the backup."
		backupFormSettingCtrl.assertContains(customFS.get("confirm_msg"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}