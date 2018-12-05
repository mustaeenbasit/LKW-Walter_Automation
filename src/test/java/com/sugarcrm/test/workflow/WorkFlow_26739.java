package com.sugarcrm.test.workflow;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest; 

public class WorkFlow_26739 extends SugarTest {
	FieldSet customData;
	DataSource userDataSet;
	UserRecord user;
	VoodooControl wfMgtCtrl, dropDownCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		userDataSet = testData.get(testName+"_user");
		sugar.opportunities.api.create();
		sugar.revLineItems.api.create(); // TODO: multiple RLI to trigger WF conditions

		// smtp settings
		FieldSet emailSetupData = testData.get("env_email_setup").get(0);
		emailSetupData.put("userName", customData.get("systemSMTPUsername"));
		emailSetupData.put("password", customData.get("systemSMTPPassword")); 
		emailSetupData.put("allowAllUsers", Boolean.toString(false));
		sugar.login();  
		sugar.admin.setEmailServer(emailSetupData);

		// TODO: VOOD-1200
		// Create user
		user = (UserRecord)sugar.users.create(userDataSet.get(0));
	}

	/**
	 * Workflows attempting to send with current user email credentials
	 * @throws Exception
	 */
	@Ignore("VOOD-743, 1052, 1273")
	@Test
	public void WorkFlow_26739_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1042
		// Workflow
		wfMgtCtrl = new VoodooControl("a", "id", "workflow_management");
		wfMgtCtrl.click();
		VoodooUtils.focusDefault();
		dropDownCtrl = new VoodooControl("i", "css", "li.active .fa-caret-down");
		dropDownCtrl.click();
		new VoodooControl("li", "css", "li.active .scroll ul li:nth-of-type(1)").click();
		VoodooUtils.focusFrame("bwc-frame");

		// Create workflow with target as RLI Module
		new VoodooControl("input", "id", "name").set(testName);
		new VoodooControl("option", "css", "select[name=base_module] option:nth-of-type(24)").click();
		new VoodooControl("input", "id", "save_workflow").click();

		// Condition : When a field in the target module contains a specified value 
		// Sales Stage: Closed Won
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tbody tr td table:nth-child(2) tbody tr:nth-child(4) td:nth-child(1) input[type='radio']").click();
		new VoodooControl("input", "id", "next").click();
		new VoodooControl("a", "css", "#href_trigger").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#lhs_field option:nth-of-type(33)").click();
		new VoodooControl("option", "css", "#trigger__field_value option:nth-of-type(2)").click();
		VoodooControl saveBtnCtrl = new VoodooControl("input", "id", "save");
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(1);
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Alert: Basic
		new VoodooControl("a", "id", "NewWorkFlowAlertShells").click();
		new VoodooControl("input", "css", "input[name='name']").set(customData.get("alert_name"));
		new VoodooControl("input", "css", "#contentTable tr td table:nth-child(3) tr td:nth-child(11) input:nth-of-type(1)").click();

		// participant: with specified user i.e new user
		new VoodooControl("input", "css", "#ComponentView input.button").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table tr table:nth-child(2) tr:nth-child(5) td:nth-child(1) input[type='radio']").click();
		new VoodooControl("a", "id", "href_specific_user").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#selector option:nth-of-type(3)").click();
		new VoodooControl("input", "css", "input[name='Save']").click();
		VoodooUtils.focusWindow(1);
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();
		sugar.logout();

		// Login as new user
		sugar.login(user);

		// Goto Profile and change user SMTP settings
		sugar.navbar.navToProfile();
		sugar.users.detailView.edit(); // Not working	
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "mail_smtpuser").set(customData.get("userSMTPUsername"));
		new VoodooControl("input", "id", "mail_smtppass").set(customData.get("userSMTPPassword"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();

		// RLI record - to trigger workflow
		sugar.revLineItems.navToListView();
		sugar.revLineItems.listView.clickRecord(1);
		sugar.revLineItems.recordView.edit();
		sugar.revLineItems.recordView.getEditField("relOpportunityName").set(sugar.opportunities.getDefaultData().get("name"));
		sugar.revLineItems.recordView.getEditField("salesStage").set(customData.get("sales_stage"));
		sugar.revLineItems.recordView.save();
		VoodooUtils.focusDefault();

		// TODO: Mail check.
		// TODO: VOOD-743 - Below commented code is EXTREMELY dangerous code because it does not have a timeout. If something should go wrong and the message window never disappears, this test will hang forever and ruin the entire test run.
		// user email settings
		/*sugar.navbar.navToModule(sugar.emails.moduleNamePlural);
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-672
		new VoodooControl("button", "id", "settingsButton").click();
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		new VoodooControl("input", "id", "addButton").click();
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		new VoodooControl("input", "id", "ie_name").set(userDataSet.get(0).get("userName"));
		new VoodooControl("input", "id", "email_user").set(userDataSet.get(0).get("emailAddress")); 
		new VoodooControl("input", "id", "email_password").set(customData.get("userPassword"));
		new VoodooControl("input", "id", "trashFolder").set(customData.get("trash"));
		new VoodooControl("input", "id", "sentFolder").set(customData.get("sent"));
		new VoodooControl("input", "id", "ie_from_addr").set(userDataSet.get(0).get("emailAddress"));
		new VoodooControl("input", "id", "saveButton").click();

		// Wait for expiration of First Msg Window
		VoodooControl msgWindowCtrl = new VoodooControl("div", "id", "sugarMsgWindow_h");
		while (msgWindowCtrl.queryVisible())
			VoodooUtils.pause(100);

		// Wait for expiration of Second Msg Window
		while (msgWindowCtrl.queryVisible())
			VoodooUtils.pause(100);

		// Wait for expiration of Third Msg Window
		while (msgWindowCtrl.queryVisible())
			VoodooUtils.pause(100);

		VoodooUtils.pause(1000); // Let action complete. Could not find suitable waitForxxx control.
		new VoodooControl("input", "xpath", "//*[@id='settingsTabDiv']/div/div[2]/table/tbody/tr[2]/td/input[contains(@value,'Done')]").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.refresh(); // Needed to refresh email module after email account is configured.
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying WF alert in my inbox
		// TODO:VOOD-792
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		VoodooUtils.waitForAlertExpiration();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span").click();
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/div/div/table/tbody/tr/td[4]/span").click();

		// syncing mail
		//new VoodooControl("button", "css", "#checkEmailButton").click(); // Not working in ENT-7500, build 1250
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/div/div/table/tbody/tr/td[4]/span").rightClick();		
		new VoodooControl("a", "css", "#folderContextMenu ul li:nth-of-type(1) a").click();
		while (msgWindowCtrl.queryVisible())
			VoodooUtils.pause(1000);

		while (msgWindowCtrl.queryVisible())
			VoodooUtils.pause(1000);

		// Verifying WF triggered, message and to address recipient
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div").assertEquals(customData.get("subject_msg"), true);
		new VoodooControl("div", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[7]/div").assertContains(userDataSet.get(0).get("emailAddress"), true);
		VoodooUtils.focusDefault();*/

		// TODO: Verify #5,#7 as in expected result (once VOOD fixed)

		// TODO: clean up takes care but need to Log out from new user and login as admin
		sugar.logout();
		sugar.login();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}