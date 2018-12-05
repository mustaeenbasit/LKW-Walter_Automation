package com.sugarcrm.test.campaigns;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.UserRecord;

public class Campaigns_19317 extends SugarTest {
	FieldSet customData, emailSetupData, userDataSet;
	UserRecord user;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		emailSetupData = testData.get(testName+"_smtp_settings").get(0);
		userDataSet = testData.get(testName+"_user").get(0);
		sugar.campaigns.api.create();
		sugar.login();

		// smtp settings
		sugar.admin.setEmailServer(emailSetupData);

		// Create user
		user = (UserRecord)sugar.users.create(userDataSet);
		sugar.logout();
	}

	/**
	 * Share action in Campaign module when Outgoing email is not setup
	 * @throws Exception
	 * */
	@Ignore("VOOD-1035. Can not access text in iframe in Email Viewer's body")
	@Test
	public void Campaigns_19317_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as new user
		sugar.login(user);

		// user email settings
		sugar.navbar.navToModule(customData.get("email_module"));
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-672
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(userDataSet.get("userName"));
		new VoodooControl("input", "id", "email_user").set(userDataSet.get("emailAddress")); 
		new VoodooControl("input", "id", "email_password").set(emailSetupData.get("password"));
		new VoodooControl("input", "id", "trashFolder").set(customData.get("trash"));
		new VoodooControl("input", "id", "sentFolder").set(customData.get("sent"));
		new VoodooControl("input", "id", "ie_from_addr").set(userDataSet.get("emailAddress"));
		new VoodooControl("input", "id", "inbound_mail_smtpuser").set(customData.get("smtp_username"));
		new VoodooControl("input", "id", "inbound_mail_smtppass").set(customData.get("smtp_password"));
		new VoodooControl("input", "id", "saveButton").click();

		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		// Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooUtils.pause(1000); // Let action complete. Could not find suitable waitForxxx control.

		// Xpath needed for DONE button
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
		VoodooUtils.focusDefault();

		// campaign record
		sugar.campaigns.navToListView();
		sugar.campaigns.listView.clickRecord(1);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		new VoodooControl("span", "css", "#detail_header_action_menu .ab").click();
		new VoodooControl("a", "css", ".subnav.ddopen li form a").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
		new VoodooControl("input", "css", ".fld_to_addresses.edit div ul li:nth-of-type(1) input").set(emailSetupData.get("userName"));
		new VoodooControl("div", "css", ".select2-drop-active li:nth-child(1) div").click();

		// TODO: VOOD-672
		new VoodooControl("a", "css", ".fld_send_button.detail a").click();
		sugar.alerts.waitForLoadingExpiration();

		// checking mail
		sugar.navbar.navToModule(customData.get("email_module"));
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying campaign mail in my inbox
		String userMailBox = String.format("//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span[contains(.,'%s')]", userDataSet.get("userName"));
		String inbox = String.format("//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'%s')]", customData.get("inbox"));

		// TODO: VOOD-792
		new VoodooControl("span", "xpath", userMailBox).click();
		new VoodooControl("span", "xpath", inbox).click();

		// pause required for syncing mail
		new VoodooControl("button", "css", "#checkEmailButton").click();
		sugar.emails.waitForSugarMsgWindow(30000);

		sugar.emails.waitForSugarMsgWindow(30000);

		VoodooControl subjectCtrl = new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div");
		subjectCtrl.assertEquals(customData.get("subject_msg"), true);
		subjectCtrl.click();
		VoodooUtils.waitForAlertExpiration(); // for loading mail content

		// verifying subject only, can't access mail body content
		new VoodooControl("td", "css", "#_blank div tr:nth-child(2) td.displayEmailValue").assertEquals(customData.get("subject_msg"), true);

		// TODO: VOOD-1035
		// TODO: Link open from mail body content 

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}