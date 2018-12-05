package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_20817 extends SugarTest {
	DataSource emailSetup;
	FieldSet emailCostumSetup;

	public void setup() throws Exception {
		sugar.login();
		emailSetup = testData.get(testName);
		emailCostumSetup = testData.get(testName+"_1").get(0);

		// configure Admin->Email Settings
		sugar.admin.setEmailServer(emailSetup.get(0));
	}

	/**
	 * Use UTF-8 character as from name in personal email account setting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_20817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		
		// TODO: VOOD-672
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click(); 
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(emailCostumSetup.get("mail_account_name"));
		new VoodooControl("input", "id", "email_user").set(emailSetup.get(0).get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSetup.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set(emailCostumSetup.get("trash_folder"));
		new VoodooControl("input", "id", "ie_from_name").set(emailCostumSetup.get("outgoing_from_name"));
		new VoodooControl("input", "id", "ie_from_addr").set(emailCostumSetup.get("outgoing_from_address"));
		VoodooUtils.waitForAlertExpiration();// here need this due to outgoing mail server dropdown take time to load
		
		new VoodooControl("input", "id", "saveButton").click();
		VoodooUtils.pause(30000); // Let save and check complete at Gmail. No suitable waitForxxx control available.
		
		new VoodooControl("img", "css", "#inboundAccountsTable td.yui-dt-col-edit div img").waitForVisible();
		new VoodooControl("img", "css", "#inboundAccountsTable td.yui-dt-col-edit div img").click();
		// Verify visibility of Settings window
		new VoodooControl("input", "id", "ie_name").assertAttribute("value", emailCostumSetup.get("mail_account_name"));
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
	
}