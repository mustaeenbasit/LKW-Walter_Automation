package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Emails_20701 extends SugarTest {
	FieldSet emailRecord;

	public void setup() throws Exception {
		emailRecord = testData.get(testName).get(0);
		sugar.login();
	}

	/**
	 * Verify Admin Email settings set up
	 * @throws Exception
	 */
	@Test
	public void Emails_20701_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Email Setting
		sugar.admin.setEmailServer(emailRecord);

		// Verify the email settings have been correctly saved
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("emailSettings").click();
		new VoodooControl("input", "id", "mail_smtpuser").assertEquals(
				emailRecord.get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}