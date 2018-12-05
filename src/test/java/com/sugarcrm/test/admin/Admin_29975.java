package com.sugarcrm.test.admin;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_29975 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Admin > Backups : Verify that validation message should be shown when Directory and Filename
	 * fields are saved as blank
	 * @throws Exception
	 */
	@Test
	public void Admin_29975_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Go to Admin > BackupManagement
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("backupManagement").click();
		VoodooUtils.waitForReady();
		
		// Leaving the Directory and Filename fields as blank and clicking Confirm Settings button
		// TODO: VOOD-1579 -Need lib support for Admin > Backup module
		new VoodooControl("input", "css", "[value='Confirm Settings']").click();
		
		// Verify validation message "Missing required field: Directory" is displayed for Directory field
		new VoodooControl("div", "css", "tr td:nth-child(2) div").assertEquals(customData.get
			("directoryMessage"), true);
		
		// Verify validation message "Missing required field: Filename" is displayed for Filename field
		new VoodooControl("div", "css", "tr:nth-child(2) td:nth-child(2) div").assertEquals
			(customData.get("filenameMessage"), true);
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}