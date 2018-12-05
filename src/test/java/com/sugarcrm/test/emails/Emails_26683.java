package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Emails_26683 extends SugarTest {
	DataSource ds, userDataSource ;
	FieldSet emailSettings;
	UserRecord demoUser;
	
	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);
		userDataSource = testData.get(testName + "_1");
		sugar.leads.api.create();
		// Configure outgoing email in admin panal
 		emailSettings = new FieldSet();
 		emailSettings.put("userName", ds.get(0).get("emailAddress"));
 		emailSettings.put("password", ds.get(0).get("password"));
 		emailSettings.put("allowAllUsers", ds.get(0).get("allowAllUsers"));
 		sugar.admin.setEmailServer(emailSettings);
 		demoUser = (UserRecord) sugar.users.create(userDataSource.get(0));
		sugar.alerts.waitForLoadingExpiration();
		sugar.logout();
		sugar.login(demoUser);
		
		// TODO: VOOD-672, Set email settings individually
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(ds.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(ds.get(0).get("emailAddress")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(ds.get(0).get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set(ds.get(0).get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();
		VoodooUtils.pause(50000); // Let save and check complete at Gmail. No suitable waitForxxx control available.
		VoodooUtils.focusDefault();

	}

	/**
	 * Parent record should be stay persistent on the emails compose screen from quick compose
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_26683_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// navigate to lead record 
		sugar.leads.navToListView();
		FieldSet fs = new FieldSet();
		fs.put("email", ds.get(0).get("emailAddress"));
		sugar.leads.listView.clickRecord(1);
		sugar.leads.recordView.edit();
		new VoodooControl("input", "css", ".newEmail.input-append").set(ds.get(0).get("emailAddress"));
		new VoodooControl("a", "css", ".btn.addEmail").click();
		sugar.leads.recordView.save();
		VoodooUtils.pause(3000); // pause needed to save record. 
		
		// Create Email using Quick Create (+)
		sugar.navbar.quickCreateAction(sugar.emails.moduleNamePlural);
		VoodooUtils.pause(8000); // pause needed to load email composer UI 
		// VOOD-843 - Need library support for email composer UI
		new VoodooControl("input", "css", "[name='subject']").set("test mail");
		VoodooUtils.focusFrame("mce_0_ifr");
		new VoodooControl("body", "id", "tinymce").set("this is a test mail");
		VoodooUtils.focusDefault();
		
		// click send email button  
		new VoodooControl("button", "css", "#drawers button.btn-link.btn-invisible.more").click();
		VoodooControl reletedToControl = new VoodooControl("span", "css", "div:nth-child(2) div.span7 span.select2-chosen");
		reletedToControl.waitForVisible();
		reletedToControl.assertContains("Mr. John Doe", true);
		new VoodooControl("a", "css", "[name='send_button']").click();
		sugar.alerts.getSuccess().closeAlert();
		sugar.leads.navToListView();
		sugar.leads.listView.clickRecord(1);
		VoodooControl emailSubPanelControl =  new VoodooControl("a", "css", "div.flex-list-view.left-actions.right-actions td:nth-child(2) a");
		emailSubPanelControl.waitForVisible();
		emailSubPanelControl.assertContains("test mail", true );
		emailSubPanelControl.click(); 
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl leadNameControl = new VoodooControl("td", "css", "#contentTable tr:nth-child(2) > td:nth-child(4)");
		leadNameControl.waitForVisible();
		leadNameControl.assertContains("Mr. John Doe", true);
		new VoodooControl("td", "css", "#contentTable  tr:nth-child(4) > td:nth-child(2)").assertContains(ds.get(0).get("emailAddress"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}