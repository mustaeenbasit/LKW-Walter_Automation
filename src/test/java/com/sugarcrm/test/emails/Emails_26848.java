package com.sugarcrm.test.emails;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26848 extends SugarTest {
	FieldSet emailSetupData;
	DataSource ds;
	ContactRecord myContact;
	VoodooControl settingsButton, doneBtn, deleteBtnCtrl;
	
	public void setup() throws Exception {
		sugar.login();
		emailSetupData = testData.get(testName+"_emailSetup").get(0);
		ds = testData.get(testName);
		
		// Set up Outbound email account
		sugar.admin.setEmailServer(emailSetupData);
		
		myContact = (ContactRecord) sugar.contacts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("emailAddress", ds.get(1).get("mail_to"));
		myContact.edit(fs);
		
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		settingsButton = new VoodooControl("button", "id", "settingsButton");
		settingsButton.click();
		
		// TODO: VOOD-672, Inbound email setting.
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("img", "css", "#outboundAccountsTable td.yui-dt-col-edit div img").waitForVisible();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		
		// Fill the the Incoming Email properties and outgoing Email properties
		new VoodooControl("input", "id", "ie_name").set(ds.get(0).get("name"));
		new VoodooControl("input", "id", "email_user").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "email_password").set(emailSetupData.get("password"));
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set(emailSetupData.get("userName"));
		new VoodooControl("input", "id", "saveButton").click();
		
		// Wait for expiration of First Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

	    // Wait for expiration of Second Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);
	    // Wait for expiration of Third Msg Window
		sugar.emails.waitForSugarMsgWindow(30000);

	    VoodooUtils.pause(5000); // Let action complete. Could not find suitable waitForxxx control.
	    doneBtn = new VoodooControl("input", "css", "#settingsTabDiv table:nth-of-type(3) input");
	    doneBtn.click();

	    VoodooUtils.acceptDialog();
	}

	/**
	 * Signature is auto populated when open email composer.
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1035. Can not access text in iframe in Email Viewer's body")
	@Test
	public void Emails_26848_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create a new signature.
		new VoodooControl("em", "css", "#generalSettings a em").click();
		new VoodooControl("input", "css", "[value='Create']").click();
		VoodooUtils.pause(1000); // wait for open new window.
		
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "name").set(ds.get(0).get("sign_name"));
		VoodooUtils.focusFrame("sigText_ifr");
		new VoodooControl("body", "id", "tinymce").set(ds.get(0).get("signature"));
		VoodooUtils.focusDefault();
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		doneBtn.waitForVisible();
		doneBtn.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.focusDefault();
		sugar.logout();
		
		// Login again to get Created signature populated.
		sugar.login();
		
		// Click on quick create to send Email.
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);
		VoodooUtils.pause(8000); // pause needed to load email composer UI
		
		// TODO: VOOD-843. Need library support for email composer UI
		new VoodooControl("input", "css", "[name='subject']").set(ds.get(1).get("subject"));
		new VoodooSelect("input", "css", "[data-voodoo-name='to_addresses'] input").set(ds.get(1).get("mail_to"));
		new VoodooControl("div", "css", ".select2-result-label").waitForVisible();
		new VoodooControl("div", "css", ".select2-result-label").click();
		VoodooUtils.focusFrame("mce_0_ifr");
		
		// Verify the signature is auto-populated in email body.
		new VoodooControl("body", "id", "tinymce").assertContains(ds.get(0).get("signature"), true);
		
		VoodooUtils.focusDefault();
		
		// Click send email button  
		new VoodooControl("a", "css", "[name='send_button']").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify signature is visible in received email body.
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "checkEmailButton").click();
		VoodooUtils.pause(10000); 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr").click();
		
		// Wait for 'Reply' button which appears only when the mail fully loads
		new VoodooControl("button", "css", "div.displayEmailValue button:nth-child(1)").waitForVisible();
		VoodooUtils.focusFrame("displayEmailFramePreview");
		new VoodooControl("body", "id", "tinymce").assertContains(ds.get(1).get("signature"), true);
		VoodooUtils.focusDefault();
		
		// Send email in contacts module
		sugar.contacts.navToListView();
		sugar.contacts.listView.clickRecord(1);
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("a", "css", "[data-action='email'][name='email_compose_button']").click();
		VoodooUtils.waitForAlertExpiration();
		
		// TODO: VOOD-843 - Need library support for email composer UI
		new VoodooControl("input", "css", "[name='subject']").set(ds.get(2).get("subject"));
		VoodooUtils.pause(4000); // pause needed to let body visible.
		VoodooUtils.focusFrame("mce_0_ifr");
		
		// Verify the signature is auto-populated in email body.
		new VoodooControl("body", "id", "tinymce").assertContains(ds.get(1).get("signature"), true);
		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", "[name='send_button']").click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.alerts.getSuccess().closeAlert();
		
		// Verify In received email body, sees the signature correctly.
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "checkEmailButton").click();
		VoodooUtils.pause(10000);
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span[contains(.,'INBOX')]").click();
		new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr").click();
		
		// Wait for 'Reply' button which appears only when the mail fully loads
		new VoodooControl("button", "css", "div.displayEmailValue button:nth-child(1)").waitForVisible();
		VoodooUtils.focusFrame("displayEmailFramePreview");
		new VoodooControl("body", "id", "tinymce").assertContains(ds.get(1).get("signature"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
