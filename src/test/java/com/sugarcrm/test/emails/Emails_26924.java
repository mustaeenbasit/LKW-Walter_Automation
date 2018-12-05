package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Emails_26924 extends SugarTest {
	FieldSet emailSetupData, myInboundData;

	public void setup() throws Exception {				
		sugar.login();

		// SMTP settings
		emailSetupData = testData.get("env_email_setup").get(0);
		// Inbound email setup
		myInboundData = testData.get(testName).get(0);

		// TODO: VOOD-866
		// fill Inbound email detail
		sugar.admin.setEmailServer(emailSetupData);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "mailboxes").click();
		new VoodooControl("button", "css", "li[data-module='InboundEmail'] span button").click();
		new VoodooControl("li", "css", "li[data-module='InboundEmail'] .dropdown-menu.scroll li:nth-of-type(1)").click();

		// click on prefill gmail default link
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();		
		// set input values
		sugar.inboundEmail.editView.getEditField("name").set(myInboundData.get("name"));
		sugar.inboundEmail.editView.getEditField("userName").set(myInboundData.get("user_name"));
		sugar.inboundEmail.editView.getEditField("password").set(myInboundData.get("user_password"));
		sugar.inboundEmail.editView.getEditField("trashFolder").set(myInboundData.get("trash_folder"));		
		new VoodooControl("input", "id", "from_name").set(myInboundData.get("name"));
		sugar.inboundEmail.editView.getEditField("fromAddress").set(myInboundData.get("user_name"));
		new VoodooControl("input", "id", "allow_outbound_group_usage").click();

		// assign to team
		new VoodooControl("button", "css", "#lineLabel_team_name > td:nth-child(1) > span > button.button.firstChild").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "#MassUpdate > table.list.view > tbody > tr.oddListRowS1 > td:nth-child(1) > input").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Save Inbound Email
		new VoodooControl("input", "css", "#EditView > table > tbody > tr > td:nth-child(1) > input:nth-child(1)").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that group email address is available in the From address
	 * @throws Exception
	 */
	@Test
	public void Emails_26924_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Change admin email address
		sugar.navbar.navToProfile();		
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "table.emailaddresses tr td:nth-child(1) input[title='primary email address']").set(myInboundData.get("admin_email"));
		new VoodooControl("input", "id", "SAVE_HEADER").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Emails module
		sugar.navbar.navToModule(sugar.emails.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		// Click on Compose button
		new VoodooControl("button", "id", "composeButton").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.pause(6000); // Required for completely load compose email form
		//Assert Inbound first Email data
		new VoodooControl("option", "css", "#addressFrom1 > option:nth-child(1)").assertContains(myInboundData.get("user_name"), true);
		new VoodooControl("option", "css", "#addressFrom1 > option:nth-child(1)").assertContains(myInboundData.get("name"), true);
		//Assert Inbound second Email data
		new VoodooControl("option", "css", "#addressFrom1 > option:nth-child(2)").assertContains(myInboundData.get("admin_email"), true);
		new VoodooControl("option", "css", "#addressFrom1 > option:nth-child(2)").assertContains(myInboundData.get("admin_name"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}