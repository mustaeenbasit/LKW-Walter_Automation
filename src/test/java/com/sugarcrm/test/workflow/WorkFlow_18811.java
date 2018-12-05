package com.sugarcrm.test.workflow;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class WorkFlow_18811 extends SugarTest {
	FieldSet emailSettings = new FieldSet();
	UserRecord demoUser;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		emailSettings = testData.get("env_email_setup").get(0);
		sugar().login();

		// Configure outgoing email in admin panel
		sugar().admin.setEmailServer(emailSettings);

		// Create a demo user
		// TODO: VOOD-1200
		FieldSet userEmail = new FieldSet();
		userEmail.put("emailAddress", emailSettings.get("userName"));
		demoUser = (UserRecord) sugar().users.create(userEmail);
	}

	/**
	 * Verify workflow alert email works.
	 * 
	 * @throws Exception
	 */
	@Test
	public void WorkFlow_18811_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to work flow management
		sugar().admin.navToAdminPanelLink("workflowManagement");
		// TODO: VOOD-1042
		new VoodooControl("i", "css", "li.active .fa-caret-down").click();

		// click on Create Workflow Definition  
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_WORKFLOW']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");

		FieldSet ds = testData.get(testName).get(0);

		// Create new workflow on Cases module
		new VoodooControl("input", "id", "name").set(ds.get("workflow_name"));

		// Select target module as "Cases"
		new VoodooControl("option", "css", "select[name=base_module] option:nth-of-type(5)").click();

		// save workflow
		new VoodooControl("input", "id", "save_workflow").click();
		VoodooUtils.waitForReady();

		// Define Condition for Workflow Execution
		new VoodooControl("a", "id", "NewWorkFlowTriggerShells").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "css", "body table:nth-child(2) tr:nth-child(5)  td input[type='radio']").click();
		new VoodooControl("a", "id", "href_filter_rel_field_1").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("input", "css", "[name='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "next").click();
		new VoodooControl("a", "id", "href_trigger").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#lhs_field [value='website']").click();
		new VoodooControl("input", "id",  "trigger__field_value").set(sugar().accounts.getDefaultData().get("website"));
		new VoodooControl("input", "id", "save").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "save").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Create a Alert for the workflow
		new VoodooControl("a", "id", "NewWorkFlowAlertShells").click();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", "[name='name']").set(ds.get("workflow_alert"));
		new VoodooControl("input", "css", "[name='alert_text']").set(ds.get("alert_text"));
		new VoodooControl("input", "css", "[title='Save']").click();
		VoodooUtils.waitForReady();

		// Create Alert Recipient 
		new VoodooControl("input", "css", "[title='Create']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);
		new VoodooControl("td", "css", "body table:nth-child(2) > tbody > tr:nth-child(5) > td").click();
		new VoodooControl("a", "id", "href_specific_user").click();
		VoodooUtils.focusWindow(2);
		new VoodooControl("option", "css", "#selector > option:nth-child(4)").click();
		new VoodooControl("input", "css", "[name='Save']").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "save").click(); 
		VoodooUtils.focusWindow(0);

		// Create a Case record to trigger the workflow
		sugar().cases.create();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();
		sugar().logout();  // logout admin

		//  Login as the specified user mentioned in workflow alert.
		sugar().login(demoUser); 
		// TODO: VOOD-672, Set email settings individually
		// Go to Emails module
		sugar().navbar.navToModule(sugar().emails.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");

		// Click Settings button
		new VoodooControl("button", "id", "settingsButton").click();
		// Go to the Mail Accounts tab in the Settings window
		new VoodooControl("a", "css", "div#settingsDialog li#accountSettings a").click();
		//Click Add button in the Mail Account area
		new VoodooControl("input", "id", "addButton").click();
		// assert incoming email section in mail accounts properties window pop up
		new VoodooControl("h4", "css", "#ieAccount > table:nth-child(8) > tbody > tr > td > h4").assertEquals("Incoming Email", true);
		// assert outgoing email section in mail accounts properties window pop up	  
		new VoodooControl("h4", "css", "#ieAccount > table:nth-child(9) > tbody > tr > td > h4").assertEquals("Outgoing Email", true);
		// click on prefill gmail default in  in mail accounts properties window pop up
		new VoodooControl("a", "id", "prefill_gmail_defaults_link").click();
		// fill the the Incoming Email properties and outgoing Email properties
		new VoodooControl("input", "id", "ie_name").set(emailSettings.get("userName"));
		new VoodooControl("input", "id", "email_user").set(emailSettings.get("userName")); // To allow to fetch email
		new VoodooControl("input", "id", "email_password").set(emailSettings.get("password")); // To allow to fetch email
		new VoodooControl("input", "id", "trashFolder").set("[Gmail]/Trash");
		new VoodooControl("input", "id", "sentFolder").set("[Gmail]/Sent Mail");
		new VoodooControl("input", "id", "ie_from_addr").set(emailSettings.get("userName"));

		//  Click Save Button
		new VoodooControl("input", "id", "saveButton").click();
		// TODO: VOOD-1052, Need support for waiting for communication with external gmail service
		VoodooUtils.waitForReady(120000);
		new VoodooControl("input", "css", "#settingsDialog > a").click();

		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").waitForVisible();
		//  click on email folder inside email tree 
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/table/tbody/tr/td[2]/span").click();
		VoodooUtils.waitForReady();
		// click on inbox
		new VoodooControl("span", "xpath", "//*[@id='emailtree']/div/div/div[1]/div/div/table/tbody/tr/td[3]/span").click();
		VoodooUtils.waitForReady();
		// right click on email inside email inbox to import one email record to Sugar
		VoodooControl emailSubjectCtrl= new VoodooControl("tr", "xpath", "//*[@id='emailGrid']/div[3]/table/tbody[2]/tr[1]/td[5]/div");
		emailSubjectCtrl.waitForVisible();
		emailSubjectCtrl.assertContains("WORKFLOW ALERT", true);
		VoodooUtils.focusDefault();	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}